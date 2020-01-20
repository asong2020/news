package com.asong.cloud.controller;


import com.asong.cloud.model.*;
import com.asong.cloud.service.CommentService;
import com.asong.cloud.service.NewsService;
import com.asong.cloud.service.UserService;
import com.asong.cloud.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class NewsController {
    //加入日志
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    private NewsService newsService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    //咨询页面
    @RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model)
    {
        try {
            News news = newsService.getById(newsId);
            if (news != null) {
                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
                List<ViewObject> commentsVOs = new ArrayList<>();
                for (Comment comment : comments) {
                    ViewObject commentVO = new ViewObject();
                    commentVO.set("comment", comment);
                    commentVO.set("user", userService.getUser(comment.getUserId()));
                    commentsVOs.add(commentVO);
                }
                model.addAttribute("comments", commentsVOs);
            }
            model.addAttribute("news", news);
            model.addAttribute("owner", userService.getUser(news.getUserId()));
        }catch (Exception e)
        {
            logger.error("获取资讯明细错误" + e.getMessage());
        }

        return "detail";
    }




    //获取图片
    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response)
    {
        try{
            response.setContentType("image/jpg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)),response.getOutputStream());
        }catch (Exception e)
        {
            logger.error("读取图片错误" +  imageName + e.getMessage());
        }
    }

    //存储图片
    @RequestMapping(path = {"/uploadImage"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file)
    {
        try{
            String fileurl = newsService.saveImage(file);
            if(fileurl == null)
            {
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0,fileurl);
        }catch (Exception e)
        {
            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败");
        }
    }

            //添加咨询
            @RequestMapping(path = {"/users/addNews"},method = {RequestMethod.POST})
            @ResponseBody
            public String addNews(@RequestParam("image") String image,
                @RequestParam("title") String title,
                @RequestParam("link") String link)
            {
                try{
                    News news = new News();
                    news.setCreatedDate(new Date());
                    news.setTitle(title);
                    news.setImage(image);
                    news.setLink(link);
                    if(hostHolder.getUser() != null)
                    {
                        news.setUserId(hostHolder.getUser().getId());
                    }else
                    {
                        //设置一个用户
                        news.setUserId(10);
                    }
                    newsService.addNews(news);
                    return ToutiaoUtil.getJSONString(0,"添加成功");
                }catch (Exception e)
                {
                    logger.error("添加咨询失败",e.getMessage());
         return ToutiaoUtil.getJSONString(1,"发布失败");
        }
    }

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content)
    {
        try{
            Comment comment = new Comment();
            //comment.setUserId(hostHolder.getUser().getId());
            comment.setUserId(1);
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);
        }catch (Exception e)
        {
         logger.error("提交评论错误"+e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

}

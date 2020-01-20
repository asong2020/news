package com.asong.cloud.service;

import com.asong.cloud.dao.NewsDAO;
import com.asong.cloud.model.News;
import com.asong.cloud.util.ToutiaoUtil;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by asong on 2020/1/7
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addNews(News news)
    {
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int newsId)
    {
        return newsDAO.getById(newsId);
    }

    public String saveImage(MultipartFile file) throws IOException
    {
        int dotpos = file.getOriginalFilename().lastIndexOf(".");
        if(dotpos<0)
        {
            return null;
        }
        //获取后缀
        String fileExt = file.getOriginalFilename().substring(dotpos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt))
        {
            return null;
        }

        String fileName = UUID.randomUUID().toString().replaceAll("-","")+ "." + fileExt;
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }
    public int updateLikeCount(int id, int count) {
        return newsDAO.updateLikeCount(id, count);
    }
}

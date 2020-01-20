package com.asong.cloud.controller;

import com.asong.cloud.service.OssService;
import com.asong.cloud.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * Oss 服务
 * Create by asong 2020/1/12
 */
@Controller
public class OssController {

    @Autowired
    private OssService ossService;

    @RequestMapping(path = {"/upload/image"},method = {RequestMethod.GET})
    @ResponseBody
    public String ossUpload(@RequestParam("file") String file)
    {
        String res = ossService.uploadFile(file);

        if("error".equals(res))
        {
            return ToutiaoUtil.getJSONString(1,"下载失败");
        }else
        {
            return ToutiaoUtil.getJSONString(0,res);
        }

    }



}

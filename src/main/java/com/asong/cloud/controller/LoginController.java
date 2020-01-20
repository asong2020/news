package com.asong.cloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.asong.cloud.async.EventModel;
import com.asong.cloud.async.EventProducer;
import com.asong.cloud.async.EventType;
import com.asong.cloud.service.UserService;
import com.asong.cloud.util.ToutiaoUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.slf4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登陆Controller层
 * create by asong 2020/1/6
 */

@Controller
public class LoginController {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
   private UserService userService;

    @Autowired
    EventProducer eventProducer;

    public String getJSONString(int code)
    {
        JSONObject json =  new JSONObject();
        json.put("code",code);
        return json.toJSONString();
    }

    public String getJSONString(int code,Map<String,Object> map)
    {
        JSONObject json = new JSONObject();
        json.put("code",code);
        for(Map.Entry<String,Object> entry : map.entrySet())
        {
            json.put(entry.getKey(),entry.getValue());
        }
        return json.toJSONString();
    }

    /**
     * 注册
     */
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.regisiter(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }

    /**
     * 登陆
     */
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId(7)
                        .setExt("username", "面试邀请").setExt("to", "1771831605@qq.com"));
                return ToutiaoUtil.getJSONString(0, "成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }
    /**
     * 退出
     * @param ticket
     * @return
     */
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}

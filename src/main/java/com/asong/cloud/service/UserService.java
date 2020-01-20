package com.asong.cloud.service;

import com.asong.cloud.dao.LoginTicketDAO;
import com.asong.cloud.dao.UserDAO;
import com.asong.cloud.model.LoginTicket;
import com.asong.cloud.model.User;

import com.asong.cloud.util.ToutiaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    public Map<String,Object> regisiter(String username, String password)
    {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username))
        {
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password))
        {
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user != null)
        {
            map.put("msgname","用户名已被注册");
            return map;
        }

        //密码强度

        user = new User();
        /**
         * 添加用户
         */
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        /**
         * 登陆
         */
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;

    }

    /**
     * 加入Ticket
     * @param userId
     * @return
     */

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }



    /**
     * 登陆
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        if (!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    /**
     * 退出
     */
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}

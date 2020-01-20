package com.asong.cloud.model;

import org.springframework.stereotype.Component;

/**
 * 存用户
         * Created by asong on 2020/1/6
         */
@Component
public class HostHolder {
    /**
     * 线程存放多用户
     */
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();;
    }
}

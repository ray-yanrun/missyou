package com.lin.missyou.core;

import com.lin.missyou.model.User;

import java.util.HashMap;
import java.util.Map;

public class LocalUser {

    // 定义线程安全的ThreadLocal
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void set(User user, Integer scope){
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("scope", scope);
        LocalUser.threadLocal.set(map);
    }

    public static User getUser(){
        return (User) LocalUser.threadLocal.get().get("user");
    }

    public static Integer getScope(){
        return (Integer) LocalUser.threadLocal.get().get("scope");
    }

    public static void clear(){
        LocalUser.threadLocal.remove();
    }
}

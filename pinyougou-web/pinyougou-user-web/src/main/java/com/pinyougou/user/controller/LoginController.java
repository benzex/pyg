package com.pinyougou.user.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录用控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-16<p>
 */
@RestController
public class LoginController {

    /** 获取登录用户名 */
    @GetMapping("/user/showName")
    public Map<String,String> showName(){

        // 获取安全上下文对象
        SecurityContext context = SecurityContextHolder.getContext();
        String loginName = context.getAuthentication().getName();
        System.out.println(loginName);

        Map<String,String> data = new HashMap<>();
        data.put("loginName", loginName);
        return data;
    }
}

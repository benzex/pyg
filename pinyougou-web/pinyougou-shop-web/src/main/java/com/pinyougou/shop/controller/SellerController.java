package com.pinyougou.shop.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 商家控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-01<p>
 */
@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference(timeout = 10000)
    private SellerService sellerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 商家申请入驻
     */
    @PostMapping("/save")
    public boolean save(@RequestBody Seller seller) {
        try {
            String password = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(password);
            sellerService.save(seller);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * 商家修改
     */
    @PostMapping("/update")
    public boolean update(@RequestBody Seller seller) {
        try {
            if(seller.getPassword()!=null){
                String newPassword = seller.getPassword();
                System.out.println("新密码"+newPassword);
                String password = passwordEncoder.encode(newPassword);
                seller.setPassword(password);
            }
            // 获取安全上下文对象
            SecurityContext context = SecurityContextHolder.getContext();
            // 获取用户名
            String sellerId = context.getAuthentication().getName();
            System.out.println(sellerId);
            sellerService.update(sellerId, seller);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 商家申请入驻
     */
    @PostMapping("/checkps")
    public boolean check(@RequestBody Seller seller) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 获取安全上下文对象
        SecurityContext context = SecurityContextHolder.getContext();
        // 获取用户名
        String sellerId = context.getAuthentication().getName();
        String password =seller.getPassword();
        System.out.println("页面来的老密码"+password);
        String oldPassword = sellerService.findOne(sellerId).getPassword();
       // return (oldPassword.equals(password));
        System.out.println("yon"+encoder.matches(password,oldPassword));
        return encoder.matches(password,oldPassword);


    }
}
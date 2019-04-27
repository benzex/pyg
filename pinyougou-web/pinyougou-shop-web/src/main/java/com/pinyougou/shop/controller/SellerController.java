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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    //修改资料
    @PostMapping("/update")
    public boolean update(@RequestBody Seller seller) {
        try {
            if (seller.getPassword() != null) {
                String newPassword = seller.getPassword();
                String password = passwordEncoder.encode(newPassword);
                seller.setPassword(password);
            }
            SecurityContext context = SecurityContextHolder.getContext();
            String sellerId = context.getAuthentication().getName();
            sellerService.update(sellerId, seller);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //页面验证旧密码
    @PostMapping("/checkps")
    public boolean check(@RequestBody Seller seller) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = seller.getPassword();
        SecurityContext context = SecurityContextHolder.getContext();
        String sellerId = context.getAuthentication().getName();
        String oldPassword = sellerService.findOne(sellerId).getPassword();
        return encoder.matches(password, oldPassword);
    }
    //修改页面回显数据
    @GetMapping("/findById")
    public Seller findById2(){
        SecurityContext context = SecurityContextHolder.getContext();
        String sellerId = context.getAuthentication().getName();
        Seller seller = sellerService.findOne(sellerId);
        //防止密码传回页面时显示在response
        seller.setPassword("*********");
        return seller;
    }
}
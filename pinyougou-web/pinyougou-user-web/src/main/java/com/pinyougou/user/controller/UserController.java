package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * 用户控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 10000)
    private UserService userService;

    /** 用户注册 */
    @PostMapping("/save")
    public boolean save(@RequestBody User user, String code){
        try{
            // 检验验证码是否正确
            boolean flag = userService.checkSmsCode(user.getPhone(), code);
            if (flag) {
                userService.save(user);
            }
            return flag;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }


    /** 发送短信验证码 */
    @GetMapping("/sendSmsCode")
    public boolean sendSmsCode(String phone){
        try{
            return userService.sendSmsCode(phone);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
	
	 /*获取所有的省 */
    @GetMapping("/findProvinces")
    public List<Provinces> getAddress(){
        try {
            return userService.findProvinces();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/findCities")
    public List<Cities> findCities(String provinceId){
        return userService.findCities(provinceId);
    }

    @GetMapping("/findAreas")
    public List<Areas> findAreas(String cityId){
        return userService.findAreas(cityId);
    }

    /*查询用户信息*/
    @GetMapping("/getUser")
    public Map<String,Object> getUser(){
        try {
            /*获取安全上下文对象*/
            SecurityContext context = SecurityContextHolder.getContext();
            /*获取用户名*/
            String loginName = context.getAuthentication().getName();
            Map<String, Object> map = userService.findOneByLoginName(loginName);
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*修改用户信息设置*/
    @PostMapping("/updateUser")
    public boolean updateUser(@RequestBody User user){
        try {
            System.out.println(user.getAddress());
            userService.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

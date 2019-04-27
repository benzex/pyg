package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import com.pinyougou.common.util.MD5Utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author lilei
 * @Date 15/04/2019 19:39
 * @Version 1.0
 **/
@RestController
@RequestMapping("/setting")
public class SettingController {
    @Reference(timeout = 10000)
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/sms")
    public boolean sendSms(String phone){
        Boolean success = userService.sendSmsCode(phone);
        return success;
    }
    @PostMapping("/loadSateUI")
    public User getSateUI(){
        try {
            return userService.loadsateUI(request.getRemoteUser());
        } catch (Exception e) {
            return null;
        }
    }
    @PostMapping("/update")
    public Boolean updateSettingUser(@RequestBody User user){
        try {
            String string = MD5Utils.getMD5String(user.getPassword());
            user.setPassword(string);
            userService.update(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @PostMapping("/updatePhone")
    public Boolean updateSettingPhone(@RequestBody User user,String code,String smsCode){
        try {
            String verifyCode  = (String)request.getSession().getAttribute("verify_code");
            if (smsCode==null && !verifyCode.equalsIgnoreCase(code)){
                return false;
            }
            boolean ok = userService.checkSmsCode(user.getPhone(),smsCode);
            if (ok){
                userService.update(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    @GetMapping("/verifyPhone")
    public Boolean verifyPhone(String code,String smsCode, String phone){
        String verifyCode  = (String)request.getSession().getAttribute("verify_code");
        if (!verifyCode.equalsIgnoreCase(code)){
            return false;
        }
        return userService.checkSmsCode(phone,smsCode);
    }

}

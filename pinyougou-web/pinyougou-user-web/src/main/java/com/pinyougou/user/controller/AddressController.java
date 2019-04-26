package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.AddressService;
import com.pinyougou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference(timeout = 1000)
    private AddressService addressService;
    @Reference(timeout = 1000)
    private UserService userService;


    @GetMapping("/getUserAndAddress")
    public Map<String,Object> getUserAndAddress(){
        /*获取安全上下文对象*/
        SecurityContext context = SecurityContextHolder.getContext();
        /*获取用户名*/
        String loginName = context.getAuthentication().getName();
        Map<String, Object> map = userService.findOneByLoginName(loginName);
        /*根据用户名获取收货地址*/
        List<Address> address = addressService.findAddressByUser(loginName);
        map.put("address",address);
        return map;
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

    /*保存收件地址*/
    @PostMapping("/save")
    public boolean save(@RequestBody Address address){
        try {
            /*获取安全上下文对象*/
            SecurityContext context = SecurityContextHolder.getContext();
            /*获取用户名*/
            String loginName = context.getAuthentication().getName();
            address.setUserId(loginName);
            addressService.save(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

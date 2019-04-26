<<<<<<< HEAD
package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.templateCode}")
    private String templateCode;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(User user) {
        try{
            // 密码加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            // 创建时间
            user.setCreated(new Date());
            // 修改时间
            user.setUpdated(user.getCreated());
            // 添加数据
            userMapper.insertSelective(user);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public User findOne(Serializable id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findByPage(User user, int page, int rows) {
        return null;
    }

    /** 发送短信验证码 */
    public boolean sendSmsCode(String phone){
        try{
            // 1. 随机生成6位数字的验证码 95db9eb9-94e8-48e7-a5b2-97c622644e70
            String code = UUID.randomUUID().toString().replaceAll("-", "")
                    .replaceAll("[a-zA-Z]", "").substring(0,6);
            System.out.println("code= " + code);


            // 2. 调用短信发送接口(HttpClientUtils)
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            // 定义Map集合封装请求参数 18502903967
            Map<String, String> params = new HashMap<>();
            params.put("phone", phone);
            params.put("signName", signName);
            params.put("templateCode", templateCode);
            params.put("templateParam", "{'number' : '"+ code +"'}");
            // 发送post请求
            String content = httpClientUtils.sendPost(smsUrl, params);
            System.out.println("content = " + content);

            // 3. 判断短信是否发送成功，如果发送成功，就需要把验证存储到Redis(时间90秒)
            // {success : true}
            Map map = JSON.parseObject(content, Map.class);
            boolean success = (boolean)map.get("success");
            if (success){
                // 把验证存储到Redis(时间90秒)
                redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            }

            return success;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 检验验证码是否正确 */
    public boolean checkSmsCode(String phone, String code){
        try{
            String oldCode = (String)redisTemplate.boundValueOps(phone).get();
            return code.equals(oldCode);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public User loadsateUI(String remoteUser) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("username",remoteUser);
        List<User> users = userMapper.selectByExample(example);
        return users.get(0);
    }

}
=======
package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.mapper.AreasMapper;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.mapper.ProvincesMapper;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProvincesMapper provincesMapper;
    @Autowired
    private CitiesMapper citiesMapper;
    @Autowired
    private AreasMapper areasMapper;
    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.templateCode}")
    private String templateCode;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(User user) {
        try{
            // 密码加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            // 创建时间
            user.setCreated(new Date());
            // 修改时间
            user.setUpdated(user.getCreated());
            // 添加数据
            userMapper.insertSelective(user);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /*修改用户信息设置*/
    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public User findOne(Serializable id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findByPage(User user, int page, int rows) {
        return null;
    }

    /** 发送短信验证码 */
    public boolean sendSmsCode(String phone){
        try{
            // 1. 随机生成6位数字的验证码 95db9eb9-94e8-48e7-a5b2-97c622644e70
            String code = UUID.randomUUID().toString().replaceAll("-", "")
                    .replaceAll("[a-zA-Z]", "").substring(0,6);
            System.out.println("code= " + code);


            // 2. 调用短信发送接口(HttpClientUtils)
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            // 定义Map集合封装请求参数 18502903967
            Map<String, String> params = new HashMap<>();
            params.put("phone", phone);
            params.put("signName", signName);
            params.put("templateCode", templateCode);
            params.put("templateParam", "{'number' : '"+ code +"'}");
            // 发送post请求
            String content = httpClientUtils.sendPost(smsUrl, params);
            System.out.println("content = " + content);

            // 3. 判断短信是否发送成功，如果发送成功，就需要把验证存储到Redis(时间90秒)
            // {success : true}
            Map map = JSON.parseObject(content, Map.class);
            boolean success = (boolean)map.get("success");
            if (success){
                // 把验证存储到Redis(时间90秒)
                redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            }

            return success;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 检验验证码是否正确 */
    public boolean checkSmsCode(String phone, String code){
        try{
            String oldCode = (String)redisTemplate.boundValueOps(phone).get();
            return code.equals(oldCode);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /*获取所有的省*/
    @Override
    public List<Provinces> findProvinces() {
        List<Provinces> provinces = provincesMapper.selectAll();
        return provinces;
    }

    /*根据省份获取城市集合*/
    @Override
    public List<Cities> findCities(String provinceId) {
        Cities cities = new Cities();
        cities.setProvinceId(provinceId);
        return citiesMapper.select(cities);
    }

    @Override
    public List<Areas> findAreas(String cityId) {
        Areas areas = new Areas();
        areas.setCityId(cityId);
        return areasMapper.select(areas);
    }

    /*查询用户信息*/
    @Override
    public Map<String,Object> findOneByLoginName(String loginName) {
        try {
            Map<String,Object> map = new HashMap<>();
            User user = new User();
            user.setUsername(loginName);
            User user1 = userMapper.selectOne(user);
            map.put("user",user1);
            map.put("birthday",new SimpleDateFormat("yyyy-MM-dd").format(user1.getBirthday()));
            return map;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
>>>>>>> f75dacd6a7dce102264a931ec541a2a9ee717f59

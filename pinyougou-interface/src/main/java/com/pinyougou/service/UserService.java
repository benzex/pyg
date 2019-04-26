package com.pinyougou.service;

import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import java.util.List;
import java.io.Serializable;
import java.util.Map;

/**
 * UserService 服务接口
 * @date 2019-03-28 09:58:00
 * @version 1.0
 */
public interface UserService {

	/** 添加方法 */
	void save(User user);

	/** 修改方法 */
	void update(User user);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	User findOne(Serializable id);

	/** 查询全部 */
	List<User> findAll();

	/** 多条件分页查询 */
	List<User> findByPage(User user, int page, int rows);

	/** 发送短信验证码 */
	boolean sendSmsCode(String phone);

	/** 检验验证码是否正确 */
	boolean checkSmsCode(String phone, String code);

<<<<<<< HEAD
<<<<<<< HEAD
    User loadsateUI(String remoteUser);
=======
=======
>>>>>>> f75dacd6a7dce102264a931ec541a2a9ee717f59
	/*获取所有的省*/
	List<Provinces> findProvinces();

	/*根据省份获取城市集合*/
	List<Cities> findCities(String provinceId);

	/*根据城市获取地区集合*/
	List<Areas> findAreas(String cityId);

	/*查询用户信息*/
	Map<String,Object> findOneByLoginName(String loginName);
<<<<<<< HEAD
>>>>>>> f75dacd6a7dce102264a931ec541a2a9ee717f59
=======
>>>>>>> f75dacd6a7dce102264a931ec541a2a9ee717f59
}
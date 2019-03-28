package com.pinyougou.service;

import com.pinyougou.pojo.Specification;
import java.util.List;
import java.io.Serializable;
/**
 * SpecificationService 服务接口
 * @date 2019-03-28 09:58:00
 * @version 1.0
 */
public interface SpecificationService {

	/** 添加方法 */
	void save(Specification specification);

	/** 修改方法 */
	void update(Specification specification);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Specification findOne(Serializable id);

	/** 查询全部 */
	List<Specification> findAll();

	/** 多条件分页查询 */
	List<Specification> findByPage(Specification specification, int page, int rows);

}
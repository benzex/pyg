package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.Item;

import java.util.List;
import java.io.Serializable;
import java.util.Map;

/**
 * GoodsService 服务接口
 * @date 2019-03-28 09:58:00
 * @version 1.0
 */
public interface GoodsService {

	/** 添加方法 */
	void save(Goods goods);

	/** 修改方法 */
	void update(Goods goods);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Goods findOne(Serializable id);

	/** 查询全部 */
	List<Goods> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(Goods goods, int page, int rows);

	/** 修改商品的状态码 */
    void updateStatus(String columnName, Long[] ids, String status);

    /** 根据spu的id查询商品信息 */
    Map<String,Object> getGoods(Long goodsId);

    /** 根据多个goodsId从tb_item表查询多个SKU商品数据 */
    List<Item> findItemByGoodsId(Long[] goodsIds);
}
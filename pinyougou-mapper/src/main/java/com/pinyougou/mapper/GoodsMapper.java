package com.pinyougou.mapper;

import com.pinyougou.pojo.Goods;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * GoodsMapper 数据访问接口
 * @date 2019-03-28 09:54:28
 * @version 1.0
 */
public interface GoodsMapper extends Mapper<Goods>{

    /** 多条件查询商品 */
    List<Map<String,Object>> findAll(Goods goods);
}
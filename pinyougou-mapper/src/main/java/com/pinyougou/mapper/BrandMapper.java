package com.pinyougou.mapper;

import com.pinyougou.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BrandMapper 数据访问接口
 * @date 2019-03-28 09:54:28
 * @version 1.0
 */
public interface BrandMapper extends Mapper<Brand>{

    /** 多条件查询品牌 */
    List<Brand> findAll(Brand brand);

    /** 批量删除 */
    void deleteAll(Serializable[] ids);

    /** 查询品牌列表(id与name) */
    @Select("SELECT id,NAME AS text FROM tb_brand")
    List<Map<String,Object>> findAllByIdAndName();
}
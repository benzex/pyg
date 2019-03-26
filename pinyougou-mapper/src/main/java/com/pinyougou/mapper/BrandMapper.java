package com.pinyougou.mapper;

import com.pinyougou.pojo.Brand;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据访问接口
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-03-26<p>
 */
public interface BrandMapper {

    /** 查询全部品牌 */
    @Select("select * from tb_brand")
    List<Brand> findAll();
}

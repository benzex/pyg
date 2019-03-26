package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Brand;
import com.pinyougou.service.BrandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 品牌控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-03-26<p>
 */
@RestController
public class BrandController {

    /**
     * 引用服务
     * timeout:调用服务超时(1000毫秒)
     * */
    @Reference(timeout = 10000)
    private BrandService brandService;

    /** 查询全部品牌 */
    @GetMapping("/brand/findAll")
    public List<Brand> findAll(){
        System.out.println("brandService: " + brandService);
        return brandService.findAll();
    }

}

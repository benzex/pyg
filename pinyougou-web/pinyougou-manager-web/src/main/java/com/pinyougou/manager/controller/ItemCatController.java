package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.service.ItemCatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品分类控制器
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference(timeout = 10000)
    private ItemCatService itemCatService;

    /** 根据父级id查询商品分类 */
    @GetMapping("/findItemCatByParentId")
    public List<ItemCat> findItemCatByParentId(Long parentId){
        return itemCatService.findItemCatByParentId(parentId);
    }
}

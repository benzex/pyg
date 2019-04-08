package com.pinyougou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Content;
import com.pinyougou.service.ContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 广告内容控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-07<p>
 */
@RestController
public class ContentController {

    @Reference(timeout = 10000)
    private ContentService contentService;

    /** 查询广告数据 */
    @GetMapping("/findContentByCategoryId")
    public List<Content> findContentByCategoryId(Long categoryId){
        return contentService.findContentByCategoryId(categoryId);
    }

}

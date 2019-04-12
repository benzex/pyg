package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 商品详情控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-12<p>
 */
@Controller
public class ItemController {

    @Reference(timeout = 10000)
    private GoodsService goodsService;

    /**
     * http://item.pinyougou.com/29883743.html
     * 请求URL路径上的参数
     */
    @GetMapping("/{goodsId}")
    public String getGoods(@PathVariable("goodsId")Long goodsId, Model model){
        System.out.println("goodsId = " + goodsId);

        // model:  就是FreeMarker的数据模型
        // springmvc会根据视图解析器把model中的数据存放的位置不一样
        Map<String,Object> dataModel = goodsService.getGoods(goodsId);
        model.addAllAttributes(dataModel);

        return "item";
    }
}

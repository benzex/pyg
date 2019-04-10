package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-09<p>
 */
@Service(interfaceName = "com.pinyougou.service.ItemSearchService")
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    /** 搜索方法 */
    public Map<String,Object> search(Map<String, Object> params){
        try{
            // 获取查询关键字
            String keywords = (String)params.get("keywords");

            // 获取当前页码
            Integer page = (Integer)params.get("page");
            if (page == null || page < 1){
                page = 1;
            }
            // 获取页大小
            Integer rows = (Integer)params.get("rows");
            if (rows == null || rows < 1){
                rows = 20;
            }


            // 判断关键字是否为空
            if (StringUtils.isNoneBlank(keywords)){ // 高亮查询
                // 创建高亮查询对象
                HighlightQuery highlightQuery = new SimpleHighlightQuery();
                // 创建条件对象
                Criteria criteria = new Criteria("keywords").is(keywords); // 分词
                // 添加搜索条件
                highlightQuery.addCriteria(criteria);



                /** ########### 1. 高亮显示 ############ */
                // 创建高亮选项对象
                HighlightOptions highlightOptions = new HighlightOptions();
                // 设置高亮域
                highlightOptions.addField("title");
                // 设置高亮格式器前缀
                highlightOptions.setSimplePrefix("<font color='red'>");
                // 设置高亮格式器后缀
                highlightOptions.setSimplePostfix("</font>");

                // 添加高亮选项对象
                highlightQuery.setHighlightOptions(highlightOptions);


                /** ########### 2. 过滤条件 ############ */
                // params : {"keywords":"小米","category":"手机","brand":"苹果",
                //           "spec":{"网络":"联通3G","机身内存":"64G"},"price":"1000-1500"}
                // 1. 按商品分类过滤
                String category = (String) params.get("category");
                if (StringUtils.isNoneBlank(category)){
                    // 创建条件对象
                    Criteria criteria1 = new Criteria("category").is(category);
                    // 添加过滤查询
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                }

                // 2. 按商品品牌过滤
                String brand = (String) params.get("brand");
                if (StringUtils.isNoneBlank(brand)){
                    // 创建条件对象
                    Criteria criteria1 = new Criteria("brand").is(brand);
                    // 添加过滤查询
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                }

                // 3. 按商品规格过滤
                // "spec":{"网络":"联通3G","机身内存":"64G"}
                Map<String, String> specMap = (Map<String,String>) params.get("spec");
                //   "spec_网络": "联通4G",
                //   "spec_机身内存": "64G",
                if (specMap != null && specMap.size() > 0){
                    // 迭代规格，产生多个过滤条件
                    for (String key : specMap.keySet()) {
                        // 创建条件对象
                        Criteria criteria1 = new Criteria("spec_" + key).is(specMap.get(key));
                        // 添加过滤查询
                        highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    }
                }

                // 4. 按商品价格过滤
                String price = (String) params.get("price");
                if (StringUtils.isNoneBlank(price)){
                    // 0-500 1000-1500 3000-*
                    String[] priceArr = price.split("-");
                    // 价格的起始不是零
                    if (!"0".equals(priceArr[0])){
                        // 创建条件对象
                        Criteria criteria1 = new Criteria("price").greaterThanEqual(priceArr[0]);
                        // 添加过滤查询
                        highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    }
                    // 价格的结束不是星号
                    if (!"*".equals(priceArr[1])){
                        // 创建条件对象
                        Criteria criteria1 = new Criteria("price").lessThanEqual(priceArr[1]);
                        // 添加过滤查询
                        highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    }
                }



                /** ########### 3. 分页 ############ */
                // 设置分页起始记录数
                highlightQuery.setOffset((page -1) * rows);
                // 设置页大小
                highlightQuery.setRows(rows);



                /** ########### 4. 排序 ############ */
                String sortField = (String)params.get("sortField");
                String sortValue = (String)params.get("sortValue");
                if (StringUtils.isNoneBlank(sortField) && StringUtils.isNoneBlank(sortValue)){
                    // 创建排序对象
                    Sort sort = new Sort("ASC".equals(sortValue) ?
                            Sort.Direction.ASC : Sort.Direction.DESC, sortField);
                    // 添加排序
                    highlightQuery.addSort(sort);
                }


                // 高亮分页查询，得到高亮分页对象
                HighlightPage<SolrItem> highlightPage = solrTemplate.
                        queryForHighlightPage(highlightQuery, SolrItem.class);

                // 获取高亮集合
                List<HighlightEntry<SolrItem>> highlighted = highlightPage.getHighlighted();
                // 迭代高亮集合
                for (HighlightEntry<SolrItem> highlightEntry : highlighted) {
                    // 获取文档对应的实体对象
                    SolrItem solrItem = highlightEntry.getEntity();
                    // 获取高亮内容集合
                    List<HighlightEntry.Highlight> highlights = highlightEntry.getHighlights();
                    // 判断高亮内容集合
                    if (highlights != null && highlights.size() > 0){
                        // 获取标题的高亮内容
                        String title = highlights.get(0).getSnipplets().get(0).toString();
                        System.out.println("title = " + title);
                        // 为标题设置高亮内容
                        solrItem.setTitle(title);
                    }
                }

                Map<String,Object> data = new HashMap<>();
                data.put("rows", highlightPage.getContent());
                // 总记录数
                data.put("total", highlightPage.getTotalElements());
                // 总记录数
                data.put("totalPages", highlightPage.getTotalPages());
                return data;

            }else{ // 简单查询
                // 创建简单查询对象
                SimpleQuery simpleQuery = new SimpleQuery("*:*");

                // 设置分页起始记录数
                simpleQuery.setOffset((page -1) * rows);
                // 设置页大小
                simpleQuery.setRows(rows);

                /** 分页查询，得到分数分页对象 */
                ScoredPage<SolrItem> scoredPage = solrTemplate.queryForPage(simpleQuery, SolrItem.class);

                Map<String,Object> data = new HashMap<>();
                data.put("rows", scoredPage.getContent());
                // 总记录数
                data.put("total", scoredPage.getTotalElements());
                // 总记录数
                data.put("totalPages", scoredPage.getTotalPages());
                return data;
            }


        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }


}

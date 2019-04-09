package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
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
            // 创建查询对象
            Query query = new SimpleQuery("*:*");

            // 获取查询关键字
            String keywords = (String)params.get("keywords");
            // 判断关键字是否为空
            if (StringUtils.isNoneBlank(keywords)){
                // 创建条件对象
                Criteria criteria = new Criteria("keywords").is(keywords); // 分词
                // 添加搜索条件
                query.addCriteria(criteria);
            }

            /** 分页查询，得到分数分页对象 */
            ScoredPage<SolrItem> scoredPage = solrTemplate.queryForPage(query, SolrItem.class);

            Map<String,Object> data = new HashMap<>();
            data.put("rows", scoredPage.getContent() );
            return data;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }


}

package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 类型模板服务接口实现类
 */
@Service(interfaceName = "com.pinyougou.service.TypeTemplateService")
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TypeTemplateMapper typeTemplateMapper;

    @Override
    public void save(TypeTemplate typeTemplate) {
        try{
            typeTemplateMapper.insertSelective(typeTemplate);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        try{
            typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {
        try{
            typeTemplateMapper.deleteAll(ids);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public TypeTemplate findOne(Serializable id) {
        return null;
    }

    @Override
    public List<TypeTemplate> findAll() {
        return null;
    }

    @Override
    public PageResult findByPage(TypeTemplate typeTemplate, int page, int rows) {
        try{
            // 开始分页
            PageInfo<TypeTemplate> pageInfo = PageHelper.startPage(page, rows)
                    .doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    typeTemplateMapper.findAll(typeTemplate);
                }
            });
            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

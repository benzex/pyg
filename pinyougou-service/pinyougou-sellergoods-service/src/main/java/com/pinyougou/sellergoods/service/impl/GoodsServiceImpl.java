package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.*;

/**
 * 商品服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-03<p>
 */
@Service(interfaceName = "com.pinyougou.service.GoodsService")
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SellerMapper sellerMapper;


    @Override
    public void save(Goods goods) {
        try{
            // 1. 往tb_goods表插入数据 (SPU)
            // 设置商品的审核状态(未审核)
            goods.setAuditStatus("0");
            goodsMapper.insertSelective(goods);

            // 2. 往tb_goods_desc表插入数据
            // 设置主键列的值
            goods.getGoodsDesc().setGoodsId(goods.getId());
            goodsDescMapper.insertSelective(goods.getGoodsDesc());

            // 判断是否启用规格
            if ("1".equals(goods.getIsEnableSpec())) {// 启用规格

                // 3. 往tb_item表插入数据(SKU)
                for (Item item : goods.getItems()) {
                    // item: {spec:{}, price:0, num:9999, status:'0', isDefault:'0'}
                    // Apple iPhone 8 Plus (A1864) 64GB 深空灰色 移动联通电信4G手机
                    // SKU商品的标题 : SPU的名称 + 规格选项的名称 （{"网络":"电信4G","机身内存":"32G"}）
                    StringBuilder title = new StringBuilder(goods.getGoodsName());
                    // 把spec规格转化成Map集合
                    Map<String, String> specMap = JSON.parseObject(item.getSpec(), Map.class);
                    for (String optionName : specMap.values()) {
                        title.append(" " + optionName);
                    }
                    item.setTitle(title.toString());

                    /** 设置SKU商品其它属性 */
                    setItemInfo(item, goods);

                    // 往tb_item添加数据
                    itemMapper.insertSelective(item);
                }
            }else{ // 不启用规格
                /** 创建SKU具体商品对象 */
                Item item = new Item();
                /** 设置SKU商品的标题 */
                item.setTitle(goods.getGoodsName());
                /** 设置SKU商品的价格 */
                item.setPrice(goods.getPrice());
                /** 设置SKU商品库存数据 */
                item.setNum(9999);
                /** 设置SKU商品启用状态 */
                item.setStatus("1");
                /** 设置是否默认*/
                item.setIsDefault("1");
                /** 设置规格选项 */
                item.setSpec("{}");

                /** 设置SKU商品其它属性 */
                setItemInfo(item, goods);

                // 往tb_item添加数据
                itemMapper.insertSelective(item);
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 设置SKU商品的其它的信息 */
    private void setItemInfo(Item item, Goods goods) {
        // SKU商品的图片
        // [{"color":"金色","url":"http://image.pinyougou.com/jd/wKgMg1qtKEOATL9nAAFti6upbx4132.jpg"},
        // {"color":"深空灰色","url":"http://image.pinyougou.com/jd/wKgMg1qtKHmAFxj7AAFZsBqChgk725.jpg"},
        // {"color":"银色","url":"http://image.pinyougou.com/jd/wKgMg1qtKJyAHQ9sAAFuOBobu-A759.jpg"}]
        List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (imageList != null && imageList.size() > 0) {
            item.setImage(imageList.get(0).get("url").toString());
        }

        // SKU商品的三级分类id
        item.setCategoryid(goods.getCategory3Id());
        // SKU商品的创建时间
        item.setCreateTime(new Date());
        // SKU商品的修改时间
        item.setUpdateTime(item.getCreateTime());
        // SKU商品关联的SPU的id
        item.setGoodsId(goods.getId());
        // SKU商品的商家id
        item.setSellerId(goods.getSellerId());

        // SKU商品的三级分类名称
        ItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
        item.setCategory(itemCat != null ? itemCat.getName() : "");
        // SKU商品的品牌
        Brand brand = brandMapper.selectByPrimaryKey(goods.getBrandId());
        item.setBrand(brand != null ? brand.getName() : "");
        // SKU商品的店铺名称
        Seller seller = sellerMapper.selectByPrimaryKey(goods.getSellerId());
        item.setSeller(seller != null ? seller.getNickName() : "");
    }

    @Override
    public void update(Goods goods) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Goods findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Goods> findAll() {
        return null;
    }

    @Override
    public PageResult findByPage(Goods goods, int page, int rows) {
        try{
            // 开启分页
            PageInfo<Map<String,Object>> pageInfo = PageHelper.startPage(page, rows)
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        goodsMapper.findAll(goods);
                    }
                });

            List<Map<String,Object>> goodsList = pageInfo.getList();
            // 循环集合
            for (Map<String, Object> map : goodsList) {
                // 判断三级分类id不是空
                if (map.get("category3Id") != null){

                    // 查询一级分类名称
                    String category1Name = itemCatMapper
                            .selectByPrimaryKey(map.get("category1Id")).getName();
                    map.put("category1Name", category1Name);

                    // 查询二级分类名称
                    String category2Name = itemCatMapper
                            .selectByPrimaryKey(map.get("category2Id")).getName();
                    map.put("category2Name", category2Name);

                    // 查询三级分类名称
                    String category3Name = itemCatMapper
                            .selectByPrimaryKey(map.get("category3Id")).getName();
                    map.put("category3Name", category3Name);

                }
            }
            return new PageResult(pageInfo.getTotal(), goodsList);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 修改商品的状态码 */
    public void updateStatus(String columnName, Long[] ids, String status){
        try{
            // UPDATE tb_goods SET audit_status = ? WHERE id IN (?,?)
            goodsMapper.updateStatus(columnName, ids, status);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 根据spu的id查询商品信息 */
    public Map<String,Object> getGoods(Long goodsId){
        try{
            Map<String,Object> dataModel = new HashMap<>();

            // 1. 查询tb_goods
            Goods goods = goodsMapper.selectByPrimaryKey(goodsId);

            // 2. 查询tb_goods_desc
            GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);

            // 3. 查询tb_item
            // SELECT * FROM tb_item WHERE goods_id = 149187842867973 ORDER BY is_default DESC
            Example example = new Example(Item.class);
            // 创建条件对象
            Example.Criteria criteria = example.createCriteria();
            //  goods_id = 149187842867973
            criteria.andEqualTo("goodsId", goodsId);
            //  ORDER BY is_default DESC
            example.orderBy("isDefault").desc();
            // 条件查询
            List<Item> itemList = itemMapper.selectByExample(example);


            dataModel.put("goods", goods);
            dataModel.put("goodsDesc", goodsDesc);
            dataModel.put("itemList", JSON.toJSONString(itemList));

            // 查询一级分类名称
            ItemCat itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id());
            dataModel.put("itemCat1", itemCat1 != null ? itemCat1.getName() : "");

            // 查询二级分类名称
            ItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id());
            dataModel.put("itemCat2", itemCat2 != null ? itemCat2.getName() : "");

            // 查询三级分类名称
            ItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
            dataModel.put("itemCat3", itemCat3 != null ? itemCat3.getName() : "");
            return dataModel;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 根据多个goodsId从tb_item表查询多个SKU商品数据 */
    public List<Item> findItemByGoodsId(Long[] goodsIds){
        try{
            // SELECT * FROM tb_item WHERE goods_id IN (?,?,?)
            Example example = new Example(Item.class);
            // 查询条件对象
            Example.Criteria criteria = example.createCriteria();
            // goods_id IN (?,?,?)
            criteria.andIn("goodsId", Arrays.asList(goodsIds));
            // 条件查询
            return itemMapper.selectByExample(example);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

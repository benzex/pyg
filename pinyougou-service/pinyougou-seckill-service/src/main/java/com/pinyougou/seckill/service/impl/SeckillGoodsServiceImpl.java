package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.pojo.SeckillGoods;
import com.pinyougou.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 秒杀商品服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-22<p>
 */
@Service(interfaceName = "com.pinyougou.service.SeckillGoodsService")
@Transactional
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(SeckillGoods seckillGoods) {

    }

    @Override
    public void update(SeckillGoods seckillGoods) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public SeckillGoods findOne(Serializable id) {
        return null;
    }

    @Override
    public List<SeckillGoods> findAll() {
        return null;
    }

    @Override
    public List<SeckillGoods> findByPage(SeckillGoods seckillGoods, int page, int rows) {
        return null;
    }

    /** 查询正在秒杀的商品 */
    public List<SeckillGoods> findSeckillGoods(){

        // 定义秒杀商品集合
        List<SeckillGoods> seckillGoodsList = null;

        /** ######## 从Redis数据库中查询秒杀商品 ######## */
        try {
            seckillGoodsList = redisTemplate.boundHashOps("seckillGoodsList").values();
            if (seckillGoodsList != null && seckillGoodsList.size() > 0){
                System.out.println("######## 从Redis数据库中查询秒杀商品 ########");
                return seckillGoodsList;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        try{
            // SELECT * FROM `tb_seckill_goods` WHERE start_time <= NOW() AND
            // end_time >= NOW() AND STATUS = 1 AND stock_count > 0
            // 创建示范对象
            Example example = new Example(SeckillGoods.class);

            // 创建条件对象
            Example.Criteria criteria = example.createCriteria();
            // 秒杀商品的开始时间小于等于当前时间 start_time <= NOW()
            criteria.andLessThanOrEqualTo("startTime", new Date());
            // 秒杀商品的结束时间大于等于当前时间 end_time >= NOW()
            criteria.andGreaterThanOrEqualTo("endTime", new Date());
            // 秒杀商品的审核状态码为1 STATUS = 1
            criteria.andEqualTo("status", 1);
            // 秒杀商品的剩余库存数据大于0 stock_count > 0
            criteria.andGreaterThan("stockCount", 0);

            // 条件查询
            seckillGoodsList = seckillGoodsMapper.selectByExample(example);

            /** ######## 把秒杀商品存入Redis数据库 ######## */
            try{
                // 迭代秒杀商品，把秒杀商品存入Redis
                for (SeckillGoods seckillGoods : seckillGoodsList) {
                    redisTemplate.boundHashOps("seckillGoodsList")
                            .put(seckillGoods.getId(), seckillGoods);
                }
                System.out.println("######## 把秒杀商品存入Redis数据库 ########");
            }catch (Exception ex){
                ex.printStackTrace();
            }

            return seckillGoodsList;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 根据秒杀商品id查询秒杀商品对象 */
    public SeckillGoods findSeckillGoodsFromRedis(Long id){
        try{
            return (SeckillGoods) redisTemplate
                    .boundHashOps("seckillGoodsList").get(id);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

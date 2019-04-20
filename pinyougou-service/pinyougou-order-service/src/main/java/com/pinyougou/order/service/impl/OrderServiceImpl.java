package com.pinyougou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.Cart;
import com.pinyougou.common.util.IdWorker;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.mapper.OrderMapper;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-20<p>
 */
@Service(interfaceName = "com.pinyougou.service.OrderService")
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;

    @Override
    public void save(Order order) {
        try{

            // 获取该用户的购物车
            List<Cart> cartList = (List<Cart>)redisTemplate
                    .boundValueOps("cart_" + order.getUserId()).get();

            // 一个Cart代表一个商家的购物车，产生一个订单
            for (Cart cart : cartList) {
                // 创建订单对象
                Order order1 = new Order();
                // 生成订单id
                long orderId = idWorker.nextId();
                // 设置订单id
                order1.setOrderId(orderId);
                // 订单支付方式
                order1.setPaymentType(order.getPaymentType());
                // 订单支付状态码: 未付款
                order1.setStatus("1");
                // 订单创建时间
                order1.setCreateTime(new Date());
                // 订单修改时间
                order1.setUpdateTime(order1.getCreateTime());
                // 订单关联的用户
                order1.setUserId(order.getUserId());
                // 订单收件地址
                order1.setReceiverAreaName(order.getReceiverAreaName());
                // 订单收件人手机号码
                order1.setReceiverMobile(order.getReceiverMobile());
                // 订单收件人姓名
                order1.setReceiver(order.getReceiver());
                // 订单的来源
                order1.setSourceType(order.getSourceType());
                // 订单关联的商家id
                order1.setSellerId(cart.getSellerId());


                // 定义订单总金额
                double money = 0;

                // 迭代商家购物车中的商品
                for (OrderItem orderItem : cart.getOrderItems()) {
                    // 设置主键id
                    orderItem.setId(idWorker.nextId());
                    // 设置关联的订单id
                    orderItem.setOrderId(orderId);

                    // 计算订单的总金额
                    money += orderItem.getTotalFee().doubleValue();

                    // 往tb_order_item表插入数据
                    orderItemMapper.insertSelective(orderItem);
                }

                // 订单支付的总金额
                order1.setPayment(new BigDecimal(money));
                // 往tb_order表插入数据
                orderMapper.insertSelective(order1);
            }


            // 删除Redis中购物车数据
            redisTemplate.delete("cart_" + order.getUserId());

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Order findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public List<Order> findByPage(Order order, int page, int rows) {
        return null;
    }
}

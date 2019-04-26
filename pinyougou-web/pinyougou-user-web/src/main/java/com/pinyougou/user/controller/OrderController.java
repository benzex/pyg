package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Order;
import com.pinyougou.service.OrderService;
import com.pinyougou.service.WeixinPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController  {

	@Reference(timeout = 10000)
	private OrderService orderService;
	@Reference(timeout = 10000)
	private WeixinPayService weixinPayService;


	@GetMapping("/getOrderByUserId")
	public PageResult getOrderByUserId(int page , int rows) {
		SecurityContext context = SecurityContextHolder.getContext();
		String userId = context.getAuthentication().getName();

		if (StringUtils.isNotBlank(userId)) {
			return orderService.findOrderByUserIdPage(userId, page, rows);
		}
		return null;
	}

	@GetMapping("/payCode")
	public Map<String,Object> payCode(String orderId){
		Order order = orderService.findOrderbyOrderId(orderId);

		return weixinPayService.genPayCode(String.valueOf(orderId),String.valueOf(order.getPayment().doubleValue() * 100));
	}

	@GetMapping("/queryPayStatus")
	public Map<String,Integer> queryPayStatus(String outTradeNo){
		Map<String, String> map = weixinPayService.queryPayStatus(outTradeNo);
		HashMap<String, Integer> data = new HashMap<>();
		if (map.size()>0){
			if ("SUCCESS".equals(map.get("trade_state"))){
				orderService.updatePayStatus(outTradeNo,map.get("transaction_id"));
				data.put("status",1);
			}
			if ("NOTPAY".equals(map.get("trade_state"))){
				data.put("status",3);
			}
		}
		return data;
	}
}

package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.service.OrderService;
import com.pinyougou.service.WeixinPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-20<p>
 */
@RestController
@RequestMapping("/order")
public class OrderController {

	@Reference(timeout = 10000)
	private OrderService orderService;
	@Reference(timeout = 10000)
	private WeixinPayService weixinPayService;

	/**
	 * 保存订单
	 */
	@PostMapping("/save")
	public boolean save(@RequestBody Order order, HttpServletRequest request) {
		try {
			// 获取登录用户名
			String userId = request.getRemoteUser();
			order.setUserId(userId);
			orderService.save(order);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 生成微信支付
	 */
	@GetMapping("/genPayCode")
	public Map<String, Object> genPayCode(HttpServletRequest request) {
		// 获取登录用户名
		String userId = request.getRemoteUser();
		// 从Redis数据库中获取支付日志
		PayLog payLog = orderService.findPayLogFromRedis(userId);
		return weixinPayService.genPayCode(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
	}

	/**
	 * 检测支付状态
	 */
	@GetMapping("/queryPayStatus")
	public Map<String, Integer> queryPayStatus(String outTradeNo) {
		Map<String, Integer> data = new HashMap<>();
		data.put("status", 3);
		try {
			// 调用微信支付服务接口
			Map<String, String> resMap = weixinPayService.queryPayStatus(outTradeNo);

			if (resMap != null) {
				// SUCCESS-支付成功
				if ("SUCCESS".equals(resMap.get("trade_state"))) {

					// 支付成功，修改支付日志的状态、订单的状态
					orderService.updatePayStatus(outTradeNo, resMap.get("transaction_id"));

					data.put("status", 1);
				}
				// NOTPAY—未支付
				if ("NOTPAY".equals(resMap.get("trade_state"))) {
					data.put("status", 2);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}


}

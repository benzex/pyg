package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController  {

	@Reference(timeout = 10000)
	private OrderService orderService;

	@GetMapping("/getOrderByUserId")
	public PageResult getOrderByUserId(int page , int rows) {
		SecurityContext context = SecurityContextHolder.getContext();
		String userId = context.getAuthentication().getName();
		System.out.println("userName" + userId);
		if (StringUtils.isNotBlank(userId)) {
			return orderService.findOrderByUserIdPage(userId,page,rows);
		}
		return null;
	}
}

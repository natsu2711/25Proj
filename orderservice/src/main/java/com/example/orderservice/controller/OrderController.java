package com.example.orderservice.controller;

import com.example.orderservice.entity.Order;
//import com.example.orderservice.feign.UserClient;
import com.example.orderservice.service.OrderService;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String createOrder(){
        Order order =new Order();
        order.setProductId(1L);
        order.setUserId(1L); // 暂时硬编码为用户ID 1
        order.setUsername("natsu"); // 暂时硬编码用户名
        order.setStatus(0); // 0 通常代表“待支付”

        order.setOrderNo(UUID.randomUUID().toString().replace("-", "")); // 1. 生成一个唯一的订单号
        order.setQuantity(1);   // 购买1件
        order.setPrice(new BigDecimal("7999.00"));
        orderService.createOrder(order);// 调用service层进行创建
        return "订单创建成功";

    }
    /* public Order createOrder(@RequestParam("userId") Long userId,@RequestParam("productId") Long productId){
        return orderService.createOrder(userId, productId);
    } */

}
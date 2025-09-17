package com.example.orderservice.controller;

import com.example.orderservice.feign.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserClient userClient;

    @GetMapping("/create")
    public String createOrder() {
        // ... 创建订单的逻辑 ...

        // 调用用户服务
        String userInfo = userClient.findById(1);

        return "Order created successfully! User info: " + userInfo;
    }
}
package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderCreateDTO;
import com.example.orderservice.dto.Result;
import com.example.orderservice.entity.Order;
//import com.example.orderservice.feign.UserClient;
import com.example.orderservice.service.OrderService;

import lombok.extern.slf4j.Slf4j;

//import java.math.BigDecimal;
//import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j; // <-- 确保导入了这个包


@RestController
@RequestMapping("/order")
@Slf4j // <--- 在这里添加注解
public class OrderController {

    @Autowired
    private OrderService orderService;

    /* @GetMapping("/create")
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

    } */
   
    /* public Order createOrder(@RequestParam("userId") Long userId,@RequestParam("productId") Long productId){
        return orderService.createOrder(userId, productId);
    } */

    //构建真实的创建订单接口
    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody OrderCreateDTO orderDto){
        try{
            Order newOrder = orderService.createOrder(orderDto);
            return Result.success(newOrder);
        }catch (Exception e){
            //捕获业务异常，返回友好的错误信息
            return Result.error(500, e.getMessage());
        }
    }
    
    @RestController
    public class TestController{
        @Autowired
        private StreamBridge streamBridge;
        // 创建一个简单的GET接口，用于手动触发消息发送
        @GetMapping("/test/send")
        public String testSend(){
            String message = "这是一条来自order-service的测试消息: " + System.currentTimeMillis();
            log.info("准备手动发送测试消息: {}", message);
            boolean isSuccess = streamBridge.send("orderSuccess-out-0", message);
            log.info("手动发送结果: {}", isSuccess);
            return "Message sent: " + isSuccess;


        }

    }

}
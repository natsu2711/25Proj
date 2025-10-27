package com.example.orderservice.service.impl;

import java.text.SimpleDateFormat;
//import java.math.BigDecimal;
import java.util.Date;
//import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orderservice.dto.OrderCreateDTO;
//import com.example.orderservice.dto.UserDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.Product;
import com.example.orderservice.feign.ProductClient;
//import com.example.orderservice.feign.UserClient;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.service.OrderService;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Random;


@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService{
    //@Autowired
    //private UserClient userClient;  //注入Feign客户端

    @Autowired
    private ProductClient productClient; // 假设已创建Feign客户端

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @GlobalTransactional // <-- Seata的核心，开启全局事务
    public Order createOrder(OrderCreateDTO orderDto){
        /* System.out.println("orderservice:开始创建订单，全局事务XID:" + io.seata.core.context.RootContext.getXID());
        // 1. 创建订单 (操作本地 order_db)
        System.out.println("正在创建订单记录...");
        orderMapper.insert(order);
        System.out.println("订单记录创建成功。");

        // 2. 远程调用商品服务扣减库存 (通过Feign)
        System.out.println("正在通过Feign远程调用product-service扣减库存...");
        productClient.deductStock(order.getProductId(), order.getQuantity());
        System.out.println("库存扣减成功。"); */

        //人为制造异常，用于测试回滚场景
       /*  if(true){
            throw new RuntimeException("这是order-service人为制造的异常！");
        } */

        log.info("order-service: 开始创建订单，全局事务XID: {}", io.seata.core.context.RootContext.getXID());

        // 1. [远程调用] 通过Feign客户端查询商品信息
        log.info("正在通过Feign远程调用product-service查询商品信息，productId: {}", orderDto.getProductId());
        Product product = productClient.getById(orderDto.getProductId());
        //[业务校验] 检查商品是否存在
        if (Objects.isNull(product)){
            throw new RuntimeException("订单创建失败：商品不存在！");

        }
        log.info("商品信息查询成功: {}", product);
        // 3. [业务逻辑] 组装订单实体
        Order order = new Order();
        order.setProductId(orderDto.getProductId());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(product.getPrice());
        order.setCreateTime(new Date());

        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + new Random().nextInt(1000);
        order.setOrderNo(orderNo);

         order.setUserId(orderDto.getUserId()); // 从DTO中获取userId并设置

        //4. [本地事务] 创建订单记录
        log.info("正在创建订单记录,订单号: {}",order.getOrderNo());
        orderMapper.insert(order);
        log.info("订单记录创建成功, orderId:{}",order.getId());

        //// 5. [远程调用] 调用商品服务扣减库存
        log.info("正在通过Feign远程调用product-service扣减库存...");
        productClient.deductStock(orderDto.getProductId(),orderDto.getQuantity());
        log.info("库存扣减调用成功。");

        log.info("order-service: 订单创建流程结束。");
        return order;

    }

    public void afterOrderCreate(Order order){
        // 3. 核心事务成功后，发送异步消息
        log.info("准备发送订单成功消息...");
        // 将订单对象转为JSON字符串发送
        String orderJson = JSON.toJSONString(order);
        // 使用在配置中定义的绑定名 "orderSuccess-out-0"
        boolean isSuccess = streamBridge.send("orderSuccess-out-0",MessageBuilder.withPayload(orderJson).build());
        if(isSuccess){
            log.info("订单成功消息发送成功！");
            
        }else{
            log.error("订单成功消息发送失败！");

        }


    }
    /* public Order createOrder(Long userId,Long productId)
    {
        System.out.println("开始调用用户服务, 用户ID: " + userId);
        
        UserDTO user = userClient.findById(userId);
        System.out.println("成功获取用户信息: " + user);
        if(user==null){
            throw new RuntimeException("用户不存在");
        
        }

        BigDecimal price =new BigDecimal("99.99");

        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setUserId(user.getId());
        order.setUsername(user.getUsername());

        order.setProductId(productId);
        order.setPrice(price);
        order.setStatus(0); //待支付

        this.save(order);
        //baseMapper.insert(order);

        // 2. 调用商品服务扣减库存 (通过Feign)
        //productClient.deductStock(order.getProductId(),order.getQuantity());


        return order;

    } */

}

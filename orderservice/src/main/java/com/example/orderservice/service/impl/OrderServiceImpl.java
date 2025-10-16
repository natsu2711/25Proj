package com.example.orderservice.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.example.orderservice.dto.UserDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.feign.ProductClient;
import com.example.orderservice.feign.UserClient;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.service.OrderService;

import io.seata.spring.annotation.GlobalTransactional;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService{
    //@Autowired
    //private UserClient userClient;  //注入Feign客户端

    @Autowired
    private ProductClient productClient; // 假设已创建Feign客户端

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @GlobalTransactional // <-- Seata的核心，开启全局事务
    public void createOrder(Order order){
        System.out.println("orderservice:开始创建订单，全局事务XID:" + io.seata.core.context.RootContext.getXID());
        // 1. 创建订单 (操作本地 order_db)
        System.out.println("正在创建订单记录...");
        orderMapper.insert(order);
        System.out.println("订单记录创建成功。");

        // 2. 远程调用商品服务扣减库存 (通过Feign)
        System.out.println("正在通过Feign远程调用product-service扣减库存...");
        productClient.deductStock(order.getProductId(), order.getQuantity());
        System.out.println("库存扣减成功。");

        //人为制造异常，用于测试回滚场景
       /*  if(true){
            throw new RuntimeException("这是order-service人为制造的异常！");
        } */

        System.out.println("order-service: 订单创建流程结束。");






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

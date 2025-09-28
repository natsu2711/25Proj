package com.example.orderservice.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orderservice.dto.UserDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.feign.UserClient;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.service.OrderService;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService{
    @Autowired
    private UserClient userClient;  //注入Feign客户端

    @Override
    public Order createOrder(Long userId,Long productId)
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
        return order;



    }

}

package com.example.orderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.orderservice.dto.OrderCreateDTO;
import com.example.orderservice.entity.Order;

public interface OrderService extends IService<Order> {
    //未来可以在这里定义创建订单等复杂业务方法
    //Order createOrder(Long userId,Long productId);

    Order createOrder(OrderCreateDTO orderDto);
    
}

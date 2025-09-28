package com.example.productservice.service.impl;
import com.example.productservice.entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.service.ProductService;

@Service// 告诉Spring这是一个服务类，需要被管理
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    // ServiceImpl同样提供了常用业务方法的实现

    @Autowired
    private StringRedisTemplate redisTemplate; // 使用StringRedisTemplate，方便处理JSON
    public static final String CACHE_PRODUCT_KEY = "product"; //缓存key的前缀


    //@Override
    //public Product findById(Long id){
        //1查询Redis缓存
        //String productJson =redisTemplate.opsForValue().get(CACHE_PRODUCT_KEY + id);

    //}


    
}



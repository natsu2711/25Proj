package com.example.productservice.service.impl;
import com.example.productservice.entity.Product;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.service.ProductService;

@Service// 告诉Spring这是一个服务类，需要被管理
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    // ServiceImpl同样提供了常用业务方法的实现

    @Autowired
    private StringRedisTemplate redisTemplate; // 使用StringRedisTemplate，方便处理JSON
    public static final String CACHE_PRODUCT_KEY = "product"; //缓存key的前缀


    @Override
    public Product findById(Long id){
        //1查询Redis缓存
        String productJson =redisTemplate.opsForValue().get(CACHE_PRODUCT_KEY + id);
        //2a缓存命中，直接返回
        if (StringUtils.hasText(productJson)) {// 使用Spring的StringUtils
            return JSON.parseObject(productJson,Product.class);// 假设使用FastJSON
            
        }
        //2b缓存未命中，查询数据库
        Product product = baseMapper.selectById(id);

        //数据库有数据
        if(product !=null){
            //4a序列化为JSON并写入Redis，设置30分钟过期
            redisTemplate.opsForValue().set(CACHE_PRODUCT_KEY+id,JSON.toJSONString(product),30,TimeUnit.MINUTES);
            //5返回数据
            return product;
        }
        //数据库也无数据(待完善)
        return null;



    }


    
}



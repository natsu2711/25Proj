package com.example.productservice.config;
import java.util.concurrent.TimeUnit;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.productservice.entity.Product;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {
    @Bean
    public Cache<String,Product> productCache(){
        return Caffeine.newBuilder()
        // 设置初始容量为100
        .initialCapacity(100)
        // 设置最大容量为500，超过后会用LRU算法淘汰
        .maximumSize(500)
        // 设置写缓存后10分钟过期
        .expireAfterWrite(10,TimeUnit.MINUTES)
        .build();



    }
    
}

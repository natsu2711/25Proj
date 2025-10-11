package com.example.productservice.service.impl;
import com.example.productservice.entity.Product;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.service.ProductService;

@Service// 告诉Spring这是一个服务类，需要被管理
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    // ServiceImpl同样提供了常用业务方法的实现

    // ================== 在这里添加常量的定义 ==================
    public static final String CACHE_PRODUCT_KEY_PREFIX = "product:";

    @Autowired
    private StringRedisTemplate redisTemplate; // 使用StringRedisTemplate，方便处理JSON
    public static final String CACHE_PRODUCT_KEY = "product"; //缓存key的前缀

 
 /*    public Product findById(Long id){
        //1查询Redis缓存
        String productJson =redisTemplate.opsForValue().get(CACHE_PRODUCT_KEY + id);
        //2a缓存命中，直接返回
        if (StringUtils.hasText(productJson)) {// 使用Spring的StringUtils
            return JSON.parseObject(productJson,Product.class);// 假设使用FastJSON
            
        }
        //2b缓存未命中，查询数据库
        Product product = baseMapper.selectById(id);
        //查询报错提示,将数据库映射和JSON序列化两个环节完全分开
        System.out.println("--- DEBUG: 从数据库查出的Product对象: " + product);

        //数据库有数据
        if(product !=null){
            //4a序列化为JSON并写入Redis，设置30分钟过期
            redisTemplate.opsForValue().set(CACHE_PRODUCT_KEY+ id ,JSON.toJSONString(product),30,TimeUnit.MINUTES);
            //5返回数据
            return product;
        }
        //数据库也无数据(待完善)
        return null;



    }

 */
    
    @Override
    public Product getById(Long id) {
        //1查询Redis缓存
        String key = CACHE_PRODUCT_KEY_PREFIX + id;
        String productJson = redisTemplate.opsForValue().get(key);

        // // 2a. 缓存命中，直接返回
        // if (StringUtils.hasText(productJson)) {
        //     System.out.println("--- LOG: Cache HIT! Returning from Redis. ---");
        //     return JSON.parseObject(productJson, Product.class);
        // }

        // 2. 缓存命中
        if(productJson !=null){
            // 2a. 如果命中但存的是空字符串，说明是“空对象”，直接返回null
            if(productJson.isEmpty()){
                System.out.println("--- LOG: Cache HIT on NULL Object for ID: " + id + ". Preventing penetration. ---");
                return null;
            
            }
            // 2b. 命中且有数据，反序列化返回
            System.out.println("--- LOG: Cache HIT! Returning from Redis for ID: " + id + ". ---");
            return JSON.parseObject(productJson,Product.class);
        }

        //  2b.缓存未命中
        System.out.println("--- LOG: Cache MISS. Querying database... ---");
        //  3.查询数据库
        Product product = baseMapper.selectById(id);

        // 4.数据库有数据
        if (product != null) {
            // 4a. 数据库有数据，正常缓存
            System.out.println("--- LOG: Database returned object: " + product);
            try {
                String jsonToCache = JSON.toJSONString(product);
                System.out.println("--- LOG: JSON serialization SUCCESS. JSON: " + jsonToCache);
                
                // 4a. 序列化为JSON并写入Redis，设置30分钟过期
                redisTemplate.opsForValue().set(key, jsonToCache, 30, TimeUnit.MINUTES);
                System.out.println("--- LOG: Redis SET command SUCCESS. ---");

            } catch (Exception e) {
                System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.err.println("--- FATAL ERROR during caching process! ---");
                e.printStackTrace(); // This will print the exact error
                System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        } else {
            System.out.println("--- LOG: Database returned NULL. ---");
            // Here you would cache the null object to prevent cache penetration
            // 4b. 数据库也无数据，缓存空对象
            // 写入一个空字符串作为标识，过期时间设置短一些，防止垃圾数据过多
            System.out.println("--- LOG: Database returned NULL for ID: " + id + ". Caching NULL object... ---");
            redisTemplate.opsForValue().set(key, "", 5, TimeUnit.MINUTES);
            //return null;
        }
        
        // 5. 返回数据
        return product;
    }

}



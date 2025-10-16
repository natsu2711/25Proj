package com.example.productservice.service.impl;
import com.example.productservice.entity.Product;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.service.ProductService;
import com.github.benmanes.caffeine.cache.Cache;

@Service// 告诉Spring这是一个服务类，需要被管理
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    // ServiceImpl同样提供了常用业务方法的实现

    // ================== 在这里添加常量的定义 ==================
    public static final String CACHE_PRODUCT_KEY_PREFIX = "product:";

    @Autowired
    private StringRedisTemplate redisTemplate; // 使用StringRedisTemplate，方便处理JSON
    public static final String CACHE_PRODUCT_KEY = "product"; //缓存key的前缀
    private static final Random RANDOM = new Random();
    @Autowired
    private Cache<String,Product> productCache; // 注入我们配置的Caffeine缓存Bean


 
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
        
        String key = CACHE_PRODUCT_KEY_PREFIX + id;
        
        String caffeineKey = id.toString();

        // 1. 先查询本地缓存 (Caffeine)
        Product product = productCache.getIfPresent(caffeineKey);
        if(product != null){
            System.out.println("--- LOG: Level 1 Cache (Caffeine) HIT for ID: " + id + " ---");
            return product;
        }


        //1查询Redis缓存 //2. 本地缓存未命中，查询远程缓存（Redis）
        String productJson = null;
        try{
            productJson = redisTemplate.opsForValue().get(key);
        }catch(Exception e){
            System.err.println("--- WARN: Failed to get from Redis. Redis might be down. ---");
        }

        
        // 2. 缓存命中
        /* if(StringUtils.hasText(productJson)){
            System.out.println("--- LOG: Level 2 Cache (Redis) HIT for ID: " + id + " ---");
            product = JSON.parseObject(productJson,Product.class);
            // 将从Redis查到的数据，回填到本地缓存
            productCache.put(caffeineKey,product);
            return product; */

        if (productJson != null){
            System.out.println("--- LOG: Level 2 Cache (Redis) HIT for ID: " + id + " ---");
            // 如果是空字符串，说明是缓存的“空对象”
            // 2a. 如果命中但存的是空字符串，说明是“空对象”，直接返回null
            if(productJson.isEmpty()){
                System.out.println("--- LOG: Cache HIT on NULL Object for ID: " + id + ". Preventing penetration. ---");
                return null;
            
            }
            // 2b. 命中且有数据，反序列化返回
            System.out.println("--- LOG: Cache HIT! Returning from Redis for ID: " + id + ". ---");
            product=JSON.parseObject(productJson,Product.class); 
            // 回填本地缓存
            if(product !=null){
                productCache.put(caffeineKey,product);
            }
            return product;
        }


        

        //  2b.多级缓存未命中//开始加锁
        synchronized(id.toString().intern()){// 在分布式环境下应使用分布式锁，如Redisson
            String threadName = Thread.currentThread().getName(); // 获取当前线程名
            // 双重检查锁定 (Double-Checked Locking)
            // 再次检查缓存，因为可能在你等待锁的时候，别的线程已经把缓存写好了,直接返回
            product = productCache.getIfPresent(caffeineKey);
            if(product != null){
                return product;
            }

            try{
                productJson = redisTemplate.opsForValue().get(key);
            }catch(Exception e){
                System.err.println("--- WARN: (DCL) Failed to get from Redis. Redis might be down. ---");
            }

            //要把这一句变成带安全气囊productJson = redisTemplate.opsForValue().get(key);
            if(productJson != null){
                if(productJson.isEmpty()){
                    return null;
                }
                // 在这里加入线程名日志
                System.out.println(String.format("--- THREAD: %s --- LOG: Double-Checked HIT! Found cache after acquiring lock.", threadName));
                product = JSON.parseObject(productJson,Product.class);
                if(product !=null){
                    productCache.put(caffeineKey,product);
                }
                
                return product;
                
            /*     if(productJson.isEmpty()){                  
                    return null;
                }

                return JSON.parseObject(productJson,Product.class); */

            }
            // 5. 缓存确实为空，现在可以安全地查询数据库了
            // 在这里加入线程名日志
            System.out.println(String.format("--- THREAD: %s --- LOG: Double-Checked Locking passed. DB query is necessary.", threadName));
            System.out.println("--- LOG: Double-Checked Locking passed. DB query is necessary for ID: " + id + " ---");
            product = super.getById(id);

            //6. 根据数据库查询结果，进行缓存操作
            if (product != null) {
                // 6a. 数据库有数据，正常缓存
                // 回填Redis
                String jsonToCache=JSON.toJSONString(product);
                // 加入随机过期时间，防止缓存雪崩
                try{
                    long ttlMinutes= 30L+ RANDOM.nextInt(10);// 基础时间30分钟，再加0-9分钟的随机数
                    redisTemplate.opsForValue().set(key,jsonToCache,ttlMinutes,TimeUnit.MINUTES); 

                }catch (Exception e){
                    System.err.println("--- WARN: Failed to set to Redis. Redis might be down. ---");
                }               
                // 回填Caffeine
                productCache.put(caffeineKey, product);              
            }else{
                // 6b. 数据库无数据，只在Redis中缓存空对象，防止污染本地缓存
                try{
                    long nullTtlMinutes = 5L +RANDOM.nextInt(5);// 基础时间5分钟，再加0-4分钟的随机数
                    redisTemplate.opsForValue().set(key,"",nullTtlMinutes,TimeUnit.MINUTES);
                }catch (Exception e){
                    System.err.println("--- WARN: Failed to set NULL object to Redis. Redis might be down. ---");

                }
                               
            }
            //7. 返回从数据库中查出的结果
            return product;
        }//锁释放

        
        

/*         // 4.数据库有数据
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
        return product; */

    }


    @Override
    @Transactional //开启本地事务，保证库存查询和更新的原子性
    public void deductStock(Long productId,Integer quantity){
        System.out.println("product-service: 开始扣减库存");

        //1查询产品获取当前库存
        Product product = baseMapper.selectById(productId);
        if(product == null){
            throw new RuntimeException("商品不存在！");

        }

        //2检查库存是否充足
        if(product.getStock()<quantity){
            throw new RuntimeException("商品库存不足！");
        }

        //3扣减库存
        System.out.println("当前库存:" + product.getStock() + ",需要扣减：" + quantity );
        product.setStock(product.getStock()-quantity);
        baseMapper.updateById(product);
        System.out.println("productservice:库存扣减成功");

        //人为制造异常，用于测试回滚场景
        /* if(true){
            throw new RuntimeException("这是productservice人为制造的异常！");
        }*/
            
        } 




    }





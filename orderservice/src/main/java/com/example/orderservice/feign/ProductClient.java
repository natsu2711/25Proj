package com.example.orderservice.feign;

import com.example.orderservice.entity.Product; // 注意：需要将Product实体类从product-service复制一份到order-service

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("productservice")
public interface ProductClient {

    //声明一个与product-service中Controller完全一致的方法
    @PostMapping("/product/deduct")
    void deductStock(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity);
    
    //声明一个远程调用product-service查询商品的方法
    @GetMapping("/product/{id}")
    Product getById(@PathVariable("id") Long id);


    
} 

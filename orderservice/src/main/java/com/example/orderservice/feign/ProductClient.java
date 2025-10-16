package com.example.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("productservice")
public interface ProductClient {

    //声明一个与product-service中Controller完全一致的方法
    @PostMapping("/product/deduct")
    void deductStock(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity);


    
} 

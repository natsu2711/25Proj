package com.example.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("userservice") //声明这是调用的“userservice服务器
public interface UserClient {
    @GetMapping("/user/{id}") // 完整复制要调用的Controller方法的路径和注解
    String findById(@PathVariable("id") Integer id);
}


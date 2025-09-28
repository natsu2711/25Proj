package com.example.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.orderservice.dto.UserDTO;

@FeignClient("userservice") //声明这是调用的“userservice服务器
public interface UserClient {
    @GetMapping("/user/{id}") // 用get方法获取用户服务中这个地址的内容
    UserDTO findById(@PathVariable("id") Long id);
}


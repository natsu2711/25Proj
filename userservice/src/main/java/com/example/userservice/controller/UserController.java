package com.example.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;

@RefreshScope //在任何需要刷新配置的Bean上添加
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired //之前feign的时候也出现过，相当于入驻
    private UserService userService;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        // 调用Service层的方法，该方法会再调用Mapper从数据库查询
        User user = userService.getById(id);
        return user;
    }
    // 【关键】注入Nacos中的测试属性
    @Value("${user.profile}")
    private String userProfile;

    // 【关键】创建一个新的端点来查看这个属性的值
    @GetMapping("/profile")
    public String getProfile() {
        return userProfile;
    }
}
package com.example.userservice.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.UserService;

@Service // 告诉Spring这是一个服务类，需要被管理
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
     // ServiceImpl同样提供了常用业务方法的实现
}

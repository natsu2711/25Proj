package com.example.userservice.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.example.userservice.entity.User;


public interface UserService extends IService<User> {
    // 继承了IService常用的业务方法，以后也可以自定义业务方法
    
}

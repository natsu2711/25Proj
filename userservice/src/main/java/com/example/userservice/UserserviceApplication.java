package com.example.userservice;

import org.mybatis.spring.annotation.MapperScan; // 1. 导入这个包
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient // 开启服务发现
@MapperScan("com.example.userservice.mapper") // 2. 添加这一行
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

}

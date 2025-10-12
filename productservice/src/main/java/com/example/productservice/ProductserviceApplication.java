package com.example.productservice;

import com.example.productservice.config.CacheConfig; // 确保导入了CacheConfig
import org.springframework.context.annotation.Import; // 确保导入了Import注解


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.example.productservice.mapper") // 2. 添加这一行
@EnableCaching //开启Spring的缓存功能
@Import(CacheConfig.class)// <-- 添加这行强制点名的注解！
public class ProductserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductserviceApplication.class, args);
	}

}
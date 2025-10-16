package com.example.productservice.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.entity.Product;


public interface ProductService extends IService<Product> {

    public Product getById(Long id);
    // 继承了IService常用的业务方法，以后也可以自定义业务方法

    public void deductStock(Long productId, Integer quantity);
    
}





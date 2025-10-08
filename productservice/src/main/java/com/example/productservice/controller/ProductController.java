package com.example.productservice.controller;


import com.example.productservice.service.ProductService;
import com.example.productservice.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/product")

public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")

    public Product findById(@PathVariable Long id){

        if(this.productService == null){
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.err.println("--- FATAL: productService is NULL! Injection failed! ---");
            return null;

        }else{
            System.out.println("--- LOG: productService is NOT NULL. Proceeding to call getById... ---");

        }
        return productService.getById(id); //假设service中已有此方法
    }
    
        
    
    
}

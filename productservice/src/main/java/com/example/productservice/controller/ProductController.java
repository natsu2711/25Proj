package com.example.productservice.controller;


import com.example.productservice.service.ProductService;

//import net.bytebuddy.asm.Advice.Return;

import com.example.productservice.entity.Product;

//import org.antlr.v4.parse.ANTLRParser.id_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/product")

public class ProductController {

    @Autowired
    private ProductService productService;

    /* @GetMapping("/{id}")

    public Product findById(@PathVariable Long id){

        if(this.productService == null){
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.err.println("--- FATAL: productService is NULL! Injection failed! ---");
            return null;

        }else{
            System.out.println("--- LOG: productService is NOT NULL. Proceeding to call getById... ---");

        }
        return productService.getById(id); //假设service中已有此方法
    } */


    //扣减库存的接口
     @PostMapping("/deduct")
     public void deductStock(@RequestParam("productId") Long productId,@RequestParam("quantity") Integer quantity){
        productService.deductStock(productId,quantity);
     }
    

    //新增接口让orderservice能查询商品信息（比如价格）
    @GetMapping("/{id}")
    public Product getById(@PathVariable("id") Long id){
        return productService.getById(id);
    }
        
    
    
}

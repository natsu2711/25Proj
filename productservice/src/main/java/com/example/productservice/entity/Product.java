package com.example.productservice.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("product") // 注意表名是 orders
public class Product {
    
    @TableId("product_id")
    private Long id;
    private String productNo;
    private BigDecimal price;
    private Long userId;
    private String username;
    private Integer stock;
    private Date createTime;
    private Date updateTime;
    
}



    


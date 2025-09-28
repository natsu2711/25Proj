package com.example.orderservice.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;


@Data
@TableName("orders") // 注意表名是 orders

public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private Long productId;
    private BigDecimal price;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
    


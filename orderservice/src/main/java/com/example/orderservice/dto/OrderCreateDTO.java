package com.example.orderservice.dto;

import lombok.Data;

@Data
public class OrderCreateDTO {
    
    private String orderNo;
    private Long userId;
    private Long productId;
    private Integer quantity;



}

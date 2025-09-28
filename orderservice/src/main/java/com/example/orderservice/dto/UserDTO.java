package com.example.orderservice.dto;

import lombok.Data;

// 这个类是专门用来接收 user-service 返回的数据的
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    // 注意：我们可能不需要用户的密码、更新时间等敏感或无关信息
    
}

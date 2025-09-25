package com.example.userservice.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data // Lombok注解，自动生成getter/setter/toString等方法
@TableName("user") // MyBatis-Plus注解，告诉框架这个类对应数据库的"user"表
public class User {
        // 字段名要和数据库表的列名一致（驼峰命名法会自动映射到下划线命名法，如createTime -> create_time）
    private Long id;
    private String username;
    private String password;
    private String email;
    private Date createTime;
    private Date updateTime;
    
}

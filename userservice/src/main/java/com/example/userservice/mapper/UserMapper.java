package com.example.userservice.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.userservice.entity.User;

@Mapper  // 告诉Spring这是一个Mapper接口
public interface UserMapper extends BaseMapper<User> {
        // 这里是空的，因为BaseMapper已经帮我们写好了 insert, delete, update, selectById, selectList等无数方法。
    // 如果有复杂的多表查询，未来可以在这里手写SQL。
    
}

package com.example.productservice.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.productservice.entity.Product;


@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // 这里是空的，因为BaseMapper已经帮我们写好了 insert, delete, update, selectById, selectList等无数方法。
    // 如果有复杂的多表查询，未来可以在这里手写SQL。
    
}



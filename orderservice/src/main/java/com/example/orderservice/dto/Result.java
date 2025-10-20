package com.example.orderservice.dto;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;// 状态码, 200表示成功, 其他表示失败
    private String message;// 提示信息
    private T data;// 数据载荷
    public static <T> Result<T> success(T data){
        Result<T> result=new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result ;
    }

    public static<T> Result<T> error(int code,String message){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    
}

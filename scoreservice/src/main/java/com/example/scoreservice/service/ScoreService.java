package com.example.scoreservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration // <--- 将其标记为一个配置类，这是最重要的修改！
public class ScoreService {
    private static final Logger log=LoggerFactory.getLogger(ScoreService.class);

    @Bean
    public Consumer<Message<String>> orderSuccess(){
        return message ->{
            log.info("接收到订单成功消息！");
            log.info("消息头：{}",message.getHeaders());
            log.info("消息体(订单JSON):{}",message.getPayload());
            log.info("准备为用户增加积分...");
            // 在这里执行增加积分的业务逻辑
            log.info("积分增加成功！");
        };
        
    }

    
}

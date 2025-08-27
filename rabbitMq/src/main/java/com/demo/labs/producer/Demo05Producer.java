package com.demo.labs.producer;

import com.demo.labs.message.Demo05Message;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Demo05Producer {
    @Autowired
    private BatchingRabbitTemplate batchRabbitTemplate;

    public void syncSend(Integer id) {
        // 创建 Demo05Message 消息
        Demo05Message message = new Demo05Message();
        message.setId(id);
        // 同步发送消息
        batchRabbitTemplate.convertAndSend(Demo05Message.EXCHANGE, Demo05Message.ROUTING_KEY, message);
    }
}

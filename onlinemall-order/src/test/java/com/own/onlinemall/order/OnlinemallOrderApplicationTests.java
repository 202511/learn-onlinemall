package com.own.onlinemall.order;

import com.own.onlinemall.order.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class OnlinemallOrderApplicationTests {
    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Test
    void sendMessage()
    {

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(12l);

        rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", orderEntity);
        System.out.println("消息发送成功");
    }




}

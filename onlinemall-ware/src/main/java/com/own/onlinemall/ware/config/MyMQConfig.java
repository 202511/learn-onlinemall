package com.own.onlinemall.ware.config;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;

@Configuration
public class MyMQConfig {
    //懒加载 只有到用时 才加载
    // 第一次要使用一个监听器 ，监听任意组件


    // 模拟延时队列
    // 一个存储死信的队列
    // 一个存储广播消息的队列
    //自动注入
    // 这样子就能自动的添加队列，  ， 队列和交换机只创建一次， 属性发生变化不会发生覆盖
    //@Bean 容器中 rabbitmq 的队列， 交换机 会自动使用rabbit提供的方法进行创建
    @Bean // 延时队列
    public Queue stockDelayQueue()
    {
        // 设置属性， 超时时间， 转发的路由键 和交换器
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "stock-event-exchange");
        arguments.put("x-dead-letter-routing-key", "stock.release");
        arguments.put("x-message-ttl", 120000); // 设置两分钟过期
        return new Queue("stock.delay.queue", true, false, false , arguments);
    }
    @Bean // 普通的队列
    public Queue stockReleaseOrderQueue()
    {
         return  new Queue("stock.release.stock.queue", true,  false, false, null);
    }
    // 一个交换机
    @Bean
    public Exchange stockEventExchange()
    {
         return new TopicExchange("stock-event-exchange",true,false);
    }
    // 第一个绑定 将延时队列和交换机进行绑定
    @Bean
    public Binding stockCreateOrderBinding()
    {
          return  new Binding("stock.delay.queue",
                  Binding.DestinationType.QUEUE,
                  "stock-event-exchange",
                  "stock.locked", null);
    }
    //第二个绑定
    @Bean
    public Binding stockReleaseOrderBinding()
    {
        return  new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.release.#", null);
    }


}

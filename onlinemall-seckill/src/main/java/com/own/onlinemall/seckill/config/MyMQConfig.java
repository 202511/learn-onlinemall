package com.own.onlinemall.seckill.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class MyMQConfig {


    // 模拟延时队列
    // 一个存储死信的队列
    // 一个存储广播消息的队列
    //自动注入
    // 这样子就能自动的添加队列，  ， 队列和交换机只创建一次， 属性发生变化不会发生覆盖
    //@Bean 容器中 rabbitmq 的队列， 交换机 会自动使用rabbit提供的方法进行创建
    @Bean // 延时队列
    public Queue orderDelayQueue()
    {
        // 设置属性， 超时时间， 转发的路由键 和交换器
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        arguments.put("x-message-ttl", 60000);
        return new Queue("order.delay.queue", true, false, false , arguments);
    }
    @Bean // 普通的队列
    public Queue orderReleaseOrderQueue()
    {
         return  new Queue("order.release.order.queue", true,  false, false, null);
    }
    // 一个交换机
    @Bean
    public Exchange orderEventExchange()
    {
         return new TopicExchange("order-event-exchange",true,false);
    }
    // 第一个绑定 将延时队列和交换机进行绑定
    @Bean
    public Binding orderCreateOrderBinding()
    {
          return  new Binding("order.delay.queue",
                  Binding.DestinationType.QUEUE,
                  "order-event-exchange",
                  "order.create.order", null);
    }
    //第二个绑定
    @Bean
    public Binding orderReleaseOrderBinding()
    {
        return  new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order", null);
    }

    @Bean
    public Binding orderReleaseOtherBinding() {
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.other.#",
                null);
    }
}

package com.own.onlinemall.order.listener;


import com.own.onlinemall.auth.to.SeckillOrderTO;
import com.own.onlinemall.order.entity.OrderEntity;
import com.own.onlinemall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = "order.seckill.order.queue")
public class OrderSeckillListener {
    @Autowired
    OrderService orderService;
    @RabbitHandler
    public void listener(SeckillOrderTO entity, Channel channel, Message message) throws IOException {
        try {
            System.out.println("准备创建秒杀单的详细信息 。。 。");
            // 解锁库存
            orderService.createSeckillOrder(entity);
            // 解锁成功，手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 解锁失败，消息入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }


}

package com.own.onlinemall.order.listener;


import com.own.onlinemall.order.entity.OrderEntity;
import com.own.onlinemall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {
    @Autowired
    OrderService orderService;
    @RabbitHandler
    public void listener(OrderEntity entity, Channel channel, Message message) throws IOException {
        try {
            System.out.println("收到过期的订单信息， 准备关闭订单");
            // 解锁库存
            orderService.closeOrder(entity);
            // 解锁成功，手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 解锁失败，消息入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}

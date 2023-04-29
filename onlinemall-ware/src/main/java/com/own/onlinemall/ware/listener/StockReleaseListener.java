package com.own.onlinemall.ware.listener;


import com.alibaba.fastjson.TypeReference;
import com.own.onlinemall.common.to.order.OrderTO;
import com.own.onlinemall.ware.entity.WareOrderTaskDetailEntity;
import com.own.onlinemall.ware.entity.WareOrderTaskEntity;
import com.own.onlinemall.ware.r.R;
import com.own.onlinemall.ware.service.WareSkuService;
import com.own.onlinemall.ware.service.impl.WareSkuServiceImpl;
import com.own.onlinemall.ware.to.StockDetailTO;
import com.own.onlinemall.ware.to.StockLockedTO;
import com.own.onlinemall.ware.vo.OrderEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 库存解锁（监听死信队列）
     * 场景：
     * 1.下订单成功【需要解锁】(订单过期未支付、被用户手动取消、其他业务调用失败（订单回滚）)
     * 2.下订单失败【无需解锁】(库存锁定失败（库存锁定已回滚，但消息已发出）)
     * <p>
     * 注意：需要开启手动确认，不要删除消息，当前解锁失败需要重复解锁
     */
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTO locked, Message message, Channel channel) throws IOException {
        //当前消息是否重新派发过来
        // Boolean redelivered = message.getMessageProperties().getRedelivered();
        try {
            // 解锁库存
            wareSkuService.unLockStock(locked);
            // 解锁成功，手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 解锁失败，消息入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    /**
     * 客户取消订单，监听到消息
     */
    //rabbitmq 发送和接受的对象必须是来自统一的路径
    @RabbitHandler
    public void listener(OrderTO entity, Channel channel, Message message) throws IOException {
        try {
            System.out.println("订单关闭， 准备解锁库存");
            // 解锁库存
            wareSkuService.unLockStock(entity);
            // 解锁成功，手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 解锁失败，消息入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}

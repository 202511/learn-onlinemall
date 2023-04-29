package com.own.onlinemall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.order.dao.OrderItemDao;
import com.own.onlinemall.order.dto.OrderItemDTO;
import com.own.onlinemall.order.entity.OrderEntity;
import com.own.onlinemall.order.entity.OrderItemEntity;
import com.own.onlinemall.order.service.OrderItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 订单项信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class OrderItemServiceImpl extends CrudServiceImpl<OrderItemDao, OrderItemEntity, OrderItemDTO> implements OrderItemService {

    @Override
    public QueryWrapper<OrderItemEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderItemEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

//    声明需要监听的所有队列
//    1.Message message 原生消息详细信息， 头+ 体
//    T<发送消息的类型> OrderEntity order spring会给我们自动封装
    //queue可以很多人监听， 但是只有一个服务收到信息
    @RabbitListener(queues = {"hello-java-queue"})
    public void recieveMessage(Message message, OrderEntity order)
    {
        System.out.println("接受到消息————————的内容"+message);
        System.out.println(order);
    }

    @Override
    public List<OrderItemEntity> getTitle(String orderSn) {
        List<OrderItemEntity> list = baseDao.selectList(new QueryWrapper<OrderItemEntity>().eq("order_sn", orderSn));

        return list;
    }


}
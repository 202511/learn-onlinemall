package com.own.onlinemall.order.dto;

import com.own.onlinemall.order.entity.OrderEntity;
import com.own.onlinemall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateTO {
    private OrderEntity order;  // 订单
    private List<OrderItemEntity> orderItems; // 订单项
    /** 订单计算的应付价格 TODO 是否可删？**/
    private BigDecimal payPrice;
    /** 运费 TODO 是否可删？**/
    private BigDecimal fare;
}

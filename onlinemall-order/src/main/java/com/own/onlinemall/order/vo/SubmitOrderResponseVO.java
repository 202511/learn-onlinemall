package com.own.onlinemall.order.vo;

import com.own.onlinemall.order.entity.OrderEntity;
import lombok.Data;

/**
 * 提交订单返回结果
 * @author: wan
 */
@Data
public class SubmitOrderResponseVO {
    private OrderEntity order;
}

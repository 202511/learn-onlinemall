package com.own.onlinemall.order.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.order.dto.OrderItemDTO;
import com.own.onlinemall.order.entity.OrderItemEntity;

import java.util.List;

/**
 * 订单项信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface OrderItemService extends CrudService<OrderItemEntity, OrderItemDTO> {

    List<OrderItemEntity> getTitle(String orderSn);

}
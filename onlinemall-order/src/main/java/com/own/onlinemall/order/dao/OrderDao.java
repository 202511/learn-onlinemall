package com.own.onlinemall.order.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface OrderDao extends BaseDao<OrderEntity> {

    void updateOrderStatus(@Param("order_sn") String out_trade_no,@Param("code") Integer code);
}
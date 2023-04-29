package com.own.onlinemall.order.service;

import com.own.onlinemall.auth.to.SeckillOrderTO;
import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.order.dto.OrderDTO;
import com.own.onlinemall.order.entity.OrderEntity;
import com.own.onlinemall.order.pay.AliPayAsyncVO;
import com.own.onlinemall.order.util.PageUtils;
import com.own.onlinemall.order.vo.OrderConfirmVO;
import com.own.onlinemall.order.vo.OrderSubmitVO;
import com.own.onlinemall.order.vo.PayVO;
import com.own.onlinemall.order.vo.SubmitOrderResponseVO;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface OrderService extends CrudService<OrderEntity, OrderDTO> {

    OrderConfirmVO confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVO submitOrder(OrderSubmitVO vo);

    OrderEntity getOrderByOrderSn(String orderSn);

    void closeOrder(OrderEntity entity);

    PayVO getOrderPay(String orderSn);

    PageUtils queryPageWithItem(Map<String, Object> params);

    String handlePayResult(AliPayAsyncVO vo);

    void createSeckillOrder(SeckillOrderTO entity);
}
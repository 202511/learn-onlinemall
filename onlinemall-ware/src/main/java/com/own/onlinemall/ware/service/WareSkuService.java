package com.own.onlinemall.ware.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.common.to.order.OrderTO;
import com.own.onlinemall.ware.dto.WareSkuDTO;
import com.own.onlinemall.ware.entity.WareSkuEntity;
import com.own.onlinemall.ware.to.StockLockedTO;
import com.own.onlinemall.ware.vo.OrderEntity;
import com.own.onlinemall.ware.vo.SkuHasStockVo;
import com.own.onlinemall.ware.vo.WareSkuLockTO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;
import java.util.List;

/**
 * 商品库存
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface WareSkuService extends CrudService<WareSkuEntity, WareSkuDTO> {

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);

    void orderLockStock(WareSkuLockTO to);

    public void  unLockStock(StockLockedTO to) ;

    void unLockStock(OrderTO entity);
}
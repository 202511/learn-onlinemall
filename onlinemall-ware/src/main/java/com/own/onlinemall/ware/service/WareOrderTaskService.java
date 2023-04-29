package com.own.onlinemall.ware.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.ware.dto.WareOrderTaskDTO;
import com.own.onlinemall.ware.entity.WareOrderTaskEntity;

/**
 * 库存工作单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface WareOrderTaskService extends CrudService<WareOrderTaskEntity, WareOrderTaskDTO> {

    WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn);
}
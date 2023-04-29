package com.own.onlinemall.order.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.order.entity.OrderReturnApplyEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单退货申请
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface OrderReturnApplyDao extends BaseDao<OrderReturnApplyEntity> {
	
}
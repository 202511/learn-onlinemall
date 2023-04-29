package com.own.onlinemall.order.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.order.entity.RefundInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface RefundInfoDao extends BaseDao<RefundInfoEntity> {
	
}
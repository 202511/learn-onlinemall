package com.own.onlinemall.order.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.order.entity.MqMessageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface MqMessageDao extends BaseDao<MqMessageEntity> {
	
}
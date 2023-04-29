package com.own.onlinemall.ware.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.ware.entity.WareOrderTaskEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存工作单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface WareOrderTaskDao extends BaseDao<WareOrderTaskEntity> {
	
}
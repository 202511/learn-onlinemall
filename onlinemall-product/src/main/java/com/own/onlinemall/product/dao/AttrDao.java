package com.own.onlinemall.product.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.product.entity.AttrEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface AttrDao extends BaseDao<AttrEntity> {
	
}
package com.own.onlinemall.product.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.product.entity.ProductAttrValueEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * spu属性值
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface ProductAttrValueDao extends BaseDao<ProductAttrValueEntity> {
	
}
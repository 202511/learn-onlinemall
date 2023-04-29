package com.own.onlinemall.coupon.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.coupon.entity.MemberPriceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品会员价格
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface MemberPriceDao extends BaseDao<MemberPriceEntity> {
	
}
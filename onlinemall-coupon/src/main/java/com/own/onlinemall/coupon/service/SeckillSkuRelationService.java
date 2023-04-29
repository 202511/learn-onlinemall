package com.own.onlinemall.coupon.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.coupon.dto.SeckillSkuRelationDTO;
import com.own.onlinemall.coupon.entity.SeckillSkuRelationEntity;

import java.util.List;

/**
 * 秒杀活动商品关联
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface SeckillSkuRelationService extends CrudService<SeckillSkuRelationEntity, SeckillSkuRelationDTO> {

    List<SeckillSkuRelationEntity> getSkuListBySessionId(Long id);
}
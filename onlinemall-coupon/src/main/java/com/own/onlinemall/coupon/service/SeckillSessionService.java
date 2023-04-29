package com.own.onlinemall.coupon.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.coupon.dto.SeckillSessionDTO;
import com.own.onlinemall.coupon.entity.SeckillSessionEntity;

import java.util.List;

/**
 * 秒杀活动场次
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface SeckillSessionService extends CrudService<SeckillSessionEntity, SeckillSessionDTO> {

    List<SeckillSessionEntity> getLatest3DaySession();
}
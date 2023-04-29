package com.own.onlinemall.seckill.service;

import com.own.onlinemall.seckill.to.SeckillSkuRedisTO;

import java.util.List;

public interface SeckillService {
    void uploadSeckillSkuLatest3Days();

    List<SeckillSkuRedisTO> getCurrentSeckillSkus();

    SeckillSkuRedisTO getSkuSeckillInfo(Long skuId);

    String kill(String killId, String key, Integer num) throws InterruptedException;
}

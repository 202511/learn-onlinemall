package com.own.onlinemall.seckill.to;

import com.own.onlinemall.seckill.vo.SkuInfoVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SeckillSkuRedisTO {
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private Integer seckillCount;
    /**
     * 每人限购数量
     */
    private Integer seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;
    // sku的详细信息
    private SkuInfoVO skuInfoVO;
    // 当前商品的开始时间
    private Long startTime;
    // 当前商品秒杀的结束时间
    private Long endTime;
    //随机码， 秒杀的每一个请求都要带随机码， 防止脚本
    private  String randomCode;

}

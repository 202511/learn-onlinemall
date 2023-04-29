package com.own.onlinemall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀活动商品关联
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("sms_seckill_sku_relation")
public class SeckillSkuRelationEntity {

    /**
     * id
     */
	private Long id;
    /**
     * 活动id
     */
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
}
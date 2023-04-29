package com.own.onlinemall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品会员价格
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("sms_member_price")
public class MemberPriceEntity {

    /**
     * id
     */
	private Long id;
    /**
     * sku_id
     */
	private Long skuId;
    /**
     * 会员等级id
     */
	private Long memberLevelId;
    /**
     * 会员等级名
     */
	private String memberLevelName;
    /**
     * 会员对应价格
     */
	private BigDecimal memberPrice;
    /**
     * 可否叠加其他优惠[0-不可叠加优惠，1-可叠加]
     */
	private Integer addOther;
}
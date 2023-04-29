package com.own.onlinemall.order.vo;


import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
@Data
public class OrderSubmitVO {
    /** 收获地址的id **/
    private Long addrId;

    /** 支付方式 **/
    private Integer payType;

    // 优惠、发票

    /** 防重令牌 **/
    private String uniqueToken;

    /** 应付价格 **/
    private BigDecimal payPrice;// 页面提交应付价格，提交订单会再算一遍价格，如果价格不相等会提示用户

    /** 订单备注 **/
    private String remarks;

}

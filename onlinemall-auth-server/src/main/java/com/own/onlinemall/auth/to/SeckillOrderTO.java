package com.own.onlinemall.auth.to;

import lombok.Data;

import javax.naming.ldap.PagedResultsControl;
import java.math.BigDecimal;

@Data
public class SeckillOrderTO {
    private String orderSn;  // 订单号
    private BigDecimal seckillPrice; // 秒杀价格
    private Integer num; // 购买数量
     private Long skuId; // 商品id
    private Long promotionSessionId; //  活动场次id
     private  Long memberId; // 会员id

}

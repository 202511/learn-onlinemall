package com.own.onlinemall.coupon.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品满减信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
public class SkuFullReductionExcel {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "spu_id")
    private Long skuId;
    @Excel(name = "满多少")
    private BigDecimal fullPrice;
    @Excel(name = "减多少")
    private BigDecimal reducePrice;
    @Excel(name = "是否参与其他优惠")
    private Integer addOther;

}
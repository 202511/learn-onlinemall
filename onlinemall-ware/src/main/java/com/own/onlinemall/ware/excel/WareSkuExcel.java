package com.own.onlinemall.ware.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 商品库存
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
public class WareSkuExcel {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "sku_id")
    private Long skuId;
    @Excel(name = "仓库id")
    private Long wareId;
    @Excel(name = "库存数")
    private Integer stock;
    @Excel(name = "sku_name")
    private String skuName;
    @Excel(name = "锁定库存")
    private Integer stockLocked;

}
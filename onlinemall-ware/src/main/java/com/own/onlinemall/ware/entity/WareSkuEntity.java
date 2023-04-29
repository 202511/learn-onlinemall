package com.own.onlinemall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 商品库存
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("wms_ware_sku")
public class WareSkuEntity {

    /**
     * id
     */

	private Long id;
    /**
     * sku_id
     */
	private Long skuId;
    /**
     * 仓库id
     */
	private Long wareId;
    /**
     * 库存数
     */
	private Integer stock;
    /**
     * sku_name
     */
	private String skuName;
    /**
     * 锁定库存
     */
	private Integer stockLocked;
}
package com.own.onlinemall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * spu属性值
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("pms_product_attr_value")
public class ProductAttrValueEntity {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 商品id
     */
	private Long spuId;
    /**
     * 属性id
     */
	private Long attrId;
    /**
     * 属性名
     */
	private String attrName;
    /**
     * 属性值
     */
	private String attrValue;
    /**
     * 顺序
     */
	private Integer attrSort;
    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】
     */
	private Integer quickShow;
}
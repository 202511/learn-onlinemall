package com.own.onlinemall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 商品属性
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("pms_attr")
public class AttrEntity {

    /**
     * 属性id
     */
    @TableId(type = IdType.AUTO)
	private Long attrId;
    /**
     * 属性名
     */
	private String attrName;
    /**
     * 是否需要检索[0-不需要，1-需要]
     */
	private Integer searchType;
    /**
     * 值类型[0-为单个值，1-可以选择多个值]
     */
	private Integer valueType;
    /**
     * 属性图标
     */
	private String icon;
    /**
     * 可选值列表[用逗号分隔]
     */
	private String valueSelect;
    /**
     * 属性类型[0-销售属性，1-基本属性
     */
	private Integer attrType;
    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
	private Long enable;
    /**
     * 所属分类
     */
	private Long catelogId;
    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
     */
	private Integer showDesc;
}
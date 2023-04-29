package com.own.onlinemall.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品三级分类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("pms_category")
public class CategoryEntity {

    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
	private Long catId;
    /**
     * 分类名称
     */
	private String name;
    /**
     * 父分类id
     */
	private Long parentCid;
    /**
     * 层级
     */
	private Integer catLevel;
    /**
     * 是否显示[0-不显示，1显示]
     */
    @TableLogic(value = "1" ,delval = "0")
	private Integer showStatus;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 图标地址
     */
	private String icon;
    /**
     * 计量单位
     */
	private String productUnit;
    /**
     * 商品数量
     */
	private Integer productCount;


    @TableField(exist = false)
    private List<CategoryEntity> children;
}
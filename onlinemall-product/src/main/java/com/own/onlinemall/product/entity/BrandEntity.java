package com.own.onlinemall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 品牌
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("pms_brand")
public class BrandEntity {

    /**
     * 品牌id
     */

    @TableId(type = IdType.AUTO)
	private Long brandId;
    /**
     * 品牌名
     */
    @NotNull
	private String name;
    /**
     * 品牌logo地址
     */
	private String logo;
    /**
     * 介绍
     */
	private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
	private Integer showStatus;
    /**
     * 检索首字母
     */
	private String firstLetter;
    /**
     * 排序
     */
	private Integer sort;
}
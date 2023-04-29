package com.own.onlinemall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * spu信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("pms_spu_info")
public class SpuInfoEntity {

    /**
     * 商品id
     */
    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 商品名称
     */
	private String spuName;
    /**
     * 商品描述
     */
	private String spuDescription;
    /**
     * 所属分类id
     */
	private Long catalogId;
    /**
     * 品牌id
     */
	private Long brandId;
    /**
     * 
     */
	private BigDecimal weight;
    /**
     * 上架状态[0 - 新建，1 - 上架，2 - 下架]
     */
	private Integer publishStatus;
    /**
     * 
     */
	private Date createTime;
    /**
     * 
     */
	private Date updateTime;
}
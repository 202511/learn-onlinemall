package com.own.onlinemall.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * spu信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@ApiModel(value = "spu信息")
public class SpuInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "商品id")
	private Long id;

	@ApiModelProperty(value = "商品名称")
	private String spuName;

	@ApiModelProperty(value = "商品描述")
	private String spuDescription;

	@ApiModelProperty(value = "所属分类id")
	private Long catalogId;

	@ApiModelProperty(value = "品牌id")
	private Long brandId;

	@ApiModelProperty(value = "")
	private BigDecimal weight;

	@ApiModelProperty(value = "上架状态[0 - 新建，1 - 上架，2 - 下架]")
	private Integer publishStatus;

	@ApiModelProperty(value = "")
	private Date createTime;

	@ApiModelProperty(value = "")
	private Date updateTime;


}
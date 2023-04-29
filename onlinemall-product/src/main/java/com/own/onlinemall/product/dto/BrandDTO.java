package com.own.onlinemall.product.dto;

import com.own.onlinemall.common.validator.group.AddGroup;
import com.own.onlinemall.common.validator.group.UpdateGroup;
import com.own.onlinemall.common.validator.self.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 品牌
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@ApiModel(value = "品牌")
public class BrandDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "品牌id")
	private Long brandId;
	@NotNull(groups = {AddGroup.class})
	@ApiModelProperty(value = "品牌名")
	private String name;

	@ApiModelProperty(value = "品牌logo地址")
	private String logo;

	@ApiModelProperty(value = "介绍")
	private String descript;

    @Status(value = {1,0},groups = {AddGroup.class})
	@ApiModelProperty(value = "显示状态[0-不显示；1-显示]")
	private Integer showStatus;

	@ApiModelProperty(value = "检索首字母")
	private String firstLetter;

	@ApiModelProperty(value = "排序")
	private Integer sort;


}
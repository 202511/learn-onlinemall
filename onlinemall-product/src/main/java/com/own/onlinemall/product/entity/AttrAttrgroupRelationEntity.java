package com.own.onlinemall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 属性&属性分组关联
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("pms_attr_attrgroup_relation")
public class AttrAttrgroupRelationEntity {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 属性id
     */
	private Long attrId;
    /**
     * 属性分组id
     */
	private Long attrGroupId;
    /**
     * 属性组内排序
     */
	private Integer attrSort;
}
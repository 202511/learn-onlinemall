package com.own.onlinemall.product.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 品牌分类关联
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
public class CategoryBrandRelationExcel {
    @Excel(name = "")
    private Long id;
    @Excel(name = "品牌id")
    private Long brandId;
    @Excel(name = "分类id")
    private Long catelogId;
    @Excel(name = "")
    private String brandName;
    @Excel(name = "")
    private String catelogName;

}
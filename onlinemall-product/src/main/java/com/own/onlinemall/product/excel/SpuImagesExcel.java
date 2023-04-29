package com.own.onlinemall.product.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * spu图片
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
public class SpuImagesExcel {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "spu_id")
    private Long spuId;
    @Excel(name = "图片名")
    private String imgName;
    @Excel(name = "图片地址")
    private String imgUrl;
    @Excel(name = "顺序")
    private Integer imgSort;
    @Excel(name = "是否默认图")
    private Integer defaultImg;

}
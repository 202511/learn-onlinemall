package com.own.onlinemall.product.vo;

import com.own.onlinemall.product.entity.SkuImagesEntity;
import com.own.onlinemall.product.entity.SkuInfoEntity;
import com.own.onlinemall.product.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

//商品详情页所需的全部内容
@Data
public class SkuItemVo {
    //1、sku基本信息（pms_sku_info）【默认图片、标题、副标题、价格】
    private SkuInfoEntity info;

    private boolean hasStock = true;// 是否有货

    //2、sku图片信息（pms_sku_images）
    private List<SkuImagesEntity> images;

    //3、当前sku所属spu下的所有销售属性组合（pms_sku_sale_attr_value）
    private List<SkuItemSaleAttrVO> saleAttr;

    //4、spu商品介绍（pms_spu_info_desc）【描述图片】
    private SpuInfoDescEntity desc;

    //5、spu规格参数信息（pms_attr）【以组为单位】
    private List<SpuItemAttrGroupVO> groupAttrs;
    private SeckillSkuVO seckillSku;





}

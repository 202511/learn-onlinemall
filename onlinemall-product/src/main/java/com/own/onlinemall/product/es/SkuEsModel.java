package com.own.onlinemall.product.es;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuEsModel {
        private  Long skuId;
        private  Long spuId;
        private String skuTitle;
        //价格
        private BigDecimal  skuPrice;
        //sku的图片
        private String  skuImage;
        //销量
        private  Long saleCount;
        //有没有库存
        private  Boolean  hasStock;
        //品牌id
        private Long brandId;
        //分类id
        private Long catalogId;
        //品牌名
        private String brandName;
        //品牌图片
        private String brandImg;
        //分类名字
        private  String catalogName;
        // 商品的规格属性 属性（属性名， 属性id， 属性值）
        private List<Attrs> attrs;
        // 静态内部类 属性
        @Data
       public  static class Attrs extends  Object{
                private Long attrId;
                private String attrName;
                private  String attrValue;
        }

}

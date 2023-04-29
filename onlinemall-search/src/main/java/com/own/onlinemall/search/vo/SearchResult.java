package com.own.onlinemall.search.vo;

import com.own.onlinemall.search.es.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class SearchResult {
    //查询到的所有商品信息。
    private List<SkuEsModel> products;
    private  Long total; //总记录数
    private Integer pageNum;// 当前页码
    private  Integer totalPages; //总页码
    private  List<BrandVo>  brands; // 涉及到的品牌
    private  List<AttrVo> attrs; // 当前查询的结果所有涉及到的所有属性
    private  List<CatalogVo> catalogs;// 当前查询结果涉及到的所有分类
    // ============================以上是要返回的数据====================================

    // 面包屑导航数据
    private List<NavVo> navs = new ArrayList<>();
    private List<Integer> pageNavs;// 导航页码[1、2、3、4、5]
    // 封装筛选条件中的属性id集合【用于面包屑，选择属性后出现在面包屑中，下面的属性栏则隐藏】
    // 该字段是提供前端用的
    private List<Long> attrIds = new ArrayList<>();

    public List<SkuEsModel> getProducts() {
        return products;
    }

    public void setProducts(List<SkuEsModel> products) {
        this.products = products;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<BrandVo> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandVo> brands) {
        this.brands = brands;
    }

    public List<AttrVo> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<AttrVo> attrs) {
        this.attrs = attrs;
    }

    public List<CatalogVo> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<CatalogVo> catalogs) {
        this.catalogs = catalogs;
    }


    @Data
    public static class NavVo {
        private String navName;// 属性名
        private String navValue;// 属性值
        private String link;// 回退地址（删除该面包屑筛选条件回退地址）
    }

    //以上是要返回给页面的全部信息。
    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
    }

    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private  String brandImg;
    }

    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private  List<String> attrValue;
    }
}

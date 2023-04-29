package com.own.onlinemall.search.vo;

import lombok.Data;

import java.util.List;
@Data
//将 页面所有可能传过来的查询条件 都封装成一个对象
public class SearchParam {
    private  String keyword; // 页面传递过来的全文匹配关键字
    private  Long catalog3Id; // 三级分类 id
    private String sort;  // 排序条件
    // 还有很多过滤条件
    private  Integer hasStock; //是否有货
    private  String skuPrice; // 价格区间
    private List<Long> brandId;// 按照品牌进行查询
    private  List<String> attrs;// 按照属性进行筛选
    private  Integer  pageNum=1; // 页码
    private String _queryString;// 原生的所有查询条件（来自url的请求参数），用于构建面包屑
}

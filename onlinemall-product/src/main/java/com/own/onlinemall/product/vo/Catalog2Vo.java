package com.own.onlinemall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Catalog2Vo {
    private  String catalog1Id;
    private List<Catalog3Vo> catalog3List;
    private  String id;
    private  String name;
    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static class Catalog3Vo{
        private String  catalog2Id;
        private  String id;
        private  String  name;
    }
}

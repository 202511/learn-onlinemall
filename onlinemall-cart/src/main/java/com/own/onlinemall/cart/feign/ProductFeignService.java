package com.own.onlinemall.cart.feign;


import com.own.onlinemall.cart.vo.SkuInfoDTO;
import com.own.onlinemall.cart.vo.SkuInfoEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@FeignClient("onlinemall-product")
public interface ProductFeignService {
    @GetMapping("/product/skuinfo/skuEntity/{id}")
    public SkuInfoEntity getT(@PathVariable("id") Long id);


    @GetMapping("/product/skusaleattrvalue/stringlist/{skuId}")
    public List<String> getSkuSaleAttrValues(@PathVariable("skuId")  Long skuId);


    @GetMapping("/product/skuinfo/{skuId}/price")
    public BigDecimal getPrice(@PathVariable("skuId") Long  skuId);
}

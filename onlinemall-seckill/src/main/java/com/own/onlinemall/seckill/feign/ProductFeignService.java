package com.own.onlinemall.seckill.feign;


import com.own.onlinemall.seckill.vo.SkuInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("onlinemall-product")
public interface ProductFeignService {
    @GetMapping("/product/skuinfo/skuEntity/{id}")
    public SkuInfoVO getT(@PathVariable("id") Long id);
}

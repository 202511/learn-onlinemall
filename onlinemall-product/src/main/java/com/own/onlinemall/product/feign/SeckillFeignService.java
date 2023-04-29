package com.own.onlinemall.product.feign;


import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.product.feign.sentinel.SeckillFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "onlinemall-seckill" ,fallback = SeckillFeignServiceImpl.class)
public interface SeckillFeignService {
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId") Long skuId);
}

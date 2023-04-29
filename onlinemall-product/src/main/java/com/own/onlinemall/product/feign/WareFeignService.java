package com.own.onlinemall.product.feign;


import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@FeignClient("onlinemall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/has/stock")
    Result<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds );
}

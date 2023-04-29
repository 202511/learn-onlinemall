package com.own.onlinemall.product.feign;


import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.product.es.SkuEsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
@FeignClient("onlinemall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    public Result productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList);
}

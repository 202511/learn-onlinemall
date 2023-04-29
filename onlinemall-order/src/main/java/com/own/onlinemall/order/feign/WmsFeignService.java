package com.own.onlinemall.order.feign;


import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.order.vo.SkuHasStockVo;
import com.own.onlinemall.order.vo.WareSkuLockTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("onlinemall-ware")
public interface WmsFeignService {

    @PostMapping("/ware/waresku/has/stock")
    public Result<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds );
    @GetMapping(value = "/ware/wareinfo/fare")
    public R getFare(@RequestParam("addrId") Long addrId);

    @PostMapping("ware/waresku/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockTO to);
}

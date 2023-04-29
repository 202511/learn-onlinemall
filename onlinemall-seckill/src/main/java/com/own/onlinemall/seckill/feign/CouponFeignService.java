package com.own.onlinemall.seckill.feign;

import com.own.onlinemall.auth.r.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("onlinemall-coupon")
public interface CouponFeignService {
    @GetMapping("/coupon/seckillsession/latest3DaySession")
    public R getLatest3DaySession();
}

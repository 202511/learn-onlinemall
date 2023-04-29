package com.own.onlinemall.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient("onlinemall-third-party")
public interface ThirdPartFeignService {
    @GetMapping("/sms/sendcode")
    public  boolean sendCode(@RequestParam("phone") String phone, @RequestParam("code" ) String code );
}

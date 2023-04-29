package com.own.onlinemall.order.feign;


import com.own.onlinemall.order.vo.MemberAddressVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("onlinemall-member")
public interface  MemberFeignService {
    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    public List<MemberAddressVO> getAddress(@PathVariable("memberId") Long memberId);
}

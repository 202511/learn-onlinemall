package com.own.onlinemall.ware.feign;


import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.ware.vo.MemberAddressVO;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("onlinemall-member")
public interface MemberFeignService {
    @GetMapping("/member/memberreceiveaddress/{id}")
    @ApiOperation("信息")
    @RequiresPermissions("member:memberreceiveaddress:info")
    public Result<MemberAddressVO> get(@PathVariable("id") Long id);

}

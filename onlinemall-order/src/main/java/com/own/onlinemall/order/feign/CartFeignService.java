package com.own.onlinemall.order.feign;


import com.own.onlinemall.order.vo.OrderItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("onlinemall-cart")
public interface CartFeignService {
    @GetMapping("/currentUserCartItems")
    public List<OrderItemVO> getCurrentUserCartItems();
}

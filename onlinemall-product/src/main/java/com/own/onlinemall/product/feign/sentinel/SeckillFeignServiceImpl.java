package com.own.onlinemall.product.feign.sentinel;

import com.own.onlinemall.auth.r.BizCodeEnume;
import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeckillFeignServiceImpl implements SeckillFeignService {
    @Override
    public R getSkuSeckillInfo(Long skuId) {
        System.out.println("熔断方法调用");
        return R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(),BizCodeEnume.TO_MANY_REQUEST.getMsg());
    }
}

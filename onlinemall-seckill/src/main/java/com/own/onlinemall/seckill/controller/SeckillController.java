package com.own.onlinemall.seckill.controller;

import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.seckill.service.SeckillService;
import com.own.onlinemall.seckill.to.SeckillSkuRedisTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SeckillController {
    @Autowired
    SeckillService seckillService;
    // 返回当前可以参与秒杀的商品
    @ResponseBody
    @GetMapping("/getCurrentSeckillSkus")
    public R getCurrentSeckillSkus()
    {
       List<SeckillSkuRedisTO> list = seckillService.getCurrentSeckillSkus();
        return R.ok().setData(list);
    }
    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId") Long skuId)
    {
       SeckillSkuRedisTO to= seckillService.getSkuSeckillInfo(skuId);
        return R.ok().setData(to);
    }

    @GetMapping(value =  "/kill")
    public String seckill(@RequestParam("killId") String killId, @RequestParam("key") String key,
                          @RequestParam("num") Integer num,
                          Model model) throws InterruptedException {
        String orderSn= seckillService.kill(killId, key, num);
        model.addAttribute("orderSn", orderSn);
        return "success";
    }

}

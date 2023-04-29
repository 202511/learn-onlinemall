package com.own.onlinemall.product.web;

import com.own.onlinemall.product.service.SkuInfoService;
import com.own.onlinemall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        System.out.println("准备查询"+skuId+"的详情");
        SkuItemVo vo= skuInfoService.item(skuId);
        model.addAttribute("item", vo);
        return "item";
    }
}

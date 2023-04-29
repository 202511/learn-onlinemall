package com.own.onlinemall.member.web;

import com.alibaba.fastjson.JSON;
import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.member.feign.OrderFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberWebController {

    @Autowired
    OrderFeignService orderFeignService ;
    @GetMapping(value = "/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
                                  Model model) {
        // 获取支付宝回参，根据sign延签，延签成功修改订单状态【不建议在同步回调修改订单状态，建议在异步回调修改订单状态】

        // 封装分页数据
        Map<String, Object> page = new HashMap<>();
        page.put("page", pageNum.toString());

        // 分页查询当前用户的订单列表、订单项
        R orderInfo = orderFeignService.listWithItem(page);
        System.out.println(JSON.toJSONString(orderInfo));
        model.addAttribute("orders", orderInfo);

        return "orderList";
    }
}

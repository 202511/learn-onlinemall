package com.own.onlinemall.order.web;

import com.own.onlinemall.order.pay.AliPayConfig;
import com.own.onlinemall.order.pay.AliPayServiceImpl;
import com.own.onlinemall.order.service.impl.OrderServiceImpl;
import com.own.onlinemall.order.vo.PayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PayWebController {
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    AliPayServiceImpl aliPayService;
    @Autowired
    AliPayConfig aliPayConfig;

    /**
     * 创建支付
     * 返回text/html页面
     * @param orderSn       订单号
     */
    @ResponseBody
    @GetMapping(value = "/html/pay", produces = "text/html")
    public String htmlPayOrder(@RequestParam(value = "orderSn", required = false) String orderSn) throws Exception {
        // 获取订单信息，构造参数
        PayVO order = orderService.getOrderPay(orderSn);
        String pay = aliPayService.pay(order);
        return pay;
    }

}

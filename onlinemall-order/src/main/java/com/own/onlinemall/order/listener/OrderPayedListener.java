package com.own.onlinemall.order.listener;


import com.alipay.api.AlipayApiException;
import com.own.onlinemall.order.pay.AliPayAsyncVO;
import com.own.onlinemall.order.pay.AliPayServiceImpl;
import com.own.onlinemall.order.service.OrderService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class OrderPayedListener {

   @Autowired
    AliPayServiceImpl aliPayService;
   @Autowired
    OrderService orderService;

    @PostMapping("/payed/notify")
    public String handleAlipayed(AliPayAsyncVO vo, HttpServletRequest request) throws AlipayApiException {
        //一定要验签
        Boolean verify = aliPayService.verify(request);
        // 只要我们收到了支付宝给我们的异步的通知， 告诉我们订单支付成功， 返回success ,  支付宝就再也不通知了
       if(verify) {
           System.out.println("签名验证成功");
           String result = orderService.handlePayResult(vo);
           return result;
       }
       else
       {
           System.out.println("签名验证失败");
           return "error";
       }

    }

}

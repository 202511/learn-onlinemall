package com.own.onlinemall.order.web;


import com.own.onlinemall.order.exception.NoStockException;
import com.own.onlinemall.order.exception.VerifyPriceException;
import com.own.onlinemall.order.service.OrderService;
import com.own.onlinemall.order.vo.OrderConfirmVO;
import com.own.onlinemall.order.vo.OrderSubmitVO;
import com.own.onlinemall.order.vo.SubmitOrderResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {
     @Autowired
    OrderService orderService;
    @GetMapping(value = "/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        // 查询结算页VO
        // 查询结算页VO
        OrderConfirmVO confirmVo = null;
        try {
            confirmVo = orderService.confirmOrder();
            model.addAttribute("confirmOrderData", confirmVo);
            // 跳转结算页
            return "confirm";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "toTrade";
    }
      // 下单功能
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVO vo, Model model, RedirectAttributes attributes)
    {
        try {
            System.out.println("我不信");
            SubmitOrderResponseVO orderVO = orderService.submitOrder(vo);
            // 创建订单成功，跳转收银台
            model.addAttribute("submitOrderResp", orderVO);// 封装VO订单数据，供页面解析[订单号、应付金额]
            return "pay";
        } catch (Exception e) {
            System.out.println(e);
            // 下单失败回到订单结算页
            if (e instanceof VerifyPriceException) {
                String message = ((VerifyPriceException) e).getMessage();
                System.out.println(message);
                attributes.addFlashAttribute("msg", "下单失败" + message);
            } else if (e instanceof NoStockException) {
                String message = ((NoStockException) e).getMessage();
                System.out.println(message);
                attributes.addFlashAttribute("msg", "下单失败" + message);
            }
            return "redirect:http://order.onlinemall.com/toTrade";
        }
    }
}

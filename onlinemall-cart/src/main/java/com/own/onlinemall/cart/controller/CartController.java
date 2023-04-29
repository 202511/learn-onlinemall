package com.own.onlinemall.cart.controller;


import com.own.onlinemall.auth.controller.AuthConstant;
import com.own.onlinemall.cart.interceptor.CartInterceptor;
import com.own.onlinemall.cart.service.CartService;
import com.own.onlinemall.cart.to.UserInfoTO;
import com.own.onlinemall.cart.vo.Cart;
import com.own.onlinemall.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class CartController  {


    @ResponseBody
    @GetMapping("/currentUserCartItems")
    public List<CartItem> getCurrentUserCartItems()
    {
         return  cartService.getUserCartItems();
    }
    // 若没有登录， 就是未登录状态
     // 需要匹配一个UserId;
     @GetMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
         Cart cart=  cartService.getCart();
         model.addAttribute("cart", cart );
          return "cartList";
      }

     @Autowired
    CartService cartService ;
      //添加商品到购物车

      @GetMapping("/addCartItem")
      public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num, RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
          CartItem cartItem = cartService.addToCart(skuId,num);
          redirectAttributes.addAttribute("skuId", skuId);
           return "redirect:http://cart.onlinemall.com/addToCartSuccessPage.html";
      }


    @GetMapping(value = "/addToCartSuccessPage.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId, Model model) {
        //重定向到成功页面。再次查询购物车数据即可
        CartItem cartItemVo = cartService.getCartItem(skuId);
        model.addAttribute("cartItem", cartItemVo);
        return "success";
    }


    @GetMapping(value = "/checkItem")
    public String checkItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "checked") Integer check) {
        cartService.checkItem(skuId, check);
        return "redirect:http://cart.onlinemall.com/cart.html";
    }
    @GetMapping(value = "/countItem")
    public String countItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "num") Integer num) {
        cartService.changeItemCount(skuId,num);
        return "redirect:http://cart.onlinemall.com/cart.html";
    }
    @GetMapping(value = "/deleteItem")
    public String deleteItem(@RequestParam("skuId") Integer skuId) {
        cartService.deleteIdCartInfo(skuId);
        return "redirect:http://cart.onlinemall.com/cart.html";
    }
}

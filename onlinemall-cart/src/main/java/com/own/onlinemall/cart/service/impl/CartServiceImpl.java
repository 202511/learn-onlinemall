package com.own.onlinemall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.own.onlinemall.cart.feign.ProductFeignService;
import com.own.onlinemall.cart.interceptor.CartInterceptor;
import com.own.onlinemall.cart.service.CartService;
import com.own.onlinemall.cart.to.UserInfoTO;
import com.own.onlinemall.cart.vo.Cart;
import com.own.onlinemall.cart.vo.CartItem;
import com.own.onlinemall.cart.vo.SkuInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    StringRedisTemplate redisTemplate ;
   @Autowired
    ProductFeignService productFeignService ;
   @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    private final String CART_PREFIX="onlinemall:cart";

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //
        String s1 = (String) cartOps.get(skuId.toString());
        if(StringUtils.isEmpty(s1))
        {
            //添加新商品
            //远程查询当前要添加的商品的信息
            CartItem cartItem=new CartItem();
            //商品添加到购物车
            CompletableFuture<Void> t1 = CompletableFuture.runAsync(() -> {
                SkuInfoEntity t = productFeignService.getT(skuId);

                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(t.getSkuDefaultImg());
                cartItem.setTitle(t.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(t.getPrice());
            },threadPoolExecutor);

            //1.封装成购物项

            //远程查询sku的组合信息。 查询销售属性值
            CompletableFuture<Void>  t2 = CompletableFuture.runAsync(() -> {
                List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(skuSaleAttrValues);
            }, threadPoolExecutor);
            CompletableFuture.allOf(t1,t2).get();
            String s = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(), s);
            return cartItem;
        }
        else
        {
            //购物车有此商品， 修改数量
            CartItem cartItem = JSON.parseObject(s1, CartItem.class);
            cartItem.setCount(cartItem.getCount()+num);
            cartOps.put(skuId.toString() , JSON.toJSONString(cartItem));
            return  cartItem;
        }


    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String o = (String)cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(o, CartItem.class);

        return cartItem;
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        // 登录 还是 未登录
        Cart cart = new Cart();
        UserInfoTO userInfoTO = CartInterceptor.threadLocal.get();
        if(userInfoTO.getUserId()!=null )
        {
             // 登录
            String cartKey =CART_PREFIX+  userInfoTO.getUserId();
            BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
            //将临时购物车和以前保存的数据进行合并， 并删除临时购物车
            //获取临时购物车的数据
            String p= CART_PREFIX + userInfoTO.getUserKey();
            List<CartItem> cartItems = getCartItems(p);
            if(cartItems!=null )
            {
                for (CartItem cartItem : cartItems) {
                    addToCart(cartItem.getSkuId(), cartItem.getCount());
                }
                //合并完成之后 清除掉临时购物车数据
                clearCart(p);
            }
            // 获取登录后的购物车数据
            List<CartItem> cartItems1 = getCartItems(cartKey);
            cart.setItems(cartItems1);

        }
        else
        {

            //未登录
            String cartKey =CART_PREFIX+  userInfoTO.getUserKey();
            BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
            List<Object> values = operations.values();
            if(values!=null&&values.size() > 0  )
            {
                List<CartItem> collect = values.stream().map((obj) -> {
                    String obj1 = (String) obj;
                    CartItem jsonObject = JSON.parseObject(obj1 ,CartItem.class);
                    return jsonObject;
                }).collect(Collectors.toList());
                cart.setItems(collect);
            }
            // 未登录
        }
        return cart;
    }

    @Override
    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check==1 ? true : false);
        String s = JSON.toJSONString(cartItem);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(), s);
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        String s = JSON.toJSONString(cartItem);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(), s);
    }

    @Override
    public void deleteIdCartInfo(Integer skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItem> getUserCartItems() {
        UserInfoTO userInfoTO = CartInterceptor.threadLocal.get();
        if(userInfoTO.getUserId()== null)
        {
            return null;
        }
        else
        {
            String cartKey=CART_PREFIX+ userInfoTO.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);
            List<CartItem> collect = cartItems.stream().filter(cartItem -> cartItem.getCheck()).map(cartItem -> {
                cartItem.setPrice(productFeignService.getPrice(cartItem.getSkuId()));
                return cartItem;
            }).collect(Collectors.toList());
            return  collect;
        }
    }

    private BoundHashOperations<String , Object, Object> getCartOps() {
        UserInfoTO userInfoTO = CartInterceptor.threadLocal.get();
        String cartKey="";  // 存进redis使用的前缀
        if(userInfoTO.getUserId()==null )
        {
          cartKey=CART_PREFIX+userInfoTO.getUserKey();
        }
        else
        {
         cartKey= CART_PREFIX+ userInfoTO.getUserId();
        }
        //所有操作绑定一个key
        BoundHashOperations<String , Object, Object> operations= redisTemplate.boundHashOps(cartKey);
        return operations ;
    }
    private  List<CartItem> getCartItems(String cartKey)
    {
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if(values!=null&&values.size() > 0  )
        {
            List<CartItem> collect = values.stream().map((obj) -> {
                String obj1 = (String) obj;
                CartItem jsonObject = JSON.parseObject(obj1 ,CartItem.class);
                return jsonObject;
            }).collect(Collectors.toList());
            return  collect;
        }
        return null ;
    }

}

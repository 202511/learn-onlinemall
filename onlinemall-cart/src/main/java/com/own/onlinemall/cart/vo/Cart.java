package com.own.onlinemall.cart.vo;

import java.math.BigDecimal;
import java.util.List;
/*
  需要计算的属性， 必须重写它的get方法
 */
public class Cart {
    List<CartItem> items;
    private  Integer countNum;  //商品数量
    private  Integer countType; //商品类型数量
    private BigDecimal totalAmount; // 总价
    private  BigDecimal reduce=new BigDecimal("0" ); // 减免价格

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        int count =0 ;
        if(items!=null && items.size()>0)
        {
            for (CartItem item : items) {
                count+=item.getCount();
            }
        }

        return count;
    }



    public Integer getCountType() {
        int count =0 ;
        if(items!=null && items.size()>0)
        {
            for (CartItem item : items) {
                count+=1;
            }
        }
        return count;
    }



    public BigDecimal getTotalAmount() {
        BigDecimal amount=new BigDecimal("0");
        // 计算购物项总价，
        if(items!=null && items.size()>0)
        {
            for (CartItem item : items) {
               BigDecimal totalPrice= item.getTotalPrice();
               amount =amount.add(totalPrice );
            }
        }
        //减去优惠值
        amount=amount.subtract(this.reduce)  ;
        return amount;
    }


    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}

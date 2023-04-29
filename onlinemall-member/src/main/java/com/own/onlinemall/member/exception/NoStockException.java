package com.own.onlinemall.member.exception;

import com.alibaba.nacos.common.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;


import java.util.Set;

/**
 * 无库存抛出的异常
 * @Created: with IntelliJ IDEA.
 * @author: wan
 */
public class NoStockException extends RuntimeException {

    @Getter @Setter
    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品id："+ skuId + "库存不足！");
    }

    public NoStockException(String msg) {
        super(msg);
    }

    public NoStockException(Set<Long> keySet) {
        super("商品id："+ StringUtils.join(keySet, ",") + "库存不足！");
    }
}

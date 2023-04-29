package com.own.onlinemall.product;

import com.own.onlinemall.product.dao.AttrGroupDao;
import com.own.onlinemall.product.dao.SkuSaleAttrValueDao;
import com.own.onlinemall.product.service.SkuSaleAttrValueService;
import com.own.onlinemall.product.vo.Attr;
import com.own.onlinemall.product.vo.SkuItemSaleAttrVO;
import com.own.onlinemall.product.vo.SpuItemAttrGroupVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = {OnlinemallProductApplication.class})
class OnlinemallProductApplicationTests {


    @Autowired
    RedissonClient redissonClient;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;
    @Test
    void contextLoads() {
        List<SkuItemSaleAttrVO> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(13l);
        System.out.println(saleAttrsBySpuId);
    }

}

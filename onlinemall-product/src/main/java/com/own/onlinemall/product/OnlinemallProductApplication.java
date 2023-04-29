package com.own.onlinemall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableRedisHttpSession
@EnableCaching
@EnableFeignClients
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.own.onlinemall.product.dao")
public class OnlinemallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinemallProductApplication.class, args);
    }

}

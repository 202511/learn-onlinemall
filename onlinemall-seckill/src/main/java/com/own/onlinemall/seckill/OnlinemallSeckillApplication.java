package com.own.onlinemall.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableFeignClients
@SpringBootApplication
public class OnlinemallSeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinemallSeckillApplication.class, args);
    }

}

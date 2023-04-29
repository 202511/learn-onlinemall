package com.own.onlinemall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
@EnableFeignClients
@EnableRedisHttpSession
@SpringBootApplication
public class OnlinemallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinemallMemberApplication.class, args);
    }

}

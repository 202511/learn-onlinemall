package com.own.onlinemall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@SpringBootApplication()
public class OnlinemallSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinemallSearchApplication.class, args);
    }

}

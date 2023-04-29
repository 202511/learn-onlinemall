package com.own.onlinemall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//引入了amqp


//seata 控制分布式事务
//每一个微服务必须创建undo_log
// 安装事务协调器  seata-server
// 整合
@EnableFeignClients
@EnableRedisHttpSession
@SpringBootApplication
public class OnlinemallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinemallOrderApplication.class, args);
    }

}

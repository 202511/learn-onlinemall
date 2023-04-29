package com.own.onlinemall.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;

@SpringBootTest
class OnlinemallAuthServerApplicationTests {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void encode()
    {

    }
}

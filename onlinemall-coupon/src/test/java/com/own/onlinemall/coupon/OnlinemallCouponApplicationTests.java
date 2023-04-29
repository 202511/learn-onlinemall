package com.own.onlinemall.coupon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class OnlinemallCouponApplicationTests {

    @Test
    void contextLoads() {
        //时间测试
        LocalDate now = LocalDate.now(); //2023-04-27
        System.out.println(now);
        LocalDate plus = now.plusDays(3);
        System.out.println(plus); //2023-04-30
        LocalTime min = LocalTime.MIN;
        LocalTime max = LocalTime.MAX;
        System.out.println(min);
        System.out.println(max);
        LocalDateTime of = LocalDateTime.of(now, min);
        LocalDateTime of1 = LocalDateTime.of(plus, max);
        System.out.println(of);
        System.out.println(of1);
        String format = of1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        2023-04-27
//        2023-04-30
//        00:00
//        23:59:59.999999999
//        2023-04-27T00:00
//        2023-04-30T23:59:59.999999999
        System.out.println(format);
    }

}

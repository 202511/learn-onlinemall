package com.own.onlinemall.thirdparty;

import com.own.onlinemall.thirdparty.component.SmsComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlinemallThirdPartyApplicationTests {
    @Autowired
    SmsComponent smsComponent;
    @Test
    void contextLoads() {
      smsComponent.sendSmsCode("13632958162", "9999f");
    }

}

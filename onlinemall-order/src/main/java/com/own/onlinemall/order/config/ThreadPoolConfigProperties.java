package com.own.onlinemall.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "onlinemall.thread")
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private  Integer maxSize;
    private  Integer keepAliveTime;
}

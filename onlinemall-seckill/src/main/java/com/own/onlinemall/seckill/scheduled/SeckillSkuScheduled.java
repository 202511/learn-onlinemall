package com.own.onlinemall.seckill.scheduled;


import com.own.onlinemall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/*
    秒杀商品的定时上架
    每天晚上3 点， 上架最近3 天需要秒杀的商品
    当天 00：00：00  - 23：59：59
    明天 00：00：00  - 23：59：59
    后天 00：00：00  - 23：59：59
   查出这三天需要秒杀的所有商品 ，
*/
@Slf4j
@Service
public class SeckillSkuScheduled {
    @Autowired
    SeckillService seckillService;

    @Autowired
    RedissonClient redissonClient;
    private  final  String upload_lock="seckill:upload:lock";

    //每天晚上3 点
    @Scheduled(cron =  "* * 3 * * ?")
    public void  uploadSecKillSkuLatest3Days()
    {
        log.info("上架秒杀的商品信息。。。");
        // 重复上架无需处理
        // 确保定时任务只执行一次
        //分布式锁， 锁的业务执行完成， 状态已经更新完成， 释放锁以后， 其他人获取到就会拿到最新的状态
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillService.uploadSeckillSkuLatest3Days();
        }
        finally {
            lock.unlock();
        }
    }

}

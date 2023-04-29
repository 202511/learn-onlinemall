package com.own.onlinemall.seckill.scheduled;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class HelloSchedule {
    /*开启一个定时任务 @EnableScheduling
    // 定时任务是阻塞的， 当前任务停滞， 下一个任务会等待
    // 解决方法： 1. 可以让业务运行以异步的方式，自己提交到线程池 即创建另一个线程去运行， 主线程继续
    //          2.  本来springboot 支持定时任务线程池， taskschedule
    //               TaskSchedulingAutoConfiguration->TaskSchedulingProperties
                     ->Pool 里面只有一个线程所以才堵塞
                    我们可以通过设置来激活spring.task.scheduling.pool.size 无效
                 3.    还需类上添加上@EnableAsync注解 来开启异步任务
                       方法上添加 @Async 注解就有效果
                       相当于让定时任务异步执行
                       我们来一下异步任务的底层配置
     TaskExecutionAutoConfiguration 在这里就配置了
     属性绑定在TaskExecutionProperties
     里面有private final Pool pool = new Pool();
     pool的配置中默认核心线程数为8  ， 最大为int的最大值
     当然我们可以直接进行配置
     ThreadPoolTaskExecutor 其实就是线程池
     最终解决: 使用异步+ 定时任务来完成定时任务不阻塞的功能
         //在spring中 cron 由六位组成， 不允许七位 秒 分 时 天 月 星期几 （没有年）
    // cron = "* * * * * 4" 周四 每秒打印一次 即 星期几的位置 1-7 代表星期一到星期日
     cron = "*5 * * * * ?" 每5秒打印一次 "
//            */
//    @Async
//    @Scheduled(cron = "* * * * * ?")
//    public void  hello() throws InterruptedException {
//         log.info("hello...");
//         Thread.sleep(3000);
//     }

}

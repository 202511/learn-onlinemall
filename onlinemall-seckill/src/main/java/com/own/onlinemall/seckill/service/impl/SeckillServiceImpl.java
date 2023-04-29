package com.own.onlinemall.seckill.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.auth.to.SeckillOrderTO;
import com.own.onlinemall.auth.vo.MemberResponseVO;
import com.own.onlinemall.seckill.feign.CouponFeignService;
import com.own.onlinemall.seckill.feign.ProductFeignService;
import com.own.onlinemall.seckill.interceptor.LoginUserInterceptor;
import com.own.onlinemall.seckill.service.SeckillService;
import com.own.onlinemall.seckill.to.SeckillSkuRedisTO;
import com.own.onlinemall.seckill.vo.SeckillSessionWithSkusVO;
import com.own.onlinemall.seckill.vo.SkuInfoVO;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    //killId 组装好的key 直接拿到封装的秒杀商品详情
    @Override
    public String kill(String killId, String key, Integer num) throws InterruptedException {
        MemberResponseVO response = LoginUserInterceptor.loginUser.get();
        // 获取当前秒杀商品的详细信息
        BoundHashOperations<String, String, String> operations = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String s = operations.get(killId);
        if(StringUtils.isEmpty(s))
        {

        }
        else
        {
            SeckillSkuRedisTO seckillSkuRedisTO = JSON.parseObject(s, SeckillSkuRedisTO.class);
            //校验合法性
            //1秒杀时间检验
            Long startTime = seckillSkuRedisTO.getStartTime();
            Long endTime = seckillSkuRedisTO.getEndTime();
            long time = new Date().getTime();
            long ttl = endTime - time; // 过期时间
            if(time>=startTime&& time<=endTime)
            {
                // 合法
                //2校验商品的随机码和商品id是否符合
                String randomCode = seckillSkuRedisTO.getRandomCode();
                Long skuId = seckillSkuRedisTO.getSkuId();
                if(randomCode.equals(key))
                {
                     // 3 验证购物数量是否合理
                    Integer seckillLimit = seckillSkuRedisTO.getSeckillLimit();
                    if(num<=seckillLimit)
                    {
                         // 验证这个用户是否重复购买 ，幂等性判断
                         // 只要秒杀成功就去占位， 写一个特定的用户id 作为key 渠道redis
                        // 用户id+ 活动场次id + 商品id
                        String redisKey=response.getId()+"_"+seckillSkuRedisTO.getPromotionSessionId()+"_"+seckillSkuRedisTO.getSkuId();
                        //设置在活动结束时， 这个key过期
                        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                        //如果占位成功 说明用户从来没有买过
                        if(aBoolean)
                        {
                             // 到这里前期校验已经完成
                            // 得到信号量
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            //等待100 毫秒 拿得出 就成功， 拿不出就秒杀失败
                            boolean b = semaphore.tryAcquire(num, 100, TimeUnit.MILLISECONDS);// 取出一定数量的商品
                            //秒杀成功
                            // 快速下单 发送mq消息  只耗费 10 毫秒， 这样性能就特别快
                            //
                            if(b) {
                                String timeId = IdWorker.getTimeId(); // 生成订单号
                                SeckillOrderTO seckillOrderTO = new SeckillOrderTO();
                                seckillOrderTO.setOrderSn(timeId);
                                seckillOrderTO.setMemberId(response.getId());
                                seckillOrderTO.setNum(num);
                                seckillOrderTO.setPromotionSessionId(seckillSkuRedisTO.getPromotionSessionId());
                                seckillOrderTO.setSeckillPrice(seckillSkuRedisTO.getSeckillPrice());
                                rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", seckillOrderTO);


                                return timeId;
                            }
                            else
                            {
                                return null;
                            }


                        }
                        else
                        {
                             return null;
                        }

                    }
                }


            }
            else
            {
                // 失败
                 return null ;
            }
        }
        return null;
    }






    //获取当前sku 是否有相关的秒杀活动
    @SentinelResource(value = "getSkuSeckillInfoSource")
    @Override
    public SeckillSkuRedisTO getSkuSeckillInfo(Long skuId) {
        // 找到所有需要参与秒杀的商品的key
        BoundHashOperations<String, String, String> operations = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = operations.keys();
        if(keys!=null && keys.size()>0)
        {
            String regx="\\d_"+skuId;
            for (String key : keys) {
                boolean matches = Pattern.matches(regx, key);
                if (matches)
                {   try(Entry seckillSkus = SphU.entry("seckillSkus"))
                    {
                        String s = operations.get(key);
                        SeckillSkuRedisTO seckillSkuRedisTO = JSON.parseObject(s, SeckillSkuRedisTO.class);
                        // 随机码
                        Long startTime = seckillSkuRedisTO.getStartTime();
                        Long endTime = seckillSkuRedisTO.getEndTime();
                        long time = new Date().getTime();
                        if (time >= startTime && time <= endTime) {

                        } else {
                            seckillSkuRedisTO.setRandomCode(null);
                        }
                        return seckillSkuRedisTO;
                    } catch (BlockException e) {
                    e.printStackTrace();
                }
                }

            }
        }
        return null;
    }




    // 返回当前时间可以参与的秒杀商品信息
    @Override
    public List<SeckillSkuRedisTO> getCurrentSeckillSkus() {
        // 确定当前时间属性哪个秒杀场次
        long time = new Date().getTime();
        Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
        for (String key : keys) {
            String replace = key.replace(SESSIONS_CACHE_PREFIX, "");
            String[] s = replace.split("_");
            long startTime = Long.parseLong(s[0]);
            long endTime= Long.parseLong(s[1]);
            // 这个场次满足现在的时间要求， 即 现在在场次的执行时间内
            if(time>=startTime&& time<=endTime)
            {
                 // 获取当前场次的商品信息 按照索引取
                List<String> range = redisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> operations = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                //绑定哈希值
                List<String> objects = operations.multiGet(range);
                if(objects!=null)
                {
                    List<SeckillSkuRedisTO> collect = objects.stream().map(item -> {
                        SeckillSkuRedisTO redisTO = JSON.parseObject( item, SeckillSkuRedisTO.class);
//                         redisTO.setRandomCode(null); 当前秒杀开始需要随机码
                        return redisTO;

                    }).collect(Collectors.toList());
                    return collect;
                }

            }

        }

        return null;
    }




    @Autowired
    CouponFeignService couponFeignService;
    private  final  String SESSIONS_CACHE_PREFIX="seckill:sessions:";
    @Override
    public void uploadSeckillSkuLatest3Days() {
        // 扫描最近三天参与秒杀的活动 ，
        R latest3DaySession = couponFeignService.getLatest3DaySession();
        if(latest3DaySession.getCode()==0)
        {
            //上架商品
            List<SeckillSessionWithSkusVO> data = latest3DaySession.getData(new TypeReference<List<SeckillSessionWithSkusVO>>() {
            });
            // 缓存到redis
            // 缓存活动信息
            saveSessionInfos(data);
            // 缓存活动的关联商品信息
            saveSessionSkuInfos(data);
        }
        // 远程调用优惠系统 ，
    }



    @Autowired
    StringRedisTemplate redisTemplate;
    private void saveSessionInfos(List<SeckillSessionWithSkusVO> data) {
        data.stream().forEach(session -> {
            Long startTime= session.getStartTime().getTime();
            Long endTime= session.getEndTime().getTime();
            String key=SESSIONS_CACHE_PREFIX+startTime+"_"+endTime;

            Boolean aBoolean = redisTemplate.hasKey(key);
            if (!aBoolean) {
                List<String> collect = session.getRelationSkus().stream().map(s->{
                    return s.getPromotionSessionId().toString()+"_"+ s.getSkuId().toString();
                }).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key, collect);
            }

        });
    }
    //秒杀不是去数据库扣库存，
    // 每一个商品设置信号量， 来一个减一个， 带着随机码减库存
    private final  String SKU_STOCK_SEMAPHORE="seckill:stock";
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    ProductFeignService productFeignService;
    private  final  String SKUKILL_CACHE_PREFIX = "seckill:skus";
    private void saveSessionSkuInfos(List<SeckillSessionWithSkusVO> data) {
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        data.stream().forEach(session->{
            session.getRelationSkus().stream().forEach(seckillSkuVO -> {
                        Boolean aBoolean = ops.hasKey(seckillSkuVO.getPromotionSessionId().toString()+"_"+seckillSkuVO.getSkuId().toString());
                        if (!aBoolean) {
                            SeckillSkuRedisTO seckillSkuRedisTO = new SeckillSkuRedisTO();
                            //1. sku的秒杀信息
                            BeanUtils.copyProperties(seckillSkuVO, seckillSkuRedisTO);
                            //2. sku的基本信息
                            SkuInfoVO t = productFeignService.getT(seckillSkuVO.getSkuId());
                            seckillSkuRedisTO.setSkuInfoVO(t);
                            // 当前商品的时间设置
                            seckillSkuRedisTO.setStartTime(session.getStartTime().getTime());
                            seckillSkuRedisTO.setEndTime(session.getEndTime().getTime());
                            // 随机码， 防止脚本
                            String token = UUID.randomUUID().toString().replace("-", "");
                            seckillSkuRedisTO.setRandomCode(token);
                            //在redis中添加信号量， 用户请求来了先对分布式信号量进行计算， 信号量值： 前缀+ 随机码 （防脚本）
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                            semaphore.trySetPermits(seckillSkuVO.getSeckillCount()); // 商品可以秒杀的件数就是分布式的信号量值
                            String s1 = JSON.toJSONString(seckillSkuRedisTO);
                            ops.put(seckillSkuVO.getPromotionSessionId().toString()+"_"+seckillSkuVO.getSkuId().toString(), s1);
                        }

                    }
                    );
        });
    }
}

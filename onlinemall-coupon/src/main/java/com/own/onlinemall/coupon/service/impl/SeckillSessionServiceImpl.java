package com.own.onlinemall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.coupon.dao.SeckillSessionDao;
import com.own.onlinemall.coupon.dto.SeckillSessionDTO;
import com.own.onlinemall.coupon.entity.SeckillSessionEntity;
import com.own.onlinemall.coupon.entity.SeckillSkuRelationEntity;
import com.own.onlinemall.coupon.service.SeckillSessionService;
import com.own.onlinemall.coupon.service.SeckillSkuRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 秒杀活动场次
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class SeckillSessionServiceImpl extends CrudServiceImpl<SeckillSessionDao, SeckillSessionEntity, SeckillSessionDTO> implements SeckillSessionService {

    @Override
    public QueryWrapper<SeckillSessionEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SeckillSessionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }
   @Autowired
    SeckillSkuRelationService service;

    @Override
    public List<SeckillSessionEntity> getLatest3DaySession() {
        //计算三天
   //     Date date = new Date();

        List<SeckillSessionEntity> list = baseDao.selectList(new QueryWrapper<SeckillSessionEntity>().between("start_time", startTime(), endTime()));
        if(list!=null && list.size() >0)
        {
              list.stream().map(session->{
                  Long id = session.getId();
                  List<SeckillSkuRelationEntity> list1=service.getSkuListBySessionId(id);
                   session.setRelationSkus(list1);
                   return  session;
              }).collect(Collectors.toList());
        }
        return list;
    }
    private String startTime()
    {
        LocalDate now = LocalDate.now(); //2023-04-27
        System.out.println(now);
        LocalTime min = LocalTime.MIN;
        LocalTime max = LocalTime.MAX;
        System.out.println(min);
        System.out.println(max);
        LocalDateTime of = LocalDateTime.of(now, min);
        System.out.println(of);
        String format = of.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return format;
    }
    private String endTime()
    {
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
        return format;
    }
}
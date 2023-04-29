package com.own.onlinemall.product.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.own.onlinemall.product.entity.CategoryEntity;
import com.own.onlinemall.product.service.CategoryService;
import com.own.onlinemall.product.vo.Catalog2Vo;
import com.own.onlinemall.product.vo.MemberResponseVO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;


    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model)
    {
        //查出所有的一级分类
        List<CategoryEntity> categoryEntities=categoryService.getLevelOneCategorys();
        model.addAttribute("categorys", categoryEntities);
        return "index";
    }
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedissonClient redissonClient;
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public  Map<String, List<Catalog2Vo>> getCatalogJson(HttpSession session) {
        //占分布式锁 ， 去redis占锁
        //锁的名字， 锁的粒度， 越细越快， 响应速度越快
        RLock lock = redissonClient.getLock("catalogJSON-lock");
        lock.lock();
//        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", "111",30,TimeUnit.SECONDS);
//        if(lock==true) // 若占锁成功
        try {
            //成功后设置锁的过期时间， 防止业务出现问题， 出现死锁的现象
            //以后复杂数据缓存中存的都是json字符串 这样就能跨平台 跨语言
            String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
            Map<String, List<Catalog2Vo>> map = null;
            if (catalogJSON == null) {
                map = categoryService.getCatalogJsonWithSpringCache();
                String s = JSON.toJSONString(map);
                stringRedisTemplate.opsForValue().set("catalogJSON", s);
            } else {
                map = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catalog2Vo>>>() {
                });
            }
            stringRedisTemplate.delete("lock");
            return map;
        }
        finally {
            lock.unlock();
        }



    }

}

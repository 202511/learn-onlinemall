package com.own.onlinemall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.coupon.dao.SeckillSkuNoticeDao;
import com.own.onlinemall.coupon.dto.SeckillSkuNoticeDTO;
import com.own.onlinemall.coupon.entity.SeckillSkuNoticeEntity;
import com.own.onlinemall.coupon.service.SeckillSkuNoticeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class SeckillSkuNoticeServiceImpl extends CrudServiceImpl<SeckillSkuNoticeDao, SeckillSkuNoticeEntity, SeckillSkuNoticeDTO> implements SeckillSkuNoticeService {

    @Override
    public QueryWrapper<SeckillSkuNoticeEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SeckillSkuNoticeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
package com.own.onlinemall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.coupon.dao.CouponHistoryDao;
import com.own.onlinemall.coupon.dto.CouponHistoryDTO;
import com.own.onlinemall.coupon.entity.CouponHistoryEntity;
import com.own.onlinemall.coupon.service.CouponHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 优惠券领取历史记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class CouponHistoryServiceImpl extends CrudServiceImpl<CouponHistoryDao, CouponHistoryEntity, CouponHistoryDTO> implements CouponHistoryService {

    @Override
    public QueryWrapper<CouponHistoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CouponHistoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
package com.own.onlinemall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.coupon.dao.CouponSpuRelationDao;
import com.own.onlinemall.coupon.dto.CouponSpuRelationDTO;
import com.own.onlinemall.coupon.entity.CouponSpuRelationEntity;
import com.own.onlinemall.coupon.service.CouponSpuRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 优惠券与产品关联
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class CouponSpuRelationServiceImpl extends CrudServiceImpl<CouponSpuRelationDao, CouponSpuRelationEntity, CouponSpuRelationDTO> implements CouponSpuRelationService {

    @Override
    public QueryWrapper<CouponSpuRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CouponSpuRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
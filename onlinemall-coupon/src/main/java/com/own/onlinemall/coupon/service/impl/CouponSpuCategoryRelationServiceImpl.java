package com.own.onlinemall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.coupon.dao.CouponSpuCategoryRelationDao;
import com.own.onlinemall.coupon.dto.CouponSpuCategoryRelationDTO;
import com.own.onlinemall.coupon.entity.CouponSpuCategoryRelationEntity;
import com.own.onlinemall.coupon.service.CouponSpuCategoryRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 优惠券分类关联
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class CouponSpuCategoryRelationServiceImpl extends CrudServiceImpl<CouponSpuCategoryRelationDao, CouponSpuCategoryRelationEntity, CouponSpuCategoryRelationDTO> implements CouponSpuCategoryRelationService {

    @Override
    public QueryWrapper<CouponSpuCategoryRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CouponSpuCategoryRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
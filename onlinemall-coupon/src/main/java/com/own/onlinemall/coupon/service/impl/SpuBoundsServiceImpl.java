package com.own.onlinemall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.coupon.dao.SpuBoundsDao;
import com.own.onlinemall.coupon.dto.SpuBoundsDTO;
import com.own.onlinemall.coupon.entity.SpuBoundsEntity;
import com.own.onlinemall.coupon.service.SpuBoundsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class SpuBoundsServiceImpl extends CrudServiceImpl<SpuBoundsDao, SpuBoundsEntity, SpuBoundsDTO> implements SpuBoundsService {

    @Override
    public QueryWrapper<SpuBoundsEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpuBoundsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
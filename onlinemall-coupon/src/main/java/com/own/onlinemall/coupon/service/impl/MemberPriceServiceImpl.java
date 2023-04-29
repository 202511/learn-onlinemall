package com.own.onlinemall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.coupon.dao.MemberPriceDao;
import com.own.onlinemall.coupon.dto.MemberPriceDTO;
import com.own.onlinemall.coupon.entity.MemberPriceEntity;
import com.own.onlinemall.coupon.service.MemberPriceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class MemberPriceServiceImpl extends CrudServiceImpl<MemberPriceDao, MemberPriceEntity, MemberPriceDTO> implements MemberPriceService {

    @Override
    public QueryWrapper<MemberPriceEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberPriceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
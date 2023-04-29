package com.own.onlinemall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.coupon.dao.SeckillSkuRelationDao;
import com.own.onlinemall.coupon.dto.SeckillSkuRelationDTO;
import com.own.onlinemall.coupon.entity.SeckillSkuRelationEntity;
import com.own.onlinemall.coupon.service.SeckillSkuRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class SeckillSkuRelationServiceImpl extends CrudServiceImpl<SeckillSkuRelationDao, SeckillSkuRelationEntity, SeckillSkuRelationDTO> implements SeckillSkuRelationService {

    @Override
    public QueryWrapper<SeckillSkuRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SeckillSkuRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<SeckillSkuRelationEntity> getSkuListBySessionId(Long id) {
        List<SeckillSkuRelationEntity> promotion_session_id = baseDao.selectList(new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id", id));
        return promotion_session_id;
    }
}
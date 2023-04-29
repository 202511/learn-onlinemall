package com.own.onlinemall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.order.dao.RefundInfoDao;
import com.own.onlinemall.order.dto.RefundInfoDTO;
import com.own.onlinemall.order.entity.RefundInfoEntity;
import com.own.onlinemall.order.service.RefundInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 退款信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class RefundInfoServiceImpl extends CrudServiceImpl<RefundInfoDao, RefundInfoEntity, RefundInfoDTO> implements RefundInfoService {

    @Override
    public QueryWrapper<RefundInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<RefundInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
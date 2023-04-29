package com.own.onlinemall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.order.dao.MqMessageDao;
import com.own.onlinemall.order.dto.MqMessageDTO;
import com.own.onlinemall.order.entity.MqMessageEntity;
import com.own.onlinemall.order.service.MqMessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class MqMessageServiceImpl extends CrudServiceImpl<MqMessageDao, MqMessageEntity, MqMessageDTO> implements MqMessageService {

    @Override
    public QueryWrapper<MqMessageEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MqMessageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
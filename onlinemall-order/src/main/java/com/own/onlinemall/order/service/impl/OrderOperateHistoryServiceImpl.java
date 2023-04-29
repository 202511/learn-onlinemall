package com.own.onlinemall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.order.dao.OrderOperateHistoryDao;
import com.own.onlinemall.order.dto.OrderOperateHistoryDTO;
import com.own.onlinemall.order.entity.OrderOperateHistoryEntity;
import com.own.onlinemall.order.service.OrderOperateHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单操作历史记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class OrderOperateHistoryServiceImpl extends CrudServiceImpl<OrderOperateHistoryDao, OrderOperateHistoryEntity, OrderOperateHistoryDTO> implements OrderOperateHistoryService {

    @Override
    public QueryWrapper<OrderOperateHistoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderOperateHistoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
package com.own.onlinemall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.ware.dao.WareOrderTaskDao;
import com.own.onlinemall.ware.dto.WareOrderTaskDTO;
import com.own.onlinemall.ware.entity.WareOrderTaskEntity;
import com.own.onlinemall.ware.service.WareOrderTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class WareOrderTaskServiceImpl extends CrudServiceImpl<WareOrderTaskDao, WareOrderTaskEntity, WareOrderTaskDTO> implements WareOrderTaskService {

    @Override
    public QueryWrapper<WareOrderTaskEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<WareOrderTaskEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn) {
        //
        WareOrderTaskEntity order_sn = baseDao.selectOne(new QueryWrapper<WareOrderTaskEntity>().eq("order_sn", orderSn));

        return order_sn;
    }
}
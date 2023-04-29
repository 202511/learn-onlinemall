package com.own.onlinemall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.ware.dao.WareOrderTaskDetailDao;
import com.own.onlinemall.ware.dto.WareOrderTaskDetailDTO;
import com.own.onlinemall.ware.entity.WareOrderTaskDetailEntity;
import com.own.onlinemall.ware.service.WareOrderTaskDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 库存工作单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class WareOrderTaskDetailServiceImpl extends CrudServiceImpl<WareOrderTaskDetailDao, WareOrderTaskDetailEntity, WareOrderTaskDetailDTO> implements WareOrderTaskDetailService {

    @Override
    public QueryWrapper<WareOrderTaskDetailEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<WareOrderTaskDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<WareOrderTaskDetailEntity> getList(Long id) {
        List<WareOrderTaskDetailEntity> list = baseDao.selectList(new QueryWrapper<WareOrderTaskDetailEntity>().eq("task_id", id).eq("lock_status", 1));
        return list;
    }
}
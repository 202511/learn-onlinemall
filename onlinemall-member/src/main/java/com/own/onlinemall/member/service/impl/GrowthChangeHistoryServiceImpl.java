package com.own.onlinemall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.member.dao.GrowthChangeHistoryDao;
import com.own.onlinemall.member.dto.GrowthChangeHistoryDTO;
import com.own.onlinemall.member.entity.GrowthChangeHistoryEntity;
import com.own.onlinemall.member.service.GrowthChangeHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 成长值变化历史记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class GrowthChangeHistoryServiceImpl extends CrudServiceImpl<GrowthChangeHistoryDao, GrowthChangeHistoryEntity, GrowthChangeHistoryDTO> implements GrowthChangeHistoryService {

    @Override
    public QueryWrapper<GrowthChangeHistoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<GrowthChangeHistoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
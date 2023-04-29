package com.own.onlinemall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.member.dao.MemberStatisticsInfoDao;
import com.own.onlinemall.member.dto.MemberStatisticsInfoDTO;
import com.own.onlinemall.member.entity.MemberStatisticsInfoEntity;
import com.own.onlinemall.member.service.MemberStatisticsInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 会员统计信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class MemberStatisticsInfoServiceImpl extends CrudServiceImpl<MemberStatisticsInfoDao, MemberStatisticsInfoEntity, MemberStatisticsInfoDTO> implements MemberStatisticsInfoService {

    @Override
    public QueryWrapper<MemberStatisticsInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberStatisticsInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
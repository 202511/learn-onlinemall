package com.own.onlinemall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.member.constant.ObjectConstant;
import com.own.onlinemall.member.dao.MemberLevelDao;
import com.own.onlinemall.member.dto.MemberLevelDTO;
import com.own.onlinemall.member.entity.MemberLevelEntity;
import com.own.onlinemall.member.service.MemberLevelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 会员等级
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class MemberLevelServiceImpl extends CrudServiceImpl<MemberLevelDao, MemberLevelEntity, MemberLevelDTO> implements MemberLevelService {

    @Override
    public QueryWrapper<MemberLevelEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberLevelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public MemberLevelEntity getDefaultLevel() {
        return baseDao.selectOne(new QueryWrapper<MemberLevelEntity>().eq("default_status", ObjectConstant.BooleanIntEnum.YES.getCode()));
    }
}
package com.own.onlinemall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.member.dao.MemberReceiveAddressDao;
import com.own.onlinemall.member.dto.MemberReceiveAddressDTO;
import com.own.onlinemall.member.entity.MemberReceiveAddressEntity;
import com.own.onlinemall.member.service.MemberReceiveAddressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class MemberReceiveAddressServiceImpl extends CrudServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity, MemberReceiveAddressDTO> implements MemberReceiveAddressService {

    @Override
    public QueryWrapper<MemberReceiveAddressEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberReceiveAddressEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<MemberReceiveAddressEntity> getAddress(Long memberId) {
       return   baseDao.selectList(new QueryWrapper<MemberReceiveAddressEntity>().eq("member_id", memberId));

    }
}
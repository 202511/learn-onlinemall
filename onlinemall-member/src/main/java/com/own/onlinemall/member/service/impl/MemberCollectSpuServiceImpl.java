package com.own.onlinemall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.member.dao.MemberCollectSpuDao;
import com.own.onlinemall.member.dto.MemberCollectSpuDTO;
import com.own.onlinemall.member.entity.MemberCollectSpuEntity;
import com.own.onlinemall.member.service.MemberCollectSpuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 会员收藏的商品
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class MemberCollectSpuServiceImpl extends CrudServiceImpl<MemberCollectSpuDao, MemberCollectSpuEntity, MemberCollectSpuDTO> implements MemberCollectSpuService {

    @Override
    public QueryWrapper<MemberCollectSpuEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberCollectSpuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
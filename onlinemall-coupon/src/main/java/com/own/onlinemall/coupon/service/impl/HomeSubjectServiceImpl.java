package com.own.onlinemall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.coupon.dao.HomeSubjectDao;
import com.own.onlinemall.coupon.dto.HomeSubjectDTO;
import com.own.onlinemall.coupon.entity.HomeSubjectEntity;
import com.own.onlinemall.coupon.service.HomeSubjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class HomeSubjectServiceImpl extends CrudServiceImpl<HomeSubjectDao, HomeSubjectEntity, HomeSubjectDTO> implements HomeSubjectService {

    @Override
    public QueryWrapper<HomeSubjectEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<HomeSubjectEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}
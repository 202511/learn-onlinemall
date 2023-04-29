package com.own.onlinemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.product.dao.AttrDao;
import com.own.onlinemall.product.dto.AttrDTO;
import com.own.onlinemall.product.entity.AttrEntity;
import com.own.onlinemall.product.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class AttrServiceImpl extends CrudServiceImpl<AttrDao, AttrEntity, AttrDTO> implements AttrService {

    @Override
    public QueryWrapper<AttrEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<AttrEntity> getSearchAttrs() {
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<>();
        attrEntityQueryWrapper.eq("search_Type", 1);
        List<AttrEntity> attrEntities = baseDao.selectList(attrEntityQueryWrapper);
        return attrEntities;
    }
}
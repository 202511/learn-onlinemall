package com.own.onlinemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.product.dao.AttrGroupDao;
import com.own.onlinemall.product.dto.AttrGroupDTO;
import com.own.onlinemall.product.entity.AttrGroupEntity;
import com.own.onlinemall.product.service.AttrGroupService;
import com.own.onlinemall.product.vo.SpuItemAttrGroupVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class AttrGroupServiceImpl extends CrudServiceImpl<AttrGroupDao, AttrGroupEntity, AttrGroupDTO> implements AttrGroupService {

    @Override
    public QueryWrapper<AttrGroupEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        List<SpuItemAttrGroupVO> spuItemAttrGroupVOList=baseDao.getAttrGroupWithAttrsBySpuId(spuId,catalogId);
        return spuItemAttrGroupVOList;
    }
}
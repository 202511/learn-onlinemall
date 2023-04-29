package com.own.onlinemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.product.dao.ProductAttrValueDao;
import com.own.onlinemall.product.dto.ProductAttrValueDTO;
import com.own.onlinemall.product.entity.AttrEntity;
import com.own.onlinemall.product.entity.ProductAttrValueEntity;
import com.own.onlinemall.product.service.AttrService;
import com.own.onlinemall.product.service.ProductAttrValueService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class ProductAttrValueServiceImpl extends CrudServiceImpl<ProductAttrValueDao, ProductAttrValueEntity, ProductAttrValueDTO> implements ProductAttrValueService {

    @Override
    public QueryWrapper<ProductAttrValueEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ProductAttrValueEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Autowired
    AttrService attrService;
    @Override
    public List<ProductAttrValueEntity> getSpuAttrs(Long id) {
        ArrayList<AttrEntity> attrEntityArrayList = new ArrayList<AttrEntity>();
        QueryWrapper<ProductAttrValueEntity> productAttrValueEntityQueryWrapper = new QueryWrapper<>();
        productAttrValueEntityQueryWrapper.eq("spu_id", id);
        List<ProductAttrValueEntity> productAttrValueEntities = baseDao.selectList(productAttrValueEntityQueryWrapper);
        return productAttrValueEntities;
    }
}
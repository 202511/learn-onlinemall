package com.own.onlinemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.product.dao.SkuSaleAttrValueDao;
import com.own.onlinemall.product.dto.SkuSaleAttrValueDTO;
import com.own.onlinemall.product.entity.SkuSaleAttrValueEntity;
import com.own.onlinemall.product.service.SkuSaleAttrValueService;
import com.own.onlinemall.product.vo.SkuItemSaleAttrVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class SkuSaleAttrValueServiceImpl extends CrudServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity, SkuSaleAttrValueDTO> implements SkuSaleAttrValueService {

    @Override
    public QueryWrapper<SkuSaleAttrValueEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SkuSaleAttrValueEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<SkuItemSaleAttrVO> getSaleAttrsBySpuId(Long spuId) {
        List<SkuItemSaleAttrVO> skuItemSaleAttrVOS=baseDao.getSaleAttrsBySpuId(spuId);
        return skuItemSaleAttrVOS;
    }

    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {

        return baseDao.getSkuSaleAttrValues(skuId);
    }
}
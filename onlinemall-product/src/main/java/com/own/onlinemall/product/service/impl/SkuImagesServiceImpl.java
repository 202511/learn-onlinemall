package com.own.onlinemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.product.dao.SkuImagesDao;
import com.own.onlinemall.product.dto.SkuImagesDTO;
import com.own.onlinemall.product.entity.SkuImagesEntity;
import com.own.onlinemall.product.service.SkuImagesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class SkuImagesServiceImpl extends CrudServiceImpl<SkuImagesDao, SkuImagesEntity, SkuImagesDTO> implements SkuImagesService {

    @Override
    public QueryWrapper<SkuImagesEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SkuImagesEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<SkuImagesEntity> getImagesBySkuId(Long skuId) {
        List<SkuImagesEntity> sku_id = baseDao.selectList(new QueryWrapper<SkuImagesEntity>().eq("sku_id", skuId));
        return sku_id;
    }
}
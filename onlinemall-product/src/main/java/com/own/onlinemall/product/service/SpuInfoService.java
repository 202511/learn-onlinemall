package com.own.onlinemall.product.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.product.dto.SpuInfoDTO;
import com.own.onlinemall.product.entity.SpuInfoEntity;

/**
 * spu信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface SpuInfoService extends CrudService<SpuInfoEntity, SpuInfoDTO> {
    //商品上架
    void up(Long id);

    SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}
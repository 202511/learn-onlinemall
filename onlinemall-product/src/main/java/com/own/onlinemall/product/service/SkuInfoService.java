package com.own.onlinemall.product.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.product.dto.SkuInfoDTO;
import com.own.onlinemall.product.entity.SkuInfoEntity;
import com.own.onlinemall.product.vo.SkuItemVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface SkuInfoService extends CrudService<SkuInfoEntity, SkuInfoDTO> {

    List<SkuInfoEntity> getSkuBySpuId(Long id);

    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;
}
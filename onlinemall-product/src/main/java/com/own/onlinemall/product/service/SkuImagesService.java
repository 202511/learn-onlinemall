package com.own.onlinemall.product.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.product.dto.SkuImagesDTO;
import com.own.onlinemall.product.entity.SkuImagesEntity;

import java.util.List;

/**
 * sku图片
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface SkuImagesService extends CrudService<SkuImagesEntity, SkuImagesDTO> {

    List<SkuImagesEntity> getImagesBySkuId(Long skuId);
}
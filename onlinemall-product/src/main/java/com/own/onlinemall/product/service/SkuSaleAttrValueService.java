package com.own.onlinemall.product.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.product.dto.SkuSaleAttrValueDTO;
import com.own.onlinemall.product.entity.SkuSaleAttrValueEntity;
import com.own.onlinemall.product.vo.SkuItemSaleAttrVO;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface SkuSaleAttrValueService extends CrudService<SkuSaleAttrValueEntity, SkuSaleAttrValueDTO> {

    List<SkuItemSaleAttrVO> getSaleAttrsBySpuId(Long spuId);

    List<String> getSkuSaleAttrValuesAsStringList(Long skuId);
}
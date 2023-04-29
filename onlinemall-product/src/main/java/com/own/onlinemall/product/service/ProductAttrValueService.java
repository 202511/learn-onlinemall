package com.own.onlinemall.product.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.product.dto.ProductAttrValueDTO;
import com.own.onlinemall.product.entity.AttrEntity;
import com.own.onlinemall.product.entity.ProductAttrValueEntity;

import java.util.List;

/**
 * spu属性值
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface ProductAttrValueService extends CrudService<ProductAttrValueEntity, ProductAttrValueDTO> {

    List<ProductAttrValueEntity> getSpuAttrs(Long id);
}
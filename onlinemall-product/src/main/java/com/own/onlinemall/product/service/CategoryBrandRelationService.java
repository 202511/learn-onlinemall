package com.own.onlinemall.product.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.product.dto.CategoryBrandRelationDTO;
import com.own.onlinemall.product.entity.CategoryBrandRelationEntity;

/**
 * 品牌分类关联
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface CategoryBrandRelationService extends CrudService<CategoryBrandRelationEntity, CategoryBrandRelationDTO> {

    void updateCategory(Long catId, String name);
}
package com.own.onlinemall.product.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.product.dto.CategoryDTO;
import com.own.onlinemall.product.entity.CategoryEntity;
import com.own.onlinemall.product.vo.Catalog2Vo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
 public interface CategoryService extends CrudService<CategoryEntity, CategoryDTO> {



    List<CategoryEntity> listWithTree();

    boolean  deleteByIdS(List<Long> ids);

    void updateDetail(CategoryDTO dto);

    List<CategoryEntity> getLevelOneCategorys();

    Map<String, List<Catalog2Vo>> getCatalogJsonWithSpringCache();
}
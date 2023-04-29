package com.own.onlinemall.product.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.product.dto.AttrDTO;
import com.own.onlinemall.product.entity.AttrEntity;

import java.util.List;

/**
 * 商品属性
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface AttrService extends CrudService<AttrEntity, AttrDTO> {

    List<AttrEntity> getSearchAttrs();
}
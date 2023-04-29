package com.own.onlinemall.product.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.product.dto.AttrGroupDTO;
import com.own.onlinemall.product.entity.AttrGroupEntity;
import com.own.onlinemall.product.vo.SpuItemAttrGroupVO;

import java.util.List;

/**
 * 属性分组
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface AttrGroupService extends CrudService<AttrGroupEntity, AttrGroupDTO> {


    List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId);
}
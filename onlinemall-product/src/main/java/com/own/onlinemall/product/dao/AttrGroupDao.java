package com.own.onlinemall.product.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.product.entity.AttrGroupEntity;
import com.own.onlinemall.product.vo.SpuItemAttrGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface AttrGroupDao extends BaseDao<AttrGroupEntity> {

    List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId,@Param("catalogId") Long catalogId);
}
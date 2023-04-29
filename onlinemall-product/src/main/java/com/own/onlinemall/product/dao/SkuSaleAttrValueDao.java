package com.own.onlinemall.product.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.product.entity.SkuSaleAttrValueEntity;
import com.own.onlinemall.product.vo.SkuItemSaleAttrVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseDao<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVO> getSaleAttrsBySpuId(Long spuId);

    List<String> getSkuSaleAttrValues(@Param("skuId") Long skuId);
}
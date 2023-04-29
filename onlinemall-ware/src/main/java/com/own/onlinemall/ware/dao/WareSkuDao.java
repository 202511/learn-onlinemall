package com.own.onlinemall.ware.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface WareSkuDao extends BaseDao<WareSkuEntity> {

    WareSkuEntity selectBySkuId(@Param("sku_id") Long skuId);

    List<Long> listWareIdHasSkuStock(@Param("sku_id") Long skuId);

    Long lockSkuStock(@Param("sku_id") Long skuId,@Param("aLong") Long aLong,@Param("num") Integer num);

    void unlockStock(@Param("sku_id") Long skuId,@Param("ware_id") Long wareId,@Param("num") Integer num);

    void updateTaskDetail(@Param("taskDetailId") Long taskDetailId);
}
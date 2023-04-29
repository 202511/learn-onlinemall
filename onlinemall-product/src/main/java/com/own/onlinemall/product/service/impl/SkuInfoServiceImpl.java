package com.own.onlinemall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.product.dao.SkuInfoDao;
import com.own.onlinemall.product.dto.SkuInfoDTO;
import com.own.onlinemall.product.entity.SkuImagesEntity;
import com.own.onlinemall.product.entity.SkuInfoEntity;
import com.own.onlinemall.product.entity.SpuInfoDescEntity;
import com.own.onlinemall.product.feign.SeckillFeignService;
import com.own.onlinemall.product.service.*;
import com.own.onlinemall.product.vo.SeckillSkuVO;
import com.own.onlinemall.product.vo.SkuItemSaleAttrVO;
import com.own.onlinemall.product.vo.SkuItemVo;
import com.own.onlinemall.product.vo.SpuItemAttrGroupVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * sku信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class SkuInfoServiceImpl extends CrudServiceImpl<SkuInfoDao, SkuInfoEntity, SkuInfoDTO> implements SkuInfoService {

    @Override
    public QueryWrapper<SkuInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<SkuInfoEntity> getSkuBySpuId(Long id) {
        List<SkuInfoEntity> skuInfoEntityList = new ArrayList<>();
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", id);
        skuInfoEntityList=baseDao.selectList(queryWrapper);
        return skuInfoEntityList;
    }
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    SeckillFeignService seckillFeignService;
    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {

        SkuItemVo skuItemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> skuInfoEntityCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //sku基本信息获取
            SkuInfoEntity skuInfoEntity = selectById(skuId);
            skuItemVo.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, threadPoolExecutor);

        CompletableFuture<Void> d1 = skuInfoEntityCompletableFuture.thenAcceptAsync((res) -> {
            //获取spu的销售属性组合
            List<SkuItemSaleAttrVO> skuItemSaleAttrVOS = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(skuItemSaleAttrVOS);
        }, threadPoolExecutor);

        CompletableFuture<Void> d2 = skuInfoEntityCompletableFuture.thenAcceptAsync((res) -> {
            //获取spu的介绍
            Long spuId = res.getSpuId();
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.selectById(spuId);
            skuItemVo.setDesc(spuInfoDescEntity);
        }, threadPoolExecutor);


        CompletableFuture<Void> d3 = skuInfoEntityCompletableFuture.thenAcceptAsync((res) -> {
            //获取spu的规格参数信息
            List<SpuItemAttrGroupVO> spuItemAttrGroupVOList = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(spuItemAttrGroupVOList);
        }, threadPoolExecutor);

        CompletableFuture<Void> d4 = CompletableFuture.runAsync(() -> {
            //sku的图片信息
            List<SkuImagesEntity> images=skuImagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(images);
        },threadPoolExecutor);

        CompletableFuture<Void> d5 = CompletableFuture.runAsync(() -> {
            // 查询当前sku是否参与秒杀活动
            R skuSeckillInfo = seckillFeignService.getSkuSeckillInfo(skuId);
            if (skuSeckillInfo.getCode() == 0) {
                SeckillSkuVO data = skuSeckillInfo.getData(new TypeReference<SeckillSkuVO>() {
                });
                skuItemVo.setSeckillSku(data);
            }
        }, threadPoolExecutor);


        //等待所有任务完成
        CompletableFuture.allOf(d1,d2,d3,d4,d5).get();
        return skuItemVo;
    }
}
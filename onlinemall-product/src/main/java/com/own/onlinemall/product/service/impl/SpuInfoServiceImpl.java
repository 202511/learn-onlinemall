package com.own.onlinemall.product.service.impl;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.product.dao.SpuInfoDao;
import com.own.onlinemall.product.dto.SpuInfoDTO;
import com.own.onlinemall.product.entity.*;
import com.own.onlinemall.product.es.SkuEsModel;
import com.own.onlinemall.product.feign.SearchFeignService;
import com.own.onlinemall.product.feign.WareFeignService;
import com.own.onlinemall.product.service.*;
import com.own.onlinemall.product.vo.SkuHasStockVo;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spu信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class SpuInfoServiceImpl extends CrudServiceImpl<SpuInfoDao, SpuInfoEntity, SpuInfoDTO> implements SpuInfoService {

    @Override
    public QueryWrapper<SpuInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    AttrService attrService;
    @Autowired
    WareFeignService wareFeignService;
    @Autowired
    SearchFeignService searchFeignService;
    @Override
    public void up(Long id) {
        //查出当前spu的规格属性
        List<ProductAttrValueEntity> spuAttrs = productAttrValueService.getSpuAttrs(id);
        List<AttrEntity> attrEntities=attrService.getSearchAttrs();
        HashSet<Long> longs = new HashSet<>();
        for (AttrEntity attrEntity : attrEntities) {
            longs.add(attrEntity.getAttrId());
        }
        List<SkuEsModel.Attrs> attrs = new ArrayList<>();
        SkuEsModel.Attrs attrs1 =null;
        for (ProductAttrValueEntity spuAttr : spuAttrs) {
            if(!longs.contains(spuAttr.getAttrId()))
            {
                 attrs1=new SkuEsModel.Attrs();
                 BeanUtils.copyProperties(spuAttr, attrs1);
                 attrs.add(attrs1);
            }
        }
        //我们要上架的总商品
        List<SkuEsModel>  skuEsModelList=new ArrayList<>();
          //组装需要的数据
        SkuEsModel esModel=new SkuEsModel();
        //查出当前spu对应的所有sku的信息 ， 品牌的名字。
        List<SkuInfoEntity> skuInfoEntityList=skuInfoService.getSkuBySpuId(id);
        List<Long> ids= skuInfoEntityList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        //统一查sku是否有库存
        Map<Long, Boolean> collect1=null;
        try {
            Result<List<SkuHasStockVo>> skusHasStock = wareFeignService.getSkusHasStock(ids);
            List<SkuHasStockVo> data = skusHasStock.getData();
            collect1 = data.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        }
        catch (Exception e)
        {
            log.error("库存服务异常:原因{}",e);
        }
        //封装每个sku的信息
        Map<Long, Boolean> finalCollect = collect1;
        List<SkuEsModel> upProduct = skuInfoEntityList.stream().map(skuInfoEntity -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            //复制属性
            BeanUtils.copyProperties(skuInfoEntity, skuEsModel);
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());
            skuEsModel.setSkuImage(skuInfoEntity.getSkuDefaultImg());
            //第一hasStock  发送远程调用库存系统查询是否由库存
            if(finalCollect==null)
            {
                skuEsModel.setHasStock(true);
            }
            else {
                skuEsModel.setHasStock(finalCollect.get(skuInfoEntity.getSkuId()));
            }

            //第二品牌和分类的信息
            //品牌
            BrandEntity brandEntity = brandService.selectById(skuEsModel.getBrandId());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            skuEsModel.setBrandName(brandEntity.getName());
            //分类
            CategoryEntity categoryEntity = categoryService.selectById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());
            //第三 查询当前sku的所有可以被用来检索的规格属性
            skuEsModel.setAttrs(attrs);

            return skuEsModel;
        }).collect(Collectors.toList());
        // skuPrice skuImg, hasStack
        //将数据发给es
        Result result = searchFeignService.productStatusUp(upProduct);
        if (result.getCode()== 0 )
        {
            System.out.println("远程调用成功");
            //修改上架状态
            baseDao.updateSpuStatus(id);
        }
        else
        {
            System.out.println("远程调用失败");
        }
    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        SkuInfoEntity skuInfoEntity = skuInfoService.selectById(skuId);
        Long spuId = skuInfoEntity.getSpuId();
        SpuInfoEntity spuInfo = baseDao.selectById(spuId);
        return spuInfo;
    }
}
package com.own.onlinemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.product.dao.BrandDao;
import com.own.onlinemall.product.dao.CategoryBrandRelationDao;
import com.own.onlinemall.product.dao.CategoryDao;
import com.own.onlinemall.product.dto.CategoryDTO;
import com.own.onlinemall.product.entity.CategoryEntity;
import com.own.onlinemall.product.service.CategoryBrandRelationService;
import com.own.onlinemall.product.service.CategoryService;
import com.own.onlinemall.product.vo.Catalog2Vo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品三级分类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class CategoryServiceImpl extends CrudServiceImpl<CategoryDao, CategoryEntity, CategoryDTO> implements CategoryService {

    @Override
    public QueryWrapper<CategoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }




    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseDao.selectList(null);
        List<CategoryEntity> collect = categoryEntities.stream().filter((categoryEntity) -> {
            if(categoryEntity.getParentCid()==0 )
            {

                 categoryEntity.setChildren(getChildren(categoryEntity, categoryEntities));
            }
            return categoryEntity.getParentCid() == 0;
        }).sorted((c1, c2)->{
            return c2.getSort()-c1.getSort();
        }).collect(Collectors.toList());


        return collect;

    }

    @Override
    public boolean deleteByIdS(List<Long> ids) {
        //TODO 1SDADA
        baseDao.deleteBatchIds(ids);
        return false;
    }
    @Autowired
    CategoryBrandRelationServiceImpl categoryBrandRelationService;
    @Override
    public void updateDetail(CategoryDTO dto) {
       this.update(dto);
       categoryBrandRelationService.updateCategory(dto.getCatId(), dto.getName());
    }

    @Override
    public List<CategoryEntity> getLevelOneCategorys() {
        QueryWrapper<CategoryEntity> categoryEntityQueryWrapper = new QueryWrapper<>();
        categoryEntityQueryWrapper.eq("cat_level", 1);
        List<CategoryEntity> categoryEntities = baseDao.selectList(categoryEntityQueryWrapper);
        return categoryEntities;
    }

    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJsonWithSpringCache() {
        // 查出所有一级分类
        List<CategoryEntity> levelOneCategorys = getLevelOneCategorys();
        //封装数据
        Map<String, List<Catalog2Vo>> parent_cid = levelOneCategorys.stream().collect(Collectors.toMap(k -> {
            return k.getCatId().toString();
        }, v -> {
            //拿到了一级分类 查到一级分类的二级分类
            List<CategoryEntity> categoryEntities = baseDao.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
            //封装上面的数据
            List<Catalog2Vo> collect = null;
            if (categoryEntities != null) {
                collect = categoryEntities.stream().map(item -> {
                    Catalog2Vo catalog2Vo = new Catalog2Vo(v.getCatId().toString(), null, item.getCatId().toString(), item.getName());
                    //找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> categoryEntities1 = baseDao.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", item.getCatId()));
                    List<Catalog2Vo.Catalog3Vo> collect1=null;
                    //进行封装
                    if(categoryEntities1!=null)
                    {
                        collect1 = categoryEntities1.stream().map(t -> {
                            Catalog2Vo.Catalog3Vo catalog3Vo = new Catalog2Vo.Catalog3Vo(item.getCatId().toString(), t.getCatId().toString(), t.getName());
                            return catalog3Vo;
                        }).collect(Collectors.toList());
                    }
                    catalog2Vo.setCatalog3List(collect1);
                    return catalog2Vo;
                }).collect(Collectors.toList());
            }
            return collect;
        }));

        return parent_cid;
    }


    private  List<CategoryEntity> getChildren(CategoryEntity root , List<CategoryEntity> entities )
    {
        List<CategoryEntity> collect = entities.stream().filter((categoryEntity -> {
            if(root.getCatId() == categoryEntity.getParentCid())
            {
                 categoryEntity.setChildren(getChildren(categoryEntity, entities));
            }
            return root.getCatId() == categoryEntity.getParentCid();
        })).collect(Collectors.toList());
        return collect;
    }
}
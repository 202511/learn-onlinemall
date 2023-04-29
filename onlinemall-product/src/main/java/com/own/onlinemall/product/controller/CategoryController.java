package com.own.onlinemall.product.controller;

import com.own.onlinemall.common.annotation.LogOperation;
import com.own.onlinemall.common.constant.Constant;
import com.own.onlinemall.common.page.PageData;
import com.own.onlinemall.common.utils.ConvertUtils;
import com.own.onlinemall.common.utils.ExcelUtils;
import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.common.validator.AssertUtils;
import com.own.onlinemall.common.validator.ValidatorUtils;
import com.own.onlinemall.common.validator.group.AddGroup;
import com.own.onlinemall.common.validator.group.DefaultGroup;
import com.own.onlinemall.common.validator.group.UpdateGroup;
import com.own.onlinemall.product.dto.CategoryDTO;
import com.own.onlinemall.product.entity.CategoryEntity;
import com.own.onlinemall.product.excel.CategoryExcel;
import com.own.onlinemall.product.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.own.onlinemall.auth.vo.MemberResponseVO;
/**
 * 商品三级分类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("product/category")
@Api(tags="商品三级分类")
public class CategoryController {



    @Autowired
    private CategoryService categoryService;
    //每一个需要缓存的数据我们都要给缓存分一个区
    @Cacheable(value = {"category"}) //代表当前方法的结果需要缓存，如果缓存中有，方法不用调用， 如果没有调用方法， 并将结果存进缓存
    @GetMapping("/list/tree")
    public Result list()
    {
        System.out.println("执行");
       List<CategoryEntity> entities=categoryService.listWithTree();
        return new Result<List<CategoryEntity>>().ok(entities);
    }








    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("product:category:page")
    public Result<PageData<CategoryDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<CategoryDTO> page = categoryService.page(params);

        return new Result<PageData<CategoryDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("product:category:info")
    public Result<CategoryDTO> get(@PathVariable("id") Long id){
        CategoryDTO categoryDTO = categoryService.get(id);
        return new Result<CategoryDTO>().ok(categoryDTO);
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("product:category:save")
    public Result save(@RequestBody CategoryDTO dto){
        //效验数据
        categoryService.save(dto);

        return new Result();
    }
    @PostMapping("/update")
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("product:category:update")
    public Result update(@RequestBody CategoryDTO dto){
        System.out.println(dto);
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        categoryService.updateDetail(dto);

        return new Result();
    }
    @PostMapping("/update/sort")
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("product:category:update")
    public Result update(@RequestBody CategoryDTO[]  dto){
        System.out.println(dto);
        //效验数据
        for (CategoryDTO categoryDTO : dto) {
            categoryService.update(categoryDTO);
        }
        return new Result();
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("product:category:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        List<Long> collect = Arrays.stream(ids).collect(Collectors.toList());
        categoryService.deleteByIdS(collect);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("product:category:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<CategoryDTO> list = categoryService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, CategoryExcel.class);
    }

}
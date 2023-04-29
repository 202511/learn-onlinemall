package com.own.onlinemall.product.controller;

import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.common.annotation.LogOperation;
import com.own.onlinemall.common.constant.Constant;
import com.own.onlinemall.common.page.PageData;
import com.own.onlinemall.common.utils.ExcelUtils;
import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.common.validator.AssertUtils;
import com.own.onlinemall.common.validator.ValidatorUtils;
import com.own.onlinemall.common.validator.group.AddGroup;
import com.own.onlinemall.common.validator.group.DefaultGroup;
import com.own.onlinemall.common.validator.group.UpdateGroup;
import com.own.onlinemall.product.dto.SpuInfoDTO;
import com.own.onlinemall.product.entity.SpuInfoEntity;
import com.own.onlinemall.product.excel.SpuInfoExcel;
import com.own.onlinemall.product.service.SpuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * spu信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("product/spuinfo")
@Api(tags="spu信息")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;


    @GetMapping("/up/{id}")
    public Result spuUp(@PathVariable("id") Long id)
    {
         spuInfoService.up(id);
         return new Result().ok("成功");
    }
@GetMapping("/skuId/{id}")
  public R getSpuInfoBySkuId(@PathVariable("id") Long skuId)
  {
      SpuInfoEntity spuInfo= spuInfoService.getSpuInfoBySkuId(skuId);
      return R.ok().setData(spuInfo);
  }




    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("product:spuinfo:page")
    public Result<PageData<SpuInfoDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SpuInfoDTO> page = spuInfoService.page(params);

        return new Result<PageData<SpuInfoDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("product:spuinfo:info")
    public Result<SpuInfoDTO> get(@PathVariable("id") Long id){
        SpuInfoDTO data = spuInfoService.get(id);

        return new Result<SpuInfoDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("product:spuinfo:save")
    public Result save(@RequestBody SpuInfoDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        spuInfoService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("product:spuinfo:update")
    public Result update(@RequestBody SpuInfoDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        spuInfoService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("product:spuinfo:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        spuInfoService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("product:spuinfo:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SpuInfoDTO> list = spuInfoService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, SpuInfoExcel.class);
    }

}
package com.own.onlinemall.ware.controller;

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
import com.own.onlinemall.ware.dto.WareSkuDTO;
import com.own.onlinemall.ware.excel.WareSkuExcel;
import com.own.onlinemall.ware.exception.NoStockException;
import com.own.onlinemall.ware.r.BizCodeEnume;
import com.own.onlinemall.ware.r.R;
import com.own.onlinemall.ware.service.WareSkuService;
import com.own.onlinemall.ware.vo.SkuHasStockVo;
import com.own.onlinemall.ware.vo.WareSkuLockTO;
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
 * 商品库存
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("ware/waresku")
@Api(tags="商品库存")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;



    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockTO to)
    {
        try {
             wareSkuService.orderLockStock(to);
            return R.ok();
        }
        catch (NoStockException e)
        {
             return R.error(BizCodeEnume.NO_STOCK_EXCEPTION.getCode(),BizCodeEnume.NO_STOCK_EXCEPTION.getMsg());
        }
    }








    //查询sku是否有库存 requestbody 将json数据转化为类型
    @PostMapping("/has/stock")
    public Result<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds )
    {
        //id .stock
        List<SkuHasStockVo> skuHasStockVos= wareSkuService.getSkusHasStock(skuIds);
        Result<List<SkuHasStockVo>> skuHasStockVoResult = new Result<>();
        skuHasStockVoResult.setData(skuHasStockVos);
        return skuHasStockVoResult;
    }








    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("ware:waresku:page")
    public Result<PageData<WareSkuDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<WareSkuDTO> page = wareSkuService.page(params);

        return new Result<PageData<WareSkuDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("ware:waresku:info")
    public Result<WareSkuDTO> get(@PathVariable("id") Long id){
        WareSkuDTO data = wareSkuService.get(id);

        return new Result<WareSkuDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("ware:waresku:save")
    public Result save(@RequestBody WareSkuDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        wareSkuService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("ware:waresku:update")
    public Result update(@RequestBody WareSkuDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        wareSkuService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("ware:waresku:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        wareSkuService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("ware:waresku:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<WareSkuDTO> list = wareSkuService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, WareSkuExcel.class);
    }

}
package com.own.onlinemall.coupon.controller;

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
import com.own.onlinemall.coupon.dto.UndoLogDTO;
import com.own.onlinemall.coupon.excel.UndoLogExcel;
import com.own.onlinemall.coupon.service.UndoLogService;
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
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("coupon/undolog")
@Api(tags="")
public class UndoLogController {
    @Autowired
    private UndoLogService undoLogService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("coupon:undolog:page")
    public Result<PageData<UndoLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<UndoLogDTO> page = undoLogService.page(params);

        return new Result<PageData<UndoLogDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("coupon:undolog:info")
    public Result<UndoLogDTO> get(@PathVariable("id") Long id){
        UndoLogDTO data = undoLogService.get(id);

        return new Result<UndoLogDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("coupon:undolog:save")
    public Result save(@RequestBody UndoLogDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        undoLogService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("coupon:undolog:update")
    public Result update(@RequestBody UndoLogDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        undoLogService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("coupon:undolog:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        undoLogService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("coupon:undolog:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<UndoLogDTO> list = undoLogService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, UndoLogExcel.class);
    }

}
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
import com.own.onlinemall.coupon.dto.SeckillSessionDTO;
import com.own.onlinemall.coupon.entity.SeckillSessionEntity;
import com.own.onlinemall.coupon.excel.SeckillSessionExcel;
import com.own.onlinemall.coupon.r.R;
import com.own.onlinemall.coupon.service.SeckillSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


/**
 * 秒杀活动场次
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("coupon/seckillsession")
@Api(tags="秒杀活动场次")
public class SeckillSessionController {





    @Autowired
    private SeckillSessionService seckillSessionService;




    @GetMapping("/latest3DaySession")
    public R  getLatest3DaySession()
    {
        List<SeckillSessionEntity> list= seckillSessionService.getLatest3DaySession();
        return R.ok().setData(list);
    }







    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("coupon:seckillsession:page")
    public Result<PageData<SeckillSessionDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SeckillSessionDTO> page = seckillSessionService.page(params);

        return new Result<PageData<SeckillSessionDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("coupon:seckillsession:info")
    public Result<SeckillSessionDTO> get(@PathVariable("id") Long id){
        SeckillSessionDTO data = seckillSessionService.get(id);

        return new Result<SeckillSessionDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("coupon:seckillsession:save")
    public Result save(@RequestBody SeckillSessionDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        seckillSessionService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("coupon:seckillsession:update")
    public Result update(@RequestBody SeckillSessionDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        seckillSessionService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("coupon:seckillsession:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        seckillSessionService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("coupon:seckillsession:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SeckillSessionDTO> list = seckillSessionService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, SeckillSessionExcel.class);
    }

}
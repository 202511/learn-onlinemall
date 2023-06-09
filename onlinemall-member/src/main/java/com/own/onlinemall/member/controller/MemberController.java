package com.own.onlinemall.member.controller;

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
import com.own.onlinemall.member.dto.MemberDTO;
import com.own.onlinemall.member.entity.MemberEntity;
import com.own.onlinemall.member.excel.MemberExcel;
import com.own.onlinemall.member.exception.PhoneException;
import com.own.onlinemall.member.exception.UsernameException;
import com.own.onlinemall.member.r.BizCodeEnume;
import com.own.onlinemall.member.r.R;
import com.own.onlinemall.member.service.MemberService;
import com.own.onlinemall.member.vo.MemberUserLoginTO;
import com.own.onlinemall.member.vo.MemberUserRegisterTO;
import com.own.onlinemall.member.vo.SocialUser;
import com.own.onlinemall.member.vo.UserRegisterVO;
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
 * 会员
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("member/member")
@Api(tags="会员")
public class MemberController {
    @Autowired
    private MemberService memberService;



    @PostMapping("/oauth2/login")
    public R oauthlogin(@RequestBody SocialUser socialUser)
    {
         MemberEntity member=memberService.OAuthlogin(socialUser);
        System.out.println(member);
        if(member== null )
         {
             return  R.error("错误");
         }
         else
         {
              return R.ok();
         }
    }


    @PostMapping("/login")
    public R login(@RequestBody MemberUserLoginTO user) {
        try {
            MemberEntity entity = memberService.login(user);
            if (entity == null) {
                return R.error(BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION);
            }
            return R.ok().setData(entity);
        } catch (Exception ex) {
            return R.error(ex.getMessage());
        }

    }




    @PostMapping("/regist")
    public R regist(@RequestBody MemberUserRegisterTO user) {
        try {
            memberService.regist(user);
            return R.ok();
        } catch (PhoneException ex) {
            System.out.println("手机号不唯一");
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION);
        } catch (UsernameException ex) {
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION);
        } catch (Exception ex) {
            return R.error(ex.getMessage());
        }

    }





    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("member:member:page")
    public Result<PageData<MemberDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<MemberDTO> page = memberService.page(params);

        return new Result<PageData<MemberDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("member:member:info")
    public Result<MemberDTO> get(@PathVariable("id") Long id){
        MemberDTO data = memberService.get(id);

        return new Result<MemberDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("member:member:save")
    public Result save(@RequestBody MemberDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        memberService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("member:member:update")
    public Result update(@RequestBody MemberDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        memberService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("member:member:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        memberService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("member:member:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<MemberDTO> list = memberService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, MemberExcel.class);
    }

}
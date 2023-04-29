package com.own.onlinemall.member.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 会员
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
public class MemberExcel {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "会员等级id")
    private Long levelId;
    @Excel(name = "用户名")
    private String username;
    @Excel(name = "密码")
    private String password;
    @Excel(name = "昵称")
    private String nickname;
    @Excel(name = "手机号码")
    private String mobile;
    @Excel(name = "邮箱")
    private String email;
    @Excel(name = "头像")
    private String header;
    @Excel(name = "性别")
    private Integer gender;
    @Excel(name = "生日")
    private Date birth;
    @Excel(name = "所在城市")
    private String city;
    @Excel(name = "职业")
    private String job;
    @Excel(name = "个性签名")
    private String sign;
    @Excel(name = "用户来源")
    private Integer sourceType;
    @Excel(name = "积分")
    private Integer integration;
    @Excel(name = "成长值")
    private Integer growth;
    @Excel(name = "启用状态")
    private Integer status;
    @Excel(name = "注册时间")
    private Date createTime;
    @Excel(name = "微博社交账户uid（应该建立关联表）")
    private String weiboUid;
    @Excel(name = "访问令牌")
    private String accessToken;
    @Excel(name = "访问令牌的过期时间")
    private String expiresIn;

}
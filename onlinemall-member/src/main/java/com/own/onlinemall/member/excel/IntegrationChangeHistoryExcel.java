package com.own.onlinemall.member.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 积分变化历史记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
public class IntegrationChangeHistoryExcel {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "member_id")
    private Long memberId;
    @Excel(name = "create_time")
    private Date createTime;
    @Excel(name = "变化的值")
    private Integer changeCount;
    @Excel(name = "备注")
    private String note;
    @Excel(name = "来源[0->购物；1->管理员修改;2->活动]")
    private Integer sourceTyoe;

}
package com.own.onlinemall.order.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 订单操作历史记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
public class OrderOperateHistoryExcel {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "订单id")
    private Long orderId;
    @Excel(name = "操作人[用户；系统；后台管理员]")
    private String operateMan;
    @Excel(name = "操作时间")
    private Date createTime;
    @Excel(name = "订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】")
    private Integer orderStatus;
    @Excel(name = "备注")
    private String note;

}
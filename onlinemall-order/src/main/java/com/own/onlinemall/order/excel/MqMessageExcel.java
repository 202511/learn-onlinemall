package com.own.onlinemall.order.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
public class MqMessageExcel {
    @Excel(name = "")
    private String messageId;
    @Excel(name = "")
    private String content;
    @Excel(name = "")
    private String toExchane;
    @Excel(name = "")
    private String routingKey;
    @Excel(name = "")
    private String classType;
    @Excel(name = "0-新建 1-已发送 2-错误抵达 3-已抵达")
    private Integer messageStatus;
    @Excel(name = "")
    private Date createTime;
    @Excel(name = "")
    private Date updateTime;

}
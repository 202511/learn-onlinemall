package com.own.onlinemall.member.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 会员收藏的商品
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
public class MemberCollectSpuExcel {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "会员id")
    private Long memberId;
    @Excel(name = "spu_id")
    private Long spuId;
    @Excel(name = "spu_name")
    private String spuName;
    @Excel(name = "spu_img")
    private String spuImg;
    @Excel(name = "create_time")
    private Date createTime;

}
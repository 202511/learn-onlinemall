package com.own.onlinemall.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 会员收藏的商品
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Data
@TableName("ums_member_collect_spu")
public class MemberCollectSpuEntity {

    /**
     * id
     */
	private Long id;
    /**
     * 会员id
     */
	private Long memberId;
    /**
     * spu_id
     */
	private Long spuId;
    /**
     * spu_name
     */
	private String spuName;
    /**
     * spu_img
     */
	private String spuImg;
    /**
     * create_time
     */
	private Date createTime;
}
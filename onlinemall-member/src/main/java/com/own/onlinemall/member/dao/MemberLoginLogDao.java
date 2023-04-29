package com.own.onlinemall.member.dao;

import com.own.onlinemall.common.dao.BaseDao;
import com.own.onlinemall.member.entity.MemberLoginLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface MemberLoginLogDao extends BaseDao<MemberLoginLogEntity> {
	
}
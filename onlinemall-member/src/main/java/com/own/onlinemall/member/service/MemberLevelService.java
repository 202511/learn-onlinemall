package com.own.onlinemall.member.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.member.dto.MemberLevelDTO;
import com.own.onlinemall.member.entity.MemberLevelEntity;

/**
 * 会员等级
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface MemberLevelService extends CrudService<MemberLevelEntity, MemberLevelDTO> {

    MemberLevelEntity getDefaultLevel();
}
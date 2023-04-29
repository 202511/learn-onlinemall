package com.own.onlinemall.member.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.member.dto.MemberDTO;
import com.own.onlinemall.member.entity.MemberEntity;
import com.own.onlinemall.member.vo.MemberUserLoginTO;
import com.own.onlinemall.member.vo.MemberUserRegisterTO;
import com.own.onlinemall.member.vo.SocialUser;

/**
 * 会员
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface MemberService extends CrudService<MemberEntity, MemberDTO> {

    void regist(MemberUserRegisterTO user) throws InterruptedException;

    MemberEntity login(MemberUserLoginTO user);

    MemberEntity OAuthlogin(SocialUser socialUser);
}
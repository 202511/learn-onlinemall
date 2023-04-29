package com.own.onlinemall.member.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.member.dto.MemberReceiveAddressDTO;
import com.own.onlinemall.member.entity.MemberReceiveAddressEntity;

import java.util.List;

/**
 * 会员收货地址
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface MemberReceiveAddressService extends CrudService<MemberReceiveAddressEntity, MemberReceiveAddressDTO> {

    List<MemberReceiveAddressEntity> getAddress(Long memberId);
}
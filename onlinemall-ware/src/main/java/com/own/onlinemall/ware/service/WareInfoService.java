package com.own.onlinemall.ware.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.ware.dto.WareInfoDTO;
import com.own.onlinemall.ware.entity.WareInfoEntity;
import com.own.onlinemall.ware.vo.FareVO;

/**
 * 仓库信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface WareInfoService extends CrudService<WareInfoEntity, WareInfoDTO> {

    FareVO getFare(Long addrId);
}
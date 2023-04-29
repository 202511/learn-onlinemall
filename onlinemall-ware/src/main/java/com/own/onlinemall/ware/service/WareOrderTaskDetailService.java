package com.own.onlinemall.ware.service;

import com.own.onlinemall.common.service.CrudService;
import com.own.onlinemall.ware.dto.WareOrderTaskDetailDTO;
import com.own.onlinemall.ware.entity.WareOrderTaskDetailEntity;

import java.util.List;

/**
 * 库存工作单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
public interface WareOrderTaskDetailService extends CrudService<WareOrderTaskDetailEntity, WareOrderTaskDetailDTO> {

    List<WareOrderTaskDetailEntity> getList(Long id);
}
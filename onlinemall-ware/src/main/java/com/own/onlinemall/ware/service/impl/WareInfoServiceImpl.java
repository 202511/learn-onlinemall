package com.own.onlinemall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.ware.dao.WareInfoDao;
import com.own.onlinemall.ware.dto.WareInfoDTO;
import com.own.onlinemall.ware.entity.WareInfoEntity;
import com.own.onlinemall.ware.feign.MemberFeignService;
import com.own.onlinemall.ware.service.WareInfoService;
import com.own.onlinemall.ware.vo.FareVO;
import com.own.onlinemall.ware.vo.MemberAddressVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class WareInfoServiceImpl extends CrudServiceImpl<WareInfoDao, WareInfoEntity, WareInfoDTO> implements WareInfoService {

    @Override
    public QueryWrapper<WareInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

   @Autowired
    MemberFeignService memberFeignService;
    @Override
    public FareVO getFare(Long addrId) {
        FareVO fareVo = new FareVO();
        Result<MemberAddressVO> memberReceiveAddressDTOResult = memberFeignService.get(addrId);
        MemberAddressVO data = memberReceiveAddressDTOResult.getData();
        if(data !=null )
        {
            String phone = data.getPhone();
            String fare = phone.substring(phone.length() - 1);
            BigDecimal bigDecimal = new BigDecimal(fare);
            fareVo.setFare(bigDecimal);
            fareVo.setAddress(data);
            return fareVo;
        }
        return null;
    }
}
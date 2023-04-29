package com.own.onlinemall.ware.to;

import lombok.Data;

import java.util.List;
@Data
public class StockLockedTO {
    private  Long id ;  // 库存工作单的id
    private  StockDetailTO  detailTO;  // 工作详情的所有id
}

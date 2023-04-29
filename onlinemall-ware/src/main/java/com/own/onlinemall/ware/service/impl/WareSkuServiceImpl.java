package com.own.onlinemall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.common.to.order.OrderTO;
import com.own.onlinemall.ware.dao.WareSkuDao;
import com.own.onlinemall.ware.dto.WareSkuDTO;
import com.own.onlinemall.ware.entity.WareOrderTaskDetailEntity;
import com.own.onlinemall.ware.entity.WareOrderTaskEntity;
import com.own.onlinemall.ware.entity.WareSkuEntity;
import com.own.onlinemall.ware.exception.NoStockException;
import com.own.onlinemall.ware.feign.MemberFeignService;
import com.own.onlinemall.ware.feign.OrderFeignService;
import com.own.onlinemall.ware.r.R;
import com.own.onlinemall.ware.service.WareOrderTaskDetailService;
import com.own.onlinemall.ware.service.WareOrderTaskService;
import com.own.onlinemall.ware.service.WareSkuService;
import com.own.onlinemall.ware.to.StockDetailTO;
import com.own.onlinemall.ware.to.StockLockedTO;
import com.own.onlinemall.ware.vo.OrderEntity;
import com.own.onlinemall.ware.vo.OrderItemVO;
import com.own.onlinemall.ware.vo.SkuHasStockVo;
import com.own.onlinemall.ware.vo.WareSkuLockTO;
import com.rabbitmq.client.Channel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品库存
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */

@Service
public class WareSkuServiceImpl extends CrudServiceImpl<WareSkuDao, WareSkuEntity, WareSkuDTO> implements WareSkuService {

    @Override
    public QueryWrapper<WareSkuEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }
   @Autowired
    OrderFeignService orderFeignService;



    // 库存自动解锁
    public void  unLockStock(StockLockedTO to)  {
        // 逻辑
        // 首先判断库存关于特定订单锁定库存的相关信息 ， 有没有在数据库中
        // 如果没有， 因为我们一开始就保存了这些信息， 后才进行锁定库存
        // 那么这肯定是锁定库存这个模块出错， @transactional  自动回滚
        // 那么我们就没必要解锁了， 因为是本地的错误， 直接就回滚了
        // 如果有， 那就代表着， 锁定库存成功，
        //  此时， 两种情况， 根据订单号 去查看有没有这个订单信息
        //  如果没有， 代表这个订单没有在支付时间内支付， 或者在生成订单的流程其中发生错误， 导致订单回滚
        // 这种情况肯定要 主动的解锁
        // 如果有， 那我们就根据这个订单的状态判断是否进行解锁，比如取消订单， 就要解锁
        System.out.println("收到库存解锁消息");
        Long id = to.getId();
        StockDetailTO detailTO = to.getDetailTO();
        Long id1 = detailTO.getId();
        WareOrderTaskDetailEntity wareOrderTaskDetailEntity1 = wareOrderTaskDetailService.selectById(id1);
        BeanUtils.copyProperties(wareOrderTaskDetailEntity1, detailTO);
        WareOrderTaskDetailEntity wareOrderTaskDetailEntity = wareOrderTaskDetailService.selectById(id1);
        if(wareOrderTaskDetailEntity!=null)
        {
                 //解锁
            WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.selectById(id);
            String orderSn = wareOrderTaskEntity.getOrderSn();
            R orderStatus = orderFeignService.getOrderStatus(orderSn);
            if(orderStatus.getCode()==0)
            {
                 // 订单数据返回成功
                OrderEntity data = orderStatus.getData(new TypeReference<OrderEntity>() {
                });
                if(data == null || data.getStatus()== 4  )
                {
                    // 订单不存在
                    // 订单已经被取消了。
                    if(detailTO.getLockStatus()==1 )
                     unLockStock(detailTO.getSkuId(), detailTO.getWareId(), detailTO.getSkuNum(), id1);
                }

            }
            else
            {
                throw  new RuntimeException("远程服务失败");
                //将消息放回队列
            }

        }
        else
        {
            //这种情况意味着是锁库存本身出问题 ，然后回滚成功
             // 无需解锁
        }
    }
   //防止订单服务卡顿， 消息一直改不了， 库存消息优先到期，
    @Override
    public void unLockStock(OrderTO entity) {
        String orderSn = entity.getOrderSn();
        // 查看最新库存解锁状态
      WareOrderTaskEntity task= wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
      Long id = task.getId();
      List<WareOrderTaskDetailEntity> list=  wareOrderTaskDetailService.getList(id);
        for (WareOrderTaskDetailEntity wareOrderTaskDetailEntity : list) {
            unLockStock(wareOrderTaskDetailEntity.getSkuId(), wareOrderTaskDetailEntity.getWareId(), wareOrderTaskDetailEntity.getSkuNum(), wareOrderTaskDetailEntity.getId());
        }
    }


    private  void unLockStock(Long skuId, Long wareId,  Integer num , Long taskDetailId)
    {
        //库存解锁
         baseDao.unlockStock(skuId,wareId,num);
         //更新库存工作单的状态
         baseDao.updateTaskDetail(taskDetailId);
    }











    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {
        ArrayList<SkuHasStockVo> skuHasStockVos = new ArrayList<>();
        SkuHasStockVo skuHasStockVo =null;
        for (Long skuId : skuIds) {
            WareSkuEntity wareSkuEntity = baseDao.selectBySkuId(skuId);
            if(wareSkuEntity!=null) {
                skuHasStockVo = new SkuHasStockVo();
                skuHasStockVo.setSkuId(skuId);
                skuHasStockVo.setHasStock(wareSkuEntity.getStock() > 0 ? true : false);
                skuHasStockVos.add(skuHasStockVo);
            }
            else
            {
                skuHasStockVo = new SkuHasStockVo();
                skuHasStockVo.setSkuId(skuId);
                skuHasStockVo.setHasStock(false);
                skuHasStockVos.add(skuHasStockVo);
            }
        }
        return   skuHasStockVos;
    }



    //解锁库存的场景
    //1. 下订单成功， 订单过期没有被支付，被系统自动取消， 被用户手动取消， 都要解锁库存
    //2. 下订单成功， 库存锁定成功， 接下来的业务调用失败， 导致订单回滚
    //  之前锁定的库存自动解锁
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    WareOrderTaskService wareOrderTaskService;
    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;
    @Override
    @Transactional(rollbackFor = NoStockException.class)
    public void orderLockStock(WareSkuLockTO to) {
        // 保存库存工作单
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(to.getOrderSn());
        wareOrderTaskService.insert(wareOrderTaskEntity);
        //找到每个商品在哪个仓库有库存
        List<OrderItemVO> locks = to.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(orderItemVO -> {
            SkuWareHasStock skuWareHasStock = new SkuWareHasStock();
            Long skuId = orderItemVO.getSkuId();
            skuWareHasStock.setSkuId(skuId);
            skuWareHasStock.setNum(orderItemVO.getCount());
            List<Long> wareList = baseDao.listWareIdHasSkuStock(skuId);
            skuWareHasStock.setWareId(wareList);
            return skuWareHasStock;
        }).collect(Collectors.toList());
        // 锁定库存
        Boolean allLock= true ;


        for (SkuWareHasStock skuWareHasStock : collect) {
            Boolean skuStocked=false;
            Long skuId = skuWareHasStock.getSkuId();
            List<Long> wareId = skuWareHasStock.getWareId();
            if(wareId==null || wareId.size() == 0 )
            {
                 throw new NoStockException(skuId);
            }
            // 每一个商品都锁定成功 ， 将当前商品锁定了几件的工作单记录发给mq
            // 锁定失败， 前面保存的工作单信息就回滚， 发送出去的消息
            for (Long aLong : wareId) {
                Long count = baseDao.lockSkuStock(skuId, aLong, skuWareHasStock.getNum());
                if(count== 1 )
                {

                    skuStocked=true;
                    //告诉mq库存锁定成功 , 发消息
                    WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity(null, skuId, "", skuWareHasStock.getNum(), wareOrderTaskEntity.getId(), aLong, 1);
                    wareOrderTaskDetailService.insert(wareOrderTaskDetailEntity);

                    StockLockedTO stockLockedTO = new StockLockedTO();
                    stockLockedTO.setId(wareOrderTaskEntity.getId());
                    //防止回滚之后找不到事务
                    StockDetailTO stockDetailTO = new StockDetailTO();
                    BeanUtils.copyProperties(wareOrderTaskDetailEntity,stockDetailTO );
                    stockLockedTO.setDetailTO(stockDetailTO);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked",stockLockedTO );


                    break;
                    //当前仓库锁成功， 重试
                }
            }
            if (skuStocked== false )
            {
                 // 当前商品没库存 ， 不用继续往下进行
                throw  new NoStockException(skuId) ;
            }
        }
    }
    @Data
    class  SkuWareHasStock{
         private Long skuId;
         private  List<Long> wareId;
         private  Integer num ;

    }

}
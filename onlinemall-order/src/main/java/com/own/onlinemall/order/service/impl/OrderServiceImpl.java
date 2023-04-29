package com.own.onlinemall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.mysql.cj.Query;
import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.auth.to.SeckillOrderTO;
import com.own.onlinemall.auth.vo.MemberResponseVO;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.common.to.order.OrderTO;
import com.own.onlinemall.common.utils.Result;
import com.own.onlinemall.order.constant.OrderConstant;
import com.own.onlinemall.order.dao.OrderDao;
import com.own.onlinemall.order.dto.OrderCreateTO;
import com.own.onlinemall.order.dto.OrderDTO;
import com.own.onlinemall.order.entity.OrderEntity;
import com.own.onlinemall.order.entity.OrderItemEntity;
import com.own.onlinemall.order.entity.PaymentInfoEntity;
import com.own.onlinemall.order.feign.CartFeignService;
import com.own.onlinemall.order.feign.MemberFeignService;
import com.own.onlinemall.order.feign.ProductFeignService;
import com.own.onlinemall.order.feign.WmsFeignService;
import com.own.onlinemall.order.interceptor.LoginUserInterceptor;
import com.own.onlinemall.order.pay.AliPayAsyncVO;
import com.own.onlinemall.order.service.OrderItemService;
import com.own.onlinemall.order.service.OrderService;
import com.own.onlinemall.order.service.PaymentInfoService;
import com.own.onlinemall.order.util.PageUtils;
import com.own.onlinemall.order.vo.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
/**
 * 订单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class OrderServiceImpl extends CrudServiceImpl<OrderDao, OrderEntity, OrderDTO> implements OrderService {
    // 秒杀
    @Override
    public void createSeckillOrder(SeckillOrderTO entity) {
        //保存秒杀订单
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(entity.getOrderSn());
        orderEntity.setMemberId(entity.getMemberId());
        orderEntity.setModifyTime(new Date());
        orderEntity.setStatus(OrderConstant.OrderStatusEnum.CREATE_NEW.getCode());
        BigDecimal multiply = entity.getSeckillPrice().multiply(new BigDecimal("" + entity.getNum()));
        orderEntity.setPayAmount(multiply);
        baseDao.insert(orderEntity);
        // 保存订单项信息
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderSn(entity.getOrderSn());
        orderItemEntity.setRealAmount(multiply);
        orderItemEntity.setSkuQuantity(entity.getNum());
        //
        orderItemService.insert(orderItemEntity);
    }



    //处理支付宝的支付结果
    @Autowired
    PaymentInfoService paymentInfoService;
    @Override
    public String handlePayResult(AliPayAsyncVO vo) {
          // 保存交易流水
        PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
        paymentInfoEntity.setAlipayTradeNo(vo.getTrade_no());  // 支付宝账号
        paymentInfoEntity.setOrderSn(vo.getOut_trade_no()); // 订单编号
        paymentInfoEntity.setPaymentStatus(vo.getTrade_status()); // 支付状态
        paymentInfoEntity.setCallbackTime(vo.getNotify_time()); //通知时间
        paymentInfoService.insert(paymentInfoEntity);
        // 修改订单的状态信息
        String trade_status = vo.getTrade_status();
        System.out.println(trade_status);
       if (trade_status.equals("TRADE_SUCCESS") || trade_status.equals("TRADE_FINISHED"))
       {
           // 支付成功状态
           String out_trade_no = vo.getOut_trade_no(); // 订单编号
           System.out.println(out_trade_no);
           System.out.println("到了到了");
           // 修改订单状态为已支付
           baseDao.updateOrderStatus(out_trade_no, OrderConstant.OrderStatusEnum.PAYED.getCode());
           return "success";
       }

        return "error";
    }



    // 分页查询用户的所有订单及其订单项

    @Override
    public PageUtils queryPageWithItem(Map<String, Object> params) {
        // 获取登录用户
        MemberResponseVO member = LoginUserInterceptor.loginUser.get();

        // 查询订单
        IPage<OrderEntity> page = baseDao.selectPage(
                getPage(params, null, false),
                new QueryWrapper<OrderEntity>()
                        .eq("member_id", member.getId())
                        .orderByDesc("modify_time")
        );
        // 查询订单项
        List<OrderEntity> collect = page.getRecords().stream().map(order -> {
            List<OrderItemEntity> t = orderItemService.getTitle(order.getOrderSn());
            order.setItemEntities(t);
            return order;
        }).collect(Collectors.toList());
        // 遍历封装订单项
        page.setRecords(collect);
        return new PageUtils( page);
    }




    private  ThreadLocal<OrderSubmitVO> confirmVOThreadLocal=new ThreadLocal<>() ;
    @Autowired
    MemberFeignService memberFeignService;
    @Override
    public QueryWrapper<OrderEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq( "id", id);

        return wrapper;
    }

   @Autowired
    CartFeignService cartFeignService;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    WmsFeignService wmsFeignService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public OrderConfirmVO confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVO orderConfirmVO = new OrderConfirmVO();
        MemberResponseVO memberResponseVO = LoginUserInterceptor.loginUser.get();
        Long id = memberResponseVO.getId();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //远程查询收获地址列表
        CompletableFuture<Void> t1 = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVO> address = memberFeignService.getAddress(id);
            orderConfirmVO.setMemberAddressVos(address);
        }, threadPoolExecutor);

        //获取远程的购物项
        CompletableFuture<Void> t2 = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVO> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
            orderConfirmVO.setItems(currentUserCartItems);
        }, threadPoolExecutor).thenRunAsync(()->
        {
            List<OrderItemVO> items = orderConfirmVO.getItems();
            List<Long> collect = items.stream().map(item -> {
                return item.getSkuId();
            }).collect(Collectors.toList());
            Result<List<SkuHasStockVo>> skusHasStock = wmsFeignService.getSkusHasStock(collect);
            List<SkuHasStockVo> data = skusHasStock.getData();
            if(data!=null)
            {
                Map<Long, Boolean> collect1 = data.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
                orderConfirmVO.setStocks(collect1 );
            }
        },threadPoolExecutor);

        //查询用户积分
        Integer integration = memberResponseVO.getIntegration();
        orderConfirmVO.setIntegration(integration);
        // 其他数据自动计算
        CompletableFuture.allOf(t1, t2).get() ;

        // 防重令牌
        String token= UUID.randomUUID().toString().replace("-", "");
        //给redis一个， 给前端一个
        //30分钟
        redisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX+id, token, 30, TimeUnit.MINUTES);
        orderConfirmVO.setUniqueToken(token);
        return orderConfirmVO;
    }
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Transactional
    @Override
    public SubmitOrderResponseVO submitOrder(OrderSubmitVO vo) {
        SubmitOrderResponseVO submitOrderResponseVO = new SubmitOrderResponseVO();
        MemberResponseVO memberResponseVO = LoginUserInterceptor.loginUser.get();
        confirmVOThreadLocal.set(vo);
        // 1. 验证防重令牌(令牌的对比和删除必须保持原子性)
        String script = "if redis.call('get', KEYS[1]) == KEYS[2] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        String key = OrderConstant.USER_ORDER_TOKEN_PREFIX+ memberResponseVO.getId();
        String value = vo.getUniqueToken();
        Long result = redisTemplate.execute(redisScript, Arrays.asList(key, value));
        // 根据返回结果判断是否成功成功匹配并删除 Redis 键值对，若果结果不为空和0，则验证通过
        if (result != null && result != 0L) {
            //创建订单
            OrderCreateTO order = createOrder();
            //验证价格 前后端的价格可能不一样
            BigDecimal payAmount = order.getOrder().getPayAmount();// 后端的价格
            BigDecimal payPrice = vo.getPayPrice();
            if(Math.abs(payAmount.subtract(payPrice).doubleValue())<0.01)
            {
                 //金额对比成功
                //保存订单
                saveOrder(order);
                // 库存锁定， 需要订单号，
                // 订单号，所有订单项（skuId）
                WareSkuLockTO wareSkuLockTO = new WareSkuLockTO();
                wareSkuLockTO.setOrderSn(order.getOrder().getOrderSn());
                List<OrderItemVO> collect = order.getOrderItems().stream().map(item -> {
                    OrderItemVO orderItemVO = new OrderItemVO();
                    orderItemVO.setSkuId(item.getSkuId());
                    orderItemVO.setCount(item.getSkuQuantity());
                    orderItemVO.setTitle(item.getSkuName());
                    return orderItemVO;
                }).collect(Collectors.toList());
                 wareSkuLockTO.setLocks(collect);
              //远程锁库存
                R r = wmsFeignService.orderLockStock(wareSkuLockTO);
                if(r.getCode()== 0 )
                {
                    submitOrderResponseVO.setOrder(order.getOrder());
                    //TODO 订单创建成功发送消息给rabbitmq
                    rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", order.getOrder());
                   return submitOrderResponseVO;
                     //锁库存成功了
                }
                else
                {
                    return  submitOrderResponseVO;
                }
            }
            else
            {
                 //失败
            }
        }
        else
        {
            System.out.println("验证失败");
        }

        return null;
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        OrderEntity orderEntity = baseDao.selectOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));

        return orderEntity;
    }









    @Override
    public void closeOrder(OrderEntity entity) {
        OrderEntity orderEntity = baseDao.selectById(entity.getId());
        //待付款
        if (orderEntity.getStatus() == OrderConstant.OrderStatusEnum.CREATE_NEW.getCode()) {
            OrderEntity orderEntity1 = new OrderEntity();
            orderEntity1.setId(entity.getId());
            orderEntity1.setStatus(OrderConstant.OrderStatusEnum.CANCLED.getCode());
            baseDao.updateById(orderEntity1);
            OrderTO orderTO = new OrderTO();
            orderEntity1.setOrderSn(orderEntity.getOrderSn());
            BeanUtils.copyProperties(orderEntity1, orderTO);
            try {
                //每一个消息 都保存都数据库， 只要发送失败， 定期扫描数据库， 将失败的消息再发一遍
                rabbitTemplate.convertAndSend("order-event-exchange", "order.release.other", orderTO);
            } catch (Exception e)
            {

            }
        }
    }

    @Override
    public PayVO getOrderPay(String orderSn) {

        OrderEntity orderByOrderSn = this.getOrderByOrderSn(orderSn);
        PayVO payVO = new PayVO();
        // 如果有余数向上取值
        BigDecimal decimal = orderByOrderSn.getPayAmount().setScale(2, BigDecimal.ROUND_UP);
        payVO.setTotal_amount(decimal.toString());
        payVO.setOut_trade_no(orderSn);
        List<OrderItemEntity>  list=    orderItemService.getTitle(orderSn);
        OrderItemEntity orderItemEntity = list.get(0);

        payVO.setSubject(orderItemEntity.getSkuName());
        payVO.setBody(orderItemEntity.getSkuAttrsVals());
        return payVO;
    }
























    @Autowired
    OrderItemService orderItemService;
    private void saveOrder(OrderCreateTO order) {
        OrderEntity order1 = order.getOrder();
        order1.setModifyTime(new Date());
        baseDao.insert(order1);
        List<OrderItemEntity> orderItems = order.getOrderItems();
        orderItemService.insertBatch(orderItems);
    }

    // 功能 ： 处理一个订单生成需要的数据
    private OrderCreateTO createOrder()
    {
        OrderCreateTO orderCreateTO = new OrderCreateTO();
        //生成订单号
        String timeId = IdWorker.getTimeId();
        // 创建订单号
        OrderEntity orderEntity = buildOrder(timeId);
        // 获取到所有订单项
        List<OrderItemEntity> orderItemEntities = buildOrderItems(timeId);
        // 计算价格相关
        computePrice(orderEntity, orderItemEntities);
        orderCreateTO.setOrder(orderEntity);
        orderCreateTO.setOrderItems(orderItemEntities);
        return  orderCreateTO ;
    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal integrationAmount=new BigDecimal("0.0");
        BigDecimal promotionAmount=new BigDecimal("0.0");
        BigDecimal growth=new BigDecimal("0.0");
        BigDecimal gift=new BigDecimal("0.0");
        //订单价格相关的 订单总额= 所有购物项相加的总和
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            BigDecimal realAmount = orderItemEntity.getRealAmount();
            coupon=coupon.add(orderItemEntity.getCouponAmount());
            integrationAmount = integrationAmount.add(orderItemEntity.getIntegrationAmount());
            promotionAmount = promotionAmount.add(orderItemEntity.getPromotionAmount());
            growth =growth.add(new BigDecimal(orderItemEntity.getGiftGrowth().toString())) ;
            gift =gift.add(new BigDecimal(orderItemEntity.getGiftIntegration().toString())) ;
            total= total.add(realAmount);
        }
        //应付的总额还有运费什么的参与运算。
        orderEntity.setTotalAmount(total);
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount())); // 订单金额 加上 运费的金额
        orderEntity.setPromotionAmount(promotionAmount);
        orderEntity.setCouponAmount(coupon);
        orderEntity.setIntegrationAmount(integrationAmount);
        orderEntity.setIntegration(gift.intValue());
        orderEntity.setGrowth(growth.intValue());
    }

    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        // 最后确定每个购物项的价格
        List<OrderItemVO> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        if(currentUserCartItems!=null &&currentUserCartItems.size()> 0 )
        {
            List<OrderItemEntity> collect = currentUserCartItems.stream().map(cartItem -> {
                OrderItemEntity orderItemEntity = buildOrderItem(cartItem);
                orderItemEntity.setOrderSn(orderSn);
                return orderItemEntity;

            }).collect(Collectors.toList());
            return  collect;
        }
        return  null;
    }

    private OrderEntity buildOrder(String timeId) {
        MemberResponseVO memberResponseVO = LoginUserInterceptor.loginUser.get();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(timeId);
        orderEntity.setMemberId(memberResponseVO.getId());
        // 获取收货地址信息
        OrderSubmitVO orderSubmitVO = confirmVOThreadLocal.get();
        R fare = wmsFeignService.getFare(orderSubmitVO.getAddrId());
        FareVO data = fare.getData(new TypeReference<FareVO>() {
        });
        BigDecimal fare1 = data.getFare(); // 运费金额
        //设置运费信息
        orderEntity.setFreightAmount(fare1);
        //设置收货人信息
        orderEntity.setReceiverCity(data.getAddress().getCity());
        orderEntity.setReceiverName(data.getAddress().getName());
        orderEntity.setReceiverPhone(data.getAddress().getPhone());
        orderEntity.setReceiverPostCode(data.getAddress().getPostCode());
        orderEntity.setReceiverProvince(data.getAddress().getProvince());
        orderEntity.setReceiverRegion(data.getAddress().getRegion());
        // 订单的状态
        orderEntity.setStatus(OrderConstant.OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);
        orderEntity.setDeleteStatus(0 ); // 默认未删除
        return orderEntity;
    }
    @Autowired
    ProductFeignService productFeignService;
    // 构建订单项
    private OrderItemEntity buildOrderItem(OrderItemVO cartItem) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        // 订单信息 ，订单号
        // 商品的spu信息
        Long skuId = cartItem.getSkuId();
        R spuInfoBySkuId = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVO data = spuInfoBySkuId.getData(new TypeReference<SpuInfoVO>() {
        });
        orderItemEntity.setSpuId(data.getId());
        orderItemEntity.setSpuBrand(data.getBrandId().toString());
        orderItemEntity.setSpuName(data.getSpuName());
        orderItemEntity.setCategoryId(data.getCatalogId());
        //商品的sku信息
        orderItemEntity.setSkuId(cartItem.getSkuId());
        orderItemEntity.setSkuName(cartItem.getTitle());
        orderItemEntity.setSkuPic(cartItem.getImage());
        orderItemEntity.setSkuPrice(cartItem.getPrice());
        String s = StringUtils.collectionToDelimitedString(cartItem.getSkuAttrValues(), ",");
        orderItemEntity.setSkuAttrsVals(s);
        orderItemEntity.setSkuQuantity(cartItem.getCount());
        // 优惠信息（不做）
        // 积分信息
        orderItemEntity.setGiftGrowth(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount().toString())).intValue());
        orderItemEntity.setGiftIntegration(cartItem.getPrice().intValue());
        //订单项的价格信息
        orderItemEntity.setPromotionAmount(new BigDecimal("0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0"));
        //当前订单项的实际金额
        BigDecimal origin = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        BigDecimal subtract = origin.subtract(orderItemEntity.getCouponAmount()).subtract(orderItemEntity.getIntegrationAmount()).subtract(orderItemEntity.getPromotionAmount());
        orderItemEntity.setRealAmount(subtract);
        return orderItemEntity ;
    }
}
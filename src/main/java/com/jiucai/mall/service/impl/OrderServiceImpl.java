package com.jiucai.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.ResponseStatusCode;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.dao.*;

import com.jiucai.mall.entity.*;
import com.jiucai.mall.utils.BigDecimalUtil;
import com.jiucai.mall.utils.IDUtil;
import com.jiucai.mall.utils.PropertiesUtil;
import com.jiucai.mall.vo.OrderPageVo;
import com.jiucai.mall.vo.OrderVo;
import com.jiucai.mall.vo.ShippingVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jiucai.mall.service.OrderService;

import javax.servlet.http.HttpSession;

@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {
    @Autowired
    private HttpSession session;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 获取订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public UniformResponse getOrderList(int pageNum, int pageSize) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        Page<OrderEntity> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",user.getId());
        /* 按订单创建时间的来排序 */
        queryWrapper.orderByDesc("create_time");
        result = orderMapper.selectPage(result,queryWrapper);

        OrderPageVo orderPageVo = new OrderPageVo();
        if(result.hasPrevious()){
            orderPageVo.setHasPreviousPage(true);
            orderPageVo.setPrePage((int)result.getCurrent()-1);
        }
        if(result.hasNext()){
            orderPageVo.setHasNextPage(true);
            orderPageVo.setPrePage((int)result.getCurrent()+1);
        }
        //ordervolist
        List<OrderVo> orderVoList = Lists.newArrayList();

        List<OrderEntity> orders = result.getRecords();
        for(OrderEntity orderEntity : orders){
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("order_no",orderEntity.getOrderNo());
            queryWrapper1.eq("user_id",user.getId());
            List<OrderItemEntity> orderItems = orderItemMapper.selectList(queryWrapper1);

            //生成ordervo
            OrderVo orderVo = new OrderVo();
            orderVo.setOrderNo(orderEntity.getOrderNo());
            orderVo.setShippingId(orderEntity.getShippingId());
            orderVo.setUserId(orderEntity.getUserId());
            orderVo.setPaymentPrice(orderEntity.getPaymentPrice());
            orderVo.setPaymentType(orderEntity.getPaymentType());
            orderVo.setPostage(orderEntity.getPostage());
            orderVo.setStatus(orderEntity.getStatus());
            orderVo.setPaymentTime(orderEntity.getPaymentTime());
            orderVo.setSendTime(orderEntity.getSendTime());
            orderVo.setCloseTime(orderEntity.getCloseTime());
            orderVo.setCreateTime(orderEntity.getCreateTime());
            orderVo.setUpdateTime(orderEntity.getUpdateTime());
            orderVo.setImageHost(PropertiesUtil.getProperty("image.server.url"));
            orderVo.setItemEntities(orderItems);

            ShippingEntity shippingEntity = shippingMapper.selectById(orderVo.getShippingId());
            if (shippingEntity != null) {
                ShippingVo shippingVo = new ShippingVo();
                shippingVo.setReceiverName(shippingEntity.getAddressName());
                shippingVo.setReceiverAddress(shippingEntity.getAddressDetail());
                shippingVo.setReceiverProvince(shippingEntity.getAddressProvince());
                shippingVo.setReceiverCity(shippingEntity.getAddressCity());
                shippingVo.setReceiverDistrict(shippingEntity.getAddressDistrict());
                shippingVo.setReceiverMobile(shippingEntity.getAddressMobile());
                shippingVo.setReceiverZip(shippingEntity.getAddressZip());
                shippingVo.setReceiverPhone(shippingEntity.getAddressPhone());
                orderVo.setShippingVo(shippingVo);
            }
            orderVoList.add(orderVo);
        }

        //orderVoList生成完了
        orderPageVo.setPages((int)result.getPages());
        orderPageVo.setPageNum((int)result.getCurrent());
        orderPageVo.setPageSize((int)result.getSize());
        orderPageVo.setTotal((int)result.getTotal());
        orderPageVo.setOrderVoList(orderVoList);

        return UniformResponse.ResponseForSuccess(orderPageVo);
    }

    /**
     * 取消订单
     * @param orderNo
     * @return
     */
    @Override
    public UniformResponse<String> cancelOrder(Long orderNo) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        queryWrapper.eq("user_id",user.getId());
        OrderEntity orderEntity = orderMapper.selectOne(queryWrapper);
        if(orderEntity==null){
            return UniformResponse.ResponseForError("订单不存在或不是您的订单");
        }
        if (orderEntity.getStatus() == Constant.orderStatus.PAID ||
                orderEntity.getStatus() == Constant.orderStatus.SHIPPED||
                orderEntity.getStatus() == Constant.orderStatus.SUCCESSFULDEAL) {
            return UniformResponse.ResponseForError("请和商家协商");
        }
        orderEntity.setStatus(Constant.orderStatus.CANCELED);
        orderEntity.setCloseTime(LocalDateTime.now());

        int rows = orderMapper.updateById(orderEntity);
        if(rows>0){
            return UniformResponse.ResponseForSuccess("订单取消成功");
        }
        return UniformResponse.ResponseForError("订单取消失败");
    }

    /**
     * 确认订单
     * @param orderNo
     * @return
     */
    @Override
    public UniformResponse<String> confirmOrder(Long orderNo) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        queryWrapper.eq("user_id",user.getId());
        OrderEntity orderEntity = orderMapper.selectOne(queryWrapper);
        if(orderEntity==null){
            return UniformResponse.ResponseForError("订单不存在或不是您的订单");
        }
        orderEntity.setEndTime(LocalDateTime.now());
        orderEntity.setStatus(Constant.orderStatus.SUCCESSFULDEAL);
        int rows = orderMapper.updateById(orderEntity);
        if(rows>0){
            return UniformResponse.ResponseForSuccess("确认收货成功");
        }
        return UniformResponse.ResponseForError("确认收货失败");
    }

    /**
     * 获取订单详情
     * @param orderNo
     * @return
     */
    @Override
    public UniformResponse<OrderVo> getOrderDetail(Long orderNo) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        queryWrapper.eq("user_id",user.getId());
        OrderEntity orderEntity = orderMapper.selectOne(queryWrapper);

        if (orderEntity == null){
            return UniformResponse.ResponseForError("订单不存在或不是您的订单");
        }

        List orderItemList = orderItemMapper.selectList(queryWrapper);
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(orderEntity.getOrderNo());
        orderVo.setShippingId(orderEntity.getShippingId());
        orderVo.setUserId(orderEntity.getUserId());
        orderVo.setPaymentPrice(orderEntity.getPaymentPrice());
        orderVo.setPaymentType(orderEntity.getPaymentType());
        orderVo.setPostage(orderEntity.getPostage());
        orderVo.setStatus(orderEntity.getStatus());
        orderVo.setPaymentTime(orderEntity.getPaymentTime());
        orderVo.setSendTime(orderEntity.getSendTime());
        orderVo.setCloseTime(orderEntity.getCloseTime());
        orderVo.setCreateTime(orderEntity.getCreateTime());
        orderVo.setUpdateTime(orderEntity.getUpdateTime());
        orderVo.setImageHost(PropertiesUtil.getProperty("image.server.url"));
        orderVo.setItemEntities(orderItemList);

        ShippingEntity shippingEntity = shippingMapper.selectById(orderVo.getShippingId());
        if (shippingEntity != null) {
            ShippingVo shippingVo = new ShippingVo();
            shippingVo.setReceiverName(shippingEntity.getAddressName());
            shippingVo.setReceiverAddress(shippingEntity.getAddressDetail());
            shippingVo.setReceiverProvince(shippingEntity.getAddressProvince());
            shippingVo.setReceiverCity(shippingEntity.getAddressCity());
            shippingVo.setReceiverDistrict(shippingEntity.getAddressDistrict());
            shippingVo.setReceiverMobile(shippingEntity.getAddressMobile());
            shippingVo.setReceiverZip(shippingEntity.getAddressZip());
            shippingVo.setReceiverPhone(shippingEntity.getAddressPhone());
            orderVo.setShippingVo(shippingVo);
        }
        return UniformResponse.ResponseForSuccess(orderVo);
    }

    /**
     * 创建订单
     * @param shippingId
     * @return
     */
    @Override
    public UniformResponse<OrderVo> createOrder(Integer shippingId) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        //先把购物车被选中的东西拿出来
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",user.getId());
        queryWrapper.eq("checked",Constant.Cart.CHECKED);
        List<CartEntity> cartList = cartMapper.selectList(queryWrapper);
        //数量等在加入购物车时做过校验，这里不重复
        if(CollectionUtils.isEmpty(cartList)){
            return UniformResponse.ResponseForError("您还未选中任何商品，请选择你想要购买的商品");
        }

        //生成订单号
        Long orderNo = IDUtil.getID();

        List<OrderItemEntity> orderItemList = new ArrayList<>();

        for(CartEntity cart : cartList){
            //先要找到产品 才能找到单价
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("id",cart.getProductId());
            ProductEntity product = productMapper.selectOne(queryWrapper1);
            //然后给订单里的每一样东西算出一个总价
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderNo(orderNo);
            orderItem.setUserId(user.getId());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
            orderItemList.add(orderItem);

            //产品 更新库存
            product.setStock(product.getStock()-cart.getQuantity());
            productMapper.updateById(product);

            //删除购物车已购买的
            cartMapper.deleteById(cart.getId());

            //都插入
            orderItemMapper.insert(orderItem);
        }
        //计算总价
        BigDecimal paymentPrice = new BigDecimal("0");
        for(OrderItemEntity orderItem : orderItemList){
            paymentPrice = BigDecimalUtil.add(paymentPrice.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderNo(orderNo);
        orderEntity.setStatus(Constant.orderStatus.UNPAID);
        orderEntity.setPaymentType(1);
        orderEntity.setUserId(user.getId());
        orderEntity.setShippingId(shippingId);
        orderEntity.setPaymentPrice(paymentPrice);
        orderEntity.setPostage(0);
        //插入
        orderMapper.insert(orderEntity);

        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(orderEntity.getOrderNo());
        orderVo.setShippingId(orderEntity.getShippingId());
        orderVo.setUserId(orderEntity.getUserId());
        orderVo.setPaymentPrice(orderEntity.getPaymentPrice());
        orderVo.setPaymentType(orderEntity.getPaymentType());
        orderVo.setPostage(orderEntity.getPostage());
        orderVo.setStatus(orderEntity.getStatus());
        orderVo.setPaymentTime(orderEntity.getPaymentTime());
        orderVo.setSendTime(orderEntity.getSendTime());
        orderVo.setCloseTime(orderEntity.getCloseTime());
        orderVo.setCreateTime(orderEntity.getCreateTime());
        orderVo.setUpdateTime(orderEntity.getUpdateTime());
        orderVo.setItemEntities(orderItemList);
        orderVo.setImageHost(PropertiesUtil.getProperty("image.server.url"));

        ShippingEntity shippingEntity = shippingMapper.selectById(orderVo.getShippingId());
        if (shippingEntity != null) {
            ShippingVo shippingVo = new ShippingVo();
            shippingVo.setReceiverName(shippingEntity.getAddressName());
            shippingVo.setReceiverAddress(shippingEntity.getAddressDetail());
            shippingVo.setReceiverProvince(shippingEntity.getAddressProvince());
            shippingVo.setReceiverCity(shippingEntity.getAddressCity());
            shippingVo.setReceiverDistrict(shippingEntity.getAddressDistrict());
            shippingVo.setReceiverMobile(shippingEntity.getAddressMobile());
            shippingVo.setReceiverZip(shippingEntity.getAddressZip());
            shippingVo.setReceiverPhone(shippingEntity.getAddressPhone());
            orderVo.setShippingVo(shippingVo);
        }
        return UniformResponse.ResponseForSuccess(orderVo);
    }
}

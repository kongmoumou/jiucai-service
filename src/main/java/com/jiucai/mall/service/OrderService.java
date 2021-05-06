package com.jiucai.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.OrderEntity;
import com.jiucai.mall.vo.OrderPageVo;
import com.jiucai.mall.vo.OrderVo;

public interface OrderService extends IService<OrderEntity> {
    /**
     * 获取订单列表
     * @param current
     * @param size
     * @return
     */
    UniformResponse<OrderPageVo> getOrderList(int current, int size);

    /**
     * 取消订单
     * @param orderNo
     * @return
     */
    UniformResponse<String> cancelOrder(Long orderNo);

    /**
     * 确认订单
     * @param orderNo
     * @return
     */
    UniformResponse<String> confirmOrder(Long orderNo);

    /**
     * 获取订单详情
     * @param orderNo
     * @return
     */
    UniformResponse<OrderVo> getOrderDetail(Long orderNo);

    /**
     * 创建订单
     * @param shippingId
     * @return
     */
    UniformResponse<OrderVo> createOrder(Integer shippingId);
}

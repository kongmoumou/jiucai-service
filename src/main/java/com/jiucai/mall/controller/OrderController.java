package com.jiucai.mall.controller;

import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.vo.OrderPageVo;
import com.jiucai.mall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jiucai.mall.service.OrderService;

@RestController
@RequestMapping(Constant.orderRouting.ORDER)
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 获取订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(Constant.orderRouting.GET_ORDER_LIST)
    public UniformResponse<OrderPageVo> getOrderList(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        UniformResponse<OrderPageVo> response = orderService.getOrderList(pageNum, pageSize);
        return response;
    }

    /**
     * 取消订单
     * @param orderNo
     * @return
     */
    @GetMapping(Constant.orderRouting.CANCEL_ORDER)
    public UniformResponse<String> cancelOrder(Long orderNo){
        UniformResponse<String> response = orderService.cancelOrder(orderNo);
        return response;
    }

    /**
     * 确认订单
     * @param orderNo
     * @return
     */
    @GetMapping(Constant.orderRouting.CONFIRM_ORDER)
    public UniformResponse<String> ConfirmOrder(Long orderNo){
        UniformResponse<String> response = orderService.confirmOrder(orderNo);
        return response;
    }

    /**
     * 查询订单详情 每个用户只有看自己的订单的权限
     * @param orderNo
     * @return
     */
    @GetMapping(Constant.orderRouting.GET_ORDER_DETAIL)
    public UniformResponse<OrderVo> getOrderDetail(Long orderNo){
        UniformResponse<OrderVo> response = orderService.getOrderDetail(orderNo);
        return response;
    }

    /**
     * 创建订单
     * @param shippingId
     * @return
     */
    @GetMapping(Constant.orderRouting.CREATE_ORDER)
    public UniformResponse<OrderVo> createOrder(Integer shippingId){
        UniformResponse<OrderVo> response = orderService.createOrder(shippingId);
        return response;
    }
}

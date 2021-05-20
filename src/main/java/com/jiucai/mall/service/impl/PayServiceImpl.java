package com.jiucai.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiucai.mall.common.Constant;
import com.jiucai.mall.dao.OrderMapper;
import com.jiucai.mall.entity.OrderEntity;
import com.jiucai.mall.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PayServiceImpl implements PayService {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 设置付款成功
     * @param orderNo
     * @return
     */
    public boolean setPaid(String orderNo){
        Long orderNoL = Long.parseLong(orderNo);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNoL);
        queryWrapper.eq("status", Constant.orderStatus.UNPAID);
        OrderEntity orderEntity = orderMapper.selectOne(queryWrapper);

        if (null != orderEntity){
            orderEntity.setStatus(Constant.orderStatus.PAID);
            /* 插入付款时间 */
            orderEntity.setPaymentTime(LocalDateTime.now());
            int rows = orderMapper.updateById(orderEntity);
            if (rows > 0) {
                /*修改成功*/
                return true;
            }else {
                /*修改失败*/
                return false;
            }
        }else {
            return false;
        }
    }
}

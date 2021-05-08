package com.jiucai.mall.vo;

import com.jiucai.mall.entity.OrderEntity;
import com.jiucai.mall.entity.OrderItemEntity;
import lombok.Data;

import java.util.List;

@Data
public class OrderVo extends OrderEntity {
    private List<OrderItemEntity> itemEntities;

    private ShippingVo shippingVo;

    private String imageHost;
}

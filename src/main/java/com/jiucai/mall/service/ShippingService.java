package com.jiucai.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.ShippingEntity;

import java.util.List;

public interface ShippingService extends IService<ShippingEntity> {
    /**
     * 获取所有邮寄地址信息
     *
     * @return
     */
    UniformResponse<List<ShippingEntity>> getShippingList();

    /**
     * 更新邮寄地址信息
     *
     * @param shippingEntity
     * @return
     */
    UniformResponse<String> updateShipping(ShippingEntity shippingEntity);

    /**
     * 创建邮寄地址信息
     *
     * @param shippingEntity
     * @return
     */
    UniformResponse<String> createShipping(ShippingEntity shippingEntity);

    /**
     * 删除某一邮寄地址信息
     *
     * @param shippingId
     * @return
     */
    UniformResponse<String> deleteShipping(Integer shippingId);
}

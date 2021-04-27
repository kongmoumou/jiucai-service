package com.jiucai.mall.controller;

import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.ShippingEntity;
import com.jiucai.mall.service.ShippingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    /**
     * 获取所有邮寄地址信息
     *
     * @return
     */
    @GetMapping("/list")
    public UniformResponse<List<ShippingEntity>> getShippingList(){
        return shippingService.getShippingList();
    }

    /**
     * 更新邮寄地址信息
     *
     * @param shippingEntity
     * @return
     */
    @PostMapping("/update")
    public UniformResponse<String> updateShipping(ShippingEntity shippingEntity) {
        return shippingService.updateShipping(shippingEntity);
    }

    /**
     * 创建邮寄地址信息
     *
     * @param shippingEntity
     * @return
     */
    @PostMapping("/create")
    public UniformResponse<String> createShipping(ShippingEntity shippingEntity){
        System.out.println(shippingEntity);
        return shippingService.createShipping(shippingEntity);
    }

    /**
     * 删除某一邮寄地址信息
     *
     * @param shippingId
     * @return
     */
    @PostMapping("/delete")
    public UniformResponse<String> deleteShipping(Integer shippingId){
        return shippingService.deleteShipping(shippingId);
    }
}

package com.jiucai.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.CartEntity;
import com.jiucai.mall.vo.CartVo;

import java.util.List;

public interface CartService extends IService<CartEntity> {
    /**
     * 获取购物车所有商品
     *
     * @return
     */
    UniformResponse<CartVo> listCart();

    /**
     * 加入购物车
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    UniformResponse<CartVo> addCart(Integer userId, Integer productId, Integer count);

    /**
     * 更新购物车
     *
     * @param productId
     * @param count
     * @return
     */
    UniformResponse<CartVo> update(Integer productId, Integer count);

    /**
     * 删除购物车商品
     *
     * @param productIds
     * @return
     */
    UniformResponse<CartVo> deleteProduct(List<Integer> productIds);

    /**
     * 选中购物车某个商品
     *
     * @param productId
     * @return
     */
    UniformResponse<CartVo> select(Integer productId);

    /**
     * 取消选中购物车某个商品
     *
     * @param productId
     * @return
     */
    UniformResponse<CartVo> unSelect(Integer productId);

    /**
     * 获取商品数量
     *
     * @return
     */
    UniformResponse<Integer> getCartProductCount();

    /**
     * 选中购物车全部商品
     *
     * @return
     */
    UniformResponse<CartVo> selectAll();

    /**
     * 取消选中购物车全部商品
     *
     * @return
     */
    UniformResponse<CartVo> unSelectAll();
}

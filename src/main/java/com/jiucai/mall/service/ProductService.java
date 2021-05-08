package com.jiucai.mall.service;

import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.ProductEntity;

public interface ProductService {

    /**
     * 查看产品详细信息
     * @param productId
     * @return
     */
    UniformResponse<Object> getProductDetail(Integer productId);

    /**
     * 搜索产品信息
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    UniformResponse<Object> getProductList(Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy);

    /**
     * 新增商品
     * @param productEntity
     * @return
     */
    UniformResponse<Object> addProduct(ProductEntity productEntity);
}

package com.jiucai.mall.controller;

import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.ProductEntity;
import com.jiucai.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(Constant.productRouting.PRODUCT)
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 查看产品详细信息
     * 
     * @param productId
     * @return
     */
    @GetMapping(Constant.productRouting.GET_PRODUCT)
    @ResponseBody
    public Object getProductDetail(Integer productId) {
        UniformResponse<Object> response = productService.getProductDetail(productId);
        return response;
    }

    /**
     * 通过categoryId或keyword搜索相关的产品信息列表
     * 
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping(Constant.productRouting.GET_PRODUCT_LIST)
    @ResponseBody
    public Object getProductList(
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "orderBy", defaultValue = "") String orderBy) {
        UniformResponse<Object> response =
                productService.getProductList(categoryId, keyword, pageNum, pageSize, orderBy);
        return response;
    }

    @PostMapping(Constant.productRouting.ADD_PRODUCT)
    @ResponseBody
    public UniformResponse addProduct(ProductEntity productEntity) {
        UniformResponse<Object> response =
                productService.addProduct(productEntity);
        return response;
    }
}

package com.jiucai.mall.controller;

import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.ProductEntity;
import com.jiucai.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.jiucai.mall.utils.OSSUtil;

import java.io.IOException;

@Controller
@RequestMapping(Constant.productRouting.PRODUCT)
public class ProductController {

    @Autowired
    private OSSUtil ossUtil;

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

    @PostMapping("addAdminProduct")
    @ResponseBody
    public UniformResponse addAdminProduct(@RequestBody ProductEntity productEntity) throws IOException {

        String mainImage = productEntity.getMainImage();
        String[] subImages = productEntity.getSubImages().split(";;;");

        String mainImageName = "";
        String subImagesName = "";

        // 上传图片
        mainImageName = ossUtil.upLoadImage(mainImage);
        for (int i = 0; i < subImages.length; ++i) {
            if (i != subImages.length - 1) {
                subImagesName += ossUtil.upLoadImage(subImages[i]);
                subImagesName += ",";
            } else {
                subImagesName += ossUtil.upLoadImage(subImages[i]);
            }
        }

        // 重置图片名字
        productEntity.setMainImage(mainImageName);
        productEntity.setSubImages(subImagesName);

        // 写入数据库
        return productService.addProduct(productEntity);

    }

}

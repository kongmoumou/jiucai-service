package com.jiucai.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.dao.CategoryMapper;
import com.jiucai.mall.dao.ProductMapper;
import com.jiucai.mall.entity.CategoryEntity;
import com.jiucai.mall.entity.ProductEntity;
import com.jiucai.mall.service.CategoryService;
import com.jiucai.mall.service.ProductService;
import com.jiucai.mall.vo.ProductDataVo;
import com.jiucai.mall.vo.ProductListVo;
import com.jiucai.mall.vo.ProductVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;

    /**
     * 产品详细信息
     * @param productId
     * @return
     */
    public UniformResponse<Object> getProductDetail(Integer productId) {
        if (productId == null) {
            String msg = "没有输入产品ID，请重试！";
            return UniformResponse.ResponseForError(msg);
        }
        ProductEntity productEntity = productMapper.selectById(productId);
        if (productEntity == null) {
            String msg = Constant.userMsg.GetProductDetail.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        } else {
            ProductVo productVo = productEntityToVo(productEntity);
            String msg = Constant.userMsg.GetProductDetail.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg, productVo);
        }
    }


    /**
     * 产品信息列表
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    public UniformResponse<Object> getProductList(Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy) {
        Page<ProductEntity> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        if (categoryId != null || StringUtils.isNotBlank(keyword)) {
            /*按类查询,有categoryID则去枚举相关商品类别下的商品；没有采用关键字查询*/
            QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<>();
            if (categoryId != null) {
                List<Integer> categoryIdList = categoryService.getAllChildrenCategoryId(categoryId).getData();
                queryWrapper.in("category_id", categoryIdList);
            }

            /*增加按关键词查询的条件*/
            if (StringUtils.isNotBlank(keyword)) {
                keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
                queryWrapper.like("name", keyword).or().like("subtitle", keyword);
            }

            /*增加排序的条件*/
            if (StringUtils.isNotBlank(orderBy)) {
                if (Constant.productListOrderBy.ORDER_METHOD.contains(orderBy)) {
                    String[] orderByStrings = orderBy.split("_");
                    String orderByColumn = orderByStrings[0];
                    String orderByMethod = orderByStrings[1];
                    if (StringUtils.equals(orderByMethod, "asc")) {
                        queryWrapper.orderByAsc(orderByColumn);
                    } else if (StringUtils.equals(orderByMethod, "desc")) {
                        queryWrapper.orderByDesc(orderByColumn);
                    }
                }
            }
            result = productMapper.selectPage(result, queryWrapper);

            ProductDataVo productDataVo = new ProductDataVo();
            productDataVo.setPageNum(pageNum);
            productDataVo.setPageSize(pageSize);
            productDataVo.setOrderBy(orderBy);
            productDataVo = productListVoToDataVo(productDataVo, result);

            if (!productDataVo.getList().isEmpty()) {
                System.out.println(result);
                String msg = Constant.userMsg.GetProductList.getSUCCESS();
                return UniformResponse.ResponseForSuccess(msg, productDataVo);
            } else {
                String msg = Constant.userMsg.GetProductList.getFAIL();
                return UniformResponse.ResponseForFail(msg);
            }
        } else {
            String msg = "查询失败,请输入正确的查询条件！";
            return UniformResponse.ResponseForError(msg);
        }

    }

    /** productEntity -> productVo */
    public ProductVo productEntityToVo(ProductEntity productEntity) {
        ProductVo productVo = new ProductVo();

        productVo.setId(productEntity.getId());
        productVo.setCategoryId(productEntity.getCategoryId());
        productVo.setName(productEntity.getName());
        productVo.setSubtitle(productEntity.getSubtitle());
        productVo.setMainImage(productEntity.getMainImage());
        productVo.setSubImages(productEntity.getSubImages());
        productVo.setDetail(productEntity.getDetail());
        productVo.setPrice(productEntity.getPrice());
        productVo.setStock(productEntity.getStock());
        productVo.setStatus(productEntity.getStatus());

        productVo.setCreateTime(productEntity.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        productVo.setUpdateTime(productEntity.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        CategoryEntity categoryEntity = categoryMapper.selectById(productEntity.getCategoryId());
        productVo.setParentCategoryId(categoryEntity.getParentId());
        // TODO: set image cdn host
        return productVo;
    }

    // productEntity -> productListVo
    public ProductListVo productEntityToListVo(ProductEntity productEntity) {
        ProductListVo ProductListVo = new ProductListVo();

        ProductListVo.setId(productEntity.getId());
        ProductListVo.setCategoryId(productEntity.getCategoryId());
        ProductListVo.setName(productEntity.getName());
        ProductListVo.setSubtitle(productEntity.getSubtitle());
        // TODO: set image cdn host
        ProductListVo.setMainImage(productEntity.getMainImage());

        ProductListVo.setPrice(productEntity.getPrice());
        ProductListVo.setStock(productEntity.getStock());
        ProductListVo.setStatus(productEntity.getStatus());

        return ProductListVo;
    }

    /*productListVo transform to ProductDataVo*/
    public ProductDataVo productListVoToDataVo(ProductDataVo productDataVo, Page<ProductEntity> result) {
        List<ProductEntity> records = result.getRecords();
        List<ProductListVo> productListVos = new LinkedList<>();
        for (ProductEntity recordItem : records) {
            productListVos.add(productEntityToListVo(recordItem));
        }
        productDataVo.setList(productListVos);

        productDataVo.setTotal(result.getTotal());
        productDataVo.setSize(result.getSize());
        productDataVo.setCurrent(result.getCurrent());
        productDataVo.setOptimizeCountSql(result.isOptimizeCountSql());
        productDataVo.setHitCount(result.isHitCount());
        productDataVo.setSearchCount(result.isSearchCount());
        productDataVo.setPages(result.getPages());
        return productDataVo;
    }

    /**
     * @param productEntity
     * @return
     */
    @Override
    public UniformResponse<Object> addProduct(ProductEntity productEntity) {
        int rows = productMapper.insert(productEntity);
        if (rows == 0) {
            return UniformResponse.ResponseForError(Constant.userMsg.AddProduct.getFAIL());
        } else {
            return UniformResponse.ResponseForSuccess(Constant.userMsg.AddProduct.getSUCCESS());
        }
    }
}
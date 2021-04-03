package com.jiucai.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVo {
    private Integer id;
    private Integer categoryId;

    private Integer parentCategoryId;

    private String name;
    private String subtitle;

    private String imageHost;

    private String mainImage;
    private String subImages;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;

    private String createTime;
    private String updateTime;
}

package com.jiucai.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductListVo {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String imageHost;
    private String mainImage;
    private Integer status;
    private BigDecimal price;
    private Integer stock;
}

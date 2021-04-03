package com.jiucai.mall.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductDataVo {
    private int pageNum;
    private int pageSize;
    private String orderBy;

    private List<ProductListVo> list;

    private long total;
    private long size;
    private long current;
    private boolean optimizeCountSql;
    private boolean hitCount;
    private boolean searchCount;
    private long pages;
}

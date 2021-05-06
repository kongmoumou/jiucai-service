package com.jiucai.mall.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderPageVo {
    private Integer pageNum;
    private Integer pageSize;
    private Integer total;
    private Boolean hasPreviousPage;
    private Boolean hasNextPage;
    private Integer prePage;
    private Integer nextPage;
    private Integer pages;
    private List<OrderVo> orderVoList;
}

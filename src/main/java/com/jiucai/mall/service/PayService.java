package com.jiucai.mall.service;

import org.springframework.stereotype.Service;

public interface PayService {
    /**
     * 设置付款成功
     * @param orderNo
     * @return
     */
    boolean setPaid(String orderNo);
}

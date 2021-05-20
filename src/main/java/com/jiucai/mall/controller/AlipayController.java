package com.jiucai.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiucai.mall.common.Constant;
import com.jiucai.mall.config.WechatAccountConfig;
import com.jiucai.mall.dao.OrderMapper;
import com.jiucai.mall.entity.OrderEntity;
import com.jiucai.mall.service.PayService;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/pay")
public class AlipayController {
    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private PayService payService;


    /**
     * 支付宝付款
     * @param orderNo
     * @param paymentPrice
     * @param orderName
     * @return
     */
    @PostMapping("/alipay")
    @ResponseBody
    public Object aliPaySubmit(String orderNo, Double paymentPrice, String orderName) {
        /* 生成新的请求 */
        PayRequest request = new PayRequest();

        //支付请求参数
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_PC);
        request.setOrderId(orderNo);
        request.setOrderAmount(paymentPrice);
        request.setOrderName(orderName);
        PayResponse payResponse = bestPayService.pay(request);
        return payResponse;
    }


    /**
     * 异步回调
     */
    @PostMapping(value = "/notify")
    public Object notify(@RequestBody(required=false) String notifyData) {
//        log.info("【异步通知】支付平台的数据request={}", notifyData);
        PayResponse response = bestPayService.asyncNotify(notifyData);
//        log.info("【异步通知】处理后的数据data={}", JsonUtil.toJson(response));

        //返回成功信息给支付平台，否则会不停的异步通知
        if (response.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            return "微信付款成功！";
        }else if (response.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
//            System.out.println(response.getOrderId().toString();
            boolean bool = payService.setPaid(response.getOrderId());
            System.out.println(bool);
            return "支付宝付款成功！";
        }
        throw new RuntimeException("错误的支付平台");
    }
}

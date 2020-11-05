package com.lin.missyou.service;

import com.lin.github.wxpay.sdk.LinWxPayConfig;
import com.lin.github.wxpay.sdk.WXPay;
import com.lin.github.wxpay.sdk.WXPayConstants;
import com.lin.github.wxpay.sdk.WXPayUtil;
import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.exception.http.ServerErrorException;
import com.lin.missyou.model.Order;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.util.HttpRequestProxy;
import org.apache.catalina.Server;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WxPaymentServiceImpl implements WxPaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Value("missyou.order.pay-callback-host")
    private String payCallbackHost;
    @Value("missyou.order.pay-callback-path")
    private String getPayCallbackPath;

    private static LinWxPayConfig wxPayConfig = new LinWxPayConfig();

    @Override
    public Map<String, String> preOrder(Long oid) {
        Long uid = LocalUser.getUser().getId();
        Optional<Order> orderOptional = orderRepository.findByUserIdAndId(uid, oid);
        Order order = orderOptional.orElseThrow(()->new NotFoundException(50009));
        if(order.needCancel()){  // 订单需要被取消
            throw new ForbiddenException(50010);
        }
        WXPay wxPay = this.assembleWxPayConfig();
        Map<String, String> wxOrder;
        try{
            wxOrder = wxPay.unifiedOrder(makePreOrderParams(order.getFinalTotalPrice(), order.getOrderNo())); // 下单预处理
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        if(unifiedOrderSuccess(wxOrder)){
            orderService.updateOrderPrepayId(order.getId(), wxOrder.get("prepay_id"));
        }

        return makePaySignature(wxOrder);
    }

    @Override
    @Transactional
    public void processPayNotify(String xmlString) {
        Map<String, String> dataMap;
        try {
            dataMap = WXPayUtil.xmlToMap(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
        WXPay wxPay = assembleWxPayConfig();
        boolean valid;
        try {
            valid = wxPay.isResponseSignatureValid(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
        if(!valid){
            throw new ServerErrorException(9999);
        }
        String returnCode = dataMap.get("return_code");
        String orderNo = dataMap.get("out_trade_no");
        String resultCode = dataMap.get("result_code");
        if(!returnCode.equals("SUCCESS")){
            throw new ServerErrorException(9999);
        }
        // resultCode和orderNo是在returnCode为Success的基础上，才会有值
        if(!resultCode.equals("SUCCESS") || orderNo == null){
            throw new ServerErrorException(9999);
        }
        this.deal(orderNo);
    }

    // 处理订单
    private void deal(String orderNo){
        Optional<Order> orderOptional = orderRepository.findFirstByOrderNo(orderNo);
        Order order = orderOptional.orElseThrow(()->new ServerErrorException(9999));
        int res = -1;
        if(order.getStatus().equals(OrderStatus.UNPAID.value()) ||
            order.getStatus().equals(OrderStatus.CANCELED.value())){
            res = orderRepository.updateStatusByOrderNo(orderNo, OrderStatus.PAID.value());
        }
        if(res != 1){
            throw new ServerErrorException(9999);
        }

    }

    // 装配微信支付配置
    private WXPay assembleWxPayConfig(){
        WXPay wxPay;
        try{
            wxPay = new WXPay(WxPaymentServiceImpl.wxPayConfig);
        } catch (Exception e){
            throw new ServerErrorException(9999);
        }
        return wxPay;
    }

    // 组装预订单参数信息
    private Map<String, String> makePreOrderParams(BigDecimal serverFinalPrice, String orderNo){
        Map<String, String> data = new HashMap<>();
        data.put("body", "Ray的商城");
        data.put("out_trade_no", orderNo);
        data.put("device_info", "Sleeve");
        data.put("fee_type", "CNY");
        data.put("trade_type", "JSAPI");
        data.put("total_fee", CommonUtil.yuanToFenPlainString(serverFinalPrice));
        data.put("openid", LocalUser.getUser().getOpenid());
        data.put("spbill_create_ip", HttpRequestProxy.getRemoteRealIp());
        data.put("notify_url", payCallbackHost+getPayCallbackPath);
        return data;
    }

    private Boolean unifiedOrderSuccess(Map<String, String> wxOrder){
        if(!wxOrder.get("return_code").equals("SUCCESS") ||
                !wxOrder.get("result_code").equals("SUCCESS")){
            throw new ParameterException(10007);
        }
        return true;
    }

    // 支付签名
    private Map<String, String> makePaySignature(Map<String, String> wxOrder){
        Map<String, String> wxPayMap = new HashMap<>();
        String packageStr = "prepay_id="+wxOrder.get("prepay_id");
        wxPayMap.put("appId", WxPaymentServiceImpl.wxPayConfig.getAppID());
        wxPayMap.put("timeStamp", CommonUtil.timestamp10());
        wxPayMap.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
        wxPayMap.put("package", packageStr);
        wxPayMap.put("signType", "HMAC-SHA256");
        String sign;
        try {
            sign = WXPayUtil.generateSignature(wxPayMap, WxPaymentServiceImpl.wxPayConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }

        Map<String, String> miniPayParams = new HashMap<>();
        miniPayParams.put("paySign", sign);
        miniPayParams.putAll(wxPayMap);
        miniPayParams.remove("appId");
        return miniPayParams;
    }
}

package com.lin.missyou.service;

import java.util.Map;

public interface WxPaymentService {

    Map<String, String> preOrder(Long oid) throws Exception;

    void processPayNotify(String xmlString);
}

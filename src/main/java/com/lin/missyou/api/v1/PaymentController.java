package com.lin.missyou.api.v1;

import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.lib.LinWxNotify;
import com.lin.missyou.service.WxPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("payment")
@Validated
public class PaymentController {

    @Autowired
    private WxPaymentService wxPaymentService;

    @ScopeLevel
    @PostMapping("/pay/order/{id}")
    public Map<String, String> preWxOrder(@PathVariable @Positive Long id) throws Exception {
        return wxPaymentService.preOrder(id);
    }

    // 微信回调接口
    @RequestMapping("/wx/notify")
    public String payCallback(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse){
        InputStream s;
        try {
            s = httpServletRequest.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return LinWxNotify.fail();
        }
        String xml = LinWxNotify.readNotify(s);
        try{
            wxPaymentService.processPayNotify(xml);
        }catch (Exception e){
            return LinWxNotify.fail();
        }
        return LinWxNotify.success();
    }
}

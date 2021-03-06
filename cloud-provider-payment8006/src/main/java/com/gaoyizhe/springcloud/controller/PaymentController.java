package com.gaoyizhe.springcloud.controller;

import com.gaoyizhe.springcloud.entities.CommonResult;
import com.gaoyizhe.springcloud.entities.Payment;
import com.gaoyizhe.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/payment/create")
    public CommonResult<Integer> create(Payment payment) {
        int result = paymentService.create(payment);
        log.info("*****插入结果：" + result);
        if (result > 0) {
            return new CommonResult<>(200, "插入数据库成功" + serverPort, result);
        } else {
            return new CommonResult<>(444, "插入数据库失败" + serverPort, null);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment != null) {
            log.info("*****查询成功：" + payment.toString());
            return new CommonResult<>(200, "查询成功" + serverPort, payment);
        } else {
            log.info("没有查到数据");
            return new CommonResult<>(444, "没有查到数据" + serverPort, null);
        }
    }
}

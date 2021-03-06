package com.gaoyizhe.springcloud.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.gaoyizhe.springcloud.entities.CommonResult;
import com.gaoyizhe.springcloud.entities.Payment;
import com.gaoyizhe.springcloud.lb.LoadBalanced;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单controller
 */
@RestController
@Slf4j
public class OrdeController {
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private LoadBalanced loadBalanced;
    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> creat(@RequestBody Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);

    }

    @GetMapping("/consumer/payment2/get/{id}")
    public CommonResult<Payment> getPaymentById2(@PathVariable("id") Long id) {
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        }

        return new CommonResult<>(4, "操作失败");
    }

    @GetMapping("/myconsumer/payment/get/{id}")
    public CommonResult<Payment> getMyPaymentById2(@PathVariable("id") Long id) {
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");

        if (CollectionUtil.isEmpty(instances)) {
            return null;
        }
        ServiceInstance serviceInstance = loadBalanced.instance(instances);
        log.info(serviceInstance.getUri().getPath());
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(serviceInstance.getUri() + "/payment/get/" + id, CommonResult.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        }

        return new CommonResult<>(4, "操作失败");
    }


}



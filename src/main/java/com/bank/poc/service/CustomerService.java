package com.bank.poc.service;

import com.bank.poc.dto.request.CreateCustomerRequest;
import com.bank.poc.dto.response.CreateCustomerResponse;

/**
 * 客户服务接口。
 */
public interface CustomerService {

    /**
     * 创建客户。
     *
     * @param request 创建客户请求
     * @return 创建客户结果
     */
    CreateCustomerResponse createCustomer(CreateCustomerRequest request);
}
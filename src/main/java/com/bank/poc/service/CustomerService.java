package com.bank.poc.service;

import com.bank.poc.dto.request.CreateCustomerRequest;
import com.bank.poc.dto.response.CreateCustomerResponse;

public interface CustomerService {

    CreateCustomerResponse createCustomer(CreateCustomerRequest request);
}
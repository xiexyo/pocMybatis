package com.bank.poc.service.impl;

import com.bank.poc.common.exception.BizException;
import com.bank.poc.dto.request.CreateCustomerRequest;
import com.bank.poc.dto.response.CreateCustomerResponse;
import com.bank.poc.entity.CustomerEntity;
import com.bank.poc.repository.CustomerRepository;
import com.bank.poc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByCertNo(request.getCertNo())) {
            throw new BizException("CUST001", "客户证件号码已存在");
        }

        LocalDateTime now = LocalDateTime.now();
        String customerId = generateCustomerId();

        CustomerEntity entity = CustomerEntity.builder()
                .customerId(customerId)
                .customerName(request.getCustomerName())
                .certType(request.getCertType())
                .certNo(request.getCertNo())
                .mobile(request.getMobile())
                .customerStatus("ACTIVE")
                .riskLevel(request.getRiskLevel() == null || request.getRiskLevel().trim().isEmpty()
                        ? "R2" : request.getRiskLevel())
                .createdTime(now)
                .updatedTime(now)
                .build();

        customerRepository.save(entity);

        return CreateCustomerResponse.builder()
                .customerId(entity.getCustomerId())
                .customerName(entity.getCustomerName())
                .certType(entity.getCertType())
                .certNo(entity.getCertNo())
                .mobile(entity.getMobile())
                .customerStatus(entity.getCustomerStatus())
                .riskLevel(entity.getRiskLevel())
                .createdTime(entity.getCreatedTime())
                .build();
    }

    private String generateCustomerId() {
        return "C"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private String generateDateCustomerId()
            //演示代码补全 随机生成
    {
        return "C"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }
}
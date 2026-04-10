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

/**
 * 客户服务实现类。
 *
 * <p>核心业务职责：
 * <ul>
 *     <li>校验客户证件唯一性</li>
 *     <li>生成客户号</li>
 *     <li>保存客户基本信息</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    /**
     * 客户仓储。
     */
    private final CustomerRepository customerRepository;

    /**
     * 创建客户。
     *
     * <p>业务规则：
     * <ul>
     *     <li>同一证件号码不能重复创建客户</li>
     *     <li>客户初始状态默认为 ACTIVE</li>
     *     <li>风险等级为空时默认赋值 R2</li>
     * </ul>
     *
     * @param request 创建客户请求
     * @return 创建结果
     */
    @Override
    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        // 业务校验：证件号唯一，避免重复建档
        if (customerRepository.existsByCertNo(request.getCertNo())) {
            throw new BizException("CUST001", "客户证件号码已存在");
        }

        LocalDateTime now = LocalDateTime.now();
        String customerId = generateCustomerId();

        // 组装客户实体
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

        // 落库保存客户信息
        customerRepository.save(entity);

        // 返回响应对象
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

    /**
     * 生成客户号。
     *
     * <p>规则说明：
     * <ul>
     *     <li>前缀：C</li>
     *     <li>时间：yyyyMMddHHmmss</li>
     *     <li>随机数：4位</li>
     * </ul>
     *
     * <p>该方式适合 POC 演示，不适合高并发生产场景。
     *
     * @return 客户号
     */
    private String generateCustomerId() {
        return "C"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    /**
     * 演示用方法。
     *
     * <p>当前业务未使用，仅作为代码补全示例保留。
     *
     * @return 随机生成的客户号
     */
    private String generateDateCustomerId()
            //演示代码补全 随机生成
    {
        return "C"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }
}
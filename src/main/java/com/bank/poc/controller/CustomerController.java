package com.bank.poc.controller;

import com.bank.poc.common.api.ApiResponse;
import com.bank.poc.dto.request.CreateCustomerRequest;
import com.bank.poc.dto.response.CreateCustomerResponse;
import com.bank.poc.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 客户管理控制器。
 *
 * <p>提供零售客户基础服务接口：
 * <ul>
 *     <li>创建客户</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "客户管理")
public class CustomerController {

    /**
     * 客户服务。
     */
    private final CustomerService customerService;

    /**
     * 创建客户。
     *
     * <p>业务规则：
     * <ul>
     *     <li>证件号码必须唯一</li>
     *     <li>客户状态初始化为 ACTIVE</li>
     *     <li>风险等级默认 R2</li>
     * </ul>
     *
     * @param request 创建客户请求
     * @return 创建结果
     */
    @PostMapping
    @Operation(summary = "创建客户")
    public ApiResponse<CreateCustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return ApiResponse.success(customerService.createCustomer(request));
    }
}
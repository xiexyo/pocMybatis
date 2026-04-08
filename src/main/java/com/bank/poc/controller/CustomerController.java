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

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "客户管理")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "创建客户")
    public ApiResponse<CreateCustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return ApiResponse.success(customerService.createCustomer(request));
    }
}
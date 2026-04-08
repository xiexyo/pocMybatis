package com.bank.poc.controller;

import com.bank.poc.common.api.ApiResponse;
import com.bank.poc.dto.request.OpenAccountRequest;
import com.bank.poc.dto.response.AccountDetailResponse;
import com.bank.poc.dto.response.OpenAccountResponse;
import com.bank.poc.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "账户管理")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/open")
    @Operation(summary = "开立账户")
    public ApiResponse<OpenAccountResponse> openAccount(@Valid @RequestBody OpenAccountRequest request) {
        return ApiResponse.success(accountService.openAccount(request));
    }

    @GetMapping("/{accountNo}")
    @Operation(summary = "查询账户详情")
    public ApiResponse<AccountDetailResponse> getAccountDetail(@PathVariable String accountNo) {
        return ApiResponse.success(accountService.getAccountDetail(accountNo));
    }
}
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

/**
 * 账户管理控制器。
 *
 * <p>提供账户类相关接口：
 * <ul>
 *     <li>开立账户</li>
 *     <li>查询账户详情</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "账户管理")
public class AccountController {

    /**
     * 账户服务。
     */
    private final AccountService accountService;

    /**
     * 开立账户。
     *
     * <p>业务规则：
     * <ul>
     *     <li>客户必须存在</li>
     *     <li>产品必须存在且处于可售状态</li>
     *     <li>若产品为一类户，则客户只能持有一个激活状态的一类户</li>
     * </ul>
     *
     * @param request 开户请求
     * @return 开户结果
     */
    @PostMapping("/open")
    @Operation(summary = "开立账户")
    public ApiResponse<OpenAccountResponse> openAccount(@Valid @RequestBody OpenAccountRequest request) {
        return ApiResponse.success(accountService.openAccount(request));
    }

    /**
     * 查询账户详情。
     *
     * <p>返回账户、客户、产品的组合信息，便于前端一次性展示账户详情页面。
     *
     * @param accountNo 账号
     * @return 账户详情
     */
    @GetMapping("/{accountNo}")
    @Operation(summary = "查询账户详情")
    public ApiResponse<AccountDetailResponse> getAccountDetail(@PathVariable String accountNo) {
        return ApiResponse.success(accountService.getAccountDetail(accountNo));
    }
}
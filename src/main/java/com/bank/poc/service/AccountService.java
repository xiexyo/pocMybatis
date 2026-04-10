package com.bank.poc.service;

import com.bank.poc.dto.request.OpenAccountRequest;
import com.bank.poc.dto.response.AccountDetailResponse;
import com.bank.poc.dto.response.OpenAccountResponse;

/**
 * 账户服务接口。
 */
public interface AccountService {

    /**
     * 开立账户。
     *
     * @param request 开户请求
     * @return 开户结果
     */
    OpenAccountResponse openAccount(OpenAccountRequest request);

    /**
     * 查询账户详情。
     *
     * @param accountNo 账号
     * @return 账户详情
     */
    AccountDetailResponse getAccountDetail(String accountNo);
}
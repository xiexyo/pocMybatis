package com.bank.poc.service;

import com.bank.poc.dto.request.OpenAccountRequest;
import com.bank.poc.dto.response.AccountDetailResponse;
import com.bank.poc.dto.response.OpenAccountResponse;

public interface AccountService {

    OpenAccountResponse openAccount(OpenAccountRequest request);

    AccountDetailResponse getAccountDetail(String accountNo);
}
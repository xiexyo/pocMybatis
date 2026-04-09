package com.bank.poc.service.impl;

import com.bank.poc.common.exception.BizException;
import com.bank.poc.dto.request.OpenAccountRequest;
import com.bank.poc.dto.response.AccountDetailResponse;
import com.bank.poc.dto.response.OpenAccountResponse;
import com.bank.poc.entity.AccountEntity;
import com.bank.poc.entity.CustomerEntity;
import com.bank.poc.entity.ProductEntity;
import com.bank.poc.repository.AccountRepository;
import com.bank.poc.repository.CustomerRepository;
import com.bank.poc.repository.ProductRepository;
import com.bank.poc.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OpenAccountResponse openAccount(OpenAccountRequest request) {
        CustomerEntity customer = customerRepository.findByCustomerId(request.getCustomerId());
        if (customer == null) {
            throw new BizException("CUST404", "客户不存在");
        }

        ProductEntity product = productRepository.findByProductCode(request.getProductCode());
        if (product == null) {
            throw new BizException("PROD404", "产品不存在");
        }

        if (!"ON_SALE".equals(product.getSaleStatus())) {
            throw new BizException("PROD001", "产品当前不可售");
        }

        if ("CLASS_I".equals(product.getAccountLevel())
                && accountRepository.existsActiveClassOneAccount(customer.getCustomerId())) {
            throw new BizException("ACCT001", "该客户已存在一类户，不能重复开立");
        }

        LocalDateTime now = LocalDateTime.now();
        String accountNo = generateAccountNo();

        AccountEntity entity = AccountEntity.builder()
                .accountNo(accountNo)
                .customerId(customer.getCustomerId())
                .accountName(customer.getCustomerName())
                .accountType(product.getProductType())
                .productCode(product.getProductCode())
                .currency(product.getCurrency())
                .balance(BigDecimal.ZERO)
                .accountStatus("ACTIVE")
                .branchCode(request.getBranchCode())
                .openDate(now)
                .createdTime(now)
                .updatedTime(now)
                .build();

        accountRepository.save(entity);

        return OpenAccountResponse.builder()
                .accountNo(entity.getAccountNo())
                .customerId(entity.getCustomerId())
                .accountName(entity.getAccountName())
                .accountType(entity.getAccountType())
                .productCode(entity.getProductCode())
                .currency(entity.getCurrency())
                .balance(entity.getBalance())
                .accountStatus(entity.getAccountStatus())
                .branchCode(entity.getBranchCode())
                .openDate(entity.getOpenDate())
                .build();
    }

    @Override
    public AccountDetailResponse getAccountDetail(String accountNo) {
        AccountEntity account = accountRepository.findByAccountNo(accountNo);
        if (account == null) {
            throw new BizException("ACCT404", "账户不存在");
        }

        CustomerEntity customer = customerRepository.findByCustomerId(account.getCustomerId());
        if (customer == null) {
            throw new BizException("CUST404", "账户对应客户不存在");
        }

        ProductEntity product = productRepository.findByProductCode(account.getProductCode());
        if (product == null) {
            throw new BizException("PROD404", "账户对应产品不存在");
        }

        return AccountDetailResponse.builder()
                .accountNo(account.getAccountNo())
                .customerId(account.getCustomerId())
                .customerName(customer.getCustomerName())
                .accountName(account.getAccountName())
                .accountType(account.getAccountType())
                .productCode(account.getProductCode())
                .productName(product.getProductName())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .accountStatus(account.getAccountStatus())
                .branchCode(account.getBranchCode())
                .openDate(account.getOpenDate())
                .createdTime(account.getCreatedTime())
                .updatedTime(account.getUpdatedTime())
                .build();
    }

    private String generateAccountNo() {
        return "62"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + ThreadLocalRandom.current().nextInt(100000, 999999);
    }
}
package com.bank.poc.repository;

import com.bank.poc.entity.AccountEntity;

public interface AccountRepository {

    void save(AccountEntity entity);

    AccountEntity findByAccountNo(String accountNo);

    boolean existsActiveClassOneAccount(String customerId);
}
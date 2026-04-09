package com.bank.poc.repository.impl;

import com.bank.poc.entity.AccountEntity;
import com.bank.poc.mapper.AccountMapper;
import com.bank.poc.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountMapper accountMapper;

    @Override
    public void save(AccountEntity entity) {
        accountMapper.insert(entity);
    }

    @Override
    public AccountEntity findByAccountNo(String accountNo) {
        return accountMapper.selectByAccountNo(accountNo);
    }

    @Override
    public boolean existsActiveClassOneAccount(String customerId) {
        return accountMapper.countActiveClassOneAccountByCustomerId(customerId) > 0;
    }
}
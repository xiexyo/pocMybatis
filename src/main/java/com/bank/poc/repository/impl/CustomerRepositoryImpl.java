package com.bank.poc.repository.impl;

import com.bank.poc.entity.CustomerEntity;
import com.bank.poc.mapper.CustomerMapper;
import com.bank.poc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerMapper customerMapper;

    @Override
    public void save(CustomerEntity entity) {
        customerMapper.insert(entity);
    }

    @Override
    public CustomerEntity findByCustomerId(String customerId) {
        return customerMapper.selectByCustomerId(customerId);
    }

    @Override
    public CustomerEntity findByCertNo(String certNo) {
        return customerMapper.selectByCertNo(certNo);
    }

    @Override
    public boolean existsByCertNo(String certNo) {
        return customerMapper.countByCertNo(certNo) > 0;
    }
}
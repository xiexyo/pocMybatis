package com.bank.poc.repository;

import com.bank.poc.entity.CustomerEntity;

public interface CustomerRepository {

    void save(CustomerEntity entity);

    CustomerEntity findByCustomerId(String customerId);

    CustomerEntity findByCertNo(String certNo);

    boolean existsByCertNo(String certNo);
}
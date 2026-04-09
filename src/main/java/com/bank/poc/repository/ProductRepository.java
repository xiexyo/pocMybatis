package com.bank.poc.repository;

import com.bank.poc.entity.ProductEntity;

public interface ProductRepository {

    ProductEntity findByProductCode(String productCode);
}
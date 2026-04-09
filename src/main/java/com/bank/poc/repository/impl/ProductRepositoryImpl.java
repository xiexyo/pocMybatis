package com.bank.poc.repository.impl;

import com.bank.poc.entity.ProductEntity;
import com.bank.poc.mapper.ProductMapper;
import com.bank.poc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductMapper productMapper;

    @Override
    public ProductEntity findByProductCode(String productCode) {
        return productMapper.selectByProductCode(productCode);
    }
}
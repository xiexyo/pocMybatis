package com.bank.poc.mapper;

import com.bank.poc.entity.CustomerEntity;
import org.apache.ibatis.annotations.Param;

public interface CustomerMapper {

    int insert(CustomerEntity entity);

    CustomerEntity selectByCustomerId(@Param("customerId") String customerId);

    CustomerEntity selectByCertNo(@Param("certNo") String certNo);

    long countByCertNo(@Param("certNo") String certNo);
}
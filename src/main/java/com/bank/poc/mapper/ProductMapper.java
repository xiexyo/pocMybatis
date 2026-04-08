package com.bank.poc.mapper;

import com.bank.poc.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {

    ProductEntity selectByProductCode(@Param("productCode") String productCode);

    List<ProductEntity> selectBySaleStatus(@Param("saleStatus") String saleStatus);
}
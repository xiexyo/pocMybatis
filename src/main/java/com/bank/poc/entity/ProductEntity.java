package com.bank.poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    private String productCode;
    private String productName;
    private String productType;
    private String currency;
    private String saleStatus;
    private String accountLevel;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
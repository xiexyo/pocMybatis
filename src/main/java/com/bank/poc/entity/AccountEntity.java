package com.bank.poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    private String accountNo;
    private String customerId;
    private String accountName;
    private String accountType;
    private String productCode;
    private String currency;
    private BigDecimal balance;
    private String accountStatus;
    private String branchCode;
    private LocalDateTime openDate;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
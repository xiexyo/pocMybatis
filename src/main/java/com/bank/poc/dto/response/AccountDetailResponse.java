package com.bank.poc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountDetailResponse {

    private String accountNo;
    private String customerId;
    private String customerName;
    private String accountName;
    private String accountType;
    private String productCode;
    private String productName;
    private String currency;
    private BigDecimal balance;
    private String accountStatus;
    private String branchCode;
    private LocalDateTime openDate;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
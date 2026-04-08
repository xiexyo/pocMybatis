package com.bank.poc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OpenAccountResponse {

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
}
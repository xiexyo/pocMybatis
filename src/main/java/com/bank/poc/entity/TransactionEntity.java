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
public class TransactionEntity {

    private String txnId;
    private String requestId;
    private String debitAccountNo;
    private String creditAccountNo;
    private String txnType;
    private String txnStatus;
    private BigDecimal amount;
    private String remark;
    private LocalDateTime txnTime;
    private LocalDateTime createdTime;
}
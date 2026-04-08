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
public class IdempotentRecordEntity {

    private String requestId;
    private String businessType;
    private String businessKey;
    private String processStatus;
    private String responseCode;
    private String responseMessage;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
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
public class CustomerEntity {

    private String customerId;
    private String customerName;
    private String certType;
    private String certNo;
    private String mobile;
    private String customerStatus;
    private String riskLevel;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
package com.bank.poc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateCustomerResponse {

    private String customerId;
    private String customerName;
    private String certType;
    private String certNo;
    private String mobile;
    private String customerStatus;
    private String riskLevel;
    private LocalDateTime createdTime;
}
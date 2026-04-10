package com.bank.poc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建客户响应对象。
 *
 * <p>返回客户创建后的核心信息，用于前端回显或后续开户流程衔接。
 */
@Data
@Builder
public class CreateCustomerResponse {

    /**
     * 客户号。
     */
    private String customerId;

    /**
     * 客户姓名。
     */
    private String customerName;

    /**
     * 证件类型。
     */
    private String certType;

    /**
     * 证件号码。
     */
    private String certNo;

    /**
     * 手机号。
     */
    private String mobile;

    /**
     * 客户状态。
     */
    private String customerStatus;

    /**
     * 风险等级。
     */
    private String riskLevel;

    /**
     * 创建时间。
     */
    private LocalDateTime createdTime;
}
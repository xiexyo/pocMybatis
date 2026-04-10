package com.bank.poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 客户实体。
 *
 * <p>对应数据库表：t_customer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    /**
     * 客户号，主键。
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
     * 例如：ACTIVE
     */
    private String customerStatus;

    /**
     * 风险等级。
     * 例如：R1、R2、R3。
     */
    private String riskLevel;

    /**
     * 创建时间。
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间。
     */
    private LocalDateTime updatedTime;
}
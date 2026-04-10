package com.bank.poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户实体。
 *
 * <p>对应数据库表：t_account
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    /**
     * 账号，主键。
     */
    private String accountNo;

    /**
     * 所属客户号。
     */
    private String customerId;

    /**
     * 户名。
     */
    private String accountName;

    /**
     * 账户类型。
     * 例如：CURRENT
     */
    private String accountType;

    /**
     * 关联产品编号。
     */
    private String productCode;

    /**
     * 币种。
     */
    private String currency;

    /**
     * 余额。
     */
    private BigDecimal balance;

    /**
     * 账户状态。
     * 例如：ACTIVE
     */
    private String accountStatus;

    /**
     * 开户机构号。
     */
    private String branchCode;

    /**
     * 开户时间。
     */
    private LocalDateTime openDate;

    /**
     * 创建时间。
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间。
     */
    private LocalDateTime updatedTime;
}
package com.bank.poc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户详情响应对象。
 *
 * <p>聚合了账户信息、客户信息、产品信息，供账户详情页展示。
 */
@Data
@Builder
public class AccountDetailResponse {

    /**
     * 账号。
     */
    private String accountNo;

    /**
     * 客户号。
     */
    private String customerId;

    /**
     * 客户姓名。
     */
    private String customerName;

    /**
     * 户名。
     */
    private String accountName;

    /**
     * 账户类型。
     */
    private String accountType;

    /**
     * 产品编号。
     */
    private String productCode;

    /**
     * 产品名称。
     */
    private String productName;

    /**
     * 币种。
     */
    private String currency;

    /**
     * 账户余额。
     */
    private BigDecimal balance;

    /**
     * 账户状态。
     */
    private String accountStatus;

    /**
     * 开户机构号。
     */
    private String branchCode;

    /**
     * 开户日期。
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
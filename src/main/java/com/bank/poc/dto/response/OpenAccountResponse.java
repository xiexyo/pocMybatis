package com.bank.poc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 开户响应对象。
 *
 * <p>返回新开账户的核心基础信息。
 */
@Data
@Builder
public class OpenAccountResponse {

    /**
     * 账号。
     */
    private String accountNo;

    /**
     * 客户号。
     */
    private String customerId;

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
     * 币种。
     */
    private String currency;

    /**
     * 账户余额。
     * 新开户默认为 0。
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
     * 开户时间。
     */
    private LocalDateTime openDate;
}
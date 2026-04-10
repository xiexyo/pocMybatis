package com.bank.poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易流水实体。
 *
 * <p>对应数据库表：t_transaction
 *
 * <p>用于记录账户间资金往来或账户自身资金变动信息。
 * 当前项目中虽然尚未开放交易接口，但表结构和实体已预留。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    /**
     * 交易流水号，主键。
     */
    private String txnId;

    /**
     * 请求唯一标识。
     * 可用于实现交易请求幂等控制。
     */
    private String requestId;

    /**
     * 借方账号。
     * 出账账户。
     */
    private String debitAccountNo;

    /**
     * 贷方账号。
     * 入账账户。
     */
    private String creditAccountNo;

    /**
     * 交易类型。
     * 例如：TRANSFER、DEPOSIT、WITHDRAW。
     */
    private String txnType;

    /**
     * 交易状态。
     * 例如：INIT、SUCCESS、FAIL。
     */
    private String txnStatus;

    /**
     * 交易金额。
     */
    private BigDecimal amount;

    /**
     * 交易备注。
     */
    private String remark;

    /**
     * 交易时间。
     */
    private LocalDateTime txnTime;

    /**
     * 创建时间。
     */
    private LocalDateTime createdTime;
}
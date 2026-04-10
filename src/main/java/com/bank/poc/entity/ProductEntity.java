package com.bank.poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 产品实体。
 *
 * <p>对应数据库表：t_product
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    /**
     * 产品编号，主键。
     */
    private String productCode;

    /**
     * 产品名称。
     */
    private String productName;

    /**
     * 产品类型。
     */
    private String productType;

    /**
     * 币种。
     */
    private String currency;

    /**
     * 销售状态。
     * 例如：ON_SALE、OFF_SALE
     */
    private String saleStatus;

    /**
     * 账户等级。
     * 例如：CLASS_I、CLASS_II
     */
    private String accountLevel;

    /**
     * 创建时间。
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间。
     */
    private LocalDateTime updatedTime;
}
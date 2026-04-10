package com.bank.poc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 开户请求对象。
 *
 * <p>用于根据客户号和产品编号开立银行账户。
 */
@Data
public class OpenAccountRequest {

    /**
     * 客户号。
     */
    @NotBlank(message = "客户号不能为空")
    private String customerId;

    /**
     * 产品编号。
     */
    @NotBlank(message = "产品编号不能为空")
    private String productCode;

    /**
     * 开户机构号。
     * 可为空，POC 场景下不做强校验。
     */
    private String branchCode;
}
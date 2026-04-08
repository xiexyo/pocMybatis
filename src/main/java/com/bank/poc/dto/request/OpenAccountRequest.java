package com.bank.poc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OpenAccountRequest {

    @NotBlank(message = "客户号不能为空")
    private String customerId;

    @NotBlank(message = "产品编号不能为空")
    private String productCode;

    private String branchCode;
}
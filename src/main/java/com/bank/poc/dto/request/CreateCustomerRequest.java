package com.bank.poc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCustomerRequest {

    @NotBlank(message = "客户姓名不能为空")
    private String customerName;

    @NotBlank(message = "证件类型不能为空")
    private String certType;

    @NotBlank(message = "证件号码不能为空")
    private String certNo;

    @NotBlank(message = "手机号不能为空")
    private String mobile;

    private String riskLevel;
}
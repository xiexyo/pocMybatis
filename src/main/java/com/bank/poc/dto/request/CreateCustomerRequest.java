package com.bank.poc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建客户请求对象。
 *
 * <p>对应零售客户开户注册场景中的客户主信息录入。
 */
@Data
public class CreateCustomerRequest {

    /**
     * 客户姓名。
     */
    @NotBlank(message = "客户姓名不能为空")
    private String customerName;

    /**
     * 证件类型。
     * 例如：身份证、护照等。
     */
    @NotBlank(message = "证件类型不能为空")
    private String certType;

    /**
     * 证件号码。
     * 在系统中要求唯一。
     */
    @NotBlank(message = "证件号码不能为空")
    private String certNo;

    /**
     * 手机号。
     */
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    /**
     * 风险等级。
     * 为空时默认赋值为 R2。
     */
    private String riskLevel;
}
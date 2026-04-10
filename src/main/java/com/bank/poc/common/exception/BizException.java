package com.bank.poc.common.exception;

/**
 * 业务异常。
 *
 * <p>用于承载可预期的业务失败场景，例如：
 * <ul>
 *     <li>客户不存在</li>
 *     <li>产品不存在</li>
 *     <li>产品不可售</li>
 *     <li>重复开户</li>
 * </ul>
 *
 * <p>该异常会被全局异常处理器统一转换为 HTTP 400 响应。
 */
public class BizException extends RuntimeException {

    /**
     * 业务错误码。
     */
    private final String code;

    /**
     * 构造业务异常。
     *
     * @param code    业务错误码
     * @param message 错误描述
     */
    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取业务错误码。
     *
     * @return 业务错误码
     */
    public String getCode() {
        return code;
    }
}
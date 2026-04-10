package com.bank.poc.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一 API 返回对象。
 *
 * <p>返回码约定：
 * <ul>
 *     <li>B0000：成功</li>
 *     <li>P0001：参数校验失败</li>
 *     <li>S9999：系统异常</li>
 *     <li>其他：业务异常码</li>
 * </ul>
 *
 * @param <T> 业务数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * 业务返回码。
     */
    private String code;

    /**
     * 返回描述信息。
     */
    private String message;

    /**
     * 返回数据。
     */
    private T data;

    /**
     * 构造成功返回结果。
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 成功响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("B0000", "success", data);
    }

    /**
     * 构造无数据的成功返回结果。
     *
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>("B0000", "success", null);
    }

    /**
     * 构造失败返回结果。
     *
     * @param code    业务错误码
     * @param message 错误描述
     * @param <T>     数据类型
     * @return 失败响应
     */
    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
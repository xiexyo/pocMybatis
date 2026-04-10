package com.bank.poc.config;

import com.bank.poc.common.api.ApiResponse;
import com.bank.poc.common.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器。
 *
 * <p>处理范围：
 * <ul>
 *     <li>业务异常：返回 400</li>
 *     <li>参数校验异常：返回 400</li>
 *     <li>系统异常：返回 500</li>
 * </ul>
 *
 * <p>统一返回格式为 {@link ApiResponse}，便于前后端联调和接口标准化。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常。
     *
     * @param ex 业务异常
     * @return 标准失败响应
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResponse<Void>> handleBizException(BizException ex, HttpServletRequest request) {
        log.warn("业务异常, uri={}, method={}, code={}, message={}",
                request.getRequestURI(),
                request.getMethod(),
                ex.getCode(),
                ex.getMessage(),
                ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ex.getCode(), ex.getMessage()));
    }

    /**
     * 处理请求参数校验异常。
     *
     * <p>当请求体字段不满足 {@code @NotBlank}、{@code @NotNull} 等校验规则时触发。
     *
     * @param ex 参数校验异常
     * @return 标准失败响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex,
                                                                       HttpServletRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError == null ? "参数校验失败" : fieldError.getField() + ":" + fieldError.getDefaultMessage();

        log.warn("参数校验异常, uri={}, method={}, message={}",
                request.getRequestURI(),
                request.getMethod(),
                message,
                ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail("P0001", message));
    }

    /**
     * 处理未捕获的系统异常。
     *
     * @param ex 系统异常
     * @return 标准失败响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex, HttpServletRequest request) {
        log.error("系统异常, uri={}, method={}",
                request.getRequestURI(),
                request.getMethod(),
                ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("S9999", ex.getMessage()));
    }
}
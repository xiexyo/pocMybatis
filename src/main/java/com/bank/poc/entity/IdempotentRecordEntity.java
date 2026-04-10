package com.bank.poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 幂等记录实体。
 *
 * <p>对应数据库表：t_idempotent_record
 *
 * <p>主要用于记录外部请求的处理状态，防止重复请求导致业务重复执行。
 * 常见应用场景：
 * <ul>
 *     <li>转账请求幂等控制</li>
 *     <li>开户请求防重复提交</li>
 *     <li>消息消费去重</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdempotentRecordEntity {

    /**
     * 请求唯一标识。
     * 一般由调用方传入，用于标识一次唯一业务请求。
     */
    private String requestId;

    /**
     * 业务类型。
     * 例如：OPEN_ACCOUNT、TRANSFER 等。
     */
    private String businessType;

    /**
     * 业务主键。
     * 用于标识具体业务对象，例如客户号、交易号、账户号等。
     */
    private String businessKey;

    /**
     * 处理状态。
     * 例如：PROCESSING、SUCCESS、FAIL。
     */
    private String processStatus;

    /**
     * 响应码。
     * 用于记录本次业务处理结果码。
     */
    private String responseCode;

    /**
     * 响应信息。
     * 用于记录本次业务处理结果描述。
     */
    private String responseMessage;

    /**
     * 创建时间。
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间。
     */
    private LocalDateTime updatedTime;
}
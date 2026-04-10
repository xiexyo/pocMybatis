package com.bank.poc.mapper;

import com.bank.poc.entity.TransactionEntity;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易流水 Mapper。
 *
 * <p>负责交易流水表 {@code t_transaction} 的数据库访问。
 */
public interface TransactionMapper {

    /**
     * 新增交易流水。
     *
     * @param entity 交易流水实体
     * @return 影响行数
     */
    int insert(TransactionEntity entity);

    /**
     * 根据交易流水号查询交易。
     *
     * @param txnId 交易流水号
     * @return 交易实体，不存在返回 null
     */
    TransactionEntity selectByTxnId(@Param("txnId") String txnId);

    /**
     * 根据请求唯一标识查询交易。
     *
     * <p>常用于交易幂等校验。
     *
     * @param requestId 请求唯一标识
     * @return 交易实体，不存在返回 null
     */
    TransactionEntity selectByRequestId(@Param("requestId") String requestId);

    /**
     * 根据账号查询相关交易流水。
     *
     * <p>会同时查询该账号作为借方或贷方参与的交易。
     *
     * @param accountNo 账号
     * @return 交易列表
     */
    List<TransactionEntity> selectByAccountNo(@Param("accountNo") String accountNo);

    /**
     * 根据交易时间范围查询交易流水。
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 交易列表
     */
    List<TransactionEntity> selectByTxnTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime);
}
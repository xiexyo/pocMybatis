package com.bank.poc.mapper;

import com.bank.poc.entity.TransactionEntity;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionMapper {

    int insert(TransactionEntity entity);

    TransactionEntity selectByTxnId(@Param("txnId") String txnId);

    TransactionEntity selectByRequestId(@Param("requestId") String requestId);

    List<TransactionEntity> selectByAccountNo(@Param("accountNo") String accountNo);

    List<TransactionEntity> selectByTxnTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime);
}
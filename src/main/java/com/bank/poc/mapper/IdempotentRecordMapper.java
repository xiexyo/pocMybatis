package com.bank.poc.mapper;

import com.bank.poc.entity.IdempotentRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IdempotentRecordMapper {

    int insert(IdempotentRecordEntity entity);

    IdempotentRecordEntity selectByRequestId(@Param("requestId") String requestId);

    List<IdempotentRecordEntity> selectByBusinessType(@Param("businessType") String businessType);
}
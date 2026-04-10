package com.bank.poc.mapper;

import com.bank.poc.entity.IdempotentRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 幂等记录 Mapper。
 *
 * <p>负责幂等记录表 {@code t_idempotent_record} 的数据库访问。
 */
public interface IdempotentRecordMapper {

    /**
     * 新增幂等记录。
     *
     * @param entity 幂等记录实体
     * @return 影响行数
     */
    int insert(IdempotentRecordEntity entity);

    /**
     * 根据请求唯一标识查询幂等记录。
     *
     * @param requestId 请求唯一标识
     * @return 幂等记录，不存在返回 null
     */
    IdempotentRecordEntity selectByRequestId(@Param("requestId") String requestId);

    /**
     * 根据业务类型查询幂等记录列表。
     *
     * @param businessType 业务类型
     * @return 幂等记录列表
     */
    List<IdempotentRecordEntity> selectByBusinessType(@Param("businessType") String businessType);
}
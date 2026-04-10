package com.bank.poc.mapper;

import com.bank.poc.entity.CustomerEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 客户 Mapper。
 *
 * <p>负责客户表 {@code t_customer} 的数据库访问。
 */
public interface CustomerMapper {

    /**
     * 新增客户。
     *
     * @param entity 客户实体
     * @return 影响行数
     */
    int insert(CustomerEntity entity);

    /**
     * 根据客户号查询客户。
     *
     * @param customerId 客户号
     * @return 客户实体，不存在返回 null
     */
    CustomerEntity selectByCustomerId(@Param("customerId") String customerId);

    /**
     * 根据证件号码查询客户。
     *
     * @param certNo 证件号码
     * @return 客户实体，不存在返回 null
     */
    CustomerEntity selectByCertNo(@Param("certNo") String certNo);

    /**
     * 根据证件号码统计客户数量。
     *
     * <p>用于校验客户证件号码唯一性。
     *
     * @param certNo 证件号码
     * @return 数量
     */
    long countByCertNo(@Param("certNo") String certNo);
}
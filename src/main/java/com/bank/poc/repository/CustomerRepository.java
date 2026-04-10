package com.bank.poc.repository;

import com.bank.poc.entity.CustomerEntity;

/**
 * 客户仓储接口。
 *
 * <p>用于封装客户相关的数据访问能力，屏蔽底层 MyBatis 细节。
 */
public interface CustomerRepository {

    /**
     * 保存客户信息。
     *
     * @param entity 客户实体
     */
    void save(CustomerEntity entity);

    /**
     * 根据客户号查询客户。
     *
     * @param customerId 客户号
     * @return 客户实体，不存在返回 null
     */
    CustomerEntity findByCustomerId(String customerId);

    /**
     * 根据证件号码查询客户。
     *
     * @param certNo 证件号码
     * @return 客户实体，不存在返回 null
     */
    CustomerEntity findByCertNo(String certNo);

    /**
     * 判断证件号码是否已存在。
     *
     * @param certNo 证件号码
     * @return true-已存在；false-不存在
     */
    boolean existsByCertNo(String certNo);
}
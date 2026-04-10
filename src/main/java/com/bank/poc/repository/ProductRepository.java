package com.bank.poc.repository;

import com.bank.poc.entity.ProductEntity;

/**
 * 产品仓储接口。
 *
 * <p>用于查询账户开户时所需的产品信息。
 */
public interface ProductRepository {

    /**
     * 根据产品编号查询产品。
     *
     * @param productCode 产品编号
     * @return 产品实体，不存在返回 null
     */
    ProductEntity findByProductCode(String productCode);
}
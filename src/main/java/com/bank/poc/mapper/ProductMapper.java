package com.bank.poc.mapper;

import com.bank.poc.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品 Mapper。
 *
 * <p>负责产品表 {@code t_product} 的数据库访问。
 */
public interface ProductMapper {

    /**
     * 根据产品编号查询产品。
     *
     * @param productCode 产品编号
     * @return 产品实体，不存在返回 null
     */
    ProductEntity selectByProductCode(@Param("productCode") String productCode);

    /**
     * 根据销售状态查询产品列表。
     *
     * @param saleStatus 销售状态
     * @return 产品列表
     */
    List<ProductEntity> selectBySaleStatus(@Param("saleStatus") String saleStatus);
}
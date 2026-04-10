package com.bank.poc.mapper;

import com.bank.poc.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账户 Mapper。
 *
 * <p>负责账户表 {@code t_account} 的数据库访问。
 */
public interface AccountMapper {

    /**
     * 新增账户。
     *
     * @param entity 账户实体
     * @return 影响行数
     */
    int insert(AccountEntity entity);

    /**
     * 根据账号查询账户。
     *
     * @param accountNo 账号
     * @return 账户实体，不存在返回 null
     */
    AccountEntity selectByAccountNo(@Param("accountNo") String accountNo);

    /**
     * 根据客户号查询名下所有账户。
     *
     * @param customerId 客户号
     * @return 账户列表
     */
    List<AccountEntity> selectByCustomerId(@Param("customerId") String customerId);

    /**
     * 根据客户号和账户类型查询账户。
     *
     * @param customerId  客户号
     * @param accountType 账户类型
     * @return 账户列表
     */
    List<AccountEntity> selectByCustomerIdAndAccountType(@Param("customerId") String customerId,
                                                         @Param("accountType") String accountType);

    /**
     * 根据客户号和账户状态查询账户。
     *
     * @param customerId    客户号
     * @param accountStatus 账户状态
     * @return 账户列表
     */
    List<AccountEntity> selectByCustomerIdAndAccountStatus(@Param("customerId") String customerId,
                                                           @Param("accountStatus") String accountStatus);

    /**
     * 统计客户名下激活状态的一类户数量。
     *
     * <p>用于控制“一类户唯一”业务规则。
     *
     * @param customerId 客户号
     * @return 一类户数量
     */
    long countActiveClassOneAccountByCustomerId(@Param("customerId") String customerId);
}
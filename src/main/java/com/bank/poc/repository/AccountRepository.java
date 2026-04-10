package com.bank.poc.repository;

import com.bank.poc.entity.AccountEntity;

/**
 * 账户仓储接口。
 *
 * <p>用于封装账户相关数据访问逻辑。
 */
public interface AccountRepository {

    /**
     * 保存账户信息。
     *
     * @param entity 账户实体
     */
    void save(AccountEntity entity);

    /**
     * 根据账号查询账户。
     *
     * @param accountNo 账号
     * @return 账户实体，不存在返回 null
     */
    AccountEntity findByAccountNo(String accountNo);

    /**
     * 判断客户是否已存在激活状态的一类户。
     *
     * <p>该规则用于控制“一类户唯一”业务约束。
     *
     * @param customerId 客户号
     * @return true-存在；false-不存在
     */
    boolean existsActiveClassOneAccount(String customerId);
}
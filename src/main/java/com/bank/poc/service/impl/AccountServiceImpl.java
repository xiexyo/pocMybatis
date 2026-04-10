package com.bank.poc.service.impl;

import com.bank.poc.common.exception.BizException;
import com.bank.poc.dto.request.OpenAccountRequest;
import com.bank.poc.dto.response.AccountDetailResponse;
import com.bank.poc.dto.response.OpenAccountResponse;
import com.bank.poc.entity.AccountEntity;
import com.bank.poc.entity.CustomerEntity;
import com.bank.poc.entity.ProductEntity;
import com.bank.poc.repository.AccountRepository;
import com.bank.poc.repository.CustomerRepository;
import com.bank.poc.repository.ProductRepository;
import com.bank.poc.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 账户服务实现类。
 *
 * <p>核心业务职责：
 * <ul>
 *     <li>校验客户是否存在</li>
 *     <li>校验产品是否存在且可售</li>
 *     <li>校验一类户唯一性规则</li>
 *     <li>生成账号并落库</li>
 *     <li>查询账户详情并聚合客户、产品信息</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    /**
     * 账户仓储。
     */
    private final AccountRepository accountRepository;

    /**
     * 客户仓储。
     */
    private final CustomerRepository customerRepository;

    /**
     * 产品仓储。
     */
    private final ProductRepository productRepository;

    /**
     * 开立账户。
     *
     * <p>业务规则：
     * <ul>
     *     <li>客户必须存在</li>
     *     <li>产品必须存在</li>
     *     <li>产品销售状态必须为 ON_SALE</li>
     *     <li>若为一类户产品，则客户不能重复开立激活状态的一类户</li>
     *     <li>新开账户默认余额为 0，状态为 ACTIVE</li>
     * </ul>
     *
     * @param request 开户请求
     * @return 开户结果
     */
    @Override
    @Transactional
    public OpenAccountResponse openAccount(OpenAccountRequest request) {
        // 1. 校验客户是否存在
        CustomerEntity customer = customerRepository.findByCustomerId(request.getCustomerId());
        if (customer == null) {
            throw new BizException("CUST404", "客户不存在");
        }

        // 2. 校验产品是否存在
        ProductEntity product = productRepository.findByProductCode(request.getProductCode());
        if (product == null) {
            throw new BizException("PROD404", "产品不存在");
        }

        // 3. 校验产品是否可售
        if (!"ON_SALE".equals(product.getSaleStatus())) {
            throw new BizException("PROD001", "产品当前不可售");
        }

        // 4. 一类户唯一性控制：
        //    同一客户只能拥有一个 ACTIVE 状态的一类户
        if ("CLASS_I".equals(product.getAccountLevel())
                && accountRepository.existsActiveClassOneAccount(customer.getCustomerId())) {
            throw new BizException("ACCT001", "该客户已存在一类户，不能重复开立");
        }

        LocalDateTime now = LocalDateTime.now();
        String accountNo = generateAccountNo();

        // 5. 组装新账户信息
        AccountEntity entity = AccountEntity.builder()
                .accountNo(accountNo)
                .customerId(customer.getCustomerId())
                .accountName(customer.getCustomerName())
                .accountType(product.getProductType())
                .productCode(product.getProductCode())
                .currency(product.getCurrency())
                .balance(BigDecimal.ZERO)
                .accountStatus("ACTIVE")
                .branchCode(request.getBranchCode())
                .openDate(now)
                .createdTime(now)
                .updatedTime(now)
                .build();

        // 6. 保存账户
        accountRepository.save(entity);

        // 7. 返回开户结果
        return OpenAccountResponse.builder()
                .accountNo(entity.getAccountNo())
                .customerId(entity.getCustomerId())
                .accountName(entity.getAccountName())
                .accountType(entity.getAccountType())
                .productCode(entity.getProductCode())
                .currency(entity.getCurrency())
                .balance(entity.getBalance())
                .accountStatus(entity.getAccountStatus())
                .branchCode(entity.getBranchCode())
                .openDate(entity.getOpenDate())
                .build();
    }

    /**
     * 查询账户详情。
     *
     * <p>查询逻辑：
     * <ol>
     *     <li>先根据账号查询账户</li>
     *     <li>再查询账户对应客户</li>
     *     <li>再查询账户对应产品</li>
     *     <li>最后聚合返回详情信息</li>
     * </ol>
     *
     * @param accountNo 账号
     * @return 账户详情
     */
    @Override
    public AccountDetailResponse getAccountDetail(String accountNo) {
        // 1. 查询账户
        AccountEntity account = accountRepository.findByAccountNo(accountNo);
        if (account == null) {
            throw new BizException("ACCT404", "账户不存在");
        }

        // 2. 查询客户
        CustomerEntity customer = customerRepository.findByCustomerId(account.getCustomerId());
        if (customer == null) {
            throw new BizException("CUST404", "账户对应客户不存在");
        }

        // 3. 查询产品
        ProductEntity product = productRepository.findByProductCode(account.getProductCode());
        if (product == null) {
            throw new BizException("PROD404", "账户对应产品不存在");
        }

        // 4. 聚合返回
        return AccountDetailResponse.builder()
                .accountNo(account.getAccountNo())
                .customerId(account.getCustomerId())
                .customerName(customer.getCustomerName())
                .accountName(account.getAccountName())
                .accountType(account.getAccountType())
                .productCode(account.getProductCode())
                .productName(product.getProductName())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .accountStatus(account.getAccountStatus())
                .branchCode(account.getBranchCode())
                .openDate(account.getOpenDate())
                .createdTime(account.getCreatedTime())
                .updatedTime(account.getUpdatedTime())
                .build();
    }

    /**
     * 生成账号。
     *
     * <p>规则说明：
     * <ul>
     *     <li>前缀：62</li>
     *     <li>时间：yyyyMMddHHmmss</li>
     *     <li>随机数：6位</li>
     * </ul>
     *
     * <p>该实现适用于 POC 演示，不适用于生产级全局唯一账号生成。
     *
     * @return 账号
     */
    private String generateAccountNo() {
        return "62"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + ThreadLocalRandom.current().nextInt(100000, 999999);
    }
}
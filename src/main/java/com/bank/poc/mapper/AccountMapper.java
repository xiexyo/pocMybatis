package com.bank.poc.mapper;

import com.bank.poc.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {

    int insert(AccountEntity entity);

    AccountEntity selectByAccountNo(@Param("accountNo") String accountNo);

    List<AccountEntity> selectByCustomerId(@Param("customerId") String customerId);

    List<AccountEntity> selectByCustomerIdAndAccountType(@Param("customerId") String customerId,
                                                         @Param("accountType") String accountType);

    List<AccountEntity> selectByCustomerIdAndAccountStatus(@Param("customerId") String customerId,
                                                           @Param("accountStatus") String accountStatus);
}
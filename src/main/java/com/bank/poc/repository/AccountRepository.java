//package com.bank.poc.repository;
//
//import com.bank.poc.entity.AccountEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface AccountRepository extends JpaRepository<AccountEntity, String> {
//
//    List<AccountEntity> findByCustomerId(String customerId);
//
//    List<AccountEntity> findByCustomerIdAndAccountType(String customerId, String accountType);
//
//    List<AccountEntity> findByCustomerIdAndAccountStatus(String customerId, String accountStatus);
//}
//package com.bank.poc.repository;
//
//import com.bank.poc.entity.TransactionEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
//
//    Optional<TransactionEntity> findByRequestId(String requestId);
//
//    List<TransactionEntity> findByDebitAccountNoOrCreditAccountNo(String debitAccountNo, String creditAccountNo);
//
//    List<TransactionEntity> findByTxnTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
//}
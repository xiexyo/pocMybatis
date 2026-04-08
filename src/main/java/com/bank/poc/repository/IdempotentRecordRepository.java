//package com.bank.poc.repository;
//
//import com.bank.poc.entity.IdempotentRecordEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface IdempotentRecordRepository extends JpaRepository<IdempotentRecordEntity, String> {
//
//    List<IdempotentRecordEntity> findByBusinessType(String businessType);
//}
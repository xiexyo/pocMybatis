//package com.bank.poc.repository;
//
//import com.bank.poc.entity.CustomerEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
//
//    Optional<CustomerEntity> findByCertNo(String certNo);
//
//    boolean existsByCertNo(String certNo);
//}
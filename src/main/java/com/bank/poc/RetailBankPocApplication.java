package com.bank.poc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bank.poc.mapper")
public class RetailBankPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailBankPocApplication.class, args);
    }
}
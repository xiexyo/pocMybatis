package com.bank.poc;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
@MapperScan("com.bank.poc.mapper")
public class RetailBankPocApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RetailBankPocApplication.class, args);

        String port = context.getEnvironment().getProperty("server.port", "8080");
        String applicationName = context.getEnvironment().getProperty("spring.application.name", "application");

        log.info("==============================================");
        log.info("应用启动成功");
        log.info("应用名称: {}", applicationName);
        log.info("本地访问: http://localhost:{}", port);
        log.info("健康检查: http://localhost:{}/health", port);
        log.info("Swagger : http://localhost:{}/swagger-ui.html", port);
        log.info("H2控制台: http://localhost:{}/h2-console", port);
        log.info("==============================================");
    }
}
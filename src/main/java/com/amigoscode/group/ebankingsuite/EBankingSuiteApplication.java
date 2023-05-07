package com.amigoscode.group.ebankingsuite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EBankingSuiteApplication {
    public static void main(String[] args) {
        SpringApplication.run(EBankingSuiteApplication.class, args);
    }

}

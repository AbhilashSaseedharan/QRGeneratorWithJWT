package org.example.myspringapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@EnableTransactionManagement
@SpringBootApplication
public class MyQRGeneratorApp {

    public static void main(String[] args) {
        SpringApplication.run(MyQRGeneratorApp.class, args);
    }

}

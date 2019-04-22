package com.gmail.rebel249.controller.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.gmail.rebel249.servicemodule",
        "com.gmail.rebel249.datamodule",
        "com.gmail.rebel249.controller"})
public class SpringBootModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModuleApplication.class, args);
    }
}

package com.springboot.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GoogleRecaptchaV3IntegrationAnnotationApp {
    public static void main(String[] args) {
        SpringApplication.run(GoogleRecaptchaV3IntegrationAnnotationApp.class, args);
    }
}
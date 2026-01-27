package com.springboot.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableJpaRepositories
@SpringBootApplication
public class SpringSecurityCodeGrantOidcFeIntegration {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityCodeGrantOidcFeIntegration.class, args);
    }
}

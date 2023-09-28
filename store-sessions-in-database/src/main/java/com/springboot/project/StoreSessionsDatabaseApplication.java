package com.springboot.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@EnableJdbcHttpSession
@SpringBootApplication
public class StoreSessionsDatabaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreSessionsDatabaseApplication.class, args);
    }
}
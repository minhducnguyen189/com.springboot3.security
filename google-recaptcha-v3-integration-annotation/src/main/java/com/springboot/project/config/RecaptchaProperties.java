package com.springboot.project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "google.recaptcha")
public class RecaptchaProperties {
    private String domain;
    private String verifyApi;
    private String secret;
}

package com.springboot.project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperty {

    private Security security;
    private String deploymentBaseUrl;

    @Getter
    @Setter
    public static class Security {
        private List<String> redirectUrls;
        private String tokenSecret;
        private Integer tokenExpirationMsec;
    }

}

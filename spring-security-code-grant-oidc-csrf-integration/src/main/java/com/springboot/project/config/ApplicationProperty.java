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
    private FrontEnd frontEnd;

    @Getter
    @Setter
    public static class Security {
        private String[] allowedApis;
        private List<String> redirectUrls;
        private String tokenSecret;
        private String tokenExpirationDuration;
        private List<String> keycloakIdTokenSpecialClaims;
        private String providerLogoutUri;
        private String logoutApiPath;
        private String loginApiPath;
        private String postLogoutRedirectUri;
    }

    @Getter
    @Setter
    public static class FrontEnd {
        private String domain;
    }

}

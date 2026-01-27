package com.springboot.project.config;

import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperty {

    /** contain all custom security configurations for oidc */
    private final Security security;

    /** deployment base url of spring boot application. Ex: example.com */
    private final String deploymentBaseUrl;

    public ApplicationProperty(
            @DefaultValue Security security,
            @DefaultValue("http://localhost:7070") String deploymentBaseUrl) {
        this.security = security;
        this.deploymentBaseUrl = deploymentBaseUrl;
    }

    @Getter
    public static class Security {
        /** white list apis */
        private final String[] allowedApis;

        /** redirect apis */
        private final List<String> redirectUrls;

        /** token secret for signing Jwt */
        private final String tokenSecret;

        /** expiration time for the access token */
        private final String tokenExpirationDuration;

        /** specific claims need to get from access token */
        private final List<String> keycloakIdTokenSpecialClaims;

        /** keycloak provider logout uri */
        private final String providerLogoutUri;

        /** logout api path */
        private final String logoutApiPath;

        /** redirect url after logging out */
        private final String postLogoutRedirectUri;

        public Security(
                @DefaultValue String[] allowedApis,
                @DefaultValue List<String> redirectUrls,
                @DefaultValue String tokenSecret,
                @DefaultValue String tokenExpirationDuration,
                @DefaultValue List<String> keycloakIdTokenSpecialClaims,
                @DefaultValue String providerLogoutUri,
                @DefaultValue String logoutApiPath,
                @DefaultValue String postLogoutRedirectUri) {
            this.allowedApis = allowedApis;
            this.redirectUrls = redirectUrls;
            this.tokenSecret = tokenSecret;
            this.tokenExpirationDuration = tokenExpirationDuration;
            this.keycloakIdTokenSpecialClaims = keycloakIdTokenSpecialClaims;
            this.providerLogoutUri = providerLogoutUri;
            this.logoutApiPath = logoutApiPath;
            this.postLogoutRedirectUri = postLogoutRedirectUri;
        }
    }
}

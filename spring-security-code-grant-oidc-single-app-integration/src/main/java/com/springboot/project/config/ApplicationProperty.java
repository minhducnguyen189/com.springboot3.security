package com.springboot.project.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperty {

    private Security security;
    private String deploymentBaseUrl;
    private BackEnd backEnd;
    private FrontEnd frontEnd;

    @Getter
    @Setter
    public static class Security {
        private String[] allowedApis;
        private List<String> redirectUrls;
        private String clientId;
        private String tokenSecret;
        private String tokenExpirationDuration;
        private List<String> keycloakIdTokenIgnoredClaims;
        private String providerLogoutUri;
        private String logoutApiPath;
        private String loginApiPath;
        private String refreshTokenApiPath;
        private String postLogoutRedirectUri;
    }

    @Getter
    @Setter
    public static class BackEnd {
        private String apiBasePath;
        private String serverBasePath;
    }

    @Getter
    @Setter
    public static class FrontEnd {
        private AngularAppConfig privateAngularApp;
        private AngularAppConfig publicAngularApp;
    }

    @Getter
    @Setter
    public static class AngularAppConfig {
        private String resourceHandlerPath;
        private String[] resourceLocations;
        private String viewController;
        private String homePage;
    }
}

package com.springboot.project.controller;

import com.springboot.project.config.ApplicationProperty;
import com.springboot.project.generated.api.AuthenticationApi;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
@RestController
public class AuthenticationController implements AuthenticationApi {

    private final ApplicationProperty applicationProperty;

    @Autowired
    public AuthenticationController(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    @Override
    public ResponseEntity<Void> authenticationLogin(String redirectPath) {
        String uiDomain = this.applicationProperty.getDeploymentBaseUrl();
        String redirectUri =
                uiDomain
                        + URI.create(
                                Optional.ofNullable(redirectPath)
                                        .orElse("/pages/private-app/dashboard"));
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUri)).build();
    }

    @Override
    public ResponseEntity<Void> refreshToken() {
        String uiDomain = this.applicationProperty.getDeploymentBaseUrl();
        String redirectUri =
                URI.create(
                                uiDomain
                                        + this.applicationProperty
                                                .getFrontEnd()
                                                .getPrivateAngularApp()
                                                .getViewController()
                                        + "/token-refresh/success")
                        .toString();
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUri)).build();
    }
}

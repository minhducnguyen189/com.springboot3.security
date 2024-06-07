package com.springboot.project.controller;

import com.springboot.project.config.ApplicationProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
public class AuthenticationController {

    private final ApplicationProperty applicationProperty;

    @Autowired
    public AuthenticationController(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/v1/authentication/login")
    public ResponseEntity<Void> authentication(String redirectPath) {
        String uiDomain = this.applicationProperty.getFrontEnd().getDomain();
        String redirectUri = uiDomain + URI.create(Optional.ofNullable(redirectPath)
                .orElse("/pages/dashboard"));
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUri)
                .build();
    }

}

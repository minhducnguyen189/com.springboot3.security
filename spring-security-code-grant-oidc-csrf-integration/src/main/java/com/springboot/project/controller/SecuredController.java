package com.springboot.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, path = "/v1/secure/messages/admin")
    public ResponseEntity<String> getAdminMessage() {
        return ResponseEntity.ok("Secured Message From Admin Api!");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @RequestMapping(method = RequestMethod.POST, path = "/v1/secure/messages/user")
    public ResponseEntity<String> getUserMessage() {
        return ResponseEntity.ok("Secured Message From User Api!");
    }

}

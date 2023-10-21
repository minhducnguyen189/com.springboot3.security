package com.springboot.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @RequestMapping(method = RequestMethod.GET, path = "/v1/public/messages")
    public ResponseEntity<String> getMessage() {
        return ResponseEntity.ok("Public Message!");
    }

}

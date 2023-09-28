package com.springboot.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @RequestMapping(method = RequestMethod.GET, path = "/v1/messages")
    public ResponseEntity<String> getMessage() {
        return ResponseEntity.ok("Hello World!");
    }

}

package com.springboot.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/v1/public/data",
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getPublicData() {
        return new ResponseEntity<>("This is public message", HttpStatus.OK);
    }
}

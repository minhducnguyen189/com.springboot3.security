package com.springboot.project.controller;

import com.springboot.project.model.KeyPairResponse;
import com.springboot.project.service.KeyPairGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyPairController {

    private final KeyPairGeneratorService keyPairGeneratorService;

    @Autowired
    public KeyPairController(KeyPairGeneratorService keyPairGeneratorService) {
        this.keyPairGeneratorService = keyPairGeneratorService;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/v1/asymmetric-encryption/actions/generate-keypair",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KeyPairResponse> getKeyPair() {
        return new ResponseEntity<>(
                this.keyPairGeneratorService.generateKeyPair(), HttpStatus.CREATED);
    }
}

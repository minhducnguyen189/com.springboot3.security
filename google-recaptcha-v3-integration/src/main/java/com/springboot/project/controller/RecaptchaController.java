package com.springboot.project.controller;

import com.springboot.project.api.RecaptchaResponse;
import com.springboot.project.model.RecaptchaTokenRequest;
import com.springboot.project.service.RecaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecaptchaController {

    private final RecaptchaService recaptchaService;

    @Autowired
    public RecaptchaController(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/v1/recaptcha/actions/verify",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecaptchaResponse> verifyRecaptchaToken(
            @RequestBody RecaptchaTokenRequest recaptchaTokenRequest) {
        RecaptchaResponse recaptchaResponse =
                this.recaptchaService.verifyRecaptcha(recaptchaTokenRequest.getToken());
        return new ResponseEntity<>(recaptchaResponse, HttpStatus.OK);
    }
}

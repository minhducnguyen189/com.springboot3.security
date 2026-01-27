package com.springboot.project.service;

import com.springboot.project.api.GoogleRecaptchaApi;
import com.springboot.project.api.RecaptchaResponse;
import com.springboot.project.api.RecaptchaValidationRequest;
import com.springboot.project.config.RecaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecaptchaService {

    private final GoogleRecaptchaApi googleRecaptchaApi;
    private final RecaptchaProperties recaptchaProperties;

    @Autowired
    public RecaptchaService(
            GoogleRecaptchaApi googleRecaptchaApi, RecaptchaProperties recaptchaProperties) {
        this.googleRecaptchaApi = googleRecaptchaApi;
        this.recaptchaProperties = recaptchaProperties;
    }

    public Boolean verifyRecaptcha(String recaptchaToken) {
        RecaptchaValidationRequest recaptchaValidationRequest = new RecaptchaValidationRequest();
        recaptchaValidationRequest.setSecret(recaptchaProperties.getSecret());
        recaptchaValidationRequest.setResponse(recaptchaToken);
        RecaptchaResponse recaptchaResponse =
                this.googleRecaptchaApi.verifyToken(recaptchaValidationRequest);
        return recaptchaResponse.getSuccess();
    }
}

package com.springboot.project.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "google-recaptcha-api", url = "${google.recaptcha.domain}")
public interface GoogleRecaptchaApi {

    @RequestMapping(
            method = RequestMethod.POST,
            path = "${google.recaptcha.verify-api}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    RecaptchaResponse verifyToken(RecaptchaValidationRequest recaptchaValidationRequest);
}

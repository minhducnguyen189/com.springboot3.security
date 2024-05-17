package com.springboot.project.api;

import feign.form.FormProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecaptchaValidationRequest {
    @FormProperty("secret")
    private String secret;

    @FormProperty("response")
    private String response;

    @FormProperty("remoteip")
    private String remoteIp;

}

package com.springboot.project.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecaptchaValidationRequest {

    private String secret;
    private String response;
    private String remoteIp;

}

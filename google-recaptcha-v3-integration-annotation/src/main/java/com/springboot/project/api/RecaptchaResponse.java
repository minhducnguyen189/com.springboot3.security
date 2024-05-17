package com.springboot.project.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecaptchaResponse {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("challege_ts")
    private String challegeTs;

    @JsonProperty("hostname")
    private String hostName;

    @JsonProperty("score")
    private Double score;

    @JsonProperty("action")
    private String action;

    @JsonProperty("errorCodes")
    private List<String> errorCodes;

}

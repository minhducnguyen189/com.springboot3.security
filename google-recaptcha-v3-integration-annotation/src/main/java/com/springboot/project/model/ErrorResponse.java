package com.springboot.project.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private int errorCode;
    private String status;
    private String api;

}

package com.springboot.project.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private int errorCode;
    private String status;
    private String api;
}

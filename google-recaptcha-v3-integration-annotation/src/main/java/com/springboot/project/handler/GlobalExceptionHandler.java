package com.springboot.project.handler;

import com.springboot.project.exception.UnAuthorizationException;
import com.springboot.project.model.ErrorResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UnAuthorizationException.class})
    public ResponseEntity<ErrorResponse> handleUnAuthorizationException(
            UnAuthorizationException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.FORBIDDEN.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setApi(request.getDescription(false));
        errorResponse.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}

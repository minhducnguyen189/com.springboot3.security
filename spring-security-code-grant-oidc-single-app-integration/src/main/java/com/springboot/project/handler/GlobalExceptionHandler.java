package com.springboot.project.handler;

import com.springboot.project.exception.ResourceNotFoundException;
import com.springboot.project.generated.model.ErrorResponseModel;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ErrorResponseModel handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        return new ErrorResponseModel()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .path(request.getDescription(false));
    }

    @ExceptionHandler(value = {Exception.class})
    public ErrorResponseModel handleException(ResourceNotFoundException ex, WebRequest request) {
        return new ErrorResponseModel()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(request.getDescription(false));
    }
}

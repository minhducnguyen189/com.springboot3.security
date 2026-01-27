package com.springboot.project.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

    @Serial private static final long serialVersionUID = -2064309544544585391L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

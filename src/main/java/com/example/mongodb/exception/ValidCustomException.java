package com.example.mongodb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidCustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private int code;

    public ValidCustomException(String message) {
        super(message);
    }

    public ValidCustomException(String message, Exception exception) {
        super(message, exception);
    }

    public ValidCustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

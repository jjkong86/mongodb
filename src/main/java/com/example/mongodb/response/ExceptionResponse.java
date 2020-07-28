package com.example.mongodb.response;

import lombok.*;

@Getter
@ToString
public class ExceptionResponse {
    int code;
    String error;
    
    @Builder
    public ExceptionResponse(int code, String error) {
    	this.code = code;
    	this.error = error;
    }
}

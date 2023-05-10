package com.bloom.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse {
    private int statusCode;
    private HttpStatus error;
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.statusCode = status.value();
        this.error = status;
        this.message = message;
    }
}

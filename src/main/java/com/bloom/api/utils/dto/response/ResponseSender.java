package com.bloom.api.utils.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ResponseSender {
    private String message;
    private int statusCode;
    private HttpStatus status;

    public ResponseSender(HttpStatus status, String message) {
        this.message = message;
        this.statusCode = status.value();
        this.status = status;
    }
}

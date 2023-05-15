package com.bloom.api.utils.dto.response;

import org.springframework.http.HttpStatus;

public class ResponseHandler {
    public static ResponseSender generateResponse(HttpStatus status, String message) {
        return new ResponseSender(status, message);
    }

    public static ResponseSender ok(String message) {
        return generateResponse(HttpStatus.OK, message);
    }
}

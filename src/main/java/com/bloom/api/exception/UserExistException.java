package com.bloom.api.exception;

public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(message);
    }
}

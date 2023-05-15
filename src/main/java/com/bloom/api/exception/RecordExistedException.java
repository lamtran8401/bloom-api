package com.bloom.api.exception;

public class RecordExistedException extends RuntimeException {
    public RecordExistedException(String message) {
        super(message);
    }
}

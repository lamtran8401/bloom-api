package com.bloom.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e, WebRequest req) {
        return new ErrorResponse(HttpStatusCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserExistException(UserExistException e, WebRequest req) {
        return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAcceptException(UnauthorizedException e, WebRequest req) {
        return new ErrorResponse(HttpStatusCode.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest req) {
        return new ErrorResponse(HttpStatusCode.UNAUTHORIZED, e.getMessage());
    }
}

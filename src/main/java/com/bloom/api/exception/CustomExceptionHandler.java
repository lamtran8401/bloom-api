package com.bloom.api.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserExistException(UserExistException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAcceptException(UnauthorizedException e) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleJwtException(JwtException e) {
        String msg;
        if (e instanceof ExpiredJwtException)
            msg = "Token expired.";
        else if (e instanceof MalformedJwtException)
            msg = "Token is not well formed.";
        else if (e instanceof UnsupportedJwtException)
            msg = "Token is not supported.";
        else if (e instanceof SignatureException)
            msg = "Token signature is invalid.";
        else
            msg = "Invalid token.";
        return new ErrorResponse(HttpStatus.UNAUTHORIZED, msg);
    }

}

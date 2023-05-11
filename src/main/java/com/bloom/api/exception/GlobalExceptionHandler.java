package com.bloom.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class GlobalExceptionHandler {
    public void handle(HttpServletResponse res, HttpStatus status, String message) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
            "error", status,
            "message", message,
            "statusCode", status.value()
        )));
    }

    public void accessDenied(HttpServletResponse res, String message) throws IOException {
        handle(res, HttpStatus.FORBIDDEN, message);
    }
}

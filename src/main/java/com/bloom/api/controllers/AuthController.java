package com.bloom.api.controllers;

import com.bloom.api.exception.RecordNotFoundException;
import com.bloom.api.services.AuthService;
import com.bloom.api.utils.dto.request.AuthenticationRequest;
import com.bloom.api.utils.dto.request.RefreshTokenRequest;
import com.bloom.api.utils.dto.request.RegistrationRequest;
import com.bloom.api.utils.dto.response.AuthenticationResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final HttpServletRequest request;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegistrationRequest req,
        HttpServletResponse response
    ) {
        AuthenticationResponse res = authService.register(req);
        setCookies(response, res);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
        @RequestBody AuthenticationRequest req,
        HttpServletResponse response
    ) {
        AuthenticationResponse res = authService.authenticate(req);
        setCookies(response, res);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        clearCookies(response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RefreshTokenRequest req) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0)
            throw new RecordNotFoundException("Refresh token not found in cookie.");

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                AuthenticationResponse res = authService.refresh(req.getUserId(), cookie.getValue());
                setCookies(response, res);
                return ResponseEntity.ok(res);
            }
        }
        throw new RecordNotFoundException("Refresh token not found in cookie.");
    }

    private void setCookies(HttpServletResponse response, AuthenticationResponse res) {
        ResponseCookie accessToken = ResponseCookie
            .from("accessToken", res.getAccessToken())
            .httpOnly(true)
            .path("/")
            .secure(isSecureRequest())
            .domain(getCookieDomain())
            .maxAge(3 * 60)
            .build();
        response.addHeader("Set-Cookie", accessToken.toString());

        ResponseCookie refreshToken = ResponseCookie
            .from("refreshToken", res.getRefreshToken())
            .httpOnly(true)
            .path("/")
            .secure(isSecureRequest())
            .domain(getCookieDomain())
            .maxAge(30 * 24 * 60 * 60)
            .build();
        response.addHeader("Set-Cookie", refreshToken.toString());
    }

    private void clearCookies(HttpServletResponse response) {
        ResponseCookie accessToken = ResponseCookie
            .from("accessToken", "")
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .build();
        response.addHeader("Set-Cookie", accessToken.toString());

        ResponseCookie refreshToken = ResponseCookie
            .from("refreshToken", "")
            .httpOnly(true)
            .path("/")
            .secure(isSecureRequest())
            .domain(getCookieDomain())
            .maxAge(0)
            .build();
        response.addHeader("Set-Cookie", refreshToken.toString());
    }

    private boolean isSecureRequest() {
        String origin = request.getHeader("Origin");
        if (origin == null) {
            return false;
        }
        return origin.startsWith("https");
    }

    private String getCookieDomain() {
        String origin = request.getHeader("Origin");
        if (origin == null) {
            return "localhost";
        } else if (origin.contains("localhost")) {
            return "localhost";
        } else {
            return "bloom-beta.vercel.app";
        }
    }
}

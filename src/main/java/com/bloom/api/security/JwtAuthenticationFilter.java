package com.bloom.api.security;

import com.bloom.api.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest req,
        @NonNull HttpServletResponse res,
        @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token = jwtService.getTokenFromRequest(req);

        if (token == null) {
            filterChain.doFilter(req, res);
            return;
        }

        String email;
        try {
            email = jwtService.extractEmail(token);
        } catch (ExpiredJwtException e) {
            logger.warn(e.getMessage());
            handleJwtException(res, e.getMessage(), HttpStatus.BAD_REQUEST);
            return;
        } catch (UnsupportedJwtException e) {
            logger.warn(e.getMessage());
            handleJwtException(res, e.getMessage(), HttpStatus.UNAUTHORIZED);
            return;
        } catch (SignatureException e) {
            logger.warn(e.getMessage());
            handleJwtException(res, e.getMessage(), HttpStatus.UNAUTHORIZED);
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = loadUserByEmail(email);

            if (userDetails == null) {
                handleJwtException(res, "User not found with this token.", HttpStatus.UNAUTHORIZED);
                return;
            }

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(req, res);
    }

    private UserDetails loadUserByEmail(String email) {
        try {
            return userDetailsService.loadUserByUsername(email);
        } catch (UnauthorizedException e) {
            logger.warn(e.getMessage(), email);
            return null;
        }
    }

    private void handleJwtException(HttpServletResponse res, String message, HttpStatus status) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
            "error", status,
            "message", message,
            "statusCode", status.value()
        )));
    }
}

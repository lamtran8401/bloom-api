package com.bloom.api.config;

import com.bloom.api.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

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

        final String email;
        try {
            email = jwtService.extractEmail(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Token expired.");
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException("Invalid token.");
        } catch (SignatureException e) {
            throw new UnauthorizedException("Invalid signature token.");
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token.");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = userDetailsService.loadUserByUsername(email);
            } catch (UsernameNotFoundException e) {
                throw new UnauthorizedException("Invalid token.");
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
}

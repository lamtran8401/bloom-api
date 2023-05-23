package com.bloom.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.expiration-time}")
    private long EXPIRATION_TIME;
    @Value("${jwt.refresh-key}")
    private String REFRESH_KEY;
    @Value("${jwt.refresh-expiration-time}")
    private long REFRESH_EXPIRATION_TIME;

    public String getTokenFromRequest(HttpServletRequest req) {
        final String authorizationHeader = req.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.substring(7);
    }

    private Key getSigningKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getAccessKey() {
        return getSigningKey(SECRET_KEY);
    }

    private Key getRefreshKey() {
        return getSigningKey(REFRESH_KEY);
    }

    public String generateToken(
        Key key,
        long expirationTime,
        Map<String, Object> extraClaims,
        UserDetails userDetails) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(getAccessKey(), EXPIRATION_TIME, new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(getRefreshKey(), REFRESH_EXPIRATION_TIME, new HashMap<>(), userDetails);
    }

    public String extractEmail(String token, Key key) {
        return extractClaim(token, key, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Key key, Function<Claims, T> claimsResolver)
        throws ExpiredJwtException, UnsupportedJwtException, SignatureException {
        final Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, Key key)
        throws ExpiredJwtException, UnsupportedJwtException, SignatureException {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String extractEmailFromRefreshToken(String token) {
        System.out.println();
        return extractEmail(token, getRefreshKey());
    }

    public String extractEmailFromAccessToken(String token) {
        return extractEmail(token, getAccessKey());
    }


    private boolean isAccessTokenExpired(String token) {
        return isTokenExpired(token, getAccessKey());
    }

    private boolean isRefreshTokenExpired(String token) {
        return isTokenExpired(token, getRefreshKey());
    }

    private boolean isTokenExpired(String token, Key key) {
        return extractExpiration(token, key).before(new Date());
    }

    private Date extractExpiration(String token, Key key) {
        return extractClaim(token, key, Claims::getExpiration);
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmailFromAccessToken(token);
        return email.equals(userDetails.getUsername()) && !isAccessTokenExpired(token);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmailFromRefreshToken(token);
        return email.equals(userDetails.getUsername()) && !isRefreshTokenExpired(token);
    }


}

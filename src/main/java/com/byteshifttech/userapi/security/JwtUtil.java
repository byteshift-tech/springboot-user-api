package com.byteshifttech.userapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for generating and validating JWT tokens.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-in-ms:3600000}")
    private long jwtExpirationInMs;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Generate JWT token for a given username (email).
     *
     * @param username the user's email
     * @return signed JWT token
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extract username (email) from token.
     *
     * @param token the JWT token
     * @return extracted username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validate if token is valid and belongs to a user.
     *
     * @param token    the JWT token
     * @param username the expected username
     * @return true if valid
     */
    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return tokenUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}

package com.cairin.util;

import com.cairin.config.SecurityConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final String secretKey;
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final int expiration;


    @Autowired
    public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") int expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
    }


//    @PostConstruct
//    public void initialize() {
//        // Initialize the SecretKey here using the secretKey property
//    }

    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        claims.put("iat", new Date());
        claims.put("exp", new Date(System.currentTimeMillis() + expiration * 1000));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }


    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }

}

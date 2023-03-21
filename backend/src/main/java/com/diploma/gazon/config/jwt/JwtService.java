package com.diploma.gazon.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final Integer TOKEN_EXPIRATION_TIME = 20 * 60 * 1000 ; // 20M
    @Value("${encryption.key}")
    private String key;

    public String generateToken(UserDetails userDetails) {
        return generateToken(Collections.emptyMap(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        Date msNow = new Date(System.currentTimeMillis());
        Date msExpiration = new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME);

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(msNow)
                .setExpiration(msExpiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String username = decodeUsername(token);
        return isTokenNonExpired(token) && username.equals(userDetails.getUsername());
    }

    private Boolean isTokenNonExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token) {
        return decodeClaim(token, Claims::getExpiration);
    }

    public String decodeUsername(String token) {
        return decodeClaim(token, Claims::getSubject);
    }

    private <T> T decodeClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(decodeClaimList(token));
    }

    private Claims decodeClaimList(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] decodedKey = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}

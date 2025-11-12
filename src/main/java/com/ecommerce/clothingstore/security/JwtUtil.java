package com.ecommerce.clothingstore.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtil {

//	  private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	    @Value("${jwt.secret}")
	    private String secret;  
	    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

	    private SecretKey getKey() {
	        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
	        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
	    }

	    
	    public String generateToken(String username) {
	        return Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith( getKey(), SignatureAlgorithm.HS512)
	                .compact();
	    }

	    public boolean isTokenExpired(String token) {
	        return extractClaims(token).getExpiration().before(new Date());
	    }
	    
	    private Claims extractClaims(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(getKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }
	    
	    public String extractUsername(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey( getKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	    }

	    public boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }


}

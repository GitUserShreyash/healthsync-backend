package com.shreyash.demo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTUtilizer {
	// Convert Base64 string -> byte[] -> SecretKey
		private final SecretKey key;

	    public JWTUtilizer(@Value("${jwt.secret}") String secret) {
	        this.key = Keys.hmacShaKeyFor(
	                Decoders.BASE64.decode(secret));
	    }
	    
	    public String generateJWTToken(String username, String role) {

	        Map<String, Object> claims = new HashMap<>();

	        claims.put("username", username);
	        claims.put("role", role);

	        return Jwts.builder()//start building JWT
	                .claims(claims)//add custom info
	                .subject(username)//general field for identifying the token
	                .issuedAt(new Date())//when the token was created 
	                .expiration(
	                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)//expiration of token i.e after 2 hours
	                )
	                .signWith(key)//secures our token with our security key 
	                .compact();// return token as String
	    }
	    
	    public Map<String, String> validateToken(String token) {

	        Map<String, String> res = new HashMap<>();

	        try {

	            Claims claims = Jwts.parser()
	                    .verifyWith(key)
	                    .build()
	                    .parseSignedClaims(token)
	                    .getPayload();

	            res.put("username", claims.get("username", String.class));
	            res.put("role", claims.get("role", String.class));
	            res.put("code", "200");

	        }
	        catch (ExpiredJwtException e) {

	            res.put("code", "401");
	            res.put("error", "Token expired. Please login again");

	        }
	        catch (JwtException e) {

	            res.put("code", "403");
	            res.put("error", "Invalid token");

	        }
	        catch (Exception e) {

	            res.put("code", "500");
	            res.put("error", e.getMessage());

	        }

	        return res;
	    }
}

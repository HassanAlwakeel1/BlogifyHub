package com.BlogifyHub.service.impl;

import com.BlogifyHub.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


import java.util.HashMap;

@Service
public class JWTServiceImpl implements JWTService {
    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSiginKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> extraClaims,
                                       UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSiginKey(),SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getSiginKey() {
        byte[] key = Decoders.BASE64.decode("Y2hvaWNlZmlnaHRpbmdwcm9iYWJseXJheXNkcmllZGFsc290ZWFtbGl0dGxlYXZvaWQ");
        return Keys.hmacShaKeyFor(key);
    }

    private <T> T extractClaims(String token , Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSiginKey()).build().parseClaimsJws(token)
                .getBody();
    }
    public String extractUserName(String token){
        return extractClaims(token , Claims ::getSubject);
    }


    public boolean isTokenValid(String token , UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private boolean isTokenExpired(String token) {
        return extractClaims(token , Claims ::getExpiration).before(new Date());
    }


}



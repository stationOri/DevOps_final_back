package org.example.oristationbackend.securiity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    public static String createJwt(String userName, Object object,String secretKey, Long expiredMs){
//        Claims claims = Jwts.claims();
//        claims.put("userName", userName);

        return Jwts.builder()
                .claim("userName", userName)
                .claim("object",object)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
package com.ecommerce.ecommerce.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtils {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    @Value("${spring.app.jwtExceptionMs}")
    private int jwtExceptionMs;

    @Value("${spring.app.jwtSecret}")
    private static String jwtSecret;
    // Getting JWT from Header
    public static String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header : {}",bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    //Generating Token From UserName
    public String generateTokenFromUserName(UserDetails userDetails){
        String userName = userDetails.getUsername();
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExceptionMs)))
                .signWith(key())
                .compact();
    }

    //Getting UserName from Token
    public static String getUserNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    //Generating sign Key
    public static Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    //Validate JWT Token
    public static boolean validateJWTToken(String authToken){
        try{
            System.out.println("Validating");
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(authToken);
            return true;
        }catch (MalformedJwtException e){
         logger.error("Exception occured "+e.getMessage());
        }catch (ExpiredJwtException e){
            logger.error("Exception occured "+e.getMessage());
        }
        return false;
    }
}

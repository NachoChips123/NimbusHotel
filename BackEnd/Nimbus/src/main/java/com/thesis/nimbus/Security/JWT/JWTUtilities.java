package com.thesis.nimbus.Security.JWT;

import com.thesis.nimbus.Security.User.NimbusUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtilities {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtilities.class);

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int jwtExpirationTime;
    public String generateJwtTokenForUser(Authentication authentication) {
        NimbusUserDetails userPrincipal = (NimbusUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationTime))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        }
        catch (MalformedJwtException e) {
            logger.error("Invalid jwt token: {} ", e.getMessage());
        }
        catch (ExpiredJwtException e) {
            logger.error("This token is Expired: {} ", e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            logger.error("This token is Not Supported: {} ", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            logger.error("No Claims Found: {}", e.getMessage());
        }
        return false;
    }
}

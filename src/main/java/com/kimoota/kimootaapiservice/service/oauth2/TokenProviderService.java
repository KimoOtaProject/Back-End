package com.kimoota.kimootaapiservice.service.oauth2;

import com.kimoota.kimootaapiservice.config.AppProperties;
import com.kimoota.kimootaapiservice.security.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

// JWT 생성 및 인증 서비스
@Service
@RequiredArgsConstructor
public class TokenProviderService {

    private static final Logger logger = LoggerFactory.getLogger(TokenProviderService.class);
    private final AppProperties appProperties;

    public String createTokenFrom(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date end = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMs());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(end)
                .signWith(
                        SignatureAlgorithm.HS512,
                        appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public Long getUserIdFrom(String token) {

        return Long.parseLong(
                Jwts
                .parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public boolean validate(String token) {

        try {
            Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token);

            return true;

        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");

        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");

        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");

        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");

        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");

        }

        return false;
    }


}

package com.springboot.project.config.oauth2;

import com.springboot.project.config.ApplicationProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class TokenProvider {

    private final ApplicationProperty applicationProperty;

    @Autowired
    public TokenProvider(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    public String createToken(Authentication authentication) {
        DefaultOidcUser userPrincipal = (DefaultOidcUser) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + this.applicationProperty.getSecurity().getTokenExpirationMsec());

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userPrincipal.getClaims().get("email"));

        return Jwts.builder()
                .setSubject(userPrincipal.getSubject())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(
                        Keys.hmacShaKeyFor(this.applicationProperty.getSecurity().getTokenSecret().getBytes()),
                        SignatureAlgorithm.HS512)
                .compact();
    }




}

package com.springboot.project.config.oauth2;

import com.springboot.project.config.ApplicationProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class TokenProvider {

    public static String ID_TOKEN_HINT_CLAIM = "id_token_hint";
    public static String OIDC_USER_CLAIM = "oidc_user_claim";

    private final ApplicationProperty applicationProperty;

    @Autowired
    public TokenProvider(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    public String createToken(Authentication authentication) {
        Date now = new Date();
        DefaultOidcUser userPrincipal = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> idTokenClaims = userPrincipal.getClaims();
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID_TOKEN_HINT_CLAIM, userPrincipal.getIdToken().getTokenValue());
        claims.put(OIDC_USER_CLAIM, userPrincipal.getUserInfo());
        for (Map.Entry<String, Object> idTokenClaim: idTokenClaims.entrySet()) {
            if (this.applicationProperty.getSecurity().getKeycloakIdTokenIgnoredClaims().contains(idTokenClaim.getKey())) {
                continue;
            }
            claims.put(idTokenClaim.getKey(), idTokenClaim.getValue());
        }
        return Jwts.builder()
                .subject(userPrincipal.getSubject())
                .issuer("spring security code grant oidc csrf integration")
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + this.getExpiredTime()))
                .signWith(
                        Keys.hmacShaKeyFor(this.applicationProperty.getSecurity().getTokenSecret().getBytes()),
                        Jwts.SIG.HS512)
                .compact();
    }

    public Claims verifyAndGetClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(this.applicationProperty.getSecurity().getTokenSecret().getBytes()))
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public int getExpiredTime() {
        Duration duration = Duration.parse(this.applicationProperty.getSecurity().getTokenExpirationDuration());
        return duration.toHoursPart() * 60 * 60 * 1000;
    }




}

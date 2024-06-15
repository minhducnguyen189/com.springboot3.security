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

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class TokenProvider {

    public static String ID_TOKEN_HINT_CLAIM = "id_token_hint";

    private final ApplicationProperty applicationProperty;

    @Autowired
    public TokenProvider(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    public String createToken(Authentication authentication) {
        DefaultOidcUser userPrincipal = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> idTokenClaims = userPrincipal.getClaims();
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID_TOKEN_HINT_CLAIM, userPrincipal.getIdToken().getTokenValue());
        for (Map.Entry<String, Object> idTokenClaim: idTokenClaims.entrySet()) {
            if (this.applicationProperty.getSecurity().getKeycloakIdTokenSpecialClaims().contains(idTokenClaim.getKey())) {
                if (idTokenClaim.getKey().equals("iss")) {
                    String issValue = String.valueOf(idTokenClaim.getValue());
                    claims.put(idTokenClaim.getKey(), issValue);
                } else {
                    Instant instantClaim = Instant.parse(String.valueOf(idTokenClaim.getValue()));
                    claims.put(idTokenClaim.getKey(), Date.from(instantClaim));
                }
                continue;
            }
            claims.put(idTokenClaim.getKey(), idTokenClaim.getValue());
        }
        return Jwts.builder()
                .subject(userPrincipal.getSubject())
                .claims(claims)
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




}

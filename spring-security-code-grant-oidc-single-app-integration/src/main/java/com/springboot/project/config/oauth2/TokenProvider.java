package com.springboot.project.config.oauth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.project.config.ApplicationProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {

    public static String ID_TOKEN_HINT_CLAIM = "id_token_hint";
    public static String OIDC_USER_CLAIM = "oidc_user_claim";
    public static String RESOURCE_ACCESS = "resource_access";
    public static String USER_ROLES_CLAIM = "user_roles";
    public static String KEYCLOAK_ROLES_CLAIM = "roles";

    private final ApplicationProperty applicationProperty;
    private final ObjectMapper objectMapper;

    @Autowired
    public TokenProvider(ApplicationProperty applicationProperty, ObjectMapper objectMapper) {
        this.applicationProperty = applicationProperty;
        this.objectMapper = objectMapper;
    }

    public String createToken(Authentication authentication) {
        Date now = new Date();
        DefaultOidcUser userPrincipal = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> idTokenClaims = userPrincipal.getClaims();
        Map<String, Object> claims = new HashMap<>();
        Optional<List<String>> roles = this.getRoles(userPrincipal);
        claims.put(ID_TOKEN_HINT_CLAIM, userPrincipal.getIdToken().getTokenValue());
        roles.ifPresent(strings -> claims.put(USER_ROLES_CLAIM, strings));
        for (Map.Entry<String, Object> idTokenClaim : idTokenClaims.entrySet()) {
            if (this.applicationProperty
                    .getSecurity()
                    .getKeycloakIdTokenIgnoredClaims()
                    .contains(idTokenClaim.getKey())) {
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
                        Keys.hmacShaKeyFor(
                                this.applicationProperty.getSecurity().getTokenSecret().getBytes()),
                        Jwts.SIG.HS512)
                .compact();
    }

    public Claims verifyAndGetClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(
                        Keys.hmacShaKeyFor(
                                this.applicationProperty.getSecurity().getTokenSecret().getBytes()))
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public int getExpiredTime() {
        Duration duration =
                Duration.parse(this.applicationProperty.getSecurity().getTokenExpirationDuration());
        return duration.toHoursPart() * 60 * 60 * 1000;
    }

    Optional<List<String>> getRoles(DefaultOidcUser oidcUser) {
        return Optional.ofNullable(oidcUser.getClaims().get(RESOURCE_ACCESS))
                .map(this::toMap)
                .map(m -> m.get(this.applicationProperty.getSecurity().getClientId()))
                .map(this::toMap)
                .map(m -> m.get(KEYCLOAK_ROLES_CLAIM))
                .map(this::toStringList);
    }

    private Map<String, Object> toMap(Object value) {
        return this.objectMapper.convertValue(value, new TypeReference<>() {});
    }

    private List<String> toStringList(Object value) {
        return this.objectMapper.convertValue(value, new TypeReference<>() {});
    }
}

package com.springboot.project.config.oauth2;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.springboot.project.config.oauth2.OAuth2AuthenticationSuccessHandler.AUTHORIZED_TOKEN_COOKIE_NAME;
import static com.springboot.project.config.oauth2.TokenProvider.OIDC_USER_CLAIM;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Autowired
    public TokenAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);
        if (StringUtils.hasText(jwt)) {
            Claims claims = tokenProvider.verifyAndGetClaims(jwt);
            Map<String, Object> oidcUserInfo = (Map<String, Object>) claims.get(OIDC_USER_CLAIM);
            OidcUserInfo oidcUserInfoObject = new OidcUserInfo(oidcUserInfo);
            List<String> realmRoles = this.getRealmRoles(oidcUserInfoObject);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(claims, null, this.getGrantedAuthorities(realmRoles));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        Optional<Cookie> authCookie = CookieProcessor.getCookie(request, AUTHORIZED_TOKEN_COOKIE_NAME);
        return authCookie.map(Cookie::getValue).orElse(null);
    }

    private List<String> getRealmRoles(OidcUserInfo  oidcUserInfoObject) {
        Map<String, Object> oidcUserClaims = oidcUserInfoObject.getClaim("claims");
        Map<String, Object> realmAccess = (Map<String, Object>) oidcUserClaims.get("realm_access");
        return (List<String>) realmAccess.get("roles");
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> authorityEntities) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorityEntities.forEach(a -> authorities.add(new SimpleGrantedAuthority(a)));
        return authorities;
    }

}

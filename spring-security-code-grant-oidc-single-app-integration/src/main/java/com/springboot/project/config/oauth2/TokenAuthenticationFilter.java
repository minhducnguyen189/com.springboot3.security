package com.springboot.project.config.oauth2;

import static com.springboot.project.config.oauth2.OAuth2AuthenticationSuccessHandler.AUTHORIZED_TOKEN_COOKIE_NAME;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.project.entity.UserRoleEnumEntity;
import com.springboot.project.model.LoginUserModel;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Autowired
    public TokenAuthenticationFilter(TokenProvider tokenProvider, ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);
        if (StringUtils.isNotEmpty(jwt)) {
            Claims claims = this.tokenProvider.verifyAndGetClaims(jwt);
            List<String> roles =
                    Optional.ofNullable(claims.get(TokenProvider.USER_ROLES_CLAIM))
                            .map(
                                    v ->
                                            this.objectMapper.convertValue(
                                                    v, new TypeReference<List<String>>() {}))
                            .orElse(List.of());
            String email = claims.getOrDefault("email", StringUtils.EMPTY).toString();
            String name = claims.getOrDefault("name", StringUtils.EMPTY).toString();
            String username =
                    claims.getOrDefault("preferred_username", StringUtils.EMPTY).toString();

            LoginUserModel loginUserModel =
                    LoginUserModel.builder()
                            .name(name)
                            .email(email)
                            .username(username)
                            .roles(roles.stream().map(UserRoleEnumEntity::valueOf).toList())
                            .build();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            loginUserModel, null, this.getGrantedAuthorities(roles));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        Optional<Cookie> authCookie =
                CookieProcessor.getCookie(request, AUTHORIZED_TOKEN_COOKIE_NAME);
        return authCookie.map(Cookie::getValue).orElse(null);
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> authorityEntities) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorityEntities.forEach(a -> authorities.add(new SimpleGrantedAuthority(a)));
        return authorities;
    }
}

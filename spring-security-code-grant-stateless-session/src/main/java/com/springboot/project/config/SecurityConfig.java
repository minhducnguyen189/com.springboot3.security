package com.springboot.project.config;

import com.springboot.project.config.oauth2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final CustomCookieRequestCache customCookieRequestCache;
    private final OAuth2LogoutSuccessHandler oAuth2LogoutSuccessHandler;

    @Autowired
    public SecurityConfig(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                          TokenAuthenticationFilter tokenAuthenticationFilter,
                          CustomCookieRequestCache customCookieRequestCache,
                          OAuth2LogoutSuccessHandler oAuth2LogoutSuccessHandler) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.customCookieRequestCache = customCookieRequestCache;
        this.oAuth2LogoutSuccessHandler = oAuth2LogoutSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/v1/public/**", "/error")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .logout(logout -> logout
                        .logoutSuccessHandler(this.oAuth2LogoutSuccessHandler))
                .oauth2Login(oauth2 ->
                        oauth2.authorizationEndpoint(authorizationEndpointConfig ->
                                        authorizationEndpointConfig
                                                .authorizationRequestRepository(this.httpCookieOAuth2AuthorizationRequestRepository))
                                .successHandler(this.oAuth2AuthenticationSuccessHandler)
                                .failureHandler(this.oAuth2AuthenticationFailureHandler))
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.NEVER)
                                .sessionConcurrency(sessionConcurrency -> sessionConcurrency.maximumSessions(1)))
                .requestCache(cache -> cache.requestCache(this.customCookieRequestCache))
                .addFilterBefore(this.tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

}

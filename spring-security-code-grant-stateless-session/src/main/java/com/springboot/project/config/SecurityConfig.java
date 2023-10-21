package com.springboot.project.config;

import com.springboot.project.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.springboot.project.config.oauth2.OAuth2AuthenticationFailureHandler;
import com.springboot.project.config.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    public SecurityConfig(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/v1/public/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .oauth2Login(oauth2 ->
                        oauth2.authorizationEndpoint(authorizationEndpointConfig ->
                                        authorizationEndpointConfig.authorizationRequestRepository(this.httpCookieOAuth2AuthorizationRequestRepository))
                                .successHandler(this.oAuth2AuthenticationSuccessHandler)
                                .failureHandler(this.oAuth2AuthenticationFailureHandler))
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .sessionConcurrency(sessionConcurrency -> sessionConcurrency.maximumSessions(1)));

        return http.build();

    }

}

package com.springboot.project.config;

import com.springboot.project.config.oauth2.CsrfCookieFilter;
import com.springboot.project.config.oauth2.CustomCookieRequestCache;
import com.springboot.project.config.oauth2.CustomLoginUrlAuthenticationEntryPoint;
import com.springboot.project.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.springboot.project.config.oauth2.OAuth2AuthenticationFailureHandler;
import com.springboot.project.config.oauth2.OAuth2AuthenticationSuccessHandler;
import com.springboot.project.config.oauth2.OAuth2LogoutSuccessHandler;
import com.springboot.project.config.oauth2.SpaCsrfTokenRequestHandler;
import com.springboot.project.config.oauth2.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;

import static com.springboot.project.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static com.springboot.project.config.oauth2.OAuth2AuthenticationSuccessHandler.AUTHORIZED_TOKEN_COOKIE_NAME;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final CustomCookieRequestCache customCookieRequestCache;
    private final OAuth2LogoutSuccessHandler oAuth2LogoutSuccessHandler;
    private final ApplicationProperty applicationProperty;

    @Autowired
    public SecurityConfig(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                          TokenAuthenticationFilter tokenAuthenticationFilter,
                          CustomCookieRequestCache customCookieRequestCache,
                          OAuth2LogoutSuccessHandler oAuth2LogoutSuccessHandler,
                          ApplicationProperty applicationProperty) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.customCookieRequestCache = customCookieRequestCache;
        this.oAuth2LogoutSuccessHandler = oAuth2LogoutSuccessHandler;
        this.applicationProperty = applicationProperty;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf ->
                        csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(this.applicationProperty.getSecurity().getAllowedApis())
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .logout(logout -> logout
                        .logoutUrl(this.applicationProperty.getSecurity().getLogoutApiPath())
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new CacheControlHeadersWriter()))
                        .logoutSuccessHandler(this.oAuth2LogoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies(
                                AUTHORIZED_TOKEN_COOKIE_NAME,
                                OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                                OAuth2ParameterNames.REDIRECT_URI)
                )
                .oauth2Login(oauth2 ->
                        oauth2.authorizationEndpoint(authorizationEndpointConfig ->
                                        authorizationEndpointConfig
                                                .authorizationRequestRepository(this.httpCookieOAuth2AuthorizationRequestRepository))
                                .authorizationEndpoint(loginUrlAuthenticationEntryPoint -> this.getLoginUrlAuthenticationEntryPoint())
                                .successHandler(this.oAuth2AuthenticationSuccessHandler)
                                .failureHandler(this.oAuth2AuthenticationFailureHandler))
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requestCache(cache -> cache.requestCache(this.customCookieRequestCache))
                .addFilterBefore(this.tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new CsrfCookieFilter(), ExceptionTranslationFilter.class);
        return http.build();

    }

    private CustomLoginUrlAuthenticationEntryPoint getLoginUrlAuthenticationEntryPoint() {
        return new CustomLoginUrlAuthenticationEntryPoint(this.applicationProperty.getSecurity().getLoginApiPath(), this.applicationProperty);
    }

}

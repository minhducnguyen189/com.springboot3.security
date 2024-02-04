package com.springboot.project.config.oauth2;

import com.springboot.project.config.ApplicationProperty;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.time.Duration;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String AUTHORIZED_TOKEN_COOKIE_NAME = "AUTHORIZED_TOKEN";

    private final TokenProvider tokenProvider;
    private final ApplicationProperty applicationProperty;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider,
                                              ApplicationProperty applicationProperty,
                                              HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.applicationProperty = applicationProperty;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = tokenProvider.createToken(authentication);
        Duration duration = Duration.parse(this.applicationProperty.getSecurity().getTokenExpirationDuration());
        CookieProcessor.addSecureCookie(response, AUTHORIZED_TOKEN_COOKIE_NAME, token, duration.toMinutesPart() * 60);
        clearAuthenticationAttributes(request, response);
        Cookie savedRequestCookie = WebUtils.getCookie(request, "REDIRECT_URI");
        if (savedRequestCookie == null) {
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            String originalURI = CookieProcessor.deserialize(savedRequestCookie, String.class);
            getRedirectStrategy().sendRedirect(request, response, originalURI);
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}

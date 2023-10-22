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
import java.util.Base64;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REDIRECT_URI;


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
        CookieProcessor.addCookie(response, AUTHORIZED_TOKEN_COOKIE_NAME, token, this.applicationProperty.getSecurity().getTokenExpirationMsec());
        clearAuthenticationAttributes(request, response);
        Cookie savedRequestCookie = WebUtils.getCookie(request, REDIRECT_URI);
        if (savedRequestCookie == null) {
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            String originalURI = decodeCookie(savedRequestCookie.getValue());
            getRedirectStrategy().sendRedirect(request, response, originalURI);
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String decodeCookie(String encodedCookieValue) {
        return new String(Base64.getDecoder().decode(encodedCookieValue.getBytes()));
    }

}

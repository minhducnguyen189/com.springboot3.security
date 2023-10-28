package com.springboot.project.config.oauth2;

import com.springboot.project.config.ApplicationProperty;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static com.springboot.project.config.oauth2.OAuth2AuthenticationSuccessHandler.AUTHORIZED_TOKEN_COOKIE_NAME;


@Component
public class OAuth2LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final ApplicationProperty applicationProperty;
    private final TokenProvider tokenProvider;

    @Autowired
    public OAuth2LogoutSuccessHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                                      ApplicationProperty applicationProperty,
                                      TokenProvider tokenProvider) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.applicationProperty = applicationProperty;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Optional<Cookie> authCookie = CookieProcessor.getCookie(request, AUTHORIZED_TOKEN_COOKIE_NAME);
        if (authCookie.isPresent()) {
            Claims claims = this.tokenProvider.verifyAndGetClaims(authCookie.get().getValue());
            String targetUrl = UriComponentsBuilder
                    .fromUriString(this.applicationProperty.getSecurity().getLogoutRedirectUri())
                    .queryParam("post_logout_redirect_uri", "http://localhost:7070/v1/public/messages")
                    .queryParam("id_token_hint", claims.get("id_token_hint"))
                    .build().toUriString();
            request.logout();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        super.handle(request, response, authentication);
    }


}

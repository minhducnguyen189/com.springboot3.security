package com.springboot.project.config.oauth2;

import static com.springboot.project.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_COOKIE_NAME;

import com.springboot.project.entity.UserRoleEnumEntity;
import com.springboot.project.model.LoginUserModel;
import com.springboot.project.service.LoginUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String AUTHORIZED_TOKEN_COOKIE_NAME = "AUTHORIZED_TOKEN";
    public static final String XSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";
    private final TokenProvider tokenProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository
            httpCookieOAuth2AuthorizationRequestRepository;
    private final LoginUserService loginUserService;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(
            TokenProvider tokenProvider,
            HttpCookieOAuth2AuthorizationRequestRepository
                    httpCookieOAuth2AuthorizationRequestRepository,
            LoginUserService loginUserService) {
        this.tokenProvider = tokenProvider;
        this.httpCookieOAuth2AuthorizationRequestRepository =
                httpCookieOAuth2AuthorizationRequestRepository;
        this.loginUserService = loginUserService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String token = tokenProvider.createToken(authentication);
        String tokenSignature = token.substring(token.lastIndexOf("."));
        int cookieExpiredTimeInSecond = this.tokenProvider.getExpiredTime() / 1000;
        CookieProcessor.addSecureCookie(
                response, AUTHORIZED_TOKEN_COOKIE_NAME, token, cookieExpiredTimeInSecond);
        CookieProcessor.addCookie(
                response, XSRF_TOKEN_COOKIE_NAME, tokenSignature, cookieExpiredTimeInSecond, false);
        this.clearAuthenticationAttributes(request, response);
        Cookie savedRequestCookie = WebUtils.getCookie(request, REDIRECT_URI_COOKIE_NAME);
        if (savedRequestCookie == null) {
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            this.createOrUpdateLoginUser(authentication);
            String originalURI = CookieProcessor.deserialize(savedRequestCookie, String.class);
            this.httpCookieOAuth2AuthorizationRequestRepository.removeRedirectCookie(
                    request, response);
            getRedirectStrategy().sendRedirect(request, response, originalURI);
        }
    }

    protected void clearAuthenticationAttributes(
            HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(
                request, response);
    }

    private void createOrUpdateLoginUser(Authentication authentication) {
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        LoginUserModel loginUserModel =
                LoginUserModel.builder()
                        .email(oidcUser.getEmail())
                        .username(oidcUser.getPreferredUsername())
                        .name(oidcUser.getFullName())
                        .build();
        Optional<List<String>> rolesOpt = this.tokenProvider.getRoles(oidcUser);
        rolesOpt.ifPresent(
                roles -> {
                    List<UserRoleEnumEntity> userRoles =
                            roles.stream()
                                    .map(String::toUpperCase)
                                    .map(UserRoleEnumEntity::valueOf)
                                    .toList();
                    loginUserModel.setRoles(userRoles);
                    this.loginUserService.upsertLoginUser(loginUserModel);
                });
    }
}

package com.springboot.project.config.oauth2;

import com.springboot.project.config.ApplicationProperty;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.CookieRequestCache;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CustomCookieRequestCache extends CookieRequestCache {

    private static final String COOKIE_NAME = "REDIRECT_URI";
    private static final String DELIMITER = "/";
    private final ApplicationProperty applicationProperty;
    private final RequestMatcher requestMatcher;

    @Autowired
    public CustomCookieRequestCache(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
        this.requestMatcher = AnyRequestMatcher.INSTANCE;
    }

    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        String redirectPath = UrlUtils.buildRequestUrl(request);
        String authRedirectApi = this.applicationProperty.getSecurity().getLoginApiPath();
        String refreshTokenApi = this.applicationProperty.getSecurity().getRefreshTokenApiPath();
        if (redirectPath.startsWith(authRedirectApi) || redirectPath.startsWith(refreshTokenApi)) {
            String redirectUrl =
                    this.applicationProperty.getDeploymentBaseUrl().concat(redirectPath);
            Cookie savedCookie = new Cookie(COOKIE_NAME, CookieProcessor.serialize(redirectUrl));
            savedCookie.setMaxAge(-1);
            savedCookie.setSecure(request.isSecure());
            savedCookie.setPath(getCookiePath(request));
            savedCookie.setHttpOnly(true);
            response.addCookie(savedCookie);
        }
    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return StringUtils.hasLength(contextPath) ? contextPath : "/";
    }
}

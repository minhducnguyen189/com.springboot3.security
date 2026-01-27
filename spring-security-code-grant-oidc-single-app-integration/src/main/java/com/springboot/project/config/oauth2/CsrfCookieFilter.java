package com.springboot.project.config.oauth2;

import com.springboot.project.config.ApplicationProperty;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

public class CsrfCookieFilter extends OncePerRequestFilter {

    private static final List<String> SAFE_HTTP_METHODS =
            List.of("OPTIONS", "GET", "HEAD", "TRACE");
    private final ApplicationProperty applicationProperty;

    @Autowired
    public CsrfCookieFilter(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        Optional<Cookie> authCookie =
                CookieProcessor.getCookie(
                        request, OAuth2AuthenticationSuccessHandler.AUTHORIZED_TOKEN_COOKIE_NAME);
        if (authCookie.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        String authToken = authCookie.get().getValue();
        String tokenSignature = authToken.substring(authToken.lastIndexOf(".") + 1);
        Optional<Cookie> xsrfCookie =
                CookieProcessor.getCookie(
                        request, OAuth2AuthenticationSuccessHandler.XSRF_TOKEN_COOKIE_NAME);
        if (xsrfCookie.isEmpty()) {
            Duration tokenDuration =
                    Duration.parse(
                            this.applicationProperty.getSecurity().getTokenExpirationDuration());
            CookieProcessor.addCookie(
                    response,
                    OAuth2AuthenticationSuccessHandler.XSRF_TOKEN_COOKIE_NAME,
                    tokenSignature,
                    Math.toIntExact(tokenDuration.getSeconds()));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            filterChain.doFilter(request, response);
            return;
        }

        if (this.invalidXsrfToken(request, xsrfCookie.get().getValue(), tokenSignature)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        filterChain.doFilter(request, response);
    }

    private boolean invalidXsrfToken(
            HttpServletRequest request, String xsrfTokenHeaderValue, String authSignature) {
        String xsrfTokenRequestValue = request.getHeader("X-XSRF-TOKEN");
        boolean isUnsafeMethod = !SAFE_HTTP_METHODS.contains(request.getMethod());
        boolean isMissingOrInvalidToken =
                StringUtils.isBlank(xsrfTokenRequestValue)
                        || !Strings.CS.equals(xsrfTokenRequestValue, xsrfTokenHeaderValue)
                        || !Strings.CS.equals(authSignature, xsrfTokenRequestValue);
        return isUnsafeMethod && isMissingOrInvalidToken;
    }
}

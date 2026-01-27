package com.springboot.project.config.oauth2;

import com.springboot.project.config.ApplicationProperty;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import org.apache.commons.lang3.Strings;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private static final String DELIMITER = "/";

    private final ApplicationProperty applicationProperty;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomLoginUrlAuthenticationEntryPoint(
            String loginFormUrl, ApplicationProperty applicationProperty) {
        super(loginFormUrl);
        this.applicationProperty = applicationProperty;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        if (request.getRequestURI().equals(DELIMITER)) {
            String redirectUrl =
                    URI.create(
                                    this.applicationProperty
                                                    .getFrontEnd()
                                                    .getPrivateAngularApp()
                                                    .getViewController()
                                            + DELIMITER
                                            + this.applicationProperty
                                                    .getFrontEnd()
                                                    .getPrivateAngularApp()
                                                    .getHomePage())
                            .toString();
            this.redirectStrategy.sendRedirect(request, response, redirectUrl);
            return;
        }

        if (request.getRequestURI()
                        .startsWith(this.applicationProperty.getBackEnd().getApiBasePath())
                && !Strings.CI.equals(
                        request.getRequestURI(),
                        this.applicationProperty.getSecurity().getLoginApiPath())
                && !Strings.CI.equals(
                        request.getRequestURI(),
                        this.applicationProperty.getSecurity().getRefreshTokenApiPath())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        super.commence(request, response, authException);
    }
}

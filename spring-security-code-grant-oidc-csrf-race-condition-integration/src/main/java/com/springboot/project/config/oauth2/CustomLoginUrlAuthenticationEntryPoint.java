package com.springboot.project.config.oauth2;

import com.springboot.project.config.ApplicationProperty;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final ApplicationProperty applicationProperty;

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
        if (!request.getContextPath()
                .startsWith(this.applicationProperty.getSecurity().getLoginApiPath())) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        super.commence(request, response, authException);
    }
}

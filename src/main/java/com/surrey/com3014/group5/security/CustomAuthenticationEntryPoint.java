package com.surrey.com3014.group5.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * An unauthorised access will be redirected to the login URL with the HttpStatus code 401.
 *
 * @author Aung Thu Moe
 */
@Component
public class CustomAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    /**
     * Autowired the loginFormUrl
     *
     * @param loginFormUrl URL where the login page can be found.
     */
    @Autowired
    public CustomAuthenticationEntryPoint(@Value("/account/login?status=401") String loginFormUrl) {
        super(loginFormUrl);
    }

    /**
     * Performs the redirect (or forward) to the login form URL. If the request is an
     * XMLHttpRequest, append the 401 HttpStatus code in the response header.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            LOGGER.debug(response.toString());
        } else {
            super.commence(request, response, authException);
        }
    }
}

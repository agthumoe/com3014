package com.surrey.com3014.group5.security;

import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class handles failed authentication attempt.
 *
 * @author Aung Thu Moe
 */
@Component
public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFailureHandler.class);

    /**
     * Called when an authentication attempt fails. On every authentication attempt fails,
     * assign a proper HttpStatus code to the servlet response and redirect back to login page
     * with a url parameter of status=#code to be handled by the view.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LOGGER.debug("Causes: {}, message: {}", exception.getCause().getClass().getName(), exception.getCause().getMessage());
        if (exception.getCause() instanceof ResourceNotFoundException) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.sendRedirect("/account/login?status=404");
        } else if (exception.getCause() instanceof UnauthorizedException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect("/account/login?status=403");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/account/login?status=400");
        }
    }
}

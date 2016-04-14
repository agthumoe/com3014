package com.surrey.com3014.group5.security;

import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * This component process user authentication.
 *
 * @author Aung Thu Moe
 */
@Component
public class SecureAuthenticationProvider implements AuthenticationProvider {
    /**
     * Userservice to retrieve user information.
     */
    @Autowired
    private UserService userService;

    /**
     * Authenticate the login user.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            return this.userService.authenticate(name, password);
        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}

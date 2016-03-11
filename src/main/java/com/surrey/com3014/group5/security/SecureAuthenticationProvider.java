package com.surrey.com3014.group5.security;

import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Aung Thu Moe
 */
public class SecureAuthenticationProvider implements AuthenticationProvider {
    private UserService userService;

    @Autowired
    public SecureAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            return this.getUserService().authenticate(name, password);
        } catch (Exception uafe) {
            throw new BadCredentialsException("Bad credentials", uafe);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}

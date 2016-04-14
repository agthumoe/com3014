package com.surrey.com3014.group5.security;

import com.surrey.com3014.group5.models.impl.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.surrey.com3014.group5.security.AuthoritiesConstants.ANONYMOUS;

/**
 * A utility class for spring security.
 *
 * @author Aung Thu Moe
 */
public final class SecurityUtils {
    /**
     * Just to prevent instantiation
     */
    private SecurityUtils() {
    }

    /**
     * Get current logged in username if user has been logged in.
     *
     * @return Current logged in username if user has been logged in, otherwise null.
     */
    public static String getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof User) {
                User springSecurityUser = (User) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    /**
     * Get the current logged in user if user has been logged in.
     *
     * @return Current logged in user if user has been logged in, otherwise null.
     */
    public static User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof User) {
                return (User) authentication.getPrincipal();
            }
            throw new ClassCastException("Spring authentication returns " + authentication.getPrincipal().getClass().getName() + ", instead of User Class");
        }
        return null;
    }

    /**
     * Check if a users is authenticated.
     *
     * @return true if the users is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(ANONYMOUS)) {
                    return false;
                }
            }
        }
        return true;
    }
}

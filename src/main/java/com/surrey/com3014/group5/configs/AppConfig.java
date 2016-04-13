package com.surrey.com3014.group5.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.format.DateTimeFormatter;

/**
 * Initialise necessary beans for the application
 *
 * @author Aung Thu Moe
 */
@Configuration
public class AppConfig {

    /**
     * Initialise {@link org.springframework.security.core.session.SessionRegistry} bean which contains session
     * information.
     *
     * @return {@link org.springframework.security.core.session.SessionRegistry} containing SessionInformation
     * @see SessionRegistry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * Initialise a {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder} as a
     * {@link org.springframework.security.crypto.password.PasswordEncoder} bean.
     *
     * @return {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder} as a
     * {@link org.springframework.security.crypto.password.PasswordEncoder} to encrypt password.
     * @see PasswordEncoder
     * @see BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Initialise a {@link java.time.format.DateTimeFormatter} bean to format date time.
     *
     * @return {@link java.time.format.DateTimeFormatter} to format date time.
     */
    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }
}

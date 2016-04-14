package com.surrey.com3014.group5.configs;

import com.surrey.com3014.group5.security.AuthenticationFailureHandler;
import com.surrey.com3014.group5.security.SecureAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.PostConstruct;

import static com.surrey.com3014.group5.security.AuthoritiesConstants.ADMIN;
import static com.surrey.com3014.group5.security.AuthoritiesConstants.USER;

/**
 * Security configuration for the application.
 *
 * @author Aung Thu Moe
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Autowired
     * {@link com.surrey.com3014.group5.security.CustomAuthenticationEntryPoint}
     * which redirect unauthorised access to the login page.
     */
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Autowired
     * {@link org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder}
     * Need this bean to set custom authentication provider for the application.
     */
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * Autowired
     * {@link com.surrey.com3014.group5.security.SecureAuthenticationProvider}
     * to handle user authentication
     */
    @Autowired
    private SecureAuthenticationProvider secureAuthenticationProvider;

    /**
     * Autowired
     * {@link org.springframework.security.core.session.SessionRegistry}
     * containing SessionInformation
     */
    @Autowired
    private SessionRegistry sessionRegistry;

    /**
     * Autowired
     * {@link com.surrey.com3014.group5.security.AuthenticationFailureHandler}
     * to handle failed authentication attempt
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * This method is called after dependency injection to set
     * {@link com.surrey.com3014.group5.security.SecureAuthenticationProvider}
     * to the
     * {@link org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder}
     */
    @PostConstruct
    public void init() {
        this.authenticationManagerBuilder.authenticationProvider(secureAuthenticationProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/bower_components/**")
            .antMatchers("/assets/**")
            .antMatchers("/swagger-ui/index.html");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
             .csrf()
                .ignoringAntMatchers("/topic/**")
                .ignoringAntMatchers("/queue/**")
         .and()
            .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry)
            .and()
        .and()
            .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority(ADMIN)
                .antMatchers("/game/**").hasAuthority(USER)
                .antMatchers("/lobby/**").hasAuthority(USER)
                .antMatchers("/api/**").authenticated()
                .antMatchers("/api/users/**").hasAuthority(ADMIN)
                .antMatchers("/error**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/login").permitAll()
        .and()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
        .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/account/login?logout")
            .deleteCookies("JSESSIONID")
            .permitAll()
        .and()
            .formLogin()
            .loginPage("/account/login")
            .loginProcessingUrl("/login")
            .usernameParameter("username")
            .passwordParameter("currentPassword")
                .failureHandler(authenticationFailureHandler)
                .permitAll()
        .and()
            .csrf().disable(); // disable csrf for the time being
    }
}

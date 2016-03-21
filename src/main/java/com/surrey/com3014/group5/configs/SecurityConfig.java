package com.surrey.com3014.group5.configs;

import com.surrey.com3014.group5.security.SecureAuthenticationProvider;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.annotation.PostConstruct;

/**
 * @author Aung Thu Moe
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    SecureAuthenticationProvider secureAuthenticationProvider;

    @Autowired
    SessionRegistry sessionRegistry;

    @PostConstruct
    public void init() {
        this.authenticationManagerBuilder.authenticationProvider(secureAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers("/admin/**").hasAuthority(ADMIN)
            .antMatchers("/api/users/**").hasAuthority(ADMIN)
            .antMatchers("/user/**").hasAuthority(USER)
            .antMatchers("/api/lobby/**").hasAnyAuthority(USER)
            .antMatchers("/assets/**").permitAll()
            .antMatchers("/bower_components/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/api/register**").permitAll()
            .antMatchers("/scripts/**").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/v2/api-docs").permitAll()
            .antMatchers("/login").permitAll()
            //            .anyRequest().denyAll()
//        .and()
//            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessUrl("/")
            .permitAll()
        .and()
            .formLogin()
            .loginProcessingUrl("/api/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .permitAll()
        .and().csrf().disable();

        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry);
    }
}

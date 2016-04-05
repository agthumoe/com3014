package com.surrey.com3014.group5.configs;

import com.surrey.com3014.group5.security.AuthenticationFailureHandler;
import com.surrey.com3014.group5.security.SecureAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.AuthenticationEntryPoint;

import static com.surrey.com3014.group5.security.AuthoritiesConstants.*;

import javax.annotation.PostConstruct;

/**
 * @author Aung Thu Moe
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private SecureAuthenticationProvider secureAuthenticationProvider;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @PostConstruct
    public void init() {
        this.authenticationManagerBuilder.authenticationProvider(secureAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
//            .csrf()
//            .ignoringAntMatchers("/websocket/**")
//        .and()
            .authorizeRequests()
            .antMatchers("/admin/**").hasAuthority(ADMIN)
            .antMatchers("/api/users/**").hasAuthority(ADMIN)
            .antMatchers("/game/**").hasAuthority(USER)
            .antMatchers("/api/lobby/**").hasAnyAuthority(USER)
            .antMatchers("/websocket/tracker").hasAuthority(ADMIN)
            .antMatchers("/websocket/**").permitAll()
            .antMatchers("/assets/**").permitAll()
            .antMatchers("/bower_components/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/api/register**").permitAll()
            .antMatchers("/scripts/**").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/v2/api-docs").permitAll()
            .antMatchers("/login").permitAll()
//            .anyRequest().authenticated()
        .and()
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessUrl("/account/login?logout")
            .deleteCookies("JSESSIONID")
            .permitAll()
        .and()
            .formLogin()
            .loginPage("/account/login")
            .loginProcessingUrl("/api/login")
            .failureHandler(authenticationFailureHandler)
            .usernameParameter("username")
            .passwordParameter("currentPassword")
            .permitAll()
        .and()
            .csrf().disable();

        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry);
    }
}

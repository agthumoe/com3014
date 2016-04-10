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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/bower_components/**")
            .antMatchers("/assets/**")
            .antMatchers("/swagger-ui/index.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
//            .csrf()
//            .ignoringAntMatchers("/topic/**")
//            .ignoringAntMatchers("/queue/**")
//        .and()
            .authorizeRequests()
            .antMatchers("/admin/**").hasAuthority(ADMIN)
            .antMatchers("/game/**").hasAuthority(USER)
            .antMatchers("/lobby/**").hasAuthority(USER)
//            .antMatchers("/api/**").authenticated()
//            .antMatchers("/api/users/**").hasAuthority(ADMIN)
            .antMatchers("/error**").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/v2/api-docs").permitAll()
            .antMatchers("/login").permitAll()
        .and()
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
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
            .failureHandler(authenticationFailureHandler)
            .usernameParameter("username")
            .passwordParameter("currentPassword")
            .permitAll()
        .and()
            .csrf().disable(); // disable csrf for the time being
        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry);
    }
}

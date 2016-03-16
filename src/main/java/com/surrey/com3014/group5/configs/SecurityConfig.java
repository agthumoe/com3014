package com.surrey.com3014.group5.configs;

import com.surrey.com3014.group5.security.SecureAuthenticationProvider;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public void configureGlobal(AuthenticationManagerBuilder auth, SecureAuthenticationProvider secureAuthenticationProvider) throws Exception {
        auth.authenticationProvider(secureAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }

    @Bean
    public SecureAuthenticationProvider secureAuthenticationProvider() {
        return new SecureAuthenticationProvider(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/admin/**").hasAuthority(ADMIN)
            .antMatchers("/api/users/**").hasAuthority(ADMIN)
            .antMatchers("/user/**").hasAuthority(USER)
            .antMatchers("/assets/**").permitAll()
            .antMatchers("/bower_components/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/api/register**").permitAll()
            .antMatchers("/scripts/**").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/v2/api-docs").permitAll()
//            .anyRequest().denyAll()
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
    }
}

package com.surrey.com3014.group5.configs;

import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.services.authority.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import static com.surrey.com3014.group5.security.AuthoritiesConstants.ADMIN;
import static com.surrey.com3014.group5.security.AuthoritiesConstants.USER;

/**
 * @author Aung Thu Moe
 */
@Configuration
public class AppConfig {
    @Autowired
    private AuthorityService authorityService;

    @Bean
    public ServerProperties getServerProperties() {
        return new ServerCustomization();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }

    @Bean(name = "adminAuthority")
    public Authority adminAuthority() {
        Authority authority = new Authority();
        authority.setType(ADMIN);
        this.authorityService.create(authority);
        return authority;
    }

    @Bean(name = "userAuthority")
    public Authority userAuthority() {
        Authority authority = new Authority();
        authority.setType(USER);
        this.authorityService.create(authority);
        return authority;
    }
}

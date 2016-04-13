package com.surrey.com3014.group5.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static com.surrey.com3014.group5.security.AuthoritiesConstants.USER;

/**
 * Security configuration for websocket.
 *
 * @author Aung Thu Moe
 * @author Spyros Balkonis
 */
@Configuration
public class WebsocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.nullDestMatcher().authenticated().simpDestMatchers("/queue/**").hasAuthority(USER)
            .simpDestMatchers("/topic/**").authenticated()
            .simpDestMatchers("/user/**").authenticated()
            .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll().anyMessage().denyAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}

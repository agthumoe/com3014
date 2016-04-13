package com.surrey.com3014.group5.configs;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Server customisation for web application.
 *
 * @author Aung Thu Moe
 */
@Component
public class ServerCustomization extends ServerProperties {

    /**
     * Customise error pages.
     *
     * @param container Servelet container to customise error pages.
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        super.customize(container);
        container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"));
    }

}

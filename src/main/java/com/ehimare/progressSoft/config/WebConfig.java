package com.ehimare.progressSoft.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/api/v1/deals/", "/api/v1/deals")
                .setKeepQueryParams(true)
                .setStatusCode(HttpStatus.PERMANENT_REDIRECT);
    }
}

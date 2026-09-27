package com.biblioteca.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(WebProps.class)
public class WebConfig implements WebMvcConfigurer {

    private final WebProps webProps;

    public WebConfig(WebProps webProps) {
        this.webProps = webProps;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(webProps.origensPermitidas().split(","))
                .allowedMethods(webProps.metodosPermitidos().split(","))
                .allowedHeaders("*")
                .allowCredentials(webProps.credenciaisPermitidas())
                .exposedHeaders(webProps.cabecalhosExpostos().split(","));
    }
}

package com.biblioteca.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web")
public record WebProps(
        String origensPermitidas,
        String metodosPermitidos,
        boolean credenciaisPermitidas,
        String cabecalhosExpostos
) {
}

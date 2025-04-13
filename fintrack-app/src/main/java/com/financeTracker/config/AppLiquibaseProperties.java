package com.financeTracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.liquibase")
@Getter
@Setter
public class AppLiquibaseProperties {
    private String serviceSchema;
    private String defaultSchema;
    private String changelogPath;
}
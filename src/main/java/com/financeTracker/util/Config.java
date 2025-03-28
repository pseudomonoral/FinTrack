package com.financeTracker.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Configuration class for application settings.
 * Maps the configuration file (e.g., YAML) into Java objects.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    public DatabaseConfig database;
    public LiquibaseConfig liquibase;

    /**
     * Configuration for database connection.
     */
    public static class DatabaseConfig {
        public String url;       // JDBC connection URL
        public String username;  // Database username
        public String password;  // Database password
        public int poolSize;     // Connection pool size
        public String schema;    // Target database schema
    }

    /**
     * Configuration for Liquibase migrations.
     */
    public static class LiquibaseConfig {
        public String changelog; // Path to Liquibase changelog file
    }
}



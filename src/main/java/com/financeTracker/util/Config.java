package com.financeTracker.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    public DatabaseConfig database;
    public LiquibaseConfig liquibase;

    public static class DatabaseConfig {
        public String url;
        public String username;
        public String password;
        public int poolSize;
        public String schema;
    }

    public static class LiquibaseConfig {
        public String changelog;
    }
}


package com.financeTracker.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Manages database connections using HikariCP connection pooling.
 */
public class DatabaseManager {
    private final HikariDataSource dataSource;

    /**
     * Initializes the database connection pool with the given configuration.
     *
     * @param config The database configuration containing connection details.
     */
    public DatabaseManager(Config.DatabaseConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.url);
        hikariConfig.setUsername(config.username);
        hikariConfig.setPassword(config.password);
        hikariConfig.setMaximumPoolSize(config.poolSize);
        hikariConfig.setDriverClassName("org.postgresql.Driver");

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Provides access to the configured data source.
     *
     * @return The configured {@link DataSource} instance.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Closes the database connection pool.
     */
    public void close() {
        dataSource.close();
    }
}
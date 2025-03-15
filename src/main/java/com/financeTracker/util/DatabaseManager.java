package com.financeTracker.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class DatabaseManager {
    private static final HikariDataSource dataSource;

    static {
        Config.DatabaseConfig config = ConfigLoader.getConfig().database;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.url);
        hikariConfig.setUsername(config.username);
        hikariConfig.setPassword(config.password);
        hikariConfig.setMaximumPoolSize(config.poolSize);
        hikariConfig.setDriverClassName("org.postgresql.Driver");

        dataSource = new HikariDataSource(hikariConfig);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}

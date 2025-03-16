package com.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class TestDataSourceProvider {

    public static DataSource dataSourceFromContainer() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(System.getProperty("DB_URL"));
        ds.setUsername(System.getProperty("DB_USERNAME"));
        ds.setPassword(System.getProperty("DB_PASSWORD"));
        ds.setMaximumPoolSize(3);
        return ds;
    }
}

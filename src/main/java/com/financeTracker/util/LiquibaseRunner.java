package com.financeTracker.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

public class LiquibaseRunner {
    public static void run() {
        try (Connection connection = DatabaseManager.getDataSource().getConnection()) {
            Database database = new liquibase.database.core.PostgresDatabase();
            database.setConnection(new JdbcConnection(connection));

            database.setLiquibaseSchemaName("public");

            String changelogPath = ConfigLoader.getConfig().liquibase.changelog;
            Liquibase liquibase = new Liquibase(changelogPath, new ClassLoaderResourceAccessor(), database);

            liquibase.update("");

            System.out.println("Liquibase миграции применены.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при запуске Liquibase", e);
        }
    }
}


package com.financeTracker.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

public class LiquibaseMigrator {
    private final DatabaseManager databaseManager;
    private final String changelogPath;

    public LiquibaseMigrator(DatabaseManager databaseManager, String changelogPath) {
        this.databaseManager = databaseManager;
        this.changelogPath = changelogPath;
    }

    public void executeMigrations() {
        try (Connection connection = databaseManager.getDataSource().getConnection()) {
            Database database = new PostgresDatabase();
            database.setConnection(new JdbcConnection(connection));
            database.setLiquibaseSchemaName("public");

            Liquibase liquibase = new Liquibase(changelogPath, new ClassLoaderResourceAccessor(), database);
            liquibase.update("");

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при запуске Liquibase", e);
        }
    }
}




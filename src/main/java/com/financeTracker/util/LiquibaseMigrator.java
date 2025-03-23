package com.financeTracker.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

/**
 * Handles database migrations using Liquibase.
 */
public class LiquibaseMigrator {
    private final DatabaseManager databaseManager;
    private final String changelogPath;

    /**
     * Initializes the Liquibase migrator with a database manager and changelog path.
     *
     * @param databaseManager The database manager providing the connection.
     * @param changelogPath   The path to the Liquibase changelog file.
     */
    public LiquibaseMigrator(DatabaseManager databaseManager, String changelogPath) {
        this.databaseManager = databaseManager;
        this.changelogPath = changelogPath;
    }

    /**
     * Executes database migrations using Liquibase.
     */
    public void executeMigrations() {
        try (Connection connection = databaseManager.getDataSource().getConnection()) {
            Database database = new PostgresDatabase();
            database.setConnection(new JdbcConnection(connection));
            database.setLiquibaseSchemaName("public");

            Liquibase liquibase = new Liquibase(changelogPath, new ClassLoaderResourceAccessor(), database);
            liquibase.update("");

        } catch (Exception e) {
            throw new RuntimeException("Error executing Liquibase migrations", e);
        }
    }
}
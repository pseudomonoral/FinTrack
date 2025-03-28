package com.testRepository;

import com.financeTracker.util.Config;
import com.financeTracker.util.DatabaseManager;
import com.financeTracker.util.LiquibaseMigrator;
import lombok.Getter;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestDatabaseContainer {
    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Getter
    private static DatabaseManager databaseManager;

    static {
        CONTAINER.start();
        System.setProperty("JDBC_URL", CONTAINER.getJdbcUrl());
        System.setProperty("DB_USERNAME", CONTAINER.getUsername());
        System.setProperty("DB_PASSWORD", CONTAINER.getPassword());

        init();
    }

    private static void init() {
        Config testConfig = new Config();
        testConfig.database = new Config.DatabaseConfig();
        testConfig.database.url = CONTAINER.getJdbcUrl();
        testConfig.database.username = CONTAINER.getUsername();
        testConfig.database.password = CONTAINER.getPassword();
        testConfig.database.poolSize = 5;

        testConfig.liquibase = new Config.LiquibaseConfig();
        testConfig.liquibase.changelog = "db/changelog/db.changelog-master.xml";

        databaseManager = new DatabaseManager(testConfig.database);
        LiquibaseMigrator liquibaseMigrator = new LiquibaseMigrator(databaseManager, testConfig.liquibase.changelog);
        liquibaseMigrator.executeMigrations();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return CONTAINER;
    }
}


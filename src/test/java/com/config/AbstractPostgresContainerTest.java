package com.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractPostgresContainerTest {

    protected static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    @BeforeAll
    static void startContainer() {
        POSTGRES_CONTAINER.start();
        System.setProperty("DB_URL", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("DB_USERNAME", POSTGRES_CONTAINER.getUsername());
        System.setProperty("DB_PASSWORD", POSTGRES_CONTAINER.getPassword());
    }

    @AfterAll
    static void stopContainer() {
        POSTGRES_CONTAINER.stop();
    }
}



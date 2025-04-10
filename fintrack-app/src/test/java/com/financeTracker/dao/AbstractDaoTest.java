package com.financeTracker.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.database.jvm.JdbcConnection;

import javax.sql.DataSource;
import java.sql.Connection;

@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractDaoTest {

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    protected JdbcTemplate jdbcTemplate;



    @BeforeAll
    void initJdbcTemplate() throws Exception {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUser(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());

        jdbcTemplate = new JdbcTemplate(dataSource);

        runLiquibaseMigrations(dataSource);
    }

    private void runLiquibaseMigrations(DataSource dataSource) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update(new Contexts());
        }
    }

    private static Long userId = 123L;

    private Long getUserId() {
        return userId++;
    }

    protected long insertTestUserWithRandomId() {
        long id = getUserId();

        String sql = """
        INSERT INTO fintrack.users 
            (id, login, password, name, user_role, user_status, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, now(), now())
    """;

        jdbcTemplate.update(sql,
                id,
                "test_user_" + id,
                "test_password",
                "Test User " + id,
                "USER",
                "ACTIVE"
        );
        return id;
    }
}


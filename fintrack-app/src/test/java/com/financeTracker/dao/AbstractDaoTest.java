package com.financeTracker.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
import java.sql.PreparedStatement;
import java.sql.Statement;

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

    private static Long userId = 1L;

    private Long getUserId() {
        return userId++;
    }

    protected long insertTestUserWithRandomId() {
        String sql = """
                    INSERT INTO fintrack.users 
                        (login, password, name, user_role, user_status, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?, now(), now())
                    RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            long suffix = getUserId();
            ps.setString(1, "test_user_" + suffix);
            ps.setString(2, "test_password");
            ps.setString(3, "Test User " + suffix);
            ps.setString(4, "USER");
            ps.setString(5, "ACTIVE");
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}


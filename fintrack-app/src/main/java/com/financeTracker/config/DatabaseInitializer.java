package com.financeTracker.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

import static com.financeTracker.constants.SchemaSql.CREATE_SCHEMA_SQL;

/**
 * Initializes the database by:
 * <ul>
 *     <li>Creating required schemas if they do not already exist</li>
 *     <li>Running Liquibase migrations with schema and changelog path configurable via application.yaml</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final AppLiquibaseProperties props;

    /**
     * Initializes schemas and runs Liquibase after application is fully ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        createSchemasIfNotExist();
        runLiquibase();
    }

    private void createSchemasIfNotExist() {
        jdbcTemplate.execute(CREATE_SCHEMA_SQL + props.getServiceSchema());
        jdbcTemplate.execute(CREATE_SCHEMA_SQL + props.getDefaultSchema());
    }

    private void runLiquibase() {
        DataSource dataSource = jdbcTemplate.getDataSource();
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not initialized in JdbcTemplate");
        }

        try (Connection conn = dataSource.getConnection()) {
            var liquibaseDatabase = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conn));

            liquibaseDatabase.setLiquibaseSchemaName(props.getServiceSchema());
            liquibaseDatabase.setDefaultSchemaName(props.getDefaultSchema());

            var liquibase = new Liquibase(
                    props.getChangelogPath(),
                    new ClassLoaderResourceAccessor(),
                    liquibaseDatabase
            );

            liquibase.update(new Contexts(), new LabelExpression());

        } catch (Exception e) {
            throw new RuntimeException("Failed to run Liquibase", e);
        }
    }
}
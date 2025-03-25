package com.financeTracker;

import com.financeTracker.util.Config;
import com.financeTracker.util.ConfigLoader;
import com.financeTracker.util.DatabaseManager;
import com.financeTracker.util.LiquibaseMigrator;


public class ApplicationRunner {

    public static void run() {
        Config config = ConfigLoader.loadConfig("config.yaml");
        DatabaseManager databaseManager = new DatabaseManager(config.database);
        LiquibaseMigrator liquibaseMigrator = new LiquibaseMigrator(databaseManager, config.liquibase.changelog);
        liquibaseMigrator.executeMigrations();

        ApplicationContext.loadContext();
    }
}


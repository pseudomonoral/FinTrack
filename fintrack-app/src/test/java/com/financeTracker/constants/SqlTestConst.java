package com.financeTracker.constants;

public interface SqlTestConst {
    String BUDGET_SCHEMA_AND_TABLE_CREATE = """
            CREATE SCHEMA IF NOT EXISTS fintrack;
            CREATE TABLE IF NOT EXISTS fintrack.budgets (
                id SERIAL PRIMARY KEY,
                user_id BIGINT NOT NULL,
                "limit" NUMERIC NOT NULL,
                created_at TIMESTAMP NOT NULL,
                updated_at TIMESTAMP NOT NULL
            );
        """;

    String GOAL_SCHEMA_AND_TABLE_CREATE = """
            CREATE SCHEMA IF NOT EXISTS fintrack;

            CREATE TABLE IF NOT EXISTS fintrack.goals (
                id SERIAL PRIMARY KEY,
                user_id BIGINT NOT NULL,
                name TEXT NOT NULL,
                target_amount NUMERIC NOT NULL,
                current_amount NUMERIC NOT NULL,
                goal_completed BOOLEAN NOT NULL,
                goal_actual BOOLEAN NOT NULL,
                created_at TIMESTAMP NOT NULL,
                updated_at TIMESTAMP NOT NULL
            );
        """;

    String TRANSACTION_SCHEMA_AND_TABLE_CREATE = """
            CREATE SCHEMA IF NOT EXISTS fintrack;

            CREATE TABLE IF NOT EXISTS fintrack.transactions (
                id SERIAL PRIMARY KEY,
                user_id BIGINT NOT NULL,
                amount NUMERIC NOT NULL,
                category TEXT NOT NULL,
                type TEXT NOT NULL,
                description TEXT,
                created_at TIMESTAMP NOT NULL DEFAULT now(),
                updated_at TIMESTAMP NOT NULL DEFAULT now()
            );
        """;

    String USER_SCHEMA_AND_TABLE_CREATE = """
            CREATE SCHEMA IF NOT EXISTS fintrack;

            CREATE TABLE IF NOT EXISTS fintrack.users (
                id SERIAL PRIMARY KEY,
                login TEXT NOT NULL,
                password TEXT NOT NULL,
                name TEXT NOT NULL,
                user_role TEXT NOT NULL,
                user_status TEXT NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT now(),
                updated_at TIMESTAMP NOT NULL DEFAULT now()
            );
        """;

    String NOTIFICATION_SCHEMA_AND_TABLE_CREATE = """
    CREATE SCHEMA IF NOT EXISTS fintrack;

    CREATE TABLE IF NOT EXISTS fintrack.notification (
        id SERIAL PRIMARY KEY,
        created_at TIMESTAMP NOT NULL,
        user_id BIGINT NOT NULL,
        type VARCHAR(30) NOT NULL,
        read BOOLEAN NOT NULL DEFAULT false
    );
    """;
}

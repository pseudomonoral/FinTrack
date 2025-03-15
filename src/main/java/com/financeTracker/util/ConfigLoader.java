package com.financeTracker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.InputStream;

import lombok.Getter;

public class ConfigLoader {
    @Getter
    private static final Config config;

    static {
        try {
            Dotenv dotenv = Dotenv.load();

            try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream("config.yaml")) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                config = mapper.readValue(inputStream, Config.class);

                String dbName = dotenv.get("POSTGRES_DB");
                String schema = config.database.schema;

                config.database.url = "jdbc:postgresql://localhost:5432/" + dbName + "?currentSchema=" + schema;
                config.database.username = dotenv.get("POSTGRES_USER");
                config.database.password = dotenv.get("POSTGRES_PASSWORD");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки конфигурации", e);
        }
    }
}

package com.financeTracker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for loading application configuration.
 * Reads configuration from a YAML file and replaces placeholders with environment variables.
 */
public class ConfigLoader {

    /**
     * Loads configuration from the specified file.
     *
     * @param fileName The name of the configuration file.
     * @return Config object with the loaded settings.
     */
    public static Config loadConfig(String fileName) {
        loadEnvFile();

        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Configuration file not found: " + fileName);
            }

            String yamlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // Replace environment variable placeholders in the config file
            for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
                yamlContent = yamlContent.replace("ENV_" + entry.getKey(), entry.getValue().toString());
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(new ByteArrayInputStream(yamlContent.getBytes()), Config.class);
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration", e);
        }
    }

    /**
     * Loads environment variables from a .env file into system properties.
     */
    private static void loadEnvFile() {
        File file = new File(".env");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Map<String, String> envMap = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    envMap.put(parts[0].trim(), parts[1].trim());
                }
            }
            envMap.forEach(System::setProperty);
        } catch (IOException e) {
            throw new RuntimeException("Error loading .env file", e);
        }
    }
}
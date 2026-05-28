package base;

import config.EnvConfig;
import org.junit.jupiter.api.BeforeAll;
import utils.RestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseApiTest {
    private static final Map<String, String> DOTENV_VALUES = loadDotEnv();

    @BeforeAll
    static void setupBaseUri() {
        RestUtils.setBaseURI(EnvConfig.getBaseUrl());
    }

    public static String getApiUsername() {
        String systemValue = System.getProperty("api.user");
        if (systemValue != null && !systemValue.isBlank()) return systemValue;
        String envValue = System.getenv("API_USER");
        if (envValue != null && !envValue.isBlank()) return envValue;
        return DOTENV_VALUES.getOrDefault("API_USER", "");
    }

    public static String getApiPassword() {
        String systemValue = System.getProperty("api.pass");
        if (systemValue != null && !systemValue.isBlank()) return systemValue;
        String envValue = System.getenv("API_PASS");
        if (envValue != null && !envValue.isBlank()) return envValue;
        return DOTENV_VALUES.getOrDefault("API_PASS", "");
    }

    private static Map<String, String> loadDotEnv() {
        Map<String, String> values = new HashMap<>();
        Path envPath = Path.of(".env");
        if (!Files.exists(envPath)) return values;
        try {
            List<String> lines = Files.readAllLines(envPath);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
                int separator = trimmed.indexOf('=');
                if (separator <= 0) continue;
                String key = trimmed.substring(0, separator).trim();
                String value = trimmed.substring(separator + 1).trim();
                values.put(key, value);
            }
        } catch (IOException ignored) {
            return values;
        }
        return values;
    }
}

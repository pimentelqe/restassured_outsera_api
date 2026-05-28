package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvConfig {
    private static final Properties props = new Properties();

    static {
        String env = resolveEnv();
        String fileName = "config/" + env + ".properties";
        try (InputStream is = EnvConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException ignored) {}
    }

    private static String resolveEnv() {
        String sysProp = System.getProperty("env");
        if (sysProp != null && !sysProp.isBlank()) return sysProp;
        String envVar = System.getenv("ENV");
        if (envVar != null && !envVar.isBlank()) return envVar;
        return "dev";
    }

    public static String getBaseUrl() {
        String sysProp = System.getProperty("base.url");
        if (sysProp != null && !sysProp.isBlank()) return sysProp;
        String envVar = System.getenv("BASE_URL");
        if (envVar != null && !envVar.isBlank()) return envVar;
        return props.getProperty("base.url", "https://dummyjson.com/");
    }
}

package com.example.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigReader {
    private static final Properties props = new Properties();
    private static final Pattern ENV_VAR_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
                // Resolve environment variables in all properties
                resolveEnvironmentVariables();
            } else {
                System.err.println("config.properties not found on classpath!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    /**
     * Resolves environment variable placeholders like ${VAR_NAME} in property values
     */
    private static void resolveEnvironmentVariables() {
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            if (value != null && value.contains("${")) {
                String resolved = resolveEnvVars(value);
                props.setProperty(key, resolved);
            }
        }
    }

    /**
     * Replaces ${VAR_NAME} with actual environment variable value
     * Falls back to empty string if env var not found
     */
    private static String resolveEnvVars(String value) {
        Matcher matcher = ENV_VAR_PATTERN.matcher(value);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String envVarName = matcher.group(1);
            String envVarValue = System.getenv(envVarName);

            if (envVarValue != null) {
                matcher.appendReplacement(result, Matcher.quoteReplacement(envVarValue));
            } else {
                // If env var not found, keep the placeholder or use empty string
                // You can change this behavior if needed
                matcher.appendReplacement(result, "");
                System.err.println("Warning: Environment variable '" + envVarName + "' not found. Using empty value.");
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static String get(String key) {
        String value = props.getProperty(key, "");
        // Also check for runtime environment variable resolution (in case props were set after static init)
        if (value.contains("${")) {
            value = resolveEnvVars(value);
        }
        return value;
    }
    
    public static String get(String key, String defaultValue) {
        String value = props.getProperty(key, defaultValue);
        // Also check for runtime environment variable resolution
        if (value.contains("${")) {
            value = resolveEnvVars(value);
        }
        return value;
    }
}


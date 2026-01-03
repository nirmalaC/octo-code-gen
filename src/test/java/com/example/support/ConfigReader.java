package com.example.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                System.err.println("config.properties not found on classpath!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key, "");
    }
    
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}


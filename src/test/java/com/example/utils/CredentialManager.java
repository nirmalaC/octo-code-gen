package com.example.utils;

public class CredentialManager {

    public static String getUsername(String userType) {
        return getEnv(userType + "_USERNAME");
    }

    public static String getPassword(String userType) {
        return getEnv(userType + "_PASSWORD");
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value == null) {
            throw new RuntimeException("Missing environment variable: " + key);
        }
        return value;
    }
}

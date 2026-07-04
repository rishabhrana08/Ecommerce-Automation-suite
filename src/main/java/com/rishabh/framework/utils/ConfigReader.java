package com.rishabh.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton wrapper around config.properties. Any value can be overridden from the
 * command line, e.g. -Dbrowser=firefox -Dheadless=true, which is how the CI matrix
 * picks browsers without touching this file.
 */
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties;

    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    private ConfigReader() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties from " + CONFIG_PATH, e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    private String get(String key, String defaultValue) {
        // system property wins over the file, so CI/CLI overrides just work
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }

    public String getBaseUrl() {
        return get("baseUrl", "https://automationexercise.com");
    }

    public String getBrowser() {
        return get("browser", "chrome");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(get("headless", "false"));
    }

    public int getImplicitWaitSeconds() {
        return Integer.parseInt(get("implicitWaitSeconds", "2"));
    }

    public int getExplicitWaitSeconds() {
        return Integer.parseInt(get("explicitWaitSeconds", "15"));
    }

    public int getPageLoadTimeoutSeconds() {
        return Integer.parseInt(get("pageLoadTimeoutSeconds", "30"));
    }

    public String getViewport() {
        return get("viewport", "1920x1080");
    }

    public String getScreenshotDir() {
        return get("screenshotOnFailureDir", "target/screenshots");
    }
}

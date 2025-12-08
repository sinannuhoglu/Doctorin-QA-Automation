package com.sinannuhoglu.core;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input =
                     ConfigReader.class.getClassLoader()
                             .getResourceAsStream("config/config.properties")) {

            if (input == null) {
                throw new IllegalStateException("config/config.properties classpath içinde bulunamadı.");
            }

            PROPERTIES.load(input);

        } catch (Exception e) {
            throw new IllegalStateException("config.properties yüklenirken hata oluştu: " + e.getMessage(), e);
        }
    }

    private ConfigReader() {
    }

    /**
     * İstenen anahtar için config değeri döner.
     * Anahtar yoksa veya boşsa null döner.
     */
    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    /**
     * Anahtar yoksa verilen default değeri döner.
     */
    public static String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Bu anahtarın mutlaka tanımlı olmasını istediğin durumlar için.
     * Örneğin: browser, implicitWait gibi.
     */
    public static String getRequired(String key) {
        String value = get(key);
        if (value == null) {
            throw new IllegalStateException("Config anahtarı bulunamadı: " + key);
        }
        return value;
    }
}

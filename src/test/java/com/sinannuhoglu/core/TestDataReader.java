package com.sinannuhoglu.core;

import java.io.InputStream;
import java.util.Properties;

/**
 * Test verilerini okuyan yardımcı sınıf.
 */
public final class TestDataReader {

    private static final Properties testData = new Properties();

    static {
        String dataPath = System.getProperty("testdata.file", "testdata/testdata.properties");

        try (InputStream input =
                     TestDataReader.class.getClassLoader()
                             .getResourceAsStream(dataPath)) {

            if (input == null) {
                throw new IllegalStateException("Test data dosyası bulunamadı: " + dataPath);
            }
            testData.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Test data dosyası yüklenirken hata oluştu: " + e.getMessage(), e);
        }
    }

    private TestDataReader() {
        // static utility
    }

    public static String get(String key) {
        String sys = System.getProperty(key);
        if (sys != null) {
            return sys.trim();
        }

        String env = System.getenv(key);
        if (env != null) {
            return env.trim();
        }

        String value = testData.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Test data anahtarı bulunamadı: " + key);
        }
        return value.trim();
    }
}

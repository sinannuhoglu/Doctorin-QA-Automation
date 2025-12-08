package com.sinannuhoglu.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        WebDriver webDriver = DRIVER.get();
        if (webDriver == null) {
            throw new IllegalStateException(
                    "WebDriver henüz başlatılmadı. Önce DriverFactory.initDriver() çağrılmalı."
            );
        }
        return webDriver;
    }

    public static void initDriver() {
        if (DRIVER.get() != null) {
            return;
        }

        String browser = ConfigReader.getOrDefault("browser", "chrome").toLowerCase();

        WebDriver webDriver;

        switch (browser) {
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                webDriver = new ChromeDriver(options);
                break;
        }

        String implicitWaitStr = ConfigReader.getOrDefault("implicitWait", "10");
        long implicitWaitSec;
        try {
            implicitWaitSec = Long.parseLong(implicitWaitStr);
        } catch (NumberFormatException e) {
            implicitWaitSec = 10L;
        }

        webDriver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(implicitWaitSec));

        DRIVER.set(webDriver);
    }

    public static void quitDriver() {
        WebDriver webDriver = DRIVER.get();
        if (webDriver != null) {
            webDriver.quit();
            DRIVER.remove();
        }
    }
}

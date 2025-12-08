package com.sinannuhoglu.hooks;

import com.sinannuhoglu.core.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Global Cucumber Hooks
 * - Her senaryo öncesi driver init
 * - Her senaryo sonrası screenshot (fail durumunda) + driver quit
 */
public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("=== Senaryo BAŞLANGIÇ: " + scenario.getName() + " ===");
        DriverFactory.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            WebDriver driver = DriverFactory.getDriver();

            if (driver != null && scenario.isFailed()) {
                try {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "Failure screenshot");
                    System.out.println("[Hooks] Senaryo başarısız, screenshot eklendi: " + scenario.getName());
                } catch (Exception e) {
                    System.out.println("[Hooks] Screenshot alınırken hata: " + e.getMessage());
                }
            }
        } finally {
            DriverFactory.quitDriver();
            System.out.println("=== Senaryo BİTİŞ   ===");
        }
    }
}

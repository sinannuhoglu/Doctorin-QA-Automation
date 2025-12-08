package com.sinannuhoglu.pages.login;

import com.sinannuhoglu.core.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TenantModalPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By tenantNameInput = By.id("Input_Name");
    private final By saveButton = By.xpath("//button[@type='submit' and normalize-space()='Kaydet']");

    public TenantModalPage() {
        this.driver = DriverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void setTenantName(String tenantName) {
        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(tenantNameInput)
        );
        input.clear();
        input.sendKeys(tenantName);
    }

    public void clickSave() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(saveButton)
        );
        button.click();
    }
}

package com.sinannuhoglu.pages.admission.definitions;

import com.sinannuhoglu.core.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class AdmissionAutoServiceItemRulesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AdmissionAutoServiceItemRulesPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // =================== LOCATORS ======================

    private final By newRuleButton = By.xpath(
            "//button[contains(@class,'e-btn')]" +
                    "[.//span[normalize-space()='Yeni Ekle'] or normalize-space()='Yeni Ekle']"
    );

    private final By ruleForm = By.cssSelector("form.e-data-form");

    private final By saveButton = By.xpath(
            "//form[contains(@class,'e-data-form')]//button[@type='submit' and normalize-space()='Kaydet']"
    );

    private final By searchInput = By.id("search-input");

    private final By gridTable = By.id("Grid_content_table");

    private final By deleteConfirmButton = By.xpath(
            "//div[contains(@class,'e-dlg-content')]//button[contains(@class,'e-btn') and normalize-space()='Evet']"
    );

    private final By deleteDialogContainer = By.cssSelector(
            "div.e-dlg-container.e-dlg-center-center[style*='display: flex']"
    );

    private final By deleteDialogFooterYesButton = By.xpath(
            ".//div[contains(@class,'e-footer-content')]//button[normalize-space()='Evet']"
    );

    private final By gridSpinner = By.cssSelector("div.e-spinner-pane");

    // =================== HELPER METODLAR ===============

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element
        );
    }

    /**
     * Stale / intercept hatalarına karşı güvenli tıklama.
     */
    private void safeClick(WebElement element) {
        int maxRetries = 3;
        for (int i = 1; i <= maxRetries; i++) {
            try {
                scrollIntoView(element);
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException ignored) {
                // Retry mekanizması
            }
        }
        scrollIntoView(element);
        element.click();
    }

    private void safeClick(By locator) {
        int maxRetries = 3;
        for (int i = 1; i <= maxRetries; i++) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                scrollIntoView(element);
                element.click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException ignored) {
                // Retry mekanizması
            }
        }
        WebElement element = driver.findElement(locator);
        scrollIntoView(element);
        element.click();
    }

    /**
     * Dropdown label metnine göre ilgili container'ı bulur.
     */
    private WebElement findDropdownContainerByLabel(String labelText) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(ruleForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        return label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')][1]" +
                        "//span[contains(@class,'e-ddl')]")
        );
    }

    private void selectFromDialogDropdown(String labelText, String optionText) {
        WebElement container = findDropdownContainerByLabel(labelText);

        WebElement icon = container.findElement(
                By.cssSelector("span.e-input-group-icon")
        );

        safeClick(icon);

        By optionLocator = By.xpath(
                "//div[contains(@class,'e-ddl') and contains(@class,'e-popup-open')]" +
                        "//li[normalize-space()='" + optionText + "']"
        );

        safeClick(optionLocator);
    }

    private WebElement findRowByServiceItem(String serviceItem) {
        By rowLocator = By.xpath(
                "//table[@id='Grid_content_table']//tbody/tr[" +
                        ".//td[1]//p[normalize-space()='" + serviceItem + "']" +
                        "]"
        );
        return wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));
    }

    /**
     * Grid üzerinde Syncfusion spinner varsa kısa bir stabilizasyon beklemesi.
     * Farklı ortamlarda ani refresh kaynaklı flakeleri azaltmak için kullanılır.
     */
    private void waitForGridToBeStable() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.attributeContains(gridSpinner, "class", "e-spin-show"));
            shortWait.until(ExpectedConditions.attributeContains(gridSpinner, "class", "e-spin-hide"));
        } catch (TimeoutException ignored) {
            // Her zaman spinner çıkmayabilir, bu durumda normal akışa devam ediyoruz.
        }
    }

    // =================== NAVIGATION =====================

    public void goToPage(String url) {
        driver.get(url);

        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        pageWait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState")
                .equals("complete"));

        pageWait.until(ExpectedConditions.visibilityOfElementLocated(newRuleButton));
    }

    // =================== YENİ KURAL FORMU ==============

    public void clickNewRuleButton() {
        safeClick(newRuleButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(ruleForm));
    }

    public void selectServiceItem(String optionText) {
        selectFromDialogDropdown("Hizmet Öğesi", optionText);
    }

    public void selectDepartment(String optionText) {
        selectFromDialogDropdown("Departman", optionText);
    }

    public void selectDoctor(String optionText) {
        selectFromDialogDropdown("Doktor", optionText);
    }

    public void selectBranch(String optionText) {
        selectFromDialogDropdown("Şube", optionText);
    }

    public void selectVisitType(String optionText) {
        selectFromDialogDropdown("Vizit Tipi", optionText);
    }

    public void clickSave() {
        safeClick(saveButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(ruleForm));
    }

    // =================== GRID & ARAMA ===================

    public void searchByServiceItem(String serviceItem) {
        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(searchInput)
        );
        input.clear();
        input.sendKeys(serviceItem);
        input.sendKeys(Keys.ENTER);

        // Spinner varsa stabilize olmasını bekle
        waitForGridToBeStable();

        wait.until(ExpectedConditions.visibilityOfElementLocated(gridTable));
        findRowByServiceItem(serviceItem);
    }

    public void verifyGridRowValues(String serviceItem,
                                    String doctor,
                                    String department,
                                    String branch,
                                    String visitType) {

        By rowLocator = By.xpath(
                "//table[@id='Grid_content_table']//tbody/tr[" +
                        ".//td[1]//p[normalize-space()='" + serviceItem + "'] and " +
                        ".//td[2]//p[normalize-space()='" + doctor + "'] and " +
                        ".//td[3]//p[normalize-space()='" + department + "'] and " +
                        ".//td[4]//p[normalize-space()='" + branch + "'] and " +
                        ".//td[5]//p[normalize-space()='" + visitType + "']" +
                        "]"
        );

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement row = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(rowLocator)
        );

        String actualServiceItem = row.findElement(By.xpath("./td[1]//p")).getText().trim();
        String actualDoctor = row.findElement(By.xpath("./td[2]//p")).getText().trim();
        String actualDepartment = row.findElement(By.xpath("./td[3]//p")).getText().trim();
        String actualBranch = row.findElement(By.xpath("./td[4]//p")).getText().trim();
        String actualVisitType = row.findElement(By.xpath("./td[5]//p")).getText().trim();

        Assert.assertEquals(actualServiceItem, serviceItem,
                "Grid satırındaki Hizmet Öğesi beklenen değer ile uyuşmuyor.");
        Assert.assertEquals(actualDoctor, doctor,
                "Grid satırındaki Doktor bilgisi beklenen değer ile uyuşmuyor.");
        Assert.assertEquals(actualDepartment, department,
                "Grid satırındaki Departman bilgisi beklenen değer ile uyuşmuyor.");
        Assert.assertEquals(actualBranch, branch,
                "Grid satırındaki Şube bilgisi beklenen değer ile uyuşmuyor.");
        Assert.assertEquals(actualVisitType, visitType,
                "Grid satırındaki Vizit Tipi beklenen değer ile uyuşmuyor.");
    }

    // =================== KAYIT MENÜSÜ & DÜZENLE/SİL ======

    public void openRowActionMenu(String serviceItem) {
        WebElement row = findRowByServiceItem(serviceItem);

        WebElement actionButton = row.findElement(
                By.xpath("./td[last()]//button[@aria-label='dropdownbutton']")
        );

        safeClick(actionButton);
    }

    public void clickRowMenuItem(String menuText) {
        By popupLocator = By.cssSelector(
                "div.e-dropdown-popup.e-popup-open[style*='display: block']"
        );

        WebElement popup = wait.until(
                ExpectedConditions.visibilityOfElementLocated(popupLocator)
        );

        By itemLocator = By.xpath(
                ".//ul[contains(@class,'e-dropdown-menu')]//li[normalize-space()='" + menuText + "']"
        );

        WebElement menuItem = popup.findElement(itemLocator);
        safeClick(menuItem);
    }

    public void confirmDelete() {
        WebElement dialog = wait.until(
                ExpectedConditions.visibilityOfElementLocated(deleteDialogContainer)
        );

        WebElement yesButton = dialog.findElement(deleteDialogFooterYesButton);
        safeClick(yesButton);

        wait.until(ExpectedConditions.invisibilityOf(dialog));

        waitForGridToBeStable();

        wait.until(ExpectedConditions.visibilityOfElementLocated(gridTable));
    }

    /**
     * Belirli bir kural kombinasyonunun artık gridde listelenmediğini doğrular.
     */
    public void verifyRuleNotExists(String serviceItem,
                                    String doctor,
                                    String department,
                                    String branch,
                                    String visitType) {

        By rowLocator = By.xpath(
                "//table[@id='Grid_content_table']//tbody/tr[" +
                        ".//td[1]//p[normalize-space()='" + serviceItem + "'] and " +
                        ".//td[2]//p[normalize-space()='" + doctor + "'] and " +
                        ".//td[3]//p[normalize-space()='" + department + "'] and " +
                        ".//td[4]//p[normalize-space()='" + branch + "'] and " +
                        ".//td[5]//p[normalize-space()='" + visitType + "']" +
                        "]"
        );

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean disappeared = shortWait.until(d -> driver.findElements(rowLocator).isEmpty());

        if (!disappeared) {
            WebElement row = driver.findElement(rowLocator);
            String rowText = row.getText().trim();
            Assert.fail("Silinmesi beklenen kural gridde hâlâ listeleniyor. Satır içeriği:\n" + rowText);
        }
    }

    // =================== BASİT DOĞRULAMA =================

    public void verifyRuleSavedSuccessfully() {
        try {
            boolean closed = wait.until(
                    ExpectedConditions.invisibilityOfElementLocated(ruleForm)
            );
            if (!closed) {
                Assert.fail("Otomatik Servis Öğesi Kuralları formu kapanmadı, kayıt başarısız görünüyor.");
            }
        } catch (TimeoutException e) {
            Assert.fail("Otomatik Servis Öğesi Kuralları formu zamanında kapanmadı, kayıt başarısız görünüyor.");
        }
    }
}

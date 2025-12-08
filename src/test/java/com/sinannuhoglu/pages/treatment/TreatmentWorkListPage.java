package com.sinannuhoglu.pages.treatment;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Tıbbi İşlemler > İş Listesi ekranı
 * URL: /treatment-service/work-list
 */
public class TreatmentWorkListPage {

    private static final Logger LOGGER = LogManager.getLogger(TreatmentWorkListPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // ===================== TOOLBAR LOCATOR'LARI =====================

    private final By dateRangeWrapper = By.cssSelector("span.e-date-range-wrapper");
    private final By dateRangeInput = By.cssSelector("span.e-date-range-wrapper input[aria-label='daterangepicker']");

    private final By detailedFilterButton = By.cssSelector("button[data-testid='detailed-filter-button']");

    private final By filterDialogContainer = By.cssSelector("div[id^='modal-dialog'][class*='e-dlg-container']");
    private final By filterClearButton = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]//button[normalize-space()='Temizle']"
    );
    private final By filterApplyButton = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]//button[normalize-space()='Uygula']"
    );

    private final By toolbarSearchInput = By.cssSelector("input#search-input[name='search-input']");
    private final By toolbarSearchIcon = By.cssSelector("span.e-input-group-icon.e-icons.e-search");

    // ===================== GRID LOCATOR'LARI =====================

    private final By mainGridContent = By.cssSelector("div.e-gridcontent");

    private final By firstRowPatientNameCell = By.xpath(
            "//div[contains(@class,'e-gridcontent')]//tbody/tr[1]/td[1]//p"
    );

    private final By firstRowDetailButton = By.xpath(
            "//div[contains(@class,'e-gridcontent')]//tbody/tr[1]/td[9]" +
                    "//button[contains(@data-testid,'work-list-detail') or normalize-space()='Detay']"
    );

    private final By loadingSpinner = By.cssSelector("div.e-spinner-pane[role='alert'][aria-label='Loading']");

    // ===================== DETAY SAYFASI SEKME LOCATOR'LARI =====================

    /**
     * Üst kartlarda yer alan sekmeler:
     *  - Finans: admission-service/finance-card/overview
     *  - Muayene: treatment-service/medical-card/overview
     */
    private final By examinationTab = By.xpath(
            "//a[contains(@href,'treatment-service/medical-card/overview') or normalize-space()='Muayene']"
    );

    private final By financeTab = By.xpath(
            "//a[contains(@href,'admission-service/finance-card/overview') or normalize-space()='Finans']"
    );

    public TreatmentWorkListPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ===================== HELPER METODLAR =======================

    private void scrollIntoView(WebElement el) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (JavascriptException ignored) {
        }
    }

    private void safeClick(WebElement el) {
        scrollIntoView(el);
        try {
            el.click();
        } catch (ElementNotInteractableException e) {
            js.executeScript("arguments[0].click();", el);
        }
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        safeClick(el);
    }

    private void waitForLoadingToFinish() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (TimeoutException ignored) {
        }
    }

    // ===================== NAVIGATION ====================

    public void goToWorkList(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "[TreatmentWorkListPage] URL null/boş. testdata.properties içinde 'treatmentWorkListUrl' tanımlı mı?"
            );
        }

        LOGGER.info("[TreatmentWorkListPage] goToWorkList -> {}", url);
        driver.get(url);

        try {
            wait.until(d -> "complete".equals(js.executeScript("return document.readyState")));
        } catch (TimeoutException ignored) {
        }

        waitForLoadingToFinish();
    }

    // ===================== TARİH ARALIĞI ====================

    /**
     * Toolbar üzerindeki tarih aralığı alanına tıklar ve popup'ın açılmasını bekler.
     * Bazı ortamlarda sadece input'a tıklamak yetmediği için ikon üzerinden de tetiklenir.
     */
    public void openDateRangePopup() {
        LOGGER.info("[TreatmentWorkListPage] openDateRangePopup");

        WebElement wrapper = wait.until(ExpectedConditions.visibilityOfElementLocated(dateRangeWrapper));
        WebElement input = wrapper.findElement(dateRangeInput);

        WebElement icon = null;
        try {
            icon = wrapper.findElement(By.cssSelector("span.e-input-group-icon.e-range-icon.e-icons"));
        } catch (NoSuchElementException ignored) {
        }

        safeClick(input);

        By popupLocator = By.cssSelector("div.e-control.e-daterangepicker.e-lib.e-popup[role='dialog']");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(popupLocator));
        } catch (TimeoutException first) {
            LOGGER.warn("[TreatmentWorkListPage] Date range popup input tıklamasıyla açılmadı, ikon denenecek.");

            if (icon != null) {
                safeClick(icon);
            } else {
                safeClick(wrapper);
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(popupLocator));
        }
    }

    /**
     * Açık olan tarih aralığı popup'ında "Bu Yıl" seçeneğini seçer.
     */
    public void selectThisYearOnDateRange() {
        LOGGER.info("[TreatmentWorkListPage] selectThisYearOnDateRange -> 'Bu Yıl'");

        By thisYearOption = By.xpath(
                "//div[contains(@class,'e-daterangepicker') and contains(@class,'e-popup')]" +
                        "//ul[contains(@class,'e-list-parent')]//li[@role='option'][contains(normalize-space(),'Bu Yıl')]"
        );

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(thisYearOption));
        safeClick(option);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(thisYearOption));
        } catch (TimeoutException ignored) {
        }

        waitForLoadingToFinish();
    }

    // ===================== FİLTRE & ARAMA ====================

    public void clickToolbarFilterButton() {
        LOGGER.info("[TreatmentWorkListPage] clickToolbarFilterButton");

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(detailedFilterButton));
        safeClick(btn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(filterDialogContainer));
        wait.until(ExpectedConditions.visibilityOfElementLocated(filterClearButton));
    }

    public void clearAndApplyDetailedFilter() {
        LOGGER.info("[TreatmentWorkListPage] clearAndApplyDetailedFilter");

        WebElement clear = wait.until(ExpectedConditions.elementToBeClickable(filterClearButton));
        safeClick(clear);

        WebElement apply = wait.until(ExpectedConditions.elementToBeClickable(filterApplyButton));
        safeClick(apply);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(filterApplyButton));
        } catch (TimeoutException ignored) {
        }

        waitForLoadingToFinish();
    }

    /**
     * Toolbar üzerindeki arama alanına hasta adını yazar ve arama ikonuna tıklar.
     * JS ile value set + input/change event'leri tetiklenir; ENTER kullanılmaz.
     */
    public void searchByPatientName(String patientName) {
        LOGGER.info("[TreatmentWorkListPage] searchByPatientName (SIMPLE) -> '{}'", patientName);

        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(toolbarSearchInput));
        scrollIntoView(searchInput);

        js.executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                searchInput,
                patientName
        );

        LOGGER.info("[TreatmentWorkListPage] search input JS set -> '{}'", searchInput.getAttribute("value"));

        WebElement searchIcon = wait.until(ExpectedConditions.elementToBeClickable(toolbarSearchIcon));
        safeClick(searchIcon);

        waitForLoadingToFinish();
    }

    // ===================== GRID DOĞRULAMA & DETAY ====================

    public String getFirstRowPatientName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mainGridContent));

        WebElement nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRowPatientNameCell));
        scrollIntoView(nameElement);
        return nameElement.getText().trim();
    }

    /**
     * Grid üzerinde ilk satırdaki hasta adının beklenen değerle eşit olup olmadığını
     * 5 saniye boyunca periyodik olarak kontrol eder.
     */
    public boolean isFirstRowPatientNameEquals(String expectedName) {
        long end = System.currentTimeMillis() + 5000;
        String actual = null;

        while (System.currentTimeMillis() < end) {
            actual = getFirstRowPatientName();
            LOGGER.info("[TreatmentWorkListPage] first row patient name check -> actual='{}', expected='{}'",
                    actual, expectedName);

            if (actual != null && actual.equalsIgnoreCase(expectedName)) {
                return true;
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
        }

        LOGGER.warn("[TreatmentWorkListPage] first row patient name eşleşmedi. final actual='{}', expected='{}'",
                actual, expectedName);
        return false;
    }

    public void clickFirstRowDetailButton() {
        LOGGER.info("[TreatmentWorkListPage] clickFirstRowDetailButton");

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(firstRowDetailButton));
        safeClick(btn);

        waitForLoadingToFinish();
    }

    // ===================== SEKME GEÇİŞLERİ ====================

    /**
     * Muayene sekmesine geçiş (mevcut muayene senaryoları için).
     */
    public void clickExaminationTab() {
        LOGGER.info("[TreatmentWorkListPage] clickExaminationTab -> 'Muayene'");

        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(examinationTab));
        safeClick(tab);

        try {
            wait.until(d -> driver.getCurrentUrl().contains("medical-card/overview"));
        } catch (TimeoutException ignored) {
        }

        waitForLoadingToFinish();
    }

    /**
     * Finans sekmesine geçiş.
     * Href'de "admission-service/finance-card/overview" bekleniyor.
     */
    public void clickFinanceTab() {
        LOGGER.info("[TreatmentWorkListPage] clickFinanceTab -> 'Finans'");

        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(financeTab));
        safeClick(tab);

        // URL'in Finans kartına geçtiğini bekleyelim (farklı ortamlarda querystring değişebilir)
        try {
            wait.until(d -> driver.getCurrentUrl().contains("finance-card/overview"));
        } catch (TimeoutException ignored) {
        }

        waitForLoadingToFinish();
    }
}

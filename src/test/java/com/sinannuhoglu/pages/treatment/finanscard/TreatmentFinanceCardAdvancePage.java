package com.sinannuhoglu.pages.treatment.finanscard;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class TreatmentFinanceCardAdvancePage {

    private static final Logger LOGGER = LogManager.getLogger(TreatmentFinanceCardAdvancePage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public TreatmentFinanceCardAdvancePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ================== LOCATORS ==================

    private final By advanceHeader = By.xpath(
            "//p[contains(@class,'text-sm') and contains(normalize-space(),'Avans')]"
    );

    private final By advancePlusButton = By.xpath(
            "//p[contains(@class,'text-sm') and contains(normalize-space(),'Avans')]" +
                    "/ancestor::div[contains(@class,'items-center justify-between')][1]" +
                    "//button[.//span[contains(@class,'hio-plus')]]"
    );

    private final By advanceListToggleButton = By.xpath(
            "//p[contains(@class,'text-sm') and contains(normalize-space(),'Avans')]" +
                    "/ancestor::div[contains(@class,'items-center justify-between')][1]" +
                    "//button[.//span[contains(@class,'hio-arrow-rtl')]]"
    );

    private final By advanceDialog = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')" +
                    " and .//form[@id='advance-create-form']]"
    );

    private final By advanceAmountInput = By.xpath(
            "//form[@id='advance-create-form']//input[starts-with(@id,'numeric-')]"
    );

    private final By advanceDescriptionTextarea = By.xpath(
            "//form[@id='advance-create-form']" +
                    "//div[contains(@class,'e-form-group') and .//label[contains(normalize-space(),'Açıklama')]]" +
                    "//textarea"
    );

    private final By advanceSaveButton = By.xpath(
            "//form[@id='advance-create-form']//button[@type='submit' and contains(normalize-space(),'Kaydet')]"
    );

    private final By advanceGridTable = By.xpath(
            "//div[starts-with(@id,'sfgrid') and contains(@class,'e-grid')]" +
                    "//table[contains(@class,'e-table')]"
    );

    private final By advanceCancelMenuItem = By.xpath(
            "//ul[contains(@class,'e-dropdown-menu')]//li[normalize-space()='İptal Et']"
    );

    private final By confirmYesButton = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//button[contains(@class,'e-primary') and contains(normalize-space(),'Evet')]"
    );

    // ================== HELPER METODLAR ==================

    private void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Ortak yazma metodu – timeout/stale durumunda bir kere daha deniyor.
     */
    private void type(By locator, String text) {
        RuntimeException lastError = null;

        for (int i = 0; i < 2; i++) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                element.clear();
                element.sendKeys(text);
                return;
            } catch (StaleElementReferenceException | TimeoutException e) {
                lastError = e;
                LOGGER.warn("[FinansKart][Avans] type() denemesi {} hata: {}", (i + 1), e.getClass().getSimpleName());
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (lastError != null) {
            throw lastError;
        }
    }

    /**
     * Avans header'ını ekrana getirir (scroll).
     */
    private WebElement scrollToAdvanceHeader() {
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(advanceHeader));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", header);
        return header;
    }

    /**
     * Avans grid'inde, verilen tutar ve açıklamaya göre satır locatörü.
     * Dinamik sfgrid ID'lerinden bağımsız çalışır.
     */
    private By buildAdvanceRowLocator(String amount, String description) {
        return By.xpath(
                "//div[starts-with(@id,'sfgrid') and contains(@class,'e-grid')]" +
                        "//table[contains(@class,'e-table')]//tbody//tr[" +
                        ".//td[3]//*[contains(normalize-space(),'" + amount + "')]" +
                        " and .//td[7]//*[contains(normalize-space(),'" + description + "')]" +
                        "]"
        );
    }

    // ================== İŞ AKIŞ METODLARI ==================

    /**
     * Avans penceresini (+) butonuna tıklayarak açar.
     */
    public void openAdvanceDialog() {
        LOGGER.info("[FinansKart][Avans] Avans oluşturma penceresi açılıyor...");

        scrollToAdvanceHeader();
        click(advancePlusButton);

        wait.until(ExpectedConditions.visibilityOfElementLocated(advanceDialog));

        try {
            Thread.sleep(700L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOGGER.info("[FinansKart][Avans] Avans oluşturma penceresi açıldı.");
    }

    /**
     * Açık olan Avans dialog'unda tutar ve açıklama girip Kaydet'e basar.
     */
    public void createAdvance(String amount, String description) {
        LOGGER.info("[FinansKart][Avans] Avans kaydı oluşturuluyor. Tutar: {}, Açıklama: {}",
                amount, description);

        type(advanceAmountInput, amount);
        type(advanceDescriptionTextarea, description);

        click(advanceSaveButton);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(advanceDialog));
        LOGGER.info("[FinansKart][Avans] Avans kaydı Kaydet ile başarıyla oluşturuldu.");
    }

    /**
     * Avans listesini (slide-over paneldeki grid) açar.
     */
    private void openAdvanceListPanel() {
        LOGGER.info("[FinansKart][Avans] Avans listesi paneli açılıyor...");

        scrollToAdvanceHeader();

        if (driver.findElements(advanceGridTable).isEmpty()) {
            click(advanceListToggleButton);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(advanceGridTable));

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOGGER.info("[FinansKart][Avans] Avans listesi paneli açık ve grid görüntüleniyor.");
    }

    /**
     * Avans grid'inde ilgili satırı bulur, tutar ve açıklamayı doğrular,
     * ardından 3 nokta menüsünden "İptal Et" seçeneğini seçer ve onay penceresinde "Evet"e basar.
     */
    public void cancelAdvance(String amount, String description) {
        LOGGER.info("[FinansKart][Avans] Avans kaydı iptal edilecek. Tutar: {}, Açıklama: {}",
                amount, description);

        openAdvanceListPanel();

        By rowLocator = buildAdvanceRowLocator(amount, description);

        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));

        WebElement amountCell = row.findElement(By.xpath("./td[3]"));
        String amountText = amountCell.getText().trim();
        Assert.assertTrue(
                amountText.contains(amount),
                "[FinansKart][Avans] Beklenen tutar bulunamadı. Beklenen: " + amount + " | Gerçek: " + amountText
        );

        WebElement descriptionCell = row.findElement(By.xpath("./td[7]"));
        String descriptionText = descriptionCell.getText().trim();
        Assert.assertTrue(
                descriptionText.contains(description),
                "[FinansKart][Avans] Beklenen açıklama bulunamadı. Beklenen: " + description + " | Gerçek: " + descriptionText
        );

        LOGGER.info("[FinansKart][Avans] Grid satırı doğrulandı. Tutar: '{}', Açıklama: '{}'",
                amountText, descriptionText);

        WebElement dropdownButton = row.findElement(
                By.xpath("./td[8]//button[.//span[contains(@class,'e-caret')]]")
        );
        js.executeScript("arguments[0].click();", dropdownButton);

        WebElement cancelItem = wait.until(
                ExpectedConditions.elementToBeClickable(advanceCancelMenuItem)
        );
        js.executeScript("arguments[0].click();", cancelItem);

        WebElement yesBtn = wait.until(
                ExpectedConditions.elementToBeClickable(confirmYesButton)
        );
        js.executeScript("arguments[0].click();", yesBtn);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(confirmYesButton));

        LOGGER.info("[FinansKart][Avans] Avans kaydı için İptal Et işlemi onaylandı.");
    }

    /**
     * Avans grid'inde ilgili tutar + açıklamaya sahip satırın tamamen silindiğini doğrular.
     */
    public void verifyAdvanceDeleted(String amount, String description) {
        LOGGER.info("[FinansKart][Avans] Avans kaydının silindiği doğrulanıyor. Tutar: {}, Açıklama: {}",
                amount, description);

        openAdvanceListPanel();

        By rowLocator = buildAdvanceRowLocator(amount, description);

        wait.until(driver -> driver.findElements(rowLocator).isEmpty());

        boolean isDeleted = driver.findElements(rowLocator).isEmpty();
        Assert.assertTrue(
                isDeleted,
                "[FinansKart][Avans] Beklenen avans kaydı hala listede görünüyor! Tutar: " +
                        amount + " | Açıklama: " + description
        );

        LOGGER.info("[FinansKart][Avans] Avans kaydı listeden başarıyla silinmiş durumda.");
    }
}

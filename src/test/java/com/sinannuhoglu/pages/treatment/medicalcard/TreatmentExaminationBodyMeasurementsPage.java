package com.sinannuhoglu.pages.treatment.medicalcard;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tıbbi İşlemler > Muayene > Vücut Ölçümleri ekranı
 * - Muayene detay sayfasındaki "Vücut Ölçümleri" kartından açılan popup
 * - Geçmiş kontrolü, aynı güne ait kaydın silinmesi
 * - Yeni Boy / Kilo kaydı oluşturulması ve geçmişte doğrulanması
 */
public class TreatmentExaminationBodyMeasurementsPage {

    private static final Logger LOGGER =
            LogManager.getLogger(TreatmentExaminationBodyMeasurementsPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // ================ LOCATORS ================

    /**
     * Muayene sayfasındaki "Vücut Ölçümleri" kartının butonu.
     * Kart:
     *  - bg-white rounded-xl ...
     *  - içinde <p>Vücut Ölçümleri</p> ve sağda ikon buton bulunuyor.
     */
    private final By bodyMeasurementsCardButton = By.xpath(
            "//div[contains(@class,'bg-white') and contains(@class,'rounded-xl')]" +
                    "[.//p[normalize-space()='Vücut Ölçümleri']]" +
                    "//button[contains(@class,'e-btn')]"
    );

    // Geçmiş accordions (ilk item "Geçmiş" olarak kullanılıyor)
    private final By historyAccordionItem =
            By.cssSelector("div.e-acrdn-item[data-index='0']");
    private final By historyAccordionHeader =
            By.cssSelector("div.e-acrdn-item[data-index='0'] div.e-acrdn-header");

    public TreatmentExaminationBodyMeasurementsPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ================ HELPERS ================

    private void scrollIntoView(WebElement el) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
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

    /**
     * Vücut Ölçümleri popup'ını temsil eden dialog elementini döndürür.
     * ID her açılışta değişebildiği için:
     *  - e-dialog container içindeki
     *  - içerik kısmında "Boy" label'ı olan dialog seçiliyor.
     */
    private WebElement getBodyMeasurementsDialog() {
        By dialogLocator = By.xpath(
                "//div[contains(@class,'e-dlg-container')]//div[contains(@class,'e-dialog')]" +
                        "[.//label[normalize-space()='Boy']]"
        );
        return wait.until(ExpectedConditions.visibilityOfElementLocated(dialogLocator));
    }

    /**
     * Dialog içindeki numerik input'u label metnine göre bulur.
     * Örn: "Boy", "Kilo"
     */
    private WebElement findNumericInputByLabel(WebElement dialog, String labelText) {
        WebElement label = dialog.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        WebElement container = label.findElement(
                By.xpath("./ancestor::div[contains(@class,'flex') and contains(@class,'flex-col')]")
        );

        return container.findElement(
                By.cssSelector("input[type='text'], input.e-control")
        );
    }

    /**
     * Numerik alanlara değer yazarken güvenli şekilde temizleyip yeni değeri girer.
     */
    private void setNumericInputValue(WebElement input, String value) {
        scrollIntoView(input);

        try {
            input.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", input);
        }

        // CTRL+A + DELETE ile temizleme
        try {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            input.sendKeys(Keys.DELETE);
        } catch (Exception ignored) {
        }

        // Hala değer varsa BACK_SPACE ile silmeye çalış
        try {
            String current = input.getAttribute("value");
            if (current != null && !current.isEmpty()) {
                for (int i = 0; i < current.length(); i++) {
                    input.sendKeys(Keys.BACK_SPACE);
                }
            }
        } catch (Exception ignored) {
        }

        input.sendKeys(value);
        input.sendKeys(Keys.TAB);
    }

    /**
     * Bugünün tarihini ekranda göründüğü formatta (dd.MM.yyyy) döndürür.
     */
    private String getTodayFormatted() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return today.format(formatter);
    }

    // ================ PUBLIC ACTIONS ================

    /**
     * Muayene sayfasında Vücut Ölçümleri kartındaki butona tıklar ve popup'ın açılmasını bekler.
     */
    public void openBodyMeasurementsDialog() {
        LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] openBodyMeasurementsDialog");

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(bodyMeasurementsCardButton)
        );
        safeClick(btn);

        // Dialog'un açılmasını bekle
        getBodyMeasurementsDialog();
    }

    /**
     * Vücut Ölçümleri popup'ında "Geçmiş" accordion item'ını açar.
     */
    public void openHistorySection() {
        LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] openHistorySection");

        WebElement dialog = getBodyMeasurementsDialog();

        WebElement item = dialog.findElement(historyAccordionItem);
        WebElement header = item.findElement(historyAccordionHeader);

        String expanded = item.getAttribute("aria-expanded");
        // Kapalıysa aç
        if (expanded == null || !"true".equalsIgnoreCase(expanded)) {
            safeClick(header);
        }

        // Panelin açıldığından emin ol
        By visiblePanelRows = By.xpath(
                ".//div[contains(@class,'e-acrdn-panel') and @role='region' and @aria-hidden='false']" +
                        "//tbody/tr"
        );
        try {
            dialog.findElements(visiblePanelRows); // sadece existence; boş olabilir
        } catch (Exception ignored) {
        }
    }

    /**
     * Geçmiş bölümünde ilk satır bugüne ait ise 6. kolondaki sil butonuna tıklar.
     * Bugüne ait kayıt yoksa işlem yapmaz.
     */
    public void deleteTodayRecordIfExists() {
        LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] deleteTodayRecordIfExists");

        WebElement dialog = getBodyMeasurementsDialog();
        String today = getTodayFormatted();
        LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] today = {}", today);

        By rowsLocator = By.xpath(
                ".//div[contains(@class,'e-acrdn-panel') and @role='region' and @aria-hidden='false']" +
                        "//tbody/tr"
        );

        List<WebElement> rows;
        try {
            rows = dialog.findElements(rowsLocator);
        } catch (NoSuchElementException e) {
            LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] history rows not found -> nothing to delete");
            return;
        }

        if (rows == null || rows.isEmpty()) {
            LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] history empty -> nothing to delete");
            return;
        }

        WebElement firstRow = rows.get(0);

        // 1. sütundaki tarih
        WebElement dateCell = firstRow.findElement(
                By.xpath("./td[1]//*[self::span or self::p or self::div]")
        );
        String dateText = dateCell.getText().trim();
        LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] first row date = {}", dateText);

        if (!today.equals(dateText)) {
            LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] first row is not today -> skip delete");
            return;
        }

        // 6. sütundaki sil butonu (son sütun)
        WebElement deleteButton = firstRow.findElement(
                By.xpath("./td[last()]//button")
        );
        safeClick(deleteButton);

        // Satırın silinmesini bekle
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.stalenessOf(firstRow));
        } catch (TimeoutException ignored) {
        }
    }

    /**
     * Boy alanına verilen değeri yazar.
     */
    public void setHeight(String heightValue) {
        LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] setHeight -> {}", heightValue);
        WebElement dialog = getBodyMeasurementsDialog();
        WebElement input = findNumericInputByLabel(dialog, "Boy");
        setNumericInputValue(input, heightValue);
    }

    /**
     * Kilo alanına verilen değeri yazar.
     */
    public void setWeight(String weightValue) {
        LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] setWeight -> {}", weightValue);
        WebElement dialog = getBodyMeasurementsDialog();
        WebElement input = findNumericInputByLabel(dialog, "Kilo");
        setNumericInputValue(input, weightValue);
    }

    /**
     * Vücut Ölçümleri popup'ındaki "Kaydet" butonuna tıklar ve dialog'un kapanmasını bekler.
     */
    public void clickSaveMeasurements() {
        LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] clickSaveMeasurements");

        WebElement dialog = getBodyMeasurementsDialog();

        WebElement saveButton = dialog.findElement(
                By.xpath(".//button[normalize-space()='Kaydet']")
        );
        safeClick(saveButton);

        // Dialog'un kapanmasını bekle
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOf(dialog));
        } catch (TimeoutException ignored) {
        }
    }

    /**
     * Geçmiş alanında bugüne ait (dd.MM.yyyy) bir satır olup olmadığını döndürür.
     */
    public boolean isTodayRecordPresentInHistory() {
        LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] isTodayRecordPresentInHistory");

        WebElement dialog = getBodyMeasurementsDialog();
        String today = getTodayFormatted();

        By rowsLocator = By.xpath(
                ".//div[contains(@class,'e-acrdn-panel') and @role='region' and @aria-hidden='false']" +
                        "//tbody/tr"
        );

        List<WebElement> rows;
        try {
            rows = dialog.findElements(rowsLocator);
        } catch (NoSuchElementException e) {
            LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] no history rows");
            return false;
        }

        if (rows == null || rows.isEmpty()) {
            LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] history is empty");
            return false;
        }

        for (WebElement row : rows) {
            WebElement dateCell = row.findElement(
                    By.xpath("./td[1]//*[self::span or self::p or self::div]")
            );
            String dateText = dateCell.getText().trim();
            LOGGER.info("[TreatmentExaminationBodyMeasurementsPage] row date = {}", dateText);

            if (today.equals(dateText)) {
                return true;
            }
        }

        return false;
    }
}

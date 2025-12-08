package com.sinannuhoglu.pages.appointment.definitions.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Randevu Kaynakları > Mesailer sekmesi için Page Object
 */
public class AppointmentResourcesShiftsPage {

    private static final Logger LOGGER = LogManager.getLogger(AppointmentResourcesShiftsPage.class);

    private final WebDriver driver;
    protected final WebDriverWait wait;

    public AppointmentResourcesShiftsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ============ LOCATORS ============

    private final By shiftsTab = By.xpath(
            "//div[contains(@class,'e-toolbar-items')]//div[contains(@class,'e-toolbar-item')]" +
                    "[.//div[contains(@class,'e-tab-text') and normalize-space()='Mesailer']]"
    );

    private final By addButton = By.xpath("//div[@id='add']//button[contains(@class,'e-btn')]");

    private final By createDateInput      = By.id("create-date");
    private final By createStartTimeInput = By.id("create-start-time");
    private final By createStartTimeIcon  = By.xpath("//input[@id='create-start-time']/following-sibling::span[contains(@class,'e-time-icon')]");
    private final By createEndTimeInput   = By.id("create-end-time");
    private final By createEndTimeIcon    = By.xpath("//input[@id='create-end-time']/following-sibling::span[contains(@class,'e-time-icon')]");
    private final By descriptionTextArea  = By.id("create-description");

    private final By startTimePopupWrapper = By.id("create-start-time_popup");
    private final By endTimePopupWrapper   = By.id("create-end-time_popup");

    private final By gridContentTable = By.cssSelector("div.e-gridcontent table#Grid_content_table");

    private final By deleteDialogContainer =
            By.cssSelector("div[id^='modal-dialog-'].e-dlg-container");

    private final By deleteYesButton =
            By.cssSelector("div[id^='modal-dialog-'].e-dlg-container button.e-btn.e-primary");

    // ============ ACTION METHODS ============

    /**
     * Randevu kaynağı detayında "Mesailer" sekmesine tıklar ve sekmenin açıldığını doğrular.
     */
    public void openShiftsTab() {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(shiftsTab));
        scrollIntoView(tab);
        tab.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(addButton));
        LOGGER.info("[Shifts] Mesailer sekmesi açıldı.");
    }

    /**
     * Mesailer sekmesinde Yeni Ekle butonuna tıklar ve formun açıldığını doğrular.
     */
    public void clickAddButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addButton));
        scrollIntoView(btn);
        btn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(createDateInput));
        LOGGER.info("[Shifts] Mesailer sekmesinde 'Yeni Ekle' butonuna tıklandı ve form açıldı.");
    }

    /**
     * Tarih alanına verilen değeri yazar.
     * İzinler’de kullandığımız yaklaşım: CTRL/COMMAND + A + BACK_SPACE + yeni değer.
     */
    public void setDate(String date) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(createDateInput));
        scrollIntoView(input);
        input.click();

        try {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        } catch (Exception ignore) { }
        try {
            input.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } catch (Exception ignore) { }
        input.sendKeys(Keys.BACK_SPACE);

        input.sendKeys(date);
        input.sendKeys(Keys.TAB); // blur

        LOGGER.info("[Shifts] Tarih alanına '{}' değeri girildi.", date);
    }

    /**
     * Başlangıç saati alanından verilen saati (örn: 08:30) seçer.
     */
    public void selectStartTime(String time) {
        WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(createStartTimeIcon));
        scrollIntoView(icon);
        icon.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(startTimePopupWrapper));

        By timeOption = By.xpath("//div[@id='create-start-time_popup']//li[normalize-space()='" + time + "']");
        WebElement optionElement = wait.until(ExpectedConditions.elementToBeClickable(timeOption));
        scrollIntoView(optionElement);
        optionElement.click();

        wait.until(ExpectedConditions.attributeContains(createStartTimeInput, "aria-expanded", "false"));

        LOGGER.info("[Shifts] Başlangıç saati '{}' olarak seçildi.", time);
    }

    /**
     * Bitiş saati alanından verilen saati (örn: 18:30) seçer.
     */
    public void selectEndTime(String time) {
        WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(createEndTimeIcon));
        scrollIntoView(icon);
        icon.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(endTimePopupWrapper));

        By timeOption = By.xpath("//div[@id='create-end-time_popup']//li[normalize-space()='" + time + "']");
        WebElement optionElement = wait.until(ExpectedConditions.elementToBeClickable(timeOption));
        scrollIntoView(optionElement);
        optionElement.click();

        wait.until(ExpectedConditions.attributeContains(createEndTimeInput, "aria-expanded", "false"));

        LOGGER.info("[Shifts] Bitiş saati '{}' olarak seçildi.", time);
    }

    /**
     * Açıklama alanına metin girer. Stale element durumuna karşı yeniden deneme yapar.
     */
    public void setDescription(String description) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionTextArea));
                scrollIntoView(textArea);
                textArea.click();

                textArea.clear();
                textArea.sendKeys(description);

                LOGGER.info("[Shifts] Açıklama alanına metin yazıldı. Son değer: {}", description);
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                LOGGER.warn("[Shifts] Açıklama alanı stale oldu, yeniden denenecek. Attempt: {}", attempts);
            }
        }
        throw new RuntimeException("Açıklama alanına veri girilirken stale element hatası alındı.");
    }

    /**
     * Kaydet butonuna tıklar ve popup’ın kapanmasını bekler.
     * Artık generic dialog değil, tıklanan butonun DOM'dan kopması/invisible olması bekleniyor.
     */
    public void clickSave() {
        LOGGER.info("[Shifts] Mesailer formunda Kaydet butonuna tıklanıyor.");

        WebElement saveButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.e-dialog.e-popup-open button.e-btn.e-primary")
                )
        );

        scrollIntoView(saveButton);
        saveButton.click();
        LOGGER.info("[Shifts] Kaydet butonuna tıklandı, popup'ın kapanması bekleniyor.");

        wait.until(ExpectedConditions.or(
                ExpectedConditions.stalenessOf(saveButton),
                ExpectedConditions.invisibilityOf(saveButton)
        ));

        LOGGER.info("[Shifts] Mesailer popup'ı kapandı (Kaydet butonu artık görünür değil).");
    }

    /**
     * Grid üzerinde ilgili Mesai kaydının oluşturulduğunu doğrular.
     * 1. sütun: Tarih
     * 2. sütun: Başlangıç saati
     * 3. sütun: Bitiş saati
     * 4. sütun: Açıklama
     */
    public void verifyShiftRow(String date, String startTime, String endTime, String description) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContentTable));

        By rowLocator = buildShiftRowLocator(date, startTime, endTime, description);
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));
        scrollIntoView(row);

        LOGGER.info(
                "[Shifts] Grid satırı bulundu. Tarih: '{}', Başlangıç: '{}', Bitiş: '{}', Açıklama: '{}'",
                date, startTime, endTime, description
        );
    }

    /**
     * Verilen kriterlere göre satırı bulur, 5. sütundaki üç nokta menü (dropdown) butonuna tıklar.
     */
    public void openActionsMenuForShift(String date, String startTime, String endTime, String description) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContentTable));

        By rowLocator = buildShiftRowLocator(date, startTime, endTime, description);
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));
        scrollIntoView(row);

        WebElement menuButton = row.findElement(
                By.cssSelector("td:nth-child(5) button.e-dropdown-btn")
        );

        wait.until(ExpectedConditions.elementToBeClickable(menuButton));
        scrollIntoView(menuButton);
        menuButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.e-dropdown-popup.e-popup-open ul[role='menu']"))
        );

        LOGGER.info("[Shifts] Grid satırındaki üç nokta menüsü açıldı (Tarih: {}, {}-{}, Açıklama: {}).",
                date, startTime, endTime, description);
    }


    /**
     * Açılmış olan üç nokta menüsünde "Sil" seçeneğine tıklar.
     */
    public void clickDeleteInActionsMenu() {
        By deleteItem = By.xpath(
                "//div[contains(@class,'e-dropdown-popup') and contains(@class,'e-popup-open')]" +
                        "//ul[@role='menu']//li[normalize-space()='Sil']"
        );

        WebElement deleteElement = wait.until(ExpectedConditions.elementToBeClickable(deleteItem));
        scrollIntoView(deleteElement);
        deleteElement.click();

        LOGGER.info("[Shifts] Üç nokta menüsünde 'Sil' seçeneğine tıklandı.");
    }

    /**
     * Silme onay penceresinde Evet (primary) butonuna tıklar ve dialog'un kapanmasını bekler.
     */
    public void confirmDeleteYes() {
        LOGGER.info("[Shifts] Silme onay penceresinde 'Evet' butonuna tıklanıyor.");

        wait.until(ExpectedConditions.visibilityOfElementLocated(deleteDialogContainer));

        WebElement yesButton = wait.until(
                ExpectedConditions.elementToBeClickable(deleteYesButton)
        );
        scrollIntoView(yesButton);
        yesButton.click();
        LOGGER.info("[Shifts] 'Evet' butonuna tıklandı, dialog'un kapanması bekleniyor.");

        // 3) Dialog'un kapanmasını bekle
        wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteDialogContainer));

        LOGGER.info("[Shifts] Silme onay dialog'u kapandı.");
    }


    /**
     * Grid üzerinde ilgili Mesai kaydının artık görünmediğini (silindiğini) doğrular.
     */
    public void verifyShiftRowDeleted(String date, String startTime, String endTime, String description) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContentTable));

        By rowLocator = buildShiftRowLocator(date, startTime, endTime, description);

        boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(rowLocator));
        if (!invisible) {
            throw new AssertionError(String.format(
                    "[Shifts] Beklenen satır hala grid üzerinde görünüyor! Tarih: %s, Saat: %s - %s, Açıklama: %s",
                    date, startTime, endTime, description
            ));
        }

        LOGGER.info(
                "[Shifts] Grid üzerinde ilgili mesai satırının silindiği doğrulandı. Tarih: '{}', Başlangıç: '{}', Bitiş: '{}', Açıklama: '{}'",
                date, startTime, endTime, description
        );
    }

    // ============ PRIVATE HELPERS ============

    /**
     * Ortak satır locator'ı – hem doğrulama hem silme akışında kullanılıyor.
     */
    private By buildShiftRowLocator(String date, String startTime, String endTime, String description) {
        String dateWithDash = date.replace('.', '-');

        String xpath =
                "//div[contains(@class,'e-gridcontent')]//table[@id='Grid_content_table']//tr[" +
                        ".//td[1][contains(normalize-space(),'" + date + "') or contains(normalize-space(),'" + dateWithDash + "')]" +
                        " and .//td[2][normalize-space()='" + startTime + "']" +
                        " and .//td[3][normalize-space()='" + endTime + "']" +
                        " and .//td[4][normalize-space()='" + description + "']" +
                        "]";

        return By.xpath(xpath);
    }

    private void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        } catch (Exception ignore) {
        }
    }
}

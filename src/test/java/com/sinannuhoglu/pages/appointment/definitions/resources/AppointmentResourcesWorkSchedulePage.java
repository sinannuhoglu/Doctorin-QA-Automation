package com.sinannuhoglu.pages.appointment.definitions.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Randevu Kaynakları > Çalışma Takvimi sekmesi için Page Object
 */
public class AppointmentResourcesWorkSchedulePage {

    private static final Logger LOGGER = LogManager.getLogger(AppointmentResourcesWorkSchedulePage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;
    private final Actions actions;
    private static final Locale TR = Locale.forLanguageTag("tr");

    private enum SaveOutcome {
        SUCCESS,
        CONFLICT,
        UNKNOWN
    }

    private String lastSelectedDayName;
    private String lastSelectedStartTime;
    private String lastSelectedEndTime;
    private String lastSelectedBranch;
    private String lastSelectedAppointmentTypesCsv;
    private String lastSelectedPlatformsCsv;
    private String lastSelectedDepartmentsCsv;

    public AppointmentResourcesWorkSchedulePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
    }

    // =====================================================
    // GENEL HELPER METODLAR
    // =====================================================

    private void scrollIntoViewCenter(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", element);
        } catch (Exception ignored) {
        }
    }

    /**
     * Elemanı tıklamadan önce ortaya kaydırıp tıklanabilir olmasını bekler.
     * Gerekirse Actions veya JS click ile fallback yapar.
     */
    private void safeClick(WebElement element) {
        try {
            scrollIntoViewCenter(element);
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e1) {
            try {
                actions.moveToElement(element).click().perform();
            } catch (Exception e2) {
                try {
                    js.executeScript("arguments[0].click();", element);
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Input alanını temizleyip verilen değeri yazar.
     * CTRL/CMD + A + BACK_SPACE + JS ile temizleme denemeleri içerir.
     */
    private void clearAndType(WebElement input, String value) {
        scrollIntoViewCenter(input);
        try {
            input.click();
        } catch (Exception ignored) {
        }

        try {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        } catch (Exception ignored) {
        }
        try {
            input.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } catch (Exception ignored) {
        }
        try {
            input.sendKeys(Keys.BACK_SPACE);
        } catch (Exception ignored) {
        }

        try {
            js.executeScript("arguments[0].value = '';", input);
        } catch (Exception ignored) {
        }

        input.sendKeys(value);
    }

    private void scrollIntoView(WebElement element) {
        scrollIntoViewCenter(element);
    }

    // =====================================================
    // LOCATORS
    // =====================================================

    // Tab bar içindeki "Çalışma Takvimi" sekmesi
    private final By workScheduleTab = By.xpath(
            "//div[contains(@class,'e-toolbar-items')]//div[contains(@class,'e-toolbar-item')]" +
                    "[.//div[contains(@class,'e-tab-text') and normalize-space()='Çalışma Takvimi']]"
    );

    private final By workScheduleDialog = By.cssSelector(
            "div[id^='dialog-'][class*='e-dlg-modal'][class*='e-popup-open']"
    );

    private final By startTimeInput = By.id("work-schedule-start-time");
    private final By startTimePopupWrapper = By.id("work-schedule-start-time_popup");

    private final By endTimeInput = By.id("work-schedule-end-time");
    private final By endTimePopupWrapper = By.id("work-schedule-end-time_popup");

    private final By branchInput = By.id("work-schedule-facility");
    private final By branchPopupWrapper = By.id("work-schedule-facility_popup");

    private final By appointmentTypesInput = By.id("work-schedule-appointment-types");
    private final By appointmentTypesPopupWrapper = By.id("work-schedule-appointment-types_popup");

    private final By platformsInput = By.id("work-schedule-source-types");
    private final By platformsPopupWrapper = By.id("work-schedule-source-types_popup");

    private final By branchesInput = By.id("work-schedule-branches");
    private final By branchesPopupWrapper = By.id("work-schedule-branches_popup");

    private final By saveButtonInDialog = By.xpath(
            "//div[@id and contains(@class,'e-dlg-modal') and contains(@class,'e-popup-open')]" +
                    "//button[normalize-space()='Kaydet']"
    );

    private final By cancelButtonInDialog = By.xpath(
            "//div[@id and contains(@class,'e-dlg-modal') and contains(@class,'e-popup-open')]" +
                    "//button[normalize-space()='İptal']"
    );

    private final By deleteButtonInDialog = By.xpath(
            "//div[@id and contains(@class,'e-dlg-modal') and contains(@class,'e-popup-open')]" +
                    "//button[normalize-space()='Sil']"
    );

    private final By deleteConfirmYesButton = By.xpath(
            "//button[normalize-space()='Evet' or normalize-space()='Evet, sil' or normalize-space()='Tamam']"
    );

    private static final By WORK_SCHEDULE_TABLE = By.cssSelector(
            ".e-content .e-table-container table.e-schedule-table"
    );

    private static final By WORK_SCHEDULE_ROWS = By.cssSelector(
            ".e-table-wrap.e-vertical-view.e-week-view.e-current-panel " +
                    "table.e-schedule-table.e-content-table tbody[role='rowgroup'] > tr"
    );

    private static final By WORK_SCHEDULE_SPINNER = By.cssSelector("div.e-spinner-pane");

    private static final By TOAST_CONTAINER = By.cssSelector("div[id^='toast_'][class*='e-toast']");

    private static final String BRANCHES_POPUP_ID = "work-schedule-branches_popup";

    // =====================================================
    // PUBLIC ACTION METHODS
    // =====================================================

    /**
     * Kaynak detayında "Çalışma Takvimi" sekmesine tıklar ve haftalık tablo yüklenene kadar bekler.
     */
    public void openWorkScheduleTab() {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(workScheduleTab));
        safeClick(tab);

        waitUntilTableReady();
        LOGGER.info("[WorkSchedule] Çalışma Takvimi sekmesi açıldı.");
    }

    /**
     * Çalışma Takvimi sekmesinde verilen gün adına (Pazartesi, Salı, ..) ait
     * haftalık görünüm hücresine tıklar ve formun açılmasını bekler.
     */
    public void selectDayCell(String dayName) {
        String trimmed = dayName == null ? "" : dayName.trim();
        this.lastSelectedDayName = trimmed;

        int index = dayIndex(trimmed); // Pazartesi=0, Salı=1...

        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            LOGGER.info("[WorkSchedule] '{}' günü için hücre tıklama denemesi {} / {}",
                    trimmed, attempt, maxAttempts);

            waitUntilTableReady();

            By dayCellSelector = By.cssSelector(
                    ".e-content .e-table-container .e-content-wrap " +
                            "table.e-schedule-table.e-content-table td.e-work-cells"
            );

            List<WebElement> days = wait.until(drv -> {
                List<WebElement> list = drv.findElements(dayCellSelector);
                return list.size() >= 7 ? list : null; // Pzt–Paz en az 7 hücre
            });

            if (index < 0 || index >= days.size()) {
                throw new IllegalStateException("[WorkSchedule] Gün indexi geçersiz: "
                        + trimmed + " -> " + index + ", hücre sayısı=" + days.size());
            }

            WebElement dayCell = days.get(index);
            scrollIntoView(dayCell);
            safeClick(dayCell);

            LOGGER.info("[WorkSchedule] '{}' gün hücresine tıklandı (deneme {}).", trimmed, attempt);

            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(startTimeInput));

                LOGGER.info("[WorkSchedule] Takvim Planı Yönetimi formu {}. denemede açıldı.", attempt);
                return;
            } catch (TimeoutException tex) {
                LOGGER.warn("[WorkSchedule] {}. denemede dialog açılmadı, tekrar denenecek.", attempt);
            }
        }

        throw new TimeoutException("[WorkSchedule] Çalışma Takvimi dialogu "
                + maxAttempts + " denemede de açılamadı (work-schedule-start-time görünmedi).");
    }

    /**
     * Başlangıç saatini (örn. 08:00) doğrudan inputa yazar.
     */
    public void selectStartTime(String time) {
        LOGGER.info("[WorkSchedule] Başlangıç Saati alanı dolduruluyor: {}", time);
        this.lastSelectedStartTime = time;

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(startTimeInput));
        clearAndType(input, time);

        wait.until(d -> time.equals(input.getAttribute("value")));
        input.sendKeys(Keys.TAB);

        LOGGER.info("[WorkSchedule] Başlangıç Saati '{}' olarak yazıldı.", time);
    }

    /**
     * Bitiş saatini (örn. 18:00) doğrudan inputa yazar.
     */
    public void selectEndTime(String time) {
        LOGGER.info("[WorkSchedule] Bitiş Saati alanı dolduruluyor: {}", time);
        this.lastSelectedEndTime = time;

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(endTimeInput));
        clearAndType(input, time);

        wait.until(d -> time.equals(input.getAttribute("value")));
        input.sendKeys(Keys.TAB);

        LOGGER.info("[WorkSchedule] Bitiş Saati '{}' olarak yazıldı.", time);
    }

    /**
     * Şube dropdown'ında tek bir seçenek seçer (örn. Çankaya).
     */
    public void selectBranch(String branchName) {
        this.lastSelectedBranch = branchName;
        selectSingleFromDropdown(branchInput, branchPopupWrapper, branchName);
        LOGGER.info("[WorkSchedule] Şube alanında '{}' seçildi.", branchName);
    }

    /**
     * Randevu tipi alanında çoklu seçim yapar.
     * optionsCsv -> "Kontrol Muayenesi, Video Muayenesi, Muayene" gibi
     */
    public void selectAppointmentTypesByCsv(String optionsCsv) {
        this.lastSelectedAppointmentTypesCsv = optionsCsv;
        List<String> options = splitCsv(optionsCsv);
        selectMultipleFromDropdown(appointmentTypesInput, appointmentTypesPopupWrapper, options);
        LOGGER.info("[WorkSchedule] Randevu Tipi alanında seçilenler: {}", options);
    }

    /**
     * Platform alanında çoklu seçim yapar.
     */
    public void selectPlatformsByCsv(String optionsCsv) {
        this.lastSelectedPlatformsCsv = optionsCsv;
        List<String> options = splitCsv(optionsCsv);
        selectMultipleFromDropdown(platformsInput, platformsPopupWrapper, options);
        LOGGER.info("[WorkSchedule] Platform alanında seçilenler: {}", options);
    }

    /**
     * Departmanlar alanında çoklu seçim yapar.
     */
    public void selectDepartmentsByCsv(String optionsCsv) {
        this.lastSelectedDepartmentsCsv = optionsCsv;
        List<String> options = splitCsv(optionsCsv);
        selectMultipleFromDropdown(branchesInput, branchesPopupWrapper, options);
        LOGGER.info("[WorkSchedule] Departmanlar alanında seçilenler: {}", options);
    }

    /**
     * Çalışma Takvimi penceresinde Kaydet butonuna tıklar.
     *
     * Normal durumda:
     *   - Kaydet sonrası dialog 15 sn içinde kapanır -> başarı kabul edilir.
     *
     * Eğer dialog kapanmaz ve bir toast görünürse (ör. "farklı takvim planı bulunmaktadır"):
     *   - Çakışma kabul edilir, sil + yeniden oluştur akışı devreye girer.
     */
    public void clickSaveInWorkScheduleDialog() {
        LOGGER.info("[WorkSchedule] Çalışma Takvimi dialogunda Kaydet butonuna tıklanıyor.");

        WebElement saveButton;
        try {
            saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveButtonInDialog));
        } catch (TimeoutException e) {
            LOGGER.error("[WorkSchedule] 'Kaydet' butonu bulunamadı veya tıklanabilir değil. Locator'ı kontrol etmek gerekiyor.", e);
            throw e;
        }

        safeClick(saveButton);

        boolean dialogClosed = waitDialogClosedShort();
        if (dialogClosed) {
            LOGGER.info("[WorkSchedule] Dialog kapandı. Başarılı kayıt olarak kabul ediliyor.");
            return;
        }

        boolean toastVisible = isAnyToastVisibleNow();
        LOGGER.info("[WorkSchedule] Kaydet sonrası dialog kapanmadı. toastVisible={}", toastVisible);

        if (toastVisible && lastSelectedDayName != null && lastSelectedStartTime != null) {
            LOGGER.info("[WorkSchedule] Çakışma/uyarı algılandı. Mevcut plan SİLİNİP aynı değerlerle yeniden oluşturulacak.");
            handleConflictByDeleteAndRecreate();
            return;
        }

        String msg = String.format(
                "[WorkSchedule] Kaydet sonrası dialog kapanmadı ve çakışma senaryosu da tetiklenemedi. Gün: %s, Başlangıç Saati: %s",
                lastSelectedDayName, lastSelectedStartTime
        );
        LOGGER.error(msg);
        throw new IllegalStateException(msg);
    }

    // =====================================================
    // SAVE OUTCOME / CONFLICT HANDLING
    // =====================================================

    /**
     * Kaydet sonrası toastları okuyup sonucu döndürür.
     */
    private SaveOutcome detectSaveOutcome() {
        try {
            WebDriverWait toastWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            toastWait.until(drv -> {
                List<WebElement> toasts = drv.findElements(TOAST_CONTAINER);
                for (WebElement t : toasts) {
                    try {
                        if (t.isDisplayed()) {
                            return true;
                        }
                    } catch (Exception ignore) {}
                }
                return null;
            });
        } catch (TimeoutException e) {
            return SaveOutcome.UNKNOWN;
        }

        try {
            List<WebElement> toasts = driver.findElements(TOAST_CONTAINER);
            for (WebElement t : toasts) {
                if (!t.isDisplayed()) continue;

                String text = t.getText();
                String lower = text == null ? "" : text.toLowerCase(TR);
                LOGGER.info("[WorkSchedule] Toast metni: {}", text);

                if (lower.contains("farklı takvim planı bulunmaktadır")) {
                    return SaveOutcome.CONFLICT;
                }
                if (lower.contains("başarı") || lower.contains("başarılı") || lower.contains("success")) {
                    return SaveOutcome.SUCCESS;
                }
            }
        } catch (Exception e) {
            LOGGER.warn("[WorkSchedule] Toast metni okunurken hata: {}", e.getMessage());
        }

        return SaveOutcome.UNKNOWN;
    }

    /**
     * Conflict durumunda:
     *  1) Açık dialog varsa İptal ile kapatır.
     *  2) Toast’ların kaybolmasını ve gridin ready olmasını bekler.
     *  3) Aynı gün + başlangıç saati hücresini açıp mevcut planı siler.
     *  4) Aynı hücre için yeni dialogu açar, son değerleri tekrar doldurur.
     *  5) Son kez Kaydet’e basar; kapanmıyorsa loglayıp İptal ile zorla kapatmaya çalışır.
     */
    private void handleConflictByDeleteAndRecreate() {
        LOGGER.info("[WorkSchedule] Conflict sonrası 'Sil + yeniden oluştur' akışı başlıyor. Gün={}, Saat={}",
                lastSelectedDayName, lastSelectedStartTime);

        try {
            WebElement cancelBtn = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(cancelButtonInDialog));
            safeClick(cancelBtn);
            waitDialogClosedShort();
            LOGGER.info("[WorkSchedule] Conflict sonrasında mevcut dialog İptal ile kapatıldı.");
        } catch (Exception e) {
            LOGGER.info("[WorkSchedule] Conflict sonrasında aktif dialog bulunamadı ya da zaten kapalı: {}", e.getMessage());
        }

        waitToastGoneSoft();
        waitUntilTableReady();

        WebElement cell;
        try {
            cell = findWorkCellFor(lastSelectedDayName, lastSelectedStartTime);
            scrollIntoView(cell);
            safeClick(cell);
            wait.until(ExpectedConditions.visibilityOfElementLocated(startTimeInput));
            LOGGER.info("[WorkSchedule] Çakışan plan için dialog yeniden açıldı (Sil için).");
        } catch (Exception e) {
            throw new RuntimeException(
                    "[WorkSchedule] Çakışma durumunda ilgili hücre bulunamadı veya tıklanamadı. " +
                            "Gün: " + lastSelectedDayName + ", Saat: " + lastSelectedStartTime, e
            );
        }

        try {
            WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(deleteButtonInDialog));
            safeClick(deleteBtn);
            LOGGER.info("[WorkSchedule] 'Sil' butonuna tıklandı.");

            try {
                WebElement yesBtn = new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(deleteConfirmYesButton));
                safeClick(yesBtn);
                LOGGER.info("[WorkSchedule] Silme onayında 'Evet' / 'Tamam' butonuna tıklandı.");
            } catch (TimeoutException ignore) {
                LOGGER.info("[WorkSchedule] Ayrı bir silme onay penceresi bulunamadı. Devam ediliyor.");
            }

            waitDialogClosedShort();
            waitToastGoneSoft();
            waitUntilTableReady();
            LOGGER.info("[WorkSchedule] Plan silindikten sonra dialog kapandı ve grid yenilendi.");
        } catch (Exception e) {
            throw new RuntimeException(
                    "[WorkSchedule] Conflict sonrası mevcut planı silme adımında hata oluştu. Gün: "
                            + lastSelectedDayName + ", Saat: " + lastSelectedStartTime, e
            );
        }

        WebElement emptyCell = findWorkCellFor(lastSelectedDayName, lastSelectedStartTime);
        scrollIntoView(emptyCell);
        safeClick(emptyCell);
        wait.until(ExpectedConditions.visibilityOfElementLocated(startTimeInput));
        LOGGER.info("[WorkSchedule] Silme sonrası yeni plan oluşturmak için dialog tekrar açıldı.");

        if (lastSelectedStartTime != null) {
            selectStartTime(lastSelectedStartTime);
        }
        if (lastSelectedEndTime != null) {
            selectEndTime(lastSelectedEndTime);
        }
        if (lastSelectedBranch != null) {
            selectBranch(lastSelectedBranch);
        }
        if (lastSelectedAppointmentTypesCsv != null) {
            selectAppointmentTypesByCsv(lastSelectedAppointmentTypesCsv);
        }
        if (lastSelectedPlatformsCsv != null) {
            selectPlatformsByCsv(lastSelectedPlatformsCsv);
        }
        if (lastSelectedDepartmentsCsv != null) {
            selectDepartmentsByCsv(lastSelectedDepartmentsCsv);
        }

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveButtonInDialog));
        safeClick(saveButton);

        boolean dialogClosedAfter = waitDialogClosedShort();
        SaveOutcome outcomeAfter = detectSaveOutcome();

        LOGGER.info("[WorkSchedule] Sil + yeniden oluşturma sonrası Kaydet durum: dialogClosed={}, outcome={}",
                dialogClosedAfter, outcomeAfter);

        if (!dialogClosedAfter) {
            LOGGER.error(
                    "[WorkSchedule] Sil + yeniden oluşturma sonrası dialog kendiliğinden kapanmadı. " +
                            "Gün={}, Saat={}, outcome={}",
                    lastSelectedDayName, lastSelectedStartTime, outcomeAfter
            );
            forceCloseDialogIfStillOpen();
        }
    }

    // =====================================================
    // GRID / TABLE HELPER'LARI
    // =====================================================

    private void waitUntilTableReady() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(WORK_SCHEDULE_TABLE));

        wait.until(drv -> {
            List<WebElement> spinners = drv.findElements(WORK_SCHEDULE_SPINNER);
            for (WebElement s : spinners) {
                try {
                    if (s.isDisplayed()) {
                        return false;
                    }
                } catch (Exception ignore) {
                }
            }
            return true;
        });
    }

    /**
     * Haftalık tabloda verilen gün adı (Pazartesi, Çarşamba, ..)
     * ve başlangıç saati (örn. 08:00) için ilgili hücreyi döndürür.
     */
    private WebElement findWorkCellFor(String dayName, String startTime) {
        waitUntilTableReady();

        int dayIdx = dayIndex(dayName);
        int rowIdx = timeToRowIndex(startTime);

        List<WebElement> rows = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(WORK_SCHEDULE_ROWS)
        );

        if (rowIdx < 0 || rowIdx >= rows.size()) {
            throw new IllegalStateException(
                    "[WorkSchedule] Saat için satır indexi geçersiz. time=" + startTime +
                            ", rowIdx=" + rowIdx + ", toplamSatir=" + rows.size()
            );
        }

        WebElement row = rows.get(rowIdx);
        List<WebElement> cells = row.findElements(By.cssSelector("td.e-work-cells"));

        if (dayIdx < 0 || dayIdx >= cells.size()) {
            throw new IllegalStateException(
                    "[WorkSchedule] Gün için sütun indexi geçersiz. day=" + dayName +
                            ", dayIdx=" + dayIdx + ", toplamSutun=" + cells.size()
            );
        }

        return cells.get(dayIdx);
    }

    /**
     * Zamanı (HH:mm) formatına göre 0–47 arası satır indexine çevirir.
     * 00:00 -> 0, 00:30 -> 1, 01:00 -> 2, ... 23:30 -> 47
     */
    private int timeToRowIndex(String time) {
        if (time == null || time.trim().isEmpty()) {
            throw new IllegalArgumentException("[WorkSchedule] timeToRowIndex için geçersiz saat: " + time);
        }

        String[] parts = time.trim().split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("[WorkSchedule] HH:mm formatında değil: " + time);
        }

        int hour = Integer.parseInt(parts[0].trim());
        int minute = Integer.parseInt(parts[1].trim());

        if (hour < 0 || hour > 23 || (minute != 0 && minute != 30)) {
            throw new IllegalArgumentException("[WorkSchedule] Desteklenmeyen saat aralığı: " + time);
        }

        return hour * 2 + (minute >= 30 ? 1 : 0);
    }

    private boolean waitDialogClosedShort() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(workScheduleDialog));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isAnyToastVisibleNow() {
        try {
            List<WebElement> toasts = driver.findElements(TOAST_CONTAINER);
            for (WebElement t : toasts) {
                if (t.isDisplayed()) {
                    LOGGER.info("[WorkSchedule] Görünen toast metni: {}", t.getText());
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void waitToastGoneSoft() {
        try {
            WebDriverWait sw = new WebDriverWait(driver, Duration.ofSeconds(5));
            sw.until(drv -> {
                List<WebElement> toasts = drv.findElements(TOAST_CONTAINER);
                for (WebElement t : toasts) {
                    try {
                        if (t.isDisplayed()) {
                            return false;
                        }
                    } catch (Exception ignore) {
                    }
                }
                return true;
            });
        } catch (Exception ignore) {
        }
    }

    // =====================================================
    // DROPDOWN / MULTI-SELECT HELPER'LARI
    // =====================================================

    @SuppressWarnings("unused")
    private void selectTimeFromPopup(By inputLocator, By popupWrapperLocator, String time) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        scrollIntoView(input);
        input.click();

        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(popupWrapperLocator));
        String popupId = popup.getAttribute("id");

        By timeOptionLocator = By.xpath(
                "//div[@id='" + popupId + "']//li[normalize-space()='" + time + "']"
        );

        WebElement timeOption = wait.until(ExpectedConditions.elementToBeClickable(timeOptionLocator));
        scrollIntoView(timeOption);
        timeOption.click();

        wait.until(ExpectedConditions.invisibilityOf(popup));
    }

    private void selectSingleFromDropdown(By inputLocator, By popupWrapperLocator, String optionText) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        scrollIntoView(input);
        safeClick(input);

        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(popupWrapperLocator));
        String popupId = popup.getAttribute("id");

        By optionLocator = By.xpath(
                "//div[@id='" + popupId + "']//li[normalize-space()='" + optionText.trim() + "']"
        );

        WebElement optionElement = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        scrollIntoView(optionElement);
        safeClick(optionElement);

        try {
            wait.until(ExpectedConditions.invisibilityOf(popup));
        } catch (TimeoutException ignored) {
        }
    }

    private void selectMultipleFromDropdown(By inputLocator, By popupWrapperLocator, List<String> options) {
        if (options == null || options.isEmpty()) {
            LOGGER.warn("[WorkSchedule] Çoklu seçim için verilen liste boş, işlem yapılmayacak.");
            return;
        }

        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        scrollIntoView(input);
        safeClick(input);

        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(popupWrapperLocator));
        String popupId = popup.getAttribute("id");

        if (BRANCHES_POPUP_ID.equals(popupId)) {
            LOGGER.debug("[WorkSchedule] Departman multi-select state based seçim başlıyor. (popupId={})", popupId);
            applyStateBasedMultiSelect(popup, options);
        } else {
            clearMultiSelectPopupSelections(popupId);

            try {
                if (!popup.isDisplayed()) {
                    LOGGER.debug("[WorkSchedule] Clear sonrası popup kapanmış, tekrar açılıyor.");
                    safeClick(input);
                    popup = wait.until(ExpectedConditions.visibilityOfElementLocated(popupWrapperLocator));
                    popupId = popup.getAttribute("id");
                }
            } catch (StaleElementReferenceException sere) {
                LOGGER.debug("[WorkSchedule] Popup DOM’u yenilenmiş, tekrar bulunuyor.");
                safeClick(input);
                popup = wait.until(ExpectedConditions.visibilityOfElementLocated(popupWrapperLocator));
                popupId = popup.getAttribute("id");
            }

            for (String option : options) {
                String trimmed = option.trim();
                if (trimmed.isEmpty()) continue;

                By optionLocator = By.xpath(
                        "//div[@id='" + popupId + "']//li[normalize-space()='" + trimmed + "']"
                );

                WebElement optionElement = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
                scrollIntoView(optionElement);
                safeClick(optionElement);

                LOGGER.debug("[WorkSchedule] Çoklu dropdown içinde '{}' seçildi. (popupId={})", trimmed, popupId);
            }
        }

        try {
            WebElement header = driver.findElement(
                    By.cssSelector("div[id^='dialog-'][class*='e-dlg-modal'] .e-dlg-header")
            );
            header.click();
        } catch (Exception ignore) {
        }
    }

    private List<String> splitCsv(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Multi-select popup'ında daha önce seçilmiş olan tüm seçenekleri temizler.
     */
    private void clearMultiSelectPopupSelections(String popupId) {
        try {
            WebElement popup = driver.findElement(By.id(popupId));

            try {
                By selectAllLocator = By.cssSelector("#" + popupId + " li.e-selectall-parent");
                List<WebElement> selectAllRows = popup.findElements(selectAllLocator);

                if (!selectAllRows.isEmpty()) {
                    WebElement selectAll = selectAllRows.get(0);

                    int safety = 0;
                    while (safety < 3) {
                        List<WebElement> selectedItems = popup.findElements(
                                By.cssSelector("#" + popupId + " li[aria-selected='true'], " +
                                        "#" + popupId + " li.e-active, " +
                                        "#" + popupId + " li.e-selected")
                        );
                        if (selectedItems.isEmpty()) break;

                        scrollIntoView(selectAll);
                        selectAll.click();
                        LOGGER.debug("[WorkSchedule] Select-all satırına tıklandı (deneme {}).", safety + 1);

                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException ignored) {}

                        safety++;
                    }
                }
            } catch (Exception e) {
                LOGGER.debug("[WorkSchedule] Select-all satırı bulunamadı veya kullanılamadı: {}", e.getMessage());
            }

            List<WebElement> stillSelected = popup.findElements(
                    By.cssSelector("#" + popupId + " li[aria-selected='true'], " +
                            "#" + popupId + " li.e-active, " +
                            "#" + popupId + " li.e-selected")
            );

            for (WebElement item : stillSelected) {
                try {
                    scrollIntoView(item);
                    item.click();
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException ignored) {}
                } catch (StaleElementReferenceException ignored) {
                }
            }

            if (BRANCHES_POPUP_ID.equals(popupId)) {
                try {
                    LOGGER.debug("[WorkSchedule] Departman popup'ı için ekstra checkbox temizliği çalıştırılıyor.");
                    js.executeScript(
                            "var popup = arguments[0];" +
                                    "var boxes = popup.querySelectorAll('li input[type=\"checkbox\"]');" +
                                    "if (!boxes) return;" +
                                    "boxes.forEach(function(cb){" +
                                    "  try { if (cb.checked) { cb.click(); } } catch(e) {}" +
                                    "});",
                            popup
                    );
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException ignored) {}
                } catch (Exception e) {
                    LOGGER.warn("[WorkSchedule] Departman popup'ında checkbox temizliği yapılırken hata oluştu: {}", e.getMessage());
                }
            }

            LOGGER.debug("[WorkSchedule] Multi-select popup'ındaki eski seçimler temizlendi. (popupId={})", popupId);
        } catch (Exception e) {
            LOGGER.warn("[WorkSchedule] Multi-select seçimleri temizlenirken hata oluştu (popupId={}): {}", popupId, e.getMessage());
        }
    }

    /**
     * Departman popup'ında state-based multi-select.
     */
    private void applyStateBasedMultiSelect(WebElement popup, List<String> options) {
        Set<String> desired = options.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.toLowerCase(TR))
                .collect(Collectors.toSet());

        List<WebElement> items = popup.findElements(By.cssSelector("li.e-list-item"));

        for (WebElement item : items) {
            try {
                String text = item.getText();
                if (text == null) text = "";
                String label = text.trim();
                String lower = label.toLowerCase(TR);

                if (lower.contains("hepsini seç") || lower.contains("tümünün seçimini kaldır")) {
                    continue;
                }

                boolean shouldBeSelected = desired.contains(lower);

                boolean isSelected = false;
                try {
                    String cls = item.getAttribute("class");
                    String aria = item.getAttribute("aria-selected");
                    if (cls != null && (cls.contains("e-active") || cls.contains("e-selected"))) {
                        isSelected = true;
                    }
                    if ("true".equalsIgnoreCase(aria)) {
                        isSelected = true;
                    }
                    try {
                        WebElement cb = item.findElement(By.cssSelector("input[type='checkbox']"));
                        if (cb.isSelected()) {
                            isSelected = true;
                        }
                    } catch (NoSuchElementException ignore) {
                    }
                } catch (Exception ignore) {
                }

                if (shouldBeSelected != isSelected) {
                    scrollIntoView(item);
                    item.click();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }

            } catch (StaleElementReferenceException ignored) {
            }
        }

        LOGGER.debug("[WorkSchedule] Departman popup'ında state-based seçim tamamlandı. İstenenler: {}", desired);
    }

    // =====================================================
    // DIALOG DURUMU
    // =====================================================

    private boolean isWorkScheduleDialogOpen() {
        try {
            List<WebElement> dialogs = driver.findElements(workScheduleDialog);
            for (WebElement d : dialogs) {
                try {
                    if (d.isDisplayed()) {
                        return true;
                    }
                } catch (Exception ignore) {}
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void forceCloseDialogIfStillOpen() {
        try {
            if (!isWorkScheduleDialogOpen()) return;

            LOGGER.warn("[WorkSchedule] Dialog hâlâ açık görünüyor, 'İptal' ile zorla kapatılacak.");

            WebElement cancelBtn = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(cancelButtonInDialog));
            safeClick(cancelBtn);
            waitDialogClosedShort();
        } catch (Exception e) {
            LOGGER.error("[WorkSchedule] Dialog zorla kapatılmaya çalışılırken hata oluştu: {}", e.getMessage());
        }
    }

    /**
     * TR gün ismini (Pazartesi, Salı, ...) 0–6 indexine çevirir.
     */
    private int dayIndex(String dayTr) {
        String d = dayTr == null ? "" : dayTr.trim().toLowerCase(TR);

        Map<String, Integer> map = new HashMap<>();
        map.put("pazartesi", 0);
        map.put("salı", 1);
        map.put("sali", 1);
        map.put("çarşamba", 2);
        map.put("carsamba", 2);
        map.put("perşembe", 3);
        map.put("persembe", 3);
        map.put("cuma", 4);
        map.put("cumartesi", 5);
        map.put("pazar", 6);

        Integer idx = map.get(d);
        if (idx == null) {
            throw new IllegalArgumentException("Geçersiz gün: " + dayTr);
        }
        return idx;
    }
}

package com.sinannuhoglu.pages.appointment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

/**
 * Randevular ekranı – Filtre Paneli + Slot & Hasta / Etkinlik Akışı Page Object
 */
public class AppointmentAppointmentsFilterPage {

    private static final Logger LOGGER = LogManager.getLogger(AppointmentAppointmentsFilterPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;
    private final Actions actions;

    private Integer lastClickedSlotY = null;

    private String lastCreatedAppointmentStartTime = null;

    public AppointmentAppointmentsFilterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
    }

    // ===================== FILTRE LOCATOR'LARI =====================

    private static final By FILTER_BUTTON =
            By.cssSelector("button[data-testid='filter-button']");

    private static final By FILTER_PANEL =
            By.cssSelector("div.appointment-filters");

    private static final By GLOBAL_OVERLAY =
            By.cssSelector("div.backdrop-blur-xs.z-20");

    private static final By LOCATION_COMBO_INPUT =
            By.cssSelector("div[data-testid='location-filter'] span[role='combobox'] input");

    private static final By DEPARTMENT_COMBO_INPUT =
            By.cssSelector("div[data-testid='department-filter'] span[role='combobox'] input");

    private static final By DOCTOR_MULTI_INPUT =
            By.cssSelector("div[data-testid='doctor-filter'] input[role='combobox']");

    private static final By OPTION_POPUP =
            By.cssSelector("div.e-ddl.e-popup-open");

    private static final By MULTI_POPUP =
            By.cssSelector("div.e-ddl.e-multi-select-list-wrapper.e-popup-open");

    private static final By DOCTOR_CHIP_CLOSE =
            By.cssSelector("span.e-chips .e-chips-close");

    private static final By ACCEPT_BUTTON =
            By.cssSelector("button[data-testid='accept-button']");

    private static final By TODAY_BUTTON =
            By.xpath("//button[.//span[normalize-space()='Bugün']]");

    private static final By SCHEDULE_CONTAINER =
            By.cssSelector("div.appointments-scheduler");

    // ===================== SLOT / TAKVİM LOCATOR'LARI =====================

    private static final By CONTENT_WRAP =
            By.cssSelector("div[id^='Schedule-'] .e-table-container .e-content-wrap");

    private static final By DAY_VIEW_ROWS =
            By.cssSelector("div[id^='Schedule-'] .e-table-container .e-content-wrap table.e-content-table tbody[role='rowgroup'] tr");

    private static final By WORK_CELL_GROUP0 =
            By.cssSelector("td.e-work-cells[data-group-index='0']");

    private static final By WORK_CELL_DEFAULT =
            By.cssSelector("td.e-work-cells");

    private static final By APPOINTMENT_TILE_ANY =
            By.cssSelector("div.e-appointment.e-lib.e-draggable[role='button'][data-group-index='0']");

    private static final By APPOINTMENT_DETAILS =
            By.cssSelector(".e-appointment-details, [data-testid='event-details']");

    // ===================== SIDEBAR (Hasta Arama / Etkinlik Paneli) =====================

    private static final By SIDEBAR_TITLE =
            By.cssSelector("[data-testid='sidebar-title'], h2.text-sm.font-semibold, h2.text-base.font-semibold");

    private static final By SIDEBAR_CONTENT =
            By.cssSelector("[data-testid='sidebar-content'], .sidebar-main-section, [data-testid='patient-search-section']");

    private static final By PATIENT_SEARCH_INPUT =
            By.cssSelector("input[data-testid='appointment-patient-search'], input[placeholder='Ara']");

    private static final By PATIENT_SEARCH_BUTTON =
            By.cssSelector("form button[type='submit'], button[data-testid='appointment-search-button']");

    private static final By PATIENT_RESULT_CONTAINER =
            By.cssSelector("[data-testid^='patient-item-']");

    private static final By PATIENT_RESULT_NAME =
            By.cssSelector("[data-testid^='patient-name-'], p[title]");

    private static final By EVENT_TAB =
            By.xpath("//div[contains(@class,'e-tab-text') and normalize-space()='Etkinlik']");

    // ===================== RANDEVU / ETKİNLİK DETAYI – KAYDET & SAAT =====================

    private static final By APPOINTMENT_FORM_SECTION =
            By.cssSelector(
                    "[data-testid='appointment-form-section'], " +
                            "[data-testid='event-form-section'], " +
                            "[data-testid*='form-section']"
            );

    private static final By APPOINTMENT_FORM_ACTIONS =
            By.cssSelector("[data-testid='form-actions']");

    private static final By APPOINTMENT_SAVE_BUTTON =
            By.cssSelector("[data-testid='save-button'], button[type='submit']");

    private static final By APPOINTMENT_START_TIME_INPUT =
            By.xpath("//label[contains(normalize-space(),'Başlangıç Saati')]/following::input[@type='text'][1]");

    // ===================== QUICK POPUP & CHECK-IN / ADMISSION =====================

    private static final By QUICK_POPUP =
            By.cssSelector("div.e-quick-popup-wrapper.e-popup-open");

    private static final By QUICK_POPUP_HEADER =
            By.cssSelector(".e-popup-header-title-text, [data-testid='quick-info-header']");

    private static final By QUICK_POPUP_STATUS_BUTTON =
            By.cssSelector("div.e-quick-popup-wrapper button[data-testid='status-button']");

    private static final By APPOINTMENT_FOOTER =
            By.cssSelector("div.e-quick-popup-wrapper [data-testid='appointment-footer'], " +
                    "div.e-quick-popup-wrapper [data-testid='closing-event-footer']");

    // ====== KAPANIŞ ETKİNLİĞİ – ETKİNLİK DETAYI POPUP & SİL ======

    private static final By CLOSING_EVENT_DELETE_BUTTON =
            By.cssSelector("button[data-testid='closing-event-delete-button'], " +
                    "div.e-quick-popup-wrapper button.e-control.e-btn.e-lib.e-danger");

    // Kapanış etkinliği kartı içindeki label
    private static final By CLOSING_EVENT_LABEL =
            By.cssSelector("div[data-testid='event-closing']");

    // ===================== HASTA KABUL (ADMISSION SIDEBAR) =====================

    private static final By ADMISSION_SIDEBAR_ROOT =
            By.cssSelector("#AdmissionSidebarId, div#AdmissionSidebarId");

    private static final By ADMISSION_SAVE_BUTTON =
            By.cssSelector("#AdmissionSidebarId button[type='submit'], #AdmissionSidebarId button[data-testid='save-button']");

    // ===================== TAKVİM PLAN DIŞI ONAY MODALI =====================

    private static final By OUT_OF_PLAN_DIALOG_CONTAINER =
            By.cssSelector("div.e-dialog.e-dlg-modal.e-popup-open[role='dialog'], " +
                    "div[id^='dialog-'][class*='e-dialog']");

    private static final By OUT_OF_PLAN_DIALOG_YES_BUTTON =
            By.cssSelector("div.e-footer-content button.e-control.e-btn.e-lib.e-primary");

    // ===================== GENEL DIALOG (Sil onayı vb.) =====================

    private static final By GENERIC_DIALOG_CONTAINER =
            By.cssSelector("div.e-dialog.e-dlg-modal.e-popup-open[role='dialog']");

    private static final By GENERIC_DIALOG_PRIMARY_BUTTON =
            By.cssSelector("button#okay-button, div.e-footer-content button.e-control.e-btn.e-lib.e-primary");

    // ===================== GENERIC HELPERS =====================

    private void waitOverlayGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(GLOBAL_OVERLAY));
        } catch (Exception ignored) {
        }
    }

    private void scrollTo(WebElement el) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
        } catch (Exception ignored) {
        }
    }

    private void safeClick(WebElement el) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (Exception e1) {
            try {
                actions.moveToElement(el).click().perform();
            } catch (Exception e2) {
                js.executeScript("arguments[0].click();", el);
            }
        }
    }

    /**
     * Locator tabanlı güvenli tıklama – stale element durumunda elementi yeniden bulur.
     */
    private void safeClick(By locator) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement el = shortWait.until(ExpectedConditions.elementToBeClickable(locator));
                scrollTo(el);
                el.click();
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("safeClick(By) - StaleElementReference, retry={}", attempt + 1);
            } catch (Exception e) {
                LOGGER.warn("safeClick(By) - click failed (attempt {}): {}", attempt + 1, e.getClass().getSimpleName());
                if (attempt == 2) {
                    WebElement el = driver.findElement(locator);
                    scrollTo(el);
                    js.executeScript("arguments[0].click();", el);
                    return;
                }
            }
        }
    }

    private WebElement waitPopup(By locator) {
        return wait.until(driver -> {
            List<WebElement> items = driver.findElements(locator);
            for (WebElement p : items) {
                try {
                    if (p.isDisplayed()) return p;
                } catch (StaleElementReferenceException ignored) {
                }
            }
            return null;
        });
    }

    private String normalizeStartTime(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.length() >= 5 && raw.charAt(2) == ':') {
            return raw.substring(0, 5);
        }
        return raw;
    }

    // ===================== SIDEBAR & MODAL DURUM YARDIMCILARI =====================

    private boolean isSidebarOpen() {
        try {
            WebElement title = driver.findElement(SIDEBAR_TITLE);
            return title.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isOutOfPlanDialogVisible() {
        List<WebElement> dialogs = driver.findElements(OUT_OF_PLAN_DIALOG_CONTAINER);
        for (WebElement dlg : dialogs) {
            try {
                if (dlg.isDisplayed() && dlg.getRect().getHeight() > 0) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    /**
     * Takvim yönetimi plan dışı slot onayı için "Evet" butonuna tıklar.
     * Modal DOM'a geç geldiği durumlarda tekrar tekrar arayıp tıklar.
     */
    private void clickOutOfPlanYesIfPresent() {
        // Modal hiç yoksa hemen çık
        if (!isOutOfPlanDialogVisible()) {
            return;
        }

        LOGGER.info("Takvim yönetim planı dışı slot için onay penceresi açıldı. 'Evet' tıklanacak.");

        WebDriverWait modalWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            WebElement dialog = modalWait.until(d -> {
                List<WebElement> list = d.findElements(OUT_OF_PLAN_DIALOG_CONTAINER);
                for (WebElement dlg : list) {
                    try {
                        if (dlg.isDisplayed() && dlg.getRect().getHeight() > 0) {
                            return dlg;
                        }
                    } catch (StaleElementReferenceException ignored) {
                    }
                }
                return null;
            });

            if (dialog == null) {
                LOGGER.warn("Out-of-plan dialog bulunamadı (null döndü).");
                return;
            }

            WebElement yesButton = null;
            List<WebElement> buttons = dialog.findElements(OUT_OF_PLAN_DIALOG_YES_BUTTON);
            for (WebElement btn : buttons) {
                try {
                    if (btn.isDisplayed() && btn.isEnabled()) {
                        yesButton = btn;
                        break;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }

            if (yesButton == null) {
                LOGGER.warn("Out-of-plan dialog içinde 'Evet' butonu bulunamadı.");
                return;
            }

            scrollTo(yesButton);
            try {
                yesButton.click();
            } catch (Exception e1) {
                LOGGER.warn("yesButton.click() başarısız, JS ile denenecek: {}", e1.getClass().getSimpleName());
                try {
                    js.executeScript("arguments[0].click();", yesButton);
                } catch (Exception e2) {
                    LOGGER.error("Out-of-plan 'Evet' JS click de başarısız: {}", e2.getClass().getSimpleName());
                }
            }

            LOGGER.info("'Evet' butonuna tıklandı.");

        } catch (TimeoutException e) {
            LOGGER.warn("Out-of-plan dialog için bekleme süresi doldu: {}", e.getMessage());
        }
    }

    /**
     * Açık olan herhangi bir e-dialog içinde primary (Tamam/Evet) butonuna tıklar.
     * Sil onayı gibi dialoglar için kullanılır.
     */
    private void clickPrimaryOnGenericDialogIfPresent() {
        WebDriverWait dialogWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement primaryBtn = dialogWait.until(d -> {
            List<WebElement> dialogs = d.findElements(GENERIC_DIALOG_CONTAINER);
            for (WebElement dlg : dialogs) {
                try {
                    if (!dlg.isDisplayed() || dlg.getRect().getHeight() <= 0) {
                        continue;
                    }
                    List<WebElement> buttons = dlg.findElements(GENERIC_DIALOG_PRIMARY_BUTTON);
                    for (WebElement b : buttons) {
                        if (b.isDisplayed() && b.isEnabled()) {
                            return b;
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
            return null;
        });

        if (primaryBtn == null) {
            LOGGER.warn("Genel dialog içinde primary (Tamam/Evet) butonu bulunamadı.");
            return;
        }

        scrollTo(primaryBtn);
        safeClick(primaryBtn);

        dialogWait.until(d -> {
            List<WebElement> dialogs = d.findElements(GENERIC_DIALOG_CONTAINER);
            for (WebElement dlg : dialogs) {
                try {
                    if (dlg.isDisplayed() && dlg.getRect().getHeight() > 0) {
                        return false;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
            return true;
        });

        LOGGER.info("Genel dialogda primary (Tamam/Evet) butonuna tıklandı.");
    }

    // ===================== FILTER BUTTON =====================

    public void clickFilterButton() {
        waitOverlayGone();
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(FILTER_BUTTON));
        scrollTo(btn);
        safeClick(btn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(FILTER_PANEL));
        waitOverlayGone();
    }

    // ===================== LOCATION =====================

    public void selectLocation(String text) {
        waitOverlayGone();

        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(LOCATION_COMBO_INPUT));
        scrollTo(input);
        safeClick(input);

        WebElement popup = waitPopup(OPTION_POPUP);

        By option = By.xpath(".//li[normalize-space()='" + text + "']");
        WebElement li = popup.findElement(option);
        scrollTo(li);
        safeClick(li);

        waitOverlayGone();
    }

    // ===================== DEPARTMENT =====================

    public void selectDepartment(String text) {
        waitOverlayGone();

        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(DEPARTMENT_COMBO_INPUT));
        scrollTo(input);
        safeClick(input);

        WebElement popup = waitPopup(OPTION_POPUP);

        By option = By.xpath(".//li[normalize-space()='" + text + "']");
        WebElement li = popup.findElement(option);
        scrollTo(li);
        safeClick(li);

        waitOverlayGone();
    }

    // ===================== DOCTOR (MULTI SELECT) =====================

    public void selectDoctor(String doctorName) {
        waitOverlayGone();

        clearChips();

        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(DOCTOR_MULTI_INPUT));
        scrollTo(input);
        safeClick(input);

        WebElement popup = waitPopup(MULTI_POPUP);

        By option = By.xpath(".//li[normalize-space()='" + doctorName + "']");
        WebElement li = popup.findElement(option);
        scrollTo(li);
        safeClick(li);

        waitOverlayGone();
    }

    private void clearChips() {
        List<WebElement> chips = driver.findElements(DOCTOR_CHIP_CLOSE);
        for (WebElement chip : chips) {
            try {
                scrollTo(chip);
                safeClick(chip);
                Thread.sleep(100);
            } catch (Exception ignored) {
            }
        }
    }

    // ===================== ACCEPT =====================

    public void clickFilterAccept() {
        waitOverlayGone();

        WebElement accept = wait.until(ExpectedConditions.elementToBeClickable(ACCEPT_BUTTON));
        scrollTo(accept);
        safeClick(accept);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(FILTER_PANEL));
        wait.until(ExpectedConditions.visibilityOfElementLocated(SCHEDULE_CONTAINER));

        LOGGER.info("Filtre uygulandı ve panel kapandı.");
    }

    // ===================== TODAY BUTTON =====================

    public void clickTodayButton() {
        WebElement today = wait.until(ExpectedConditions.elementToBeClickable(TODAY_BUTTON));
        scrollTo(today);
        safeClick(today);

        wait.until(ExpectedConditions.visibilityOfElementLocated(SCHEDULE_CONTAINER));
    }

    // ===================== SLOT HELPER METOTLARI =====================

    private List<WebElement> dayRows() {
        return wait.until(d -> {
            List<WebElement> rows = d.findElements(DAY_VIEW_ROWS);
            return (rows != null && !rows.isEmpty()) ? rows : null;
        });
    }

    private WebElement extractCellFromRow(WebElement row) {
        List<WebElement> group0 = row.findElements(WORK_CELL_GROUP0);
        if (!group0.isEmpty()) {
            return group0.get(0);
        }
        List<WebElement> any = row.findElements(WORK_CELL_DEFAULT);
        if (!any.isEmpty()) {
            return any.get(0);
        }
        try {
            return row.findElement(By.cssSelector("td"));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private boolean isCellEmpty(WebElement cell) {
        Rectangle cellRect = cell.getRect();

        List<WebElement> tiles = driver.findElements(APPOINTMENT_TILE_ANY);
        for (WebElement t : tiles) {
            try {
                if (!t.isDisplayed() || t.getRect().getHeight() <= 0) continue;
                Rectangle r = t.getRect();

                boolean verticalOverlap =
                        r.getY() < cellRect.getY() + cellRect.getHeight() &&
                                r.getY() + r.getHeight() > cellRect.getY();

                boolean horizontalOverlap =
                        r.getX() < cellRect.getX() + cellRect.getWidth() &&
                                r.getX() + r.getWidth() > cellRect.getX();

                if (verticalOverlap && horizontalOverlap) {
                    return false;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return true;
    }

    /**
     * Takvimde ilk BOŞ randevu slotunu bulup tıklar
     * ve slotun Y koordinatını kaydeder.
     */
    public void clickFirstEmptySlot() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(SCHEDULE_CONTAINER));

        try {
            WebElement wrap = driver.findElement(CONTENT_WRAP);
            js.executeScript("arguments[0].scrollTop = 0;", wrap);
        } catch (Exception ignored) {
        }

        List<WebElement> rows = dayRows();
        if (rows == null || rows.isEmpty()) {
            throw new AssertionError("Takvim satırları bulunamadı!");
        }

        for (WebElement row : rows) {
            WebElement cell = extractCellFromRow(row);
            if (cell == null) continue;

            scrollTo(cell);

            if (isCellEmpty(cell)) {
                lastClickedSlotY = cell.getRect().getY();

                safeClick(cell);
                LOGGER.info("Boş slot bulundu ve tıklandı. Y={}", lastClickedSlotY);

                waitSidebarOpened();
                return;
            }
        }

        throw new AssertionError("Hiç boş randevu slotu bulunamadı.");
    }

    /**
     * Slot tıklandıktan sonra:
     *  - Ya direkt Hasta Arama sidebari açılır
     *  - Ya da önce Takvim Planı dışı onay modali gelir, 'Evet' denir, sonra sidebar açılır.
     */
    private void waitSidebarOpened() {
        wait.until(driver -> isSidebarOpen() || isOutOfPlanDialogVisible());

        if (isOutOfPlanDialogVisible() && !isSidebarOpen()) {
            clickOutOfPlanYesIfPresent();
        }

        wait.until(driver -> isSidebarOpen());

        WebElement title = driver.findElement(SIDEBAR_TITLE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(SIDEBAR_CONTENT));

        LOGGER.info("Hasta Arama / Randevu oluşturma paneli açıldı. Title: {}", title.getText());
    }

    // ===================== HASTA ARAMA & SEÇİM =====================

    public void searchPatientInSidebar(String fullName) {
        WebElement input = wait.until(driver -> {
            List<WebElement> list = driver.findElements(PATIENT_SEARCH_INPUT);
            return list.isEmpty() ? null : list.get(0);
        });

        scrollTo(input);
        input.clear();
        input.sendKeys(fullName);

        WebElement searchBtn = wait.until(driver -> {
            List<WebElement> list = driver.findElements(PATIENT_SEARCH_BUTTON);
            for (WebElement b : list) {
                try {
                    if (b.isDisplayed() && b.isEnabled()) return b;
                } catch (Exception ignored) {
                }
            }
            return null;
        });

        scrollTo(searchBtn);
        safeClick(searchBtn);

        wait.until(driver -> {
            List<WebElement> items = driver.findElements(PATIENT_RESULT_CONTAINER);
            for (WebElement item : items) {
                try {
                    if (item.isDisplayed() && item.getRect().getHeight() > 0) {
                        return true;
                    }
                } catch (Exception ignored) {
                }
            }
            return false;
        });

        LOGGER.info("Hasta Arama sonuçları listelendi. Aranan hasta: {}", fullName);
    }

    public void selectPatientFromResults(String fullName) {
        List<WebElement> candidates = wait.until(driver -> {
            List<WebElement> items = driver.findElements(PATIENT_RESULT_CONTAINER);
            return items.isEmpty() ? null : items;
        });

        WebElement target = null;

        for (WebElement container : candidates) {
            try {
                WebElement nameEl = container.findElement(PATIENT_RESULT_NAME);
                String text = nameEl.getText().trim();
                if (text.equalsIgnoreCase(fullName)) {
                    target = container;
                    break;
                }
            } catch (Exception ignored) {
            }
        }

        if (target == null) {
            target = candidates.get(0);
        }

        scrollTo(target);
        safeClick(target);

        wait.until(ExpectedConditions.visibilityOfElementLocated(APPOINTMENT_FORM_SECTION));
        wait.until(ExpectedConditions.visibilityOfElementLocated(APPOINTMENT_FORM_ACTIONS));

        LOGGER.info("Hasta Arama sonuçlarından hasta seçildi: {}", fullName);
    }

    // ===================== HASTA ARAMA PANELİNDE ETKİNLİK SEKME AKTİVASYONU =====================

    /**
     * Hasta arama panelinde "Etkinlik" sekmesine tıklar ve
     * Etkinlik formunun yüklendiğini doğrular.
     */
    public void clickEventTabOnSidebar() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(SIDEBAR_CONTENT));

        WebElement eventTab = wait.until(ExpectedConditions.elementToBeClickable(EVENT_TAB));
        scrollTo(eventTab);
        safeClick(eventTab);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(APPOINTMENT_FORM_SECTION));
        } catch (TimeoutException e) {
            LOGGER.warn("'Etkinlik' sekmesi sonrası form section bulunamadı, " +
                    "muhtemelen farklı data-testid kullanılıyor. Devam ediliyor.");
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(APPOINTMENT_FORM_ACTIONS));

        LOGGER.info("Hasta Arama panelinde 'Etkinlik' sekmesine tıklandı.");
    }

    // ===================== RANDEVU / ETKİNLİK DETAYI KAYDET =====================

    public void clickAppointmentSave() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(APPOINTMENT_FORM_ACTIONS));

        try {
            WebElement startTimeInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(APPOINTMENT_START_TIME_INPUT));
            String raw = startTimeInput.getAttribute("value");
            lastCreatedAppointmentStartTime = normalizeStartTime(raw);
            LOGGER.info("Randevu / Etkinlik Detayı başlangıç saati okundu: {}", lastCreatedAppointmentStartTime);
        } catch (Exception e) {
            LOGGER.warn("Başlangıç saati okunamadı: {}", e.getClass().getSimpleName());
            lastCreatedAppointmentStartTime = null;
        }

        safeClick(APPOINTMENT_SAVE_BUTTON);

        wait.until(driver -> {
            try {
                WebElement content = driver.findElement(SIDEBAR_CONTENT);
                return !content.isDisplayed();
            } catch (Exception e) {
                return true;
            }
        });

        LOGGER.info("Randevu / Etkinlik Detayı penceresinde Kaydet butonuna tıklandı ve panel kapandı.");
    }

    // ===================== QUICK POPUP =====================

    /**
     * Quick popup'ın gerçekten açıldığından ve içindeki header/footer/butonların
     * yüklenmiş olduğundan emin olur.
     */
    private void waitQuickPopupReady() {
        WebElement popup = wait.until(driver -> {
            List<WebElement> list = driver.findElements(QUICK_POPUP); // div.e-quick-popup-wrapper.e-popup-open
            for (WebElement p : list) {
                try {
                    if (p.isDisplayed() && p.getRect().getHeight() > 0) {
                        return p;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
            return null;
        });

        if (popup == null) {
            throw new TimeoutException("Açık quick popup wrapper bulunamadı.");
        }

        wait.until(driver -> {
            try {
                List<WebElement> headers = popup.findElements(
                        By.cssSelector(".e-popup-header-title-text, [data-testid='quick-info-header']")
                );
                for (WebElement h : headers) {
                    if (h.isDisplayed()) {
                        return true;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }

            try {
                List<WebElement> footers = popup.findElements(
                        By.cssSelector("[data-testid='appointment-footer'], [data-testid='closing-event-footer']")
                );
                for (WebElement f : footers) {
                    if (f.isDisplayed()) {
                        return true;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }

            try {
                List<WebElement> buttons = popup.findElements(
                        By.cssSelector("button[data-testid='status-button'], button[data-testid='closing-event-delete-button']")
                );
                for (WebElement b : buttons) {
                    if (b.isDisplayed() && b.isEnabled()) {
                        return true;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }

            return false;
        });

        LOGGER.info("Randevu / Etkinlik Detayı quick popup açıldı ve hazır.");
    }

    // ===================== CHECK-IN & ADMISSION =====================

    public void clickCheckInOnQuickPopup() {
        waitQuickPopupReady();

        WebElement statusBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(QUICK_POPUP_STATUS_BUTTON));
        String text = statusBtn.getText().trim();

        if (!text.equalsIgnoreCase("Check-in")) {
            LOGGER.info("Status butonu zaten '{}' durumda, Check-in tıklanmadı.", text);
            return;
        }

        scrollTo(statusBtn);
        safeClick(statusBtn);

        wait.until(driver -> {
            try {
                WebElement btn = driver.findElement(QUICK_POPUP_STATUS_BUTTON);
                String t = btn.getText().trim();
                return t.equalsIgnoreCase("Admission");
            } catch (Exception e) {
                return false;
            }
        });

        LOGGER.info("Check-in işlemi yapıldı, buton 'Admission' durumuna geçti.");
    }

    public void clickAdmissionOnQuickPopup() {
        waitQuickPopupReady();

        WebElement statusBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(QUICK_POPUP_STATUS_BUTTON));
        String text = statusBtn.getText().trim();

        if (!text.equalsIgnoreCase("Admission")) {
            LOGGER.warn("Status butonu beklenen 'Admission' değil: {}", text);
        }

        scrollTo(statusBtn);
        safeClick(statusBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(ADMISSION_SIDEBAR_ROOT));

        LOGGER.info("Admission butonuna tıklandı, Hasta Kabul penceresi açıldı.");
    }

    // ===================== DOĞRU RANDEVU KARTINI BULMA (HASTA İSMİYLE) =====================

    /**
     * Aynı hastanın birden fazla kartı olabildiği için:
     *  - İsim + başlangıç saati + lastClickedSlotY kombinasyonu ile kart bulmaya çalışır.
     *  - Bulamazsa isim + Y,
     *  - O da yoksa sadece isim ile fallback yapar.
     */
    private WebElement findAppointmentTile(String fullName) {
        String expectedName = fullName.trim().toUpperCase(Locale.ROOT);
        String expectedTime = normalizeStartTime(lastCreatedAppointmentStartTime);

        List<WebElement> tiles = wait.until(d -> {
            List<WebElement> list = d.findElements(APPOINTMENT_TILE_ANY);
            return list.isEmpty() ? null : list;
        });

        WebElement byNameOnly = null;
        WebElement byNameAndY = null;

        for (WebElement t : tiles) {
            try {
                if (!t.isDisplayed()) continue;

                String text = t.getText().toUpperCase(Locale.ROOT);

                if (!text.contains(expectedName)) continue;

                if (byNameOnly == null) byNameOnly = t;

                int y = t.getRect().getY();

                if (lastClickedSlotY != null && Math.abs(y - lastClickedSlotY) <= 10) {
                    if (byNameAndY == null) byNameAndY = t;
                }

                if (expectedTime != null && text.contains(expectedTime)) {
                    if (lastClickedSlotY == null || Math.abs(y - lastClickedSlotY) <= 10) {
                        LOGGER.info("Kart bulundu (isim + saat + Y). text='{}', y={}, lastY={}",
                                text, y, lastClickedSlotY);
                        return t;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }

        if (byNameAndY != null) {
            LOGGER.info("Kart bulundu (isim + Y).");
            return byNameAndY;
        }

        if (byNameOnly != null) {
            LOGGER.info("Kart bulundu (sadece isim).");
            return byNameOnly;
        }

        return null;
    }

    // ===================== RANDEVU KARTI / QUICK POPUP AÇMA (HASTA İSMİYLE) =====================

    public void openAppointmentCardForPatient(String fullName) {
        for (int attempt = 0; attempt < 3; attempt++) {
            WebElement tile = findAppointmentTile(fullName);
            if (tile == null) {
                throw new AssertionError("Uygun randevu kartı bulunamadı. Hasta=" +
                        fullName + ", startTime=" + lastCreatedAppointmentStartTime +
                        ", lastY=" + lastClickedSlotY);
            }

            try {
                scrollTo(tile);

                try {
                    tile.click();
                } catch (ElementClickInterceptedException e1) {
                    LOGGER.warn("tile.click() intercept edildi, JS click denenecek (attempt={}): {}",
                            attempt + 1, e1.getClass().getSimpleName());
                    js.executeScript("arguments[0].click();", tile);
                }

                waitQuickPopupReady();
                LOGGER.info("İlgili randevu kartı açıldı. Hasta={}, Saat={}",
                        fullName, lastCreatedAppointmentStartTime);
                return;

            } catch (StaleElementReferenceException e) {
                LOGGER.warn("openAppointmentCardForPatient - tile stale oldu, yeniden denenecek (attempt={})",
                        attempt + 1);
            } catch (TimeoutException e) {
                LOGGER.warn("Quick popup açılmadı, kart tekrar tıklanacak (attempt={}): {}",
                        attempt + 1, e.getMessage());
            }
        }

        throw new AssertionError("Randevu kartı tıklanmasına rağmen quick popup açılamadı. Hasta=" +
                fullName + ", startTime=" + lastCreatedAppointmentStartTime);
    }

    // ===================== HASTA KABUL KAYDET =====================

    public void saveAdmissionForm() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ADMISSION_SIDEBAR_ROOT));

        safeClick(ADMISSION_SAVE_BUTTON);

        wait.until(driver -> {
            try {
                WebElement el = driver.findElement(ADMISSION_SIDEBAR_ROOT);
                return !el.isDisplayed();
            } catch (Exception e) {
                return true;
            }
        });

        LOGGER.info("Hasta Kabul penceresinde Kaydet butonuna tıklandı ve pencere kapandı.");
    }

    // ========================================================================
    // ===================== KAPANIŞ ETKİNLİĞİ AKIŞI ===========================
    // ========================================================================

    /**
     * Kapanış etkinliği kartını bulur.
     *
     * - Önce data-testid="event-closing" içeren kartları bulur.
     * - Birden fazla varsa, lastClickedSlotY değerine en yakın olanı tercih eder.
     */
    private WebElement findClosingEventTile() {
        List<WebElement> labels = wait.until(d -> {
            List<WebElement> list = d.findElements(CLOSING_EVENT_LABEL);
            return list.isEmpty() ? null : list;
        });

        if (labels == null || labels.isEmpty()) {
            LOGGER.warn("findClosingEventTile: data-testid='event-closing' bulunamadı.");
            return null;
        }

        WebElement bestByY = null;

        for (WebElement label : labels) {
            try {
                if (!label.isDisplayed()) continue;

                WebElement tile = label.findElement(
                        By.xpath("./ancestor::div[contains(@class,'e-appointment')][1]")
                );
                if (tile == null || !tile.isDisplayed()) continue;

                if (lastClickedSlotY != null) {
                    int y = tile.getRect().getY();
                    if (Math.abs(y - lastClickedSlotY) <= 10) {
                        LOGGER.info("Kapanış etkinliği kartı bulundu (Y koordinatı eşleşti). y={}, lastY={}",
                                y, lastClickedSlotY);
                        return tile;
                    }
                }

                if (bestByY == null) {
                    bestByY = tile;
                }

            } catch (StaleElementReferenceException ignored) {
            }
        }

        if (bestByY != null) {
            LOGGER.info("Kapanış etkinliği kartı bulundu (ilk görünen kart).");
        } else {
            LOGGER.warn("findClosingEventTile: uygun kart bulunamadı.");
        }

        return bestByY;
    }

    /**
     * Başlangıç saati bilgisini kullanarak (14:00 gibi) oluşturulan
     * kapanış etkinliği kartını bulur.
     */
    private WebElement findAppointmentTileByStartTime() {
        String expectedTime = normalizeStartTime(lastCreatedAppointmentStartTime);

        if (expectedTime == null) {
            LOGGER.warn("findAppointmentTileByStartTime: lastCreatedAppointmentStartTime boş. Saat bilgisiyle eşleştirme zayıflayacak.");
        }

        List<WebElement> tiles = wait.until(d -> {
            List<WebElement> list = d.findElements(APPOINTMENT_TILE_ANY);
            return list.isEmpty() ? null : list;
        });

        WebElement byTimeAndY = null;
        WebElement byTimeOnly = null;

        for (WebElement t : tiles) {
            try {
                if (!t.isDisplayed()) continue;

                String text = t.getText();
                String aria = t.getAttribute("aria-label");

                boolean timeMatch = false;
                if (expectedTime != null) {
                    if (text != null && text.contains(expectedTime)) {
                        timeMatch = true;
                    }
                    if (!timeMatch && aria != null && aria.contains(expectedTime)) {
                        timeMatch = true;
                    }
                }

                if (!timeMatch) continue;

                if (byTimeOnly == null) {
                    byTimeOnly = t;
                }

                int y = t.getRect().getY();
                if (lastClickedSlotY != null && Math.abs(y - lastClickedSlotY) <= 10) {
                    byTimeAndY = t;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }

        if (byTimeAndY != null) {
            LOGGER.info("Kapanış etkinliği kartı bulundu (saat + Y).");
            return byTimeAndY;
        }

        if (byTimeOnly != null) {
            LOGGER.info("Kapanış etkinliği kartı bulundu (sadece saat).");
            return byTimeOnly;
        }

        return null;
    }

    /**
     * Oluşturulan kapanış etkinliği kartına tıklar ve Etkinlik Detayı popup'ını açar.
     */
    public void openClosingEventCardByTime() {
        WebElement tile = findClosingEventTile();
        if (tile == null) {
            throw new AssertionError("Kapanış etkinliği kartı bulunamadı. lastY=" + lastClickedSlotY);
        }

        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                scrollTo(tile);
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("openClosingEventCardByTime - tile stale, yeniden bulunuyor (attempt={})",
                        attempt + 1);
                tile = findClosingEventTile();
                if (tile == null) break;
                scrollTo(tile);
            }

            safeClick(tile);

            try {
                waitQuickPopupReady();
                LOGGER.info("Kapanış etkinliği quick popup açıldı.");
                return;
            } catch (TimeoutException e) {
                LOGGER.warn("Kapanış etkinliği quick popup açılmadı, kart tekrar tıklanacak (attempt={})",
                        attempt + 1);
                tile = findClosingEventTile();
                if (tile == null) break;
            }
        }

        throw new AssertionError("Kapanış etkinliği kartı tıklanmasına rağmen quick popup açılamadı. lastY=" +
                lastClickedSlotY);
    }

    /**
     * Etkinlik Detayı popup'ında Sil butonuna tıklar,
     * ardından gelen onay dialogunda Tamam'a basar.
     */
    public void deleteClosingEventFromQuickPopup() {
        waitQuickPopupReady();

        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(CLOSING_EVENT_DELETE_BUTTON));
        scrollTo(deleteBtn);
        safeClick(deleteBtn);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(QUICK_POPUP));
        } catch (Exception ignored) {
        }

        clickPrimaryOnGenericDialogIfPresent();

        LOGGER.info("Etkinlik Detayı penceresinde Sil'e tıklandı, silme onay dialogunda Tamam seçildi.");
    }
}

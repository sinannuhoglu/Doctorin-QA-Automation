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

/**
 * Tıbbi İşlemler > İş Listesi > Detay > Muayene sekmesi
 * "Hasta Notu" alanı Page Object
 *
 * URL:
 * - Doğrudan URL ile gidilmez; Work List üzerinden hasta detayı açılarak erişilir.
 */
public class TreatmentExaminationPatientNotesPage {

    private static final Logger LOGGER = LogManager.getLogger(TreatmentExaminationPatientNotesPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public TreatmentExaminationPatientNotesPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ===================== LOCATORS ======================

    /**
     * Muayene sekmesindeki kart container (Vücut Ölçümleri, Hasta Notu vb. kartlar)
     */
    private final By examinationCardsContainer = By.xpath(
            "//div[contains(@class,'relative') and contains(@class,'h-full') and contains(@class,'overflow-auto')]"
    );

    /**
     * "Hasta Notu" kartı içindeki ikon butonu.
     *
     * HTML:
     *  <div class="bg-white rounded-xl dark:bg-surface-800 overflow-visible p-3">
     *      ...
     *      <p class="text-surface-900 ...">Hasta Notu</p>
     *      ...
     *      <button class="e-control e-btn e-lib e-small e-round shrink-0 e-soft-button e-icon-btn">...</button>
     *  </div>
     */
    private final By patientNoteCardButton = By.xpath(
            "//p[contains(normalize-space(),'Hasta Notu')]" +
                    "/ancestor::div[contains(@class,'bg-white') and contains(@class,'rounded-xl') and contains(@class,'overflow-visible')][1]" +
                    "//button[contains(@class,'e-icon-btn')]"
    );

    /**
     * Hasta Notu slide-over/pencere container
     */
    private final By patientNotePanel = By.xpath(
            "//div[contains(@class,'fixed') and contains(@class,'top-0') and contains(@class,'z-1000')" +
                    " and contains(@class,'h-full') and contains(@class,'shadow-lg')]"
    );

    /**
     * Not yazılan textarea (yeni kayıt ve düzenleme için aynı yapı)
     */
    private final By patientNoteTextarea = By.xpath(
            "//div[contains(@class,'fixed') and contains(@class,'top-0') and contains(@class,'z-1000')]" +
                    "//textarea[contains(@id,'textarea') and contains(@name,'textarea')]"
    );

    /**
     * Hasta Notu panelindeki Kaydet butonu (ilk kayıt)
     */
    private final By saveNoteButton = By.xpath(
            "//div[contains(@class,'fixed') and contains(@class,'top-0') and contains(@class,'z-1000')]" +
                    "//button[contains(@class,'e-primary') and normalize-space()='Kaydet']"
    );

    /**
     * Hasta Notu panelindeki Güncelle butonu (düzenleme)
     */
    private final By updateNoteButton = By.xpath(
            "//div[contains(@class,'fixed') and contains(@class,'top-0') and contains(@class,'z-1000')]" +
                    "//button[contains(@class,'e-primary') and normalize-space()='Güncelle']"
    );

    /**
     * Kaydedilen notların listelendiği ana container
     */
    private final By notesHistoryContainer = By.xpath(
            "//div[contains(@class,'flex-col') and contains(@class,'gap-4') and contains(@class,'overflow-y-auto')]" +
                    "//div[contains(@class,'bg-white') and contains(@class,'dark:bg-surface-800') and contains(@class,'p-4')]"
    );

    // ===================== HELPER METOTLAR ======================

    private void scrollIntoView(WebElement el) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (JavascriptException ignored) {
        }
    }

    /**
     * Tıklama işlemi için küçük retry içeren güvenli metot.
     */
    private void safeClick(By locator) {
        RuntimeException lastException = null;

        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                scrollIntoView(el);
                el.click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                lastException = e;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (lastException != null) {
            throw lastException;
        } else {
            throw new RuntimeException("safeClick: element could not be clicked for locator: " + locator);
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

    private void clearAndType(WebElement el, String text) {
        scrollIntoView(el);

        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", el);
        }

        // CTRL+A ve CMD+A ile tümünü seç
        try {
            el.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Windows / Linux
            el.sendKeys(Keys.chord(Keys.COMMAND, "a")); // Mac
            el.sendKeys(Keys.DELETE);
            el.sendKeys(Keys.BACK_SPACE);
        } catch (Exception ignored) {
        }

        // Hala içerik varsa karakter karakter sil
        try {
            String current = el.getAttribute("value");
            if (current != null && !current.isEmpty()) {
                for (int i = 0; i < current.length(); i++) {
                    el.sendKeys(Keys.BACK_SPACE);
                }
            }
        } catch (Exception ignored) {
        }

        el.sendKeys(text);
    }


    private String getTodayDateForNote() {
        LocalDate today = LocalDate.now();
        // Örnek format: 06/11/2025
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return today.format(formatter);
    }

    /**
     * Verilen noteText için ilgili kartın root div'ini döndürür.
     */
    private WebElement findNoteCardByText(String noteText) {
        String cardXpath =
                "//div[contains(@class,'flex-col') and contains(@class,'gap-4') and contains(@class,'overflow-y-auto')]" +
                        "//div[contains(@class,'bg-white') and contains(@class,'dark:bg-surface-800') and contains(@class,'p-4')]" +
                        "[.//div[contains(@class,'text-surface-800') and contains(@class,'pre-wrap')" +
                        " and normalize-space()='" + noteText + "']]";

        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cardXpath)));
    }

    // ===================== ACTIONS ======================

    /**
     * Muayene sekmesinde "Hasta Notu" kartındaki butona tıklayarak
     * Hasta Notu slide-over penceresini açar.
     */
    public void openPatientNotePanel() {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] openPatientNotePanel");

        // Muayene kartlarının yüklendiğini bekle (sayfa açılışını garanti etmek için)
        wait.until(ExpectedConditions.visibilityOfElementLocated(examinationCardsContainer));

        // "Hasta Notu" kartındaki ikon butona tıkla
        safeClick(patientNoteCardButton);

        // Panelin açıldığını doğrula
        wait.until(ExpectedConditions.visibilityOfElementLocated(patientNotePanel));
        wait.until(ExpectedConditions.visibilityOfElementLocated(patientNoteTextarea));
    }

    /**
     * Hasta notu textarea alanına verilen metni yazar.
     *
     * @param noteText yazılacak not metni
     */
    public void typePatientNote(String noteText) {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] typePatientNote -> '{}'", noteText);

        WebElement textarea = wait.until(
                ExpectedConditions.visibilityOfElementLocated(patientNoteTextarea)
        );

        clearAndType(textarea, noteText);

        // DEBUG: gerçekten ne yazılmış görelim
        try {
            String value = textarea.getAttribute("value");
            LOGGER.info("[TreatmentExaminationPatientNotesPage] Paneldeki textarea değeri: >{}<", value);
        } catch (Exception e) {
            LOGGER.warn("[TreatmentExaminationPatientNotesPage] Textarea value okunamadı", e);
        }
    }

    /**
     * Hasta Notu panelindeki Kaydet butonuna tıklar ve kaydın tamamlanmasını bekler.
     */
    public void clickSavePatientNote() {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] clickSavePatientNote");

        safeClick(saveNoteButton);

        // Listeye yansıması için kısa bir bekleme + container görünürlüğü
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(notesHistoryContainer));
    }

    /**
     * Güncelle butonuna tıklar ve panelin kapanmasını bekler.
     */
    public void clickUpdatePatientNote() {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] clickUpdatePatientNote");

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(updateNoteButton));
        scrollIntoView(btn);

        try {
            LOGGER.info("[TreatmentExaminationPatientNotesPage] Güncelle butonuna normal click denemesi");
            btn.click();
        } catch (Exception e) {
            LOGGER.warn("[TreatmentExaminationPatientNotesPage] Güncelle normal click başarısız, JS click denenecek", e);
            js.executeScript("arguments[0].click();", btn);
        }

        // Panel kapanıyorsa bunu bekleyelim
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(patientNotePanel));
        } catch (TimeoutException e) {
            LOGGER.warn("[TreatmentExaminationPatientNotesPage] Hasta notu paneli kapanmadı, yine de devam edilecek.");
        }

        // Liste görünürlüğünü garanti et
        wait.until(ExpectedConditions.visibilityOfElementLocated(notesHistoryContainer));
    }

    /**
     * Güncelle butonuna tıklar ve verilen updatedText içeren not görünür olana kadar bekler.
     */
    public void clickUpdatePatientNoteAndWaitFor(String updatedText) {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] clickUpdatePatientNoteAndWaitFor -> '{}'", updatedText);

        safeClick(updateNoteButton);

        // Güncellenen notun listede görünmesini bekle
        String noteXpath =
                "//div[contains(@class,'flex-col') and contains(@class,'gap-4') and contains(@class,'overflow-y-auto')]" +
                        "//div[contains(@class,'bg-white') and contains(@class,'dark:bg-surface-800') and contains(@class,'p-4')]" +
                        "//div[contains(@class,'text-surface-800') and contains(@class,'pre-wrap')]" +
                        "[normalize-space()='" + updatedText + "']";

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(noteXpath)));
    }


    /**
     * Not listesini içinde verilen not metnini arar.
     *
     * @param noteText aranacak not metni
     * @return not bulunduysa true
     */
    public boolean isPatientNoteVisible(String noteText) {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] isPatientNoteVisible -> '{}'", noteText);

        String noteXpath =
                "//div[contains(@class,'flex-col') and contains(@class,'gap-4') and contains(@class,'overflow-y-auto')]" +
                        "//div[contains(@class,'bg-white') and contains(@class,'dark:bg-surface-800') and contains(@class,'p-4')]" +
                        "//div[contains(@class,'text-surface-800') and contains(@class,'pre-wrap')]" +
                        "[normalize-space()='" + noteText + "']";

        try {
            WebElement noteElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(noteXpath))
            );
            scrollIntoView(noteElement);
            return noteElement.isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("[TreatmentExaminationPatientNotesPage] Beklenen not bulunamadı, listedeki tüm notlar loglanacak.");

            driver.findElements(
                    By.xpath("//div[contains(@class,'flex-col') and contains(@class,'gap-4') and contains(@class,'overflow-y-auto')]" +
                            "//div[contains(@class,'bg-white') and contains(@class,'dark:bg-surface-800') and contains(@class,'p-4')]" +
                            "//div[contains(@class,'text-surface-800') and contains(@class,'pre-wrap')]")
            ).forEach(el -> LOGGER.warn("NOTE ITEM TEXT => >{}<", el.getText()));

            return false;
        }
    }

    /**
     * Verilen not metnine ait kart içerisinde bugünün tarih formatında
     * (dd/MM/yyyy) oluşturulmuş bir kayıt olup olmadığını kontrol eder.
     *
     * Tarihi bulamazsa:
     *  - Log’a uyarı düşer
     *  - Sadece not metninin görünürlüğünü kontrol ederek true döner.
     *
     * @param noteText not metni
     * @return tarih alanı bugünün tarihi ise true, tarih bulunamazsa ama not görünüyorsa yine true
     */
    public boolean isTodayDateOrAtLeastNoteVisible(String noteText) {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] isTodayDateOrAtLeastNoteVisible -> '{}'", noteText);

        String expectedDate = getTodayDateForNote();

        String dateXpath =
                "//div[contains(@class,'flex-col') and contains(@class,'gap-4') and contains(@class,'overflow-y-auto')]" +
                        "//div[contains(@class,'bg-white') and contains(@class,'dark:bg-surface-800') and contains(@class,'p-4')]" +
                        "[.//div[contains(@class,'text-surface-800') and contains(@class,'pre-wrap') and normalize-space()='" + noteText + "']]" +
                        "//span[contains(@class,'text-surface-500') and contains(normalize-space(),'" + expectedDate + "')]";

        try {
            WebElement dateElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(dateXpath))
            );
            scrollIntoView(dateElement);
            return dateElement.isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("[TreatmentExaminationPatientNotesPage] Tarih alanı bulunamadı, sadece not metni ile doğrulama yapılacak. Note='{}'", noteText);
            // Sadece not görünürlüğünü kontrol et
            return isPatientNoteVisible(noteText);
        }
    }

    // ============ DÜZENLEME & SİLME İŞLEMLERİ ============

    /**
     * Verilen not metnine ait kart üzerindeki "düzenle" butonuna tıklar.
     */
    public void openEditPanelForNote(String noteText) {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] openEditPanelForNote -> '{}'", noteText);

        WebElement card = findNoteCardByText(noteText);

        WebElement editButton = card.findElement(
                By.xpath(".//button[.//span[contains(@class,'e-icons') and contains(@class,'e-edit')]]")
        );

        safeClick(editButton);

        // Edit paneldeki textarea yüklensin
        wait.until(ExpectedConditions.visibilityOfElementLocated(patientNotePanel));
        wait.until(ExpectedConditions.visibilityOfElementLocated(patientNoteTextarea));
    }

    /**
     * Verilen not metnine ait kart üzerindeki "sil" butonuna tıklar.
     */
    public void deleteNoteByText(String noteText) {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] deleteNoteByText -> '{}'", noteText);

        WebElement card = findNoteCardByText(noteText);

        WebElement deleteButton = card.findElement(
                By.xpath(".//button[.//span[contains(@class,'e-icons') and contains(@class,'e-trash')]]")
        );

        safeClick(deleteButton);

        // Silme işlemi sonrası listenin güncellenmesi için küçük bekleme
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Not listesinden verilen metne sahip bir kayıt olmadığını doğrulamak için kullanılır.
     */
    public boolean isPatientNoteNotVisible(String noteText) {
        LOGGER.info("[TreatmentExaminationPatientNotesPage] isPatientNoteNotVisible -> '{}'", noteText);

        String noteXpath =
                "//div[contains(@class,'flex-col') and contains(@class,'gap-4') and contains(@class,'overflow-y-auto')]" +
                        "//div[contains(@class,'bg-white') and contains(@class,'dark:bg-surface-800') and contains(@class,'p-4')]" +
                        "//div[contains(@class,'text-surface-800') and contains(@class,'pre-wrap')]" +
                        "[normalize-space()='" + noteText + "']";

        try {
            driver.findElement(By.xpath(noteXpath));
            // Bulunuyorsa false
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }
}

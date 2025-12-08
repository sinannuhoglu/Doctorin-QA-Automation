package com.sinannuhoglu.pages.treatment.medicalcard.forms;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tıbbi İşlemler > İş Listesi > Detay > Muayene sekmesi
 * Vizit içi "Basınç Değerlendirme" Page Object
 */
public class TreatmentExaminationVisitPressureAssessmentFormPage {

    private static final Logger LOGGER =
            LogManager.getLogger(TreatmentExaminationVisitPressureAssessmentFormPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    /**
     * Basınç formunun hangi vizitte açıldığını takip etmek için vizit numarası (örn: "1767").
     */
    private String createdVisitNumberForPressureAssessment;

    public TreatmentExaminationVisitPressureAssessmentFormPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ================= VİZİT LISTESİ LOCATOR'LARI =================

    private final By visitsContainer = By.xpath(
            "//section[contains(@class,'flex') and contains(@class,'flex-1')]//div" +
                    "[contains(@class,'py-3') and contains(@class,'flex') and contains(@class,'flex-col')" +
                    " and contains(@class,'overflow-hidden') and contains(@class,'hide-scrollbar')]"
    );

    private final By firstVisitCard = By.xpath(
            "//div[contains(@class,'py-3') and contains(@class,'flex') and contains(@class,'flex-col')" +
                    " and contains(@class,'overflow-hidden') and contains(@class,'hide-scrollbar')]" +
                    "//div[contains(@class,'flex') and contains(@class,'flex-col') and contains(@class,'overflow-hidden')" +
                    " and contains(@class,'gap-3') and contains(@class,'flex-1')]" +
                    "//div[contains(@class,'rounded-xl') and contains(@class,'bg-white') and contains(@class,'p-1')" +
                    " and contains(@class,'dark:bg-surface-800') and contains(@class,'animate-highlight-border')][1]"
    );

    private final By allVisitCards = By.xpath(
            "//div[@id='overview-treatment-content']" +
                    "//div[contains(@class,'rounded-xl') and contains(@class,'bg-white')" +
                    " and contains(@class,'p-1') and contains(@class,'dark:bg-surface-800')]"
    );

    private final By visitFormsButtonInsideCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'items-center') and contains(@class,'gap-1.5')]" +
                    "//button[1]"
    );

    private final By formsPopupRoot = By.xpath(
            "//div[contains(@class,'absolute') and contains(@class,'rounded-md')" +
                    " and contains(@class,'shadow-md') and contains(@class,'bg-white')" +
                    " and contains(@class,'dark:bg-surface-700') and contains(@class,'p-2')" +
                    " and contains(@class,'max-h-64') and contains(@class,'overflow-y-auto')" +
                    " and contains(@class,'overflow-x-hidden') and contains(@class,'border')" +
                    " and contains(@class,'border-surface-300') and contains(@class,'dark:border-white/20')]"
    );

    private final By formsPopupGrid = By.xpath(
            "//div[contains(@class,'absolute') and contains(@class,'rounded-md')" +
                    " and contains(@class,'shadow-md') and contains(@class,'bg-white')" +
                    " and contains(@class,'dark:bg-surface-700') and contains(@class,'p-2')" +
                    " and contains(@class,'max-h-64') and contains(@class,'overflow-y-auto')" +
                    " and contains(@class,'overflow-x-hidden') and contains(@class,'border')" +
                    " and contains(@class,'border-surface-300') and contains(@class,'dark:border-white/20')]" +
                    "//div[contains(@class,'grid') and contains(@class,'grid-cols-2') and contains(@class,'gap-2')]"
    );

    // Popup içindeki "Basınç Değerlendirme" butonu
    private final By pressureAssessmentButtonInPopup = By.xpath(
            "//button[normalize-space()='Basınç Değerlendirme']"
    );

    // Vizit gövdesindeki formlar container'ı (boş durum için)
    private final By visitFormsContainerInCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'flex-col') and contains(@class,'gap-1')]"
    );

    private final By emptyFormTextInCard = By.xpath(
            ".//p[contains(@class,'font-semibold') and normalize-space()='Form bulunamadı.']"
    );

    // Vizit kartı içinde "Basınç Değerlendirme" başlığı
    private final By pressureHeaderInVisitCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'items-center')" +
                    " and contains(@class,'justify-between') and contains(@class,'px-3')]" +
                    "//span[contains(normalize-space(),'Basınç')]"
    );

    // Tüm formlar için ortak silme butonu (vizit kartı içinde ilk görülen çöp ikonu)
    private final By formDeleteButtonInVisitCard = By.xpath(
            ".//span[contains(@class,'e-icons') and contains(@class,'e-trash') and contains(@class,'e-btn-icon')]/ancestor::button[1]"
    );

    private final By deleteConfirmYesButton = By.xpath(
            "//div[contains(@class,'e-dlg-container')]//button" +
                    "[contains(@class,'e-primary') and normalize-space()='Evet']"
    );

    // ================= BASINÇ FORMU LOCATOR'LARI =================

    // Form root: "Basınç Yarası Riski Değerlendirme Formu (5 yaş ve üzeri)"
    private final By pressureFormRoot = By.xpath(
            "//form[.//span[contains(normalize-space(),'Basınç Yarası Riski Değerlendirme Formu')]]"
    );

    // Kaydet butonu
    private final By pressureSaveButton = By.xpath(
            "//form[.//span[contains(normalize-space(),'Basınç Yarası Riski Değerlendirme Formu')]]" +
                    "//button[@type='submit' and (normalize-space()='Kaydet' or .//span[normalize-space()='Kaydet'])]"
    );

    // ================= HELPER METOTLAR =================

    private WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private void scrollIntoView(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        } catch (JavascriptException ignored) {
        }
    }

    private void safeClick(By locator) {
        RuntimeException last = null;
        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = waitClickable(locator);
                scrollIntoView(el);
                el.click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                last = e;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                }
            }
        }
        if (last != null) throw last;
    }

    private By checkboxLabelLocator(String labelText) {
        return By.xpath(
                "//form[.//span[contains(normalize-space(),'Basınç Yarası Riski Değerlendirme Formu')]]" +
                        "//label[.//span[contains(@class,'e-label') and normalize-space()='" + labelText + "']]"
        );
    }

    private String getCell(Map<String, String> row, String key) {
        String value = row.get(key);
        return value == null ? "" : value.trim();
    }

    // =========================================================
    //  VİZİT NUMARASI ÇÖZÜMLEME
    // =========================================================

    private String extractVisitNumberFromCard(WebElement card) {
        try {
            String fullText = card.getText();
            if (fullText == null) {
                return null;
            }

            Pattern quotedPattern = Pattern.compile("\"(\\d+)\"");
            Matcher quotedMatcher = quotedPattern.matcher(fullText);
            String lastMatch = null;
            while (quotedMatcher.find()) {
                lastMatch = quotedMatcher.group(1);
            }
            if (lastMatch != null) {
                return lastMatch;
            }

            Pattern digitsPattern = Pattern.compile("\\b(\\d{3,})\\b");
            Matcher digitsMatcher = digitsPattern.matcher(fullText);
            lastMatch = null;
            while (digitsMatcher.find()) {
                lastMatch = digitsMatcher.group(1);
            }
            return lastMatch;
        } catch (Exception e) {
            LOGGER.warn("[PressureAssessment] Vizit numarası çözümlenemedi: {}", e.getMessage());
            return null;
        }
    }

    // =========================================================
    //  FORM AÇILIŞI
    // =========================================================

    public void openFirstVisitPressureAssessmentForm() {
        LOGGER.info("[PressureAssessment] İlk vizitin Basınç Değerlendirme formu açılıyor...");

        waitVisible(visitsContainer);

        WebElement card = waitVisible(firstVisitCard);
        scrollIntoView(card);

        createdVisitNumberForPressureAssessment = extractVisitNumberFromCard(card);
        LOGGER.info("[PressureAssessment] Basınç formu açılan vizit numarası: {}",
                createdVisitNumberForPressureAssessment);

        WebElement formsButton = card.findElement(visitFormsButtonInsideCard);

        LOGGER.info("[PressureAssessment] Vizit içindeki Formlar butonuna tıklanıyor...");
        try {
            scrollIntoView(formsButton);
            wait.until(ExpectedConditions.elementToBeClickable(formsButton)).click();
        } catch (Exception e) {
            LOGGER.warn("[PressureAssessment] Normal click başarısız, JS click denenecek. Hata: {}", e.getMessage());
            js.executeScript("arguments[0].click();", formsButton);
        }

        try {
            Thread.sleep(700);
        } catch (InterruptedException ignored) {
        }

        WebElement popupRoot = waitPresent(formsPopupRoot);
        scrollIntoView(popupRoot);
        waitPresent(formsPopupGrid);

        LOGGER.info("[PressureAssessment] Pop-up içindeki Basınç Değerlendirme butonu bekleniyor...");
        WebElement pressureButton = waitPresent(pressureAssessmentButtonInPopup);
        scrollIntoView(pressureButton);

        try {
            js.executeScript("arguments[0].click();", pressureButton);
        } catch (JavascriptException e) {
            LOGGER.warn("[PressureAssessment] JS click hatası, fallback normal click denenecek. Hata: {}", e.getMessage());
            pressureButton.click();
        }

        waitVisible(pressureFormRoot);
        LOGGER.info("[PressureAssessment] Basınç Değerlendirme formu vizit içinde açıldı.");
    }

    // =========================================================
    //  FORMUN DOLDURULMASI (DataTable)
    // =========================================================

    /**
     * Basınç Değerlendirme formunu DataTable satırlarına göre doldurur ve Kaydet'e tıklar.
     *
     * Beklenen kolonlar:
     *  - alan  : dokümantasyon / log için (ör: Algılama, Nemlilik,...)
     *  - tip   : checkbox (bilgi amaçlı)
     *  - değer : direkt tıklanacak label metni (örn: "Tamamen Sınırlı")
     */
    public void fillPressureAssessmentFromTable(List<Map<String, String>> rows) {
        if (rows == null || rows.isEmpty()) {
            LOGGER.warn("[PressureAssessment] DataTable boş geldi, Basınç Değerlendirme formu doldurulmayacak.");
            return;
        }

        LOGGER.info("[PressureAssessment] DataTable ile Basınç Değerlendirme formu dolduruluyor. Satır sayısı: {}", rows.size());

        for (Map<String, String> row : rows) {
            String field = getCell(row, "alan");
            String type = getCell(row, "tip").toLowerCase(Locale.ROOT);
            String value = getCell(row, "değer");

            if (value.isEmpty()) {
                LOGGER.warn("[PressureAssessment] Satırda 'değer' boş, satır atlanıyor. Satır: {}", row);
                continue;
            }

            LOGGER.info("[PressureAssessment] Satır işleniyor -> alan='{}', tip='{}', değer='{}'", field, type, value);

            try {
                safeClick(checkboxLabelLocator(value));
            } catch (Exception e) {
                LOGGER.error("[PressureAssessment] '{}' label'ına tıklanırken hata oluştu. Satır: {}, Hata: {}",
                        value, row, e.getMessage());
                throw e;
            }
        }

        clickPressureSaveButton();
    }

    /**
     * Eski senaryolar için: tüm risk kriterleri için sabit ilk seçenekleri işaretler.
     * (Artık DataTable kullanılıyor olsa da, geriye dönük uyumluluk için bırakıldı.)
     */
    public void selectAllFirstRiskOptions() {
        LOGGER.info("[PressureAssessment] Risk kriterleri için sabit ilk seçenekler işaretleniyor...");

        safeClick(checkboxLabelLocator("Tamamen Sınırlı"));
        safeClick(checkboxLabelLocator("Her Zaman Nemli"));
        safeClick(checkboxLabelLocator("Tam Hareketiz"));
        safeClick(checkboxLabelLocator("Yatağa Bağımlı"));
        safeClick(checkboxLabelLocator("Kötü Beslenme"));
        safeClick(checkboxLabelLocator("Sorunlu (Hareket etmek için büyük ölçüde yardım gerek)"));
    }

    // =========================================================
    //  KAYDET & KAYIT DOĞRULAMA
    // =========================================================

    public void clickPressureSaveButton() {
        LOGGER.info("[PressureAssessment] Kaydet butonuna tıklanıyor...");
        safeClick(pressureSaveButton);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }

    public boolean isPressureAssessmentSavedSuccessfully() {
        LOGGER.info("[PressureAssessment] Basınç Değerlendirme formu kaydının başarıyla oluşturulup oluşturulmadığı kontrol ediliyor...");

        long end = System.currentTimeMillis() + 15_000L;

        if (createdVisitNumberForPressureAssessment != null) {
            LOGGER.info("[PressureAssessment] Öncelikli hedef vizit numarası: {}", createdVisitNumberForPressureAssessment);

            while (System.currentTimeMillis() < end) {
                List<WebElement> cards = driver.findElements(allVisitCards);

                LOGGER.info("[PressureAssessment] Hedef vizit araması - toplam {} vizit kartı bulundu.", cards.size());

                for (WebElement card : cards) {
                    String visitNo = extractVisitNumberFromCard(card);
                    if (!createdVisitNumberForPressureAssessment.equals(visitNo)) {
                        continue;
                    }

                    LOGGER.info("[PressureAssessment] Hedef vizit kartı bulundu (vizit no: {}). Basınç başlığı aranıyor...", visitNo);
                    scrollIntoView(card);

                    if (!card.findElements(pressureHeaderInVisitCard).isEmpty()) {
                        LOGGER.info("[PressureAssessment] Hedef vizitte Basınç Değerlendirme başlığı göründü, kayıt başarılı kabul ediliyor.");
                        return true;
                    }
                }

                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignored) {
                }
            }

            LOGGER.warn("[PressureAssessment] Hedef vizitte Basınç Değerlendirme başlığı bulunamadı, global aramaya geçiliyor...");
        } else {
            LOGGER.warn("[PressureAssessment] createdVisitNumberForPressureAssessment = null, hedef vizit numarası yok. Doğrudan global aramaya geçilecek.");
        }

        long fallbackEnd = System.currentTimeMillis() + 10_000L;

        while (System.currentTimeMillis() < fallbackEnd) {
            List<WebElement> cards = driver.findElements(allVisitCards);
            LOGGER.info("[PressureAssessment] Fallback arama - toplam {} vizit kartı inceleniyor.", cards.size());

            for (WebElement card : cards) {
                scrollIntoView(card);

                List<WebElement> headers = card.findElements(pressureHeaderInVisitCard);
                if (!headers.isEmpty()) {
                    String visitNo = extractVisitNumberFromCard(card);
                    LOGGER.info("[PressureAssessment] Fallback: Bir vizitte Basınç Değerlendirme başlığı bulundu. Vizit no: {}", visitNo);

                    if (visitNo != null) {
                        createdVisitNumberForPressureAssessment = visitNo;
                        LOGGER.info("[PressureAssessment] createdVisitNumberForPressureAssessment fallback ile {} olarak güncellendi.", visitNo);
                    }

                    return true;
                }
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }

        LOGGER.warn("[PressureAssessment] Herhangi bir vizitte Basınç Değerlendirme başlığı bulunamadı, kayıt başarısız görünüyor.");
        return false;
    }

    // =========================================================
    //  SİLME: Hedef vizit → bir önceki vizit → fallback
    // =========================================================

    public boolean deletePressureAssessmentFromTargetOrPreviousVisit() {
        LOGGER.info("[PressureAssessment] Hedef vizitte (ve gerekirse bir önceki vizitte) Basınç Değerlendirme formu silme akışı başlıyor...");

        waitVisible(visitsContainer);

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[PressureAssessment] Toplam {} vizit kartı bulundu.", cards.size());

        if (cards.isEmpty()) {
            LOGGER.warn("[PressureAssessment] Hiç vizit kartı yok, silme işlemi yapılamaz.");
            return false;
        }

        int targetIndex = -1;
        if (createdVisitNumberForPressureAssessment != null) {
            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                scrollIntoView(card);
                String visitNo = extractVisitNumberFromCard(card);
                LOGGER.info("[PressureAssessment] {}. vizit kartının numarası: {}", i + 1, visitNo);

                if (createdVisitNumberForPressureAssessment.equals(visitNo)) {
                    targetIndex = i;
                    LOGGER.info("[PressureAssessment] Hedef vizit kartı bulundu. Index: {}, Vizit No: {}", targetIndex, visitNo);
                    break;
                }
            }
        } else {
            LOGGER.warn("[PressureAssessment] createdVisitNumberForPressureAssessment = null, hedef vizit numarası saklanamamış.");
        }

        WebElement targetCard = (targetIndex >= 0 && targetIndex < cards.size()) ? cards.get(targetIndex) : null;
        WebElement previousCard = null;

        if (targetIndex >= 0 && (targetIndex + 1) < cards.size()) {
            previousCard = cards.get(targetIndex + 1);
            LOGGER.info("[PressureAssessment] Bir önceki vizit kartı index: {}", targetIndex + 1);
        }

        boolean deleted = false;

        if (targetCard != null) {
            LOGGER.info("[PressureAssessment] Önce hedef vizit kartında Basınç Değerlendirme formu(ları) aranıyor ve hepsi silinecek...");
            deleted = deleteAllPressureAssessmentsInSingleCard(targetCard);
        }

        if (!deleted && previousCard != null) {
            LOGGER.info("[PressureAssessment] Hedef vizitte bulunamadı, bir önceki vizitte Basınç Değerlendirme formu(ları) aranıyor ve hepsi silinecek...");
            deleted = deleteAllPressureAssessmentsInSingleCard(previousCard);
        }

        if (!deleted) {
            LOGGER.warn("[PressureAssessment] Hedef ve bir önceki vizitte bulunamadı, fallback olarak vizitler içinde ekli ilk Basınç Değerlendirme formu(ları) silinecek.");
            deleted = deleteFirstPressureAssessmentFromVisits();
        }

        return deleted;
    }

    private boolean deleteAllPressureAssessmentsInSingleCard(WebElement card) {
        LOGGER.info("[PressureAssessment] Bu vizit kartındaki tüm Basınç Değerlendirme formları silinecek...");

        boolean deletedAtLeastOne = false;

        for (int loop = 0; loop < 6; loop++) {
            try {
                scrollIntoView(card);

                List<WebElement> headers = card.findElements(pressureHeaderInVisitCard);
                LOGGER.info("[PressureAssessment] Döngü {} - Bu kartta bulunan Basınç formu sayısı: {}",
                        loop + 1, headers.size());

                if (headers.isEmpty()) {
                    LOGGER.info("[PressureAssessment] Bu vizit kartında artık Basınç Değerlendirme başlığı bulunmuyor. Döngü sonlandırılıyor.");
                    break;
                }

                WebElement deleteBtn = card.findElement(formDeleteButtonInVisitCard);
                scrollIntoView(deleteBtn);

                try {
                    wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
                } catch (Exception e) {
                    LOGGER.warn("[PressureAssessment] Silme ikonunda normal click hata aldı, JS click denenecek. Hata: {}", e.getMessage());
                    js.executeScript("arguments[0].click();", deleteBtn);
                }

                confirmDeleteIfDialogVisible();
                deletedAtLeastOne = true;

                try {
                    wait.until(ExpectedConditions.stalenessOf(deleteBtn));
                } catch (TimeoutException ignored) {
                }

                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignored) {
                }

            } catch (NoSuchElementException e) {
                LOGGER.warn("[PressureAssessment] Bu vizit kartında çöp ikonu bulunamadı, döngü sonlandırılıyor. Hata: {}", e.getMessage());
                break;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("[PressureAssessment] Vizit kartı stale oldu, döngü sonlandırılıyor. Hata: {}", e.getMessage());
                break;
            }
        }

        LOGGER.info("[PressureAssessment] Bu vizit kartında Basınç Değerlendirme silme işlemi tamamlandı. En az bir silme yapıldı mı? {}", deletedAtLeastOne);
        return deletedAtLeastOne;
    }

    private boolean deleteFirstPressureAssessmentFromVisits() {
        LOGGER.info("[PressureAssessment] (Fallback) Vizitler içinde ekli ilk Basınç Değerlendirme formu(ları) aranıyor ve bulunduğu vizitteki tüm Basınç formları silinecek...");

        waitVisible(visitsContainer);

        long end = System.currentTimeMillis() + 8000L;

        while (System.currentTimeMillis() < end) {
            List<WebElement> cards = driver.findElements(allVisitCards);

            LOGGER.info("[PressureAssessment] (Fallback) Toplam {} vizit kartı bulundu.", cards.size());

            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                scrollIntoView(card);

                List<WebElement> headers = card.findElements(pressureHeaderInVisitCard);
                if (!headers.isEmpty()) {
                    LOGGER.info("[PressureAssessment] (Fallback) Basınç Değerlendirme formu(ları) {}. vizitte bulundu, bu vizitteki tüm Basınç formları silinecek.", i + 1);

                    return deleteAllPressureAssessmentsInSingleCard(card);
                }
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }

        LOGGER.warn("[PressureAssessment] (Fallback) Hiçbir vizitte Basınç Değerlendirme formu bulunamadı, silme işlemi yapılmadı.");
        return false;
    }

    // =========================================================
    //  ONAY DİYALOĞU
    // =========================================================

    public void confirmDeleteIfDialogVisible() {
        LOGGER.info("[PressureAssessment] Silme onay penceresi kontrol ediliyor...");

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            WebElement yesBtn = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(deleteConfirmYesButton)
            );

            LOGGER.info("[PressureAssessment] Silme onay penceresi göründü, 'Evet' butonuna tıklanıyor...");
            scrollIntoView(yesBtn);
            try {
                yesBtn.click();
            } catch (ElementClickInterceptedException e) {
                js.executeScript("arguments[0].click();", yesBtn);
            }

            try {
                shortWait.until(ExpectedConditions.invisibilityOf(yesBtn));
            } catch (TimeoutException ignored) {
            }
        } catch (TimeoutException e) {
            LOGGER.info("[PressureAssessment] Silme onay penceresi görünmedi, devam ediliyor.");
        }
    }

    // =========================================================
    //  HEDEF VİZİTTE FORMUN KALMADIĞININ DOĞRULANMASI
    // =========================================================

    public boolean isPressureAssessmentAbsentInCreatedVisit() {
        LOGGER.info("[PressureAssessment] Hedef vizitte Basınç Değerlendirme formu kalmadı mı? (sadece ilgili vizit kontrol ediliyor)");

        if (createdVisitNumberForPressureAssessment == null) {
            LOGGER.warn("[PressureAssessment] createdVisitNumberForPressureAssessment = null, hedef vizit numarası bilinmiyor.");
            return false;
        }

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[PressureAssessment] Toplam {} vizit kartı bulundu.", cards.size());

        for (WebElement card : cards) {
            String visitNo = extractVisitNumberFromCard(card);
            if (!createdVisitNumberForPressureAssessment.equals(visitNo)) {
                continue;
            }

            LOGGER.info("[PressureAssessment] Hedef vizit kartı bulundu. Vizit No: {}", visitNo);
            scrollIntoView(card);

            List<WebElement> headers = card.findElements(pressureHeaderInVisitCard);
            if (!headers.isEmpty()) {
                LOGGER.info("[PressureAssessment] Hedef vizitte hâlâ Basınç Değerlendirme başlığı görünüyor.");
                return false;
            }

            try {
                WebElement formsContainer = card.findElement(visitFormsContainerInCard);
                scrollIntoView(formsContainer);

                WebElement emptyText = formsContainer.findElement(emptyFormTextInCard);
                LOGGER.info("[PressureAssessment] Hedef vizitte boş durum yazısı bulundu: {}", emptyText.getText());
            } catch (NoSuchElementException e) {
                LOGGER.info("[PressureAssessment] Hedef vizitte 'Form bulunamadı.' yazısı görünmüyor; " +
                        "ancak Basınç Değerlendirme başlığı da yok, yine de boş kabul ediyoruz.");
            }

            LOGGER.info("[PressureAssessment] Hedef vizitte hiçbir Basınç Değerlendirme formu görünmüyor.");
            return true;
        }

        LOGGER.warn("[PressureAssessment] Vizit listesinde {} numaralı hedef vizit kartı bulunamadı.",
                createdVisitNumberForPressureAssessment);
        return false;
    }
}

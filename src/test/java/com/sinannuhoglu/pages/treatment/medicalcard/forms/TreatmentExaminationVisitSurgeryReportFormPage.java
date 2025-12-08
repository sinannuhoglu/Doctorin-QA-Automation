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
 * Vizit içi "Ameliyat Raporu" Page Object
 */
public class TreatmentExaminationVisitSurgeryReportFormPage {

    private static final Logger LOGGER =
            LogManager.getLogger(TreatmentExaminationVisitSurgeryReportFormPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    /**
     * Ameliyat Raporu formunun hangi vizitte açıldığını takip etmek için vizit numarası (örn: "1767").
     */
    private String createdVisitNumberForSurgeryReport;

    public TreatmentExaminationVisitSurgeryReportFormPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ================= VİZİT LISTESİ LOCATOR'LARI =================

    // Muayene sekmesindeki vizit listesi container
    private final By visitsContainer = By.xpath(
            "//section[contains(@class,'flex') and contains(@class,'flex-1')]//div" +
                    "[contains(@class,'py-3') and contains(@class,'flex') and contains(@class,'flex-col')" +
                    " and contains(@class,'overflow-hidden') and contains(@class,'hide-scrollbar')]"
    );

    // En üstteki aktif vizit kartı (animate-highlight-border)
    private final By firstVisitCard = By.xpath(
            "//div[contains(@class,'py-3') and contains(@class,'flex') and contains(@class,'flex-col')" +
                    " and contains(@class,'overflow-hidden') and contains(@class,'hide-scrollbar')]" +
                    "//div[contains(@class,'flex') and contains(@class,'flex-col') and contains(@class,'overflow-hidden')" +
                    " and contains(@class,'gap-3') and contains(@class,'flex-1')]" +
                    "//div[contains(@class,'rounded-xl') and contains(@class,'bg-white') and contains(@class,'p-1')" +
                    " and contains(@class,'dark:bg-surface-800') and contains(@class,'animate-highlight-border')][1]"
    );

    // Tüm vizit kartları (1., 2., 3. ... sırayla)
    private final By allVisitCards = By.xpath(
            "//div[@id='overview-treatment-content']" +
                    "//div[contains(@class,'rounded-xl') and contains(@class,'bg-white')" +
                    " and contains(@class,'p-1') and contains(@class,'dark:bg-surface-800')]"
    );

    // Vizit kartı header'ındaki "Formlar" butonu
    private final By visitFormsButtonInsideCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'items-center') and contains(@class,'gap-1.5')]" +
                    "//button[1]"
    );

    // Açılan formlar popup'ı (root div)
    private final By formsPopupRoot = By.xpath(
            "//div[contains(@class,'absolute') and contains(@class,'rounded-md')" +
                    " and contains(@class,'shadow-md') and contains(@class,'bg-white')" +
                    " and contains(@class,'dark:bg-surface-700') and contains(@class,'p-2')" +
                    " and contains(@class,'max-h-64') and contains(@class,'overflow-y-auto')" +
                    " and contains(@class,'overflow-x-hidden') and contains(@class,'border')" +
                    " and contains(@class,'border-surface-300') and contains(@class,'dark:border-white/20')]"
    );

    // Popup içindeki grid container
    private final By formsPopupGrid = By.xpath(
            "//div[contains(@class,'absolute') and contains(@class,'rounded-md')" +
                    " and contains(@class,'shadow-md') and contains(@class,'bg-white')" +
                    " and contains(@class,'dark:bg-surface-700') and contains(@class,'p-2')" +
                    " and contains(@class,'max-h-64') and contains(@class,'overflow-y-auto')" +
                    " and contains(@class,'overflow-x-hidden') and contains(@class,'border')" +
                    " and contains(@class,'border-surface-300') and contains(@class,'dark:border-white/20')]" +
                    "//div[contains(@class,'grid') and contains(@class,'grid-cols-2') and contains(@class,'gap-2')]"
    );

    // Popup içindeki "Ameliyat Raporu" butonu
    private final By surgeryReportButtonInPopup = By.xpath(
            "//button[normalize-space()='Ameliyat Raporu']"
    );

    // Vizit gövdesindeki formlar container'ı (flex flex-col gap-1)
    private final By visitFormsContainerInCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'flex-col') and contains(@class,'gap-1')]"
    );

    // Boş durumda görünen "Form bulunamadı." metni
    private final By emptyFormTextInCard = By.xpath(
            ".//p[contains(@class,'font-semibold') and normalize-space()='Form bulunamadı.']"
    );

    // Vizit kartı içinde "Ameliyat Raporu" başlığı (header satırı)
    private final By surgeryReportHeaderInVisitCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'items-center')" +
                    " and contains(@class,'justify-between') and contains(@class,'px-3')]" +
                    "//span[normalize-space()='Ameliyat Raporu']"
    );

    // Aynı vizit kartı içinde çöp/sil butonu (tüm formlar için ortak)
    private final By formDeleteButtonInVisitCard = By.xpath(
            ".//span[contains(@class,'e-icons') and contains(@class,'e-trash') and contains(@class,'e-btn-icon')]/ancestor::button[1]"
    );

    // Silme onay dialogu "Evet" butonu (varsa)
    private final By deleteConfirmYesButton = By.xpath(
            "//div[contains(@class,'e-dlg-container')]//button" +
                    "[contains(@class,'e-primary') and normalize-space()='Evet']"
    );

    // ================= AMELİYAT RAPORU FORM LOCATOR'LARI =================

    // Form root – Ameliyat Raporu formu
    private final By surgeryReportFormRoot = By.xpath(
            "//form[.//label[normalize-space()='Ameliyat Tarihi']]"
    );

    // Ameliyat Tarihi – datepicker ikonu
    private final By surgeryDateIcon = By.xpath(
            "//label[normalize-space()='Ameliyat Tarihi']" +
                    "/following::span[contains(@class,'e-date-icon')][1]"
    );

    // Ameliyat Saati – timepicker input
    private final By surgeryTimeInput = By.xpath(
            "//label[normalize-space()='Ameliyat Saati']" +
                    "/following::input[contains(@class,'e-timepicker') or contains(@class,'e-input')][1]"
    );

    // Ameliyat Ekibi (Cerrah, Asistan, Anestezist) – textarea
    private final By surgeryTeamTextarea = By.xpath(
            "//label[contains(normalize-space(),'Ameliyat Ekibi')]" +
                    "/following::textarea[1]"
    );

    // Ameliyat Öncesi Tanısı – input
    private final By preOpDiagnosisInput = By.xpath(
            "//label[normalize-space()='Ameliyat Öncesi Tanısı']" +
                    "/following::input[1]"
    );

    // Ameliyat Sonrası Tanısı – input
    private final By postOpDiagnosisInput = By.xpath(
            "//label[normalize-space()='Ameliyat Sonrası Tanısı']" +
                    "/following::input[1]"
    );

    // Alınan Numuneler – textarea
    private final By takenSamplesTextarea = By.xpath(
            "//label[normalize-space()='Alınan Numuneler']" +
                    "/following::textarea[1]"
    );

    // Periperatif Komplikasyonlar – textarea
    private final By periopComplicationsTextarea = By.xpath(
            "//label[normalize-space()='Periperatif Komplikasyonlar']" +
                    "/following::textarea[1]"
    );

    // Kaybedilen ve Transfüzyon Yapılan Kan Miktarı – textarea
    private final By bloodLossTextarea = By.xpath(
            "//label[normalize-space()='Kaybedilen ve Transfüzyon Yapılan Kan Miktarı']" +
                    "/following::textarea[1]"
    );

    // Ameliyatta Konulan Dren / Tüp / Sonda – textarea
    private final By drainInfoTextarea = By.xpath(
            "//label[normalize-space()='Ameliyatta Konulan Dren / Tüp / Sonda']" +
                    "/following::textarea[1]"
    );

    // Kaydet butonu (sadece Ameliyat Raporu formu içinde)
    private final By surgerySaveButton = By.xpath(
            "//form[.//label[normalize-space()='Ameliyat Tarihi']]" +
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

    /**
     * Retry'lı clear & type.
     */
    private void clearAndType(By locator, String text) {
        RuntimeException last = null;

        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = wait.until(
                        ExpectedConditions.refreshed(
                                ExpectedConditions.visibilityOfElementLocated(locator)
                        )
                );

                scrollIntoView(el);

                try {
                    el.click();
                } catch (ElementClickInterceptedException e) {
                    js.executeScript("arguments[0].click();", el);
                }

                try {
                    el.clear();
                } catch (InvalidElementStateException ignored) {
                }

                try {
                    el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                    el.sendKeys(Keys.chord(Keys.COMMAND, "a"));
                    el.sendKeys(Keys.DELETE);
                } catch (Exception ignored) {
                }

                el.sendKeys(text);
                return;
            } catch (StaleElementReferenceException e) {
                last = e;
                LOGGER.warn("[SurgeryReport] clearAndType sırasında stale aldı, yeniden denenecek... Deneme: {}", i + 1);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (last != null) {
            throw last;
        }
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

            // Önce tırnak içinde sayıları arayalım: "1767"
            Pattern quotedPattern = Pattern.compile("\"(\\d+)\"");
            Matcher quotedMatcher = quotedPattern.matcher(fullText);
            String lastMatch = null;
            while (quotedMatcher.find()) {
                lastMatch = quotedMatcher.group(1);
            }
            if (lastMatch != null) {
                return lastMatch;
            }

            // Yedek: 3 ve üzeri rakamdan oluşan son blok
            Pattern digitsPattern = Pattern.compile("\\b(\\d{3,})\\b");
            Matcher digitsMatcher = digitsPattern.matcher(fullText);
            lastMatch = null;
            while (digitsMatcher.find()) {
                lastMatch = digitsMatcher.group(1);
            }
            return lastMatch;
        } catch (Exception e) {
            LOGGER.warn("[SurgeryReport] Vizit numarası çözümlenemedi: {}", e.getMessage());
            return null;
        }
    }

    // =========================================================
    //  FORM AÇILIŞI
    // =========================================================

    public void openFirstVisitSurgeryReportForm() {
        LOGGER.info("[SurgeryReport] İlk vizitin Ameliyat Raporu formu açılıyor...");

        waitVisible(visitsContainer);

        WebElement card = waitVisible(firstVisitCard);
        scrollIntoView(card);

        createdVisitNumberForSurgeryReport = extractVisitNumberFromCard(card);
        LOGGER.info("[SurgeryReport] Ameliyat Raporu açılan vizit numarası: {}", createdVisitNumberForSurgeryReport);

        WebElement formsButton = card.findElement(visitFormsButtonInsideCard);

        LOGGER.info("[SurgeryReport] Vizit içindeki Formlar butonuna tıklanıyor...");
        try {
            scrollIntoView(formsButton);
            wait.until(ExpectedConditions.elementToBeClickable(formsButton)).click();
        } catch (Exception e) {
            LOGGER.warn("[SurgeryReport] Normal click başarısız, JS click denenecek. Hata: {}", e.getMessage());
            js.executeScript("arguments[0].click();", formsButton);
        }

        try {
            Thread.sleep(700);
        } catch (InterruptedException ignored) {
        }

        WebElement popupRoot = waitPresent(formsPopupRoot);
        scrollIntoView(popupRoot);

        waitPresent(formsPopupGrid);

        LOGGER.info("[SurgeryReport] Pop-up içindeki Ameliyat Raporu butonu bekleniyor...");
        WebElement surgeryReportButton = waitPresent(surgeryReportButtonInPopup);
        scrollIntoView(surgeryReportButton);

        try {
            js.executeScript("arguments[0].click();", surgeryReportButton);
        } catch (JavascriptException e) {
            LOGGER.warn("[SurgeryReport] JS click hatası, fallback normal click denenecek. Hata: {}", e.getMessage());
            surgeryReportButton.click();
        }

        waitVisible(surgeryReportFormRoot);
        LOGGER.info("[SurgeryReport] Ameliyat Raporu formu vizit içinde açıldı.");
    }

    // =========================================================
    //  FORM ALANLARININ DOLDURULMASI (DataTable)
    // =========================================================

    /**
     * Ameliyat Raporu formunu DataTable satırlarına göre doldurur ve Kaydet'e tıklar.
     *
     * Beklenen kolonlar:
     *  - alan  : "Ameliyat Tarihi", "Ameliyat Saati", "Ameliyat Ekibi", ...
     *  - tip   : date / time / text / textarea (şimdilik sadece log amaçlı)
     *  - değer : alanın değeri (örn: BUGÜN, 12:30, "Kronik tonsillit", ...)
     */
    public void fillSurgeryReportFromTable(List<Map<String, String>> rows) {
        if (rows == null || rows.isEmpty()) {
            LOGGER.warn("[SurgeryReport] DataTable boş geldi, Ameliyat Raporu formu doldurulmayacak.");
            return;
        }

        LOGGER.info("[SurgeryReport] DataTable ile Ameliyat Raporu formu dolduruluyor. Satır sayısı: {}", rows.size());

        for (Map<String, String> row : rows) {
            String field = getCell(row, "alan");
            String type = getCell(row, "tip").toLowerCase(Locale.ROOT);
            String value = getCell(row, "değer");

            LOGGER.info("[SurgeryReport] Satır işleniyor -> alan='{}', tip='{}', değer='{}'", field, type, value);

            if (field.isEmpty()) {
                LOGGER.warn("[SurgeryReport] 'alan' değeri boş, satır atlanıyor. Satır: {}", row);
                continue;
            }

            switch (field) {
                case "Ameliyat Tarihi":
                    if ("bugün".equalsIgnoreCase(value)) {
                        setSurgeryDateToToday();
                    } else if (!value.isEmpty()) {
                        LOGGER.warn("[SurgeryReport] Ameliyat Tarihi için sadece 'BUGÜN' senaryosu destekleniyor. Gelen değer: '{}'", value);
                    }
                    break;

                case "Ameliyat Saati":
                    if (!value.isEmpty()) {
                        setSurgeryTime(value);
                    }
                    break;

                case "Ameliyat Ekibi":
                    if (!value.isEmpty()) {
                        fillSurgeryTeam(value);
                    }
                    break;

                case "Ameliyat Öncesi Tanısı":
                    if (!value.isEmpty()) {
                        fillPreOpDiagnosis(value);
                    }
                    break;

                case "Ameliyat Sonrası Tanısı":
                    if (!value.isEmpty()) {
                        fillPostOpDiagnosis(value);
                    }
                    break;

                case "Alınan Numuneler":
                    if (!value.isEmpty()) {
                        fillTakenSamples(value);
                    }
                    break;

                case "Periperatif Komplikasyonlar":
                    if (!value.isEmpty()) {
                        fillPeriopComplications(value);
                    }
                    break;

                case "Kaybedilen ve Transfüzyon Yapılan Kan Miktarı":
                    if (!value.isEmpty()) {
                        fillBloodLoss(value);
                    }
                    break;

                case "Ameliyatta Konulan Dren Tüp Sonda":
                case "Ameliyatta Konulan Dren / Tüp / Sonda":
                    if (!value.isEmpty()) {
                        fillDrainInfo(value);
                    }
                    break;

                default:
                    LOGGER.warn("[SurgeryReport] Tanımsız alan ismi: '{}'. Bu satır için özel bir işlem yapılmayacak.", field);
            }
        }

        clickSurgerySaveButton();
    }

    // Eski alan bazlı metotlar – DataTable metodu bunları tekrar kullanıyor

    public void setSurgeryDateToToday() {
        LOGGER.info("[SurgeryReport] Ameliyat Tarihi için bugün seçiliyor...");

        safeClick(surgeryDateIcon);

        WebDriverWait dateWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            // Önce "Bugün" butonunu dene
            WebElement todayBtn = dateWait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[normalize-space()='Bugün' or @title='Bugün']")
                    )
            );
            scrollIntoView(todayBtn);
            todayBtn.click();
            return;
        } catch (TimeoutException ignored) {
        }

        try {
            // Alternatif: takvim hücresindeki "today" sınıfı
            WebElement todayCell = dateWait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("td.e-today, span.e-day.e-today")
                    )
            );
            scrollIntoView(todayCell);
            todayCell.click();
        } catch (TimeoutException e) {
            LOGGER.warn("[SurgeryReport] Takvimde bugünün tarihi bulunamadı, tarih alanı manuel kalacak.");
        }
    }

    public void setSurgeryTime(String time) {
        LOGGER.info("[SurgeryReport] Ameliyat Saati alanı dolduruluyor...");
        clearAndType(surgeryTimeInput, time);
    }

    public void fillSurgeryTeam(String text) {
        LOGGER.info("[SurgeryReport] Ameliyat Ekibi alanı dolduruluyor...");
        clearAndType(surgeryTeamTextarea, text);
    }

    public void fillPreOpDiagnosis(String text) {
        LOGGER.info("[SurgeryReport] Ameliyat Öncesi Tanısı alanı dolduruluyor...");
        clearAndType(preOpDiagnosisInput, text);
    }

    public void fillPostOpDiagnosis(String text) {
        LOGGER.info("[SurgeryReport] Ameliyat Sonrası Tanısı alanı dolduruluyor...");
        clearAndType(postOpDiagnosisInput, text);
    }

    public void fillTakenSamples(String text) {
        LOGGER.info("[SurgeryReport] Alınan Numuneler alanı dolduruluyor...");
        clearAndType(takenSamplesTextarea, text);
    }

    public void fillPeriopComplications(String text) {
        LOGGER.info("[SurgeryReport] Periperatif Komplikasyonlar alanı dolduruluyor...");
        clearAndType(periopComplicationsTextarea, text);
    }

    public void fillBloodLoss(String text) {
        LOGGER.info("[SurgeryReport] Kaybedilen ve Transfüzyon Yapılan Kan Miktarı alanı dolduruluyor...");
        clearAndType(bloodLossTextarea, text);
    }

    public void fillDrainInfo(String text) {
        LOGGER.info("[SurgeryReport] Ameliyatta Konulan Dren / Tüp / Sonda alanı dolduruluyor...");
        clearAndType(drainInfoTextarea, text);
    }

    // =========================================================
    //  KAYDET & KAYIT DOĞRULAMA
    // =========================================================

    public void clickSurgerySaveButton() {
        LOGGER.info("[SurgeryReport] Kaydet butonuna tıklanıyor...");
        safeClick(surgerySaveButton);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Kayıt doğrulaması:
     *  - Hedef vizit numarasını kullanarak DOM'da vizit kartını bulur.
     *  - Hem bu vizitte hem de hemen altındaki (index+1) vizitte
     *    "Ameliyat Raporu" başlığını arar.
     */
    public boolean isSurgeryReportSavedSuccessfully() {
        LOGGER.info("[SurgeryReport] Ameliyat Raporu kaydının başarıyla oluşturulup oluşturulmadığı kontrol ediliyor...");

        if (createdVisitNumberForSurgeryReport == null) {
            LOGGER.warn("[SurgeryReport] createdVisitNumberForSurgeryReport = null, hedef vizit numarası bilinmiyor.");
            return false;
        }

        long end = System.currentTimeMillis() + 8000L;

        while (System.currentTimeMillis() < end) {
            List<WebElement> cards = driver.findElements(allVisitCards);
            LOGGER.info("[SurgeryReport] Kayıt kontrolü için toplam {} vizit kartı incelenecek.", cards.size());

            if (cards.isEmpty()) {
                LOGGER.warn("[SurgeryReport] Hiç vizit kartı bulunamadı.");
                return false;
            }

            int targetIndex = -1;

            // Önce hedef vizit kartını index olarak bul
            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                String visitNo = extractVisitNumberFromCard(card);
                LOGGER.info("[SurgeryReport] {}. vizit kartının numarası: {}", i + 1, visitNo);

                if (createdVisitNumberForSurgeryReport.equals(visitNo)) {
                    targetIndex = i;
                    LOGGER.info("[SurgeryReport] Kayıt alınan hedef vizit kartı bulundu. Index: {}, Vizit No: {}",
                            targetIndex, visitNo);
                    break;
                }
            }

            // Hedef vizit numarası DOM'da çözümlenemiyorsa fallback: ilk karta bak
            if (targetIndex == -1) {
                LOGGER.warn("[SurgeryReport] Hedef vizit numarası DOM'da bulunamadı, fallback olarak ilk karta bakılacak.");
                targetIndex = 0;
            }

            // Hedef vizit + hemen altındaki vizit (varsa) kontrol edilir
            int[] indexesToCheck = new int[]{targetIndex, targetIndex + 1};

            for (int idx : indexesToCheck) {
                if (idx < 0 || idx >= cards.size()) continue;

                WebElement cardToCheck = cards.get(idx);
                scrollIntoView(cardToCheck);

                List<WebElement> headers = cardToCheck.findElements(surgeryReportHeaderInVisitCard);
                if (!headers.isEmpty()) {
                    LOGGER.info("[SurgeryReport] Ameliyat Raporu başlığı {}. vizit kartında bulundu (index: {}).",
                            idx + 1, idx);
                    return true;
                }
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }

        LOGGER.warn("[SurgeryReport] Açılan vizitte ve hemen altındaki vizitte Ameliyat Raporu başlığı bulunamadı, kayıt başarısız görünüyor.");
        return false;
    }

    // =========================================================
    //  SİLME: Hedef vizit → bir önceki vizit → fallback
    // =========================================================

    /**
     * Formun açıldığı vizit numarasını kullanarak:
     *  1) Önce ilgili vizit kartında Ameliyat Raporu'nu arar ve siler.
     *  2) Bulunamazsa, DOM'daki bir sonraki kartta (bir önceki vizit) arar ve siler.
     *  3) Hâlâ bulunamazsa genel arama fallback'i ile ilk bulduğu Ameliyat Raporu'nu siler.
     */
    public boolean deleteSurgeryReportFromTargetOrPreviousVisit() {
        LOGGER.info("[SurgeryReport] Hedef vizitte (ve gerekirse bir önceki vizitte) Ameliyat Raporu silme akışı başlıyor...");

        waitVisible(visitsContainer);

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[SurgeryReport] Toplam {} vizit kartı bulundu.", cards.size());

        if (cards.isEmpty()) {
            LOGGER.warn("[SurgeryReport] Hiç vizit kartı yok, silme işlemi yapılamaz.");
            return false;
        }

        int targetIndex = -1;
        if (createdVisitNumberForSurgeryReport != null) {
            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                scrollIntoView(card);
                String visitNo = extractVisitNumberFromCard(card);
                LOGGER.info("[SurgeryReport] {}. vizit kartının numarası: {}", i + 1, visitNo);

                if (createdVisitNumberForSurgeryReport.equals(visitNo)) {
                    targetIndex = i;
                    LOGGER.info("[SurgeryReport] Hedef vizit kartı bulundu. Index: {}, Vizit No: {}", targetIndex, visitNo);
                    break;
                }
            }
        } else {
            LOGGER.warn("[SurgeryReport] createdVisitNumberForSurgeryReport = null, hedef vizit numarası saklanamamış.");
        }

        WebElement targetCard = (targetIndex >= 0 && targetIndex < cards.size()) ? cards.get(targetIndex) : null;
        WebElement previousCard = null;

        if (targetIndex >= 0 && (targetIndex + 1) < cards.size()) {
            previousCard = cards.get(targetIndex + 1);
            LOGGER.info("[SurgeryReport] Bir önceki vizit kartı index: {}", targetIndex + 1);
        }

        if (targetCard != null) {
            LOGGER.info("[SurgeryReport] Önce hedef vizit kartında Ameliyat Raporu aranıyor...");
            if (deleteSurgeryReportInSingleCard(targetCard)) {
                return true;
            }
        }

        if (previousCard != null) {
            LOGGER.info("[SurgeryReport] Hedef vizitte bulunamadı, bir önceki vizitte Ameliyat Raporu aranıyor...");
            if (deleteSurgeryReportInSingleCard(previousCard)) {
                return true;
            }
        }

        LOGGER.warn("[SurgeryReport] Hedef ve bir önceki vizitte bulunamadı, fallback olarak ilk bulunan Ameliyat Raporu silinecek.");
        return deleteFirstSurgeryReportFromVisits();
    }

    private boolean deleteSurgeryReportInSingleCard(WebElement card) {
        scrollIntoView(card);

        for (int retry = 0; retry < 3; retry++) {
            List<WebElement> headers = card.findElements(surgeryReportHeaderInVisitCard);
            if (!headers.isEmpty()) {
                LOGGER.info("[SurgeryReport] Bu vizit kartında Ameliyat Raporu başlığı bulundu (retry: {}).", retry);

                WebElement deleteBtn = card.findElement(formDeleteButtonInVisitCard);
                scrollIntoView(deleteBtn);

                try {
                    wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
                } catch (Exception e) {
                    LOGGER.warn("[SurgeryReport] Silme ikonunda normal click hata aldı, JS click denenecek. Hata: {}", e.getMessage());
                    js.executeScript("arguments[0].click();", deleteBtn);
                }

                confirmDeleteIfDialogVisible();

                try {
                    wait.until(ExpectedConditions.stalenessOf(deleteBtn));
                } catch (TimeoutException ignored) {
                }

                return true;
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }

        LOGGER.info("[SurgeryReport] Bu vizit kartında Ameliyat Raporu başlığı bulunamadı.");
        return false;
    }

    private boolean deleteFirstSurgeryReportFromVisits() {
        LOGGER.info("[SurgeryReport] (Fallback) Vizitler içinde ekli ilk Ameliyat Raporu aranıyor ve silinecek...");

        waitVisible(visitsContainer);

        long end = System.currentTimeMillis() + 8000L;

        while (System.currentTimeMillis() < end) {
            List<WebElement> cards = driver.findElements(allVisitCards);

            LOGGER.info("[SurgeryReport] (Fallback) Toplam {} vizit kartı bulundu.", cards.size());

            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                scrollIntoView(card);

                List<WebElement> headers = card.findElements(surgeryReportHeaderInVisitCard);
                if (!headers.isEmpty()) {
                    LOGGER.info("[SurgeryReport] (Fallback) Ameliyat Raporu {}. vizitte bulundu, silme ikonuna tıklanacak.", i + 1);

                    WebElement deleteBtn = card.findElement(formDeleteButtonInVisitCard);
                    scrollIntoView(deleteBtn);

                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
                    } catch (Exception e) {
                        LOGGER.warn("[SurgeryReport] (Fallback) Normal click silme ikonunda hata aldı, JS click denenecek. Hata: {}", e.getMessage());
                        js.executeScript("arguments[0].click();", deleteBtn);
                    }

                    confirmDeleteIfDialogVisible();

                    try {
                        wait.until(ExpectedConditions.stalenessOf(deleteBtn));
                    } catch (TimeoutException ignored) {
                    }

                    return true;
                }
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }

        LOGGER.warn("[SurgeryReport] (Fallback) Hiçbir vizitte Ameliyat Raporu bulunamadı, silme işlemi yapılmadı.");
        return false;
    }

    public void confirmDeleteIfDialogVisible() {
        LOGGER.info("[SurgeryReport] Silme onay penceresi kontrol ediliyor...");

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            WebElement yesBtn = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(deleteConfirmYesButton)
            );

            LOGGER.info("[SurgeryReport] Silme onay penceresi göründü, 'Evet' butonuna tıklanıyor...");
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
            LOGGER.info("[SurgeryReport] Silme onay penceresi görünmedi, devam ediliyor.");
        }
    }

    // =========================================================
    //  HEDEF VİZİTTE FORMUN KALMADIĞININ DOĞRULANMASI
    // =========================================================

    /**
     * Ameliyat Raporu’nun açıldığı vizitte (createdVisitNumberForSurgeryReport)
     * artık hiçbir Ameliyat Raporu kalmadı mı?
     *
     *  - Sadece ilgili vizit kartına bakar.
     *  - Başka vizitleri kontrol etmez.
     *  - Bekleme / uzun döngü kullanmaz, anlık DOM’a bakar.
     */
    public boolean isSurgeryReportAbsentInCreatedVisit() {
        LOGGER.info("[SurgeryReport] Hedef vizitte Ameliyat Raporu kalmadı mı? (sadece ilgili vizit kontrol ediliyor)");

        if (createdVisitNumberForSurgeryReport == null) {
            LOGGER.warn("[SurgeryReport] createdVisitNumberForSurgeryReport = null, hedef vizit numarası bilinmiyor.");
            return false;
        }

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[SurgeryReport] Toplam {} vizit kartı bulundu.", cards.size());

        for (WebElement card : cards) {
            String visitNo = extractVisitNumberFromCard(card);
            if (!createdVisitNumberForSurgeryReport.equals(visitNo)) {
                continue;
            }

            LOGGER.info("[SurgeryReport] Hedef vizit kartı bulundu. Vizit No: {}", visitNo);
            scrollIntoView(card);

            List<WebElement> headers = card.findElements(surgeryReportHeaderInVisitCard);
            if (!headers.isEmpty()) {
                LOGGER.info("[SurgeryReport] Hedef vizitte hâlâ Ameliyat Raporu başlığı görünüyor.");
                return false;
            }

            try {
                WebElement formsContainer = card.findElement(visitFormsContainerInCard);
                scrollIntoView(formsContainer);

                WebElement emptyText = formsContainer.findElement(emptyFormTextInCard);
                LOGGER.info("[SurgeryReport] Hedef vizitte boş durum yazısı bulundu: {}", emptyText.getText());
            } catch (NoSuchElementException e) {
                LOGGER.info("[SurgeryReport] Hedef vizitte 'Form bulunamadı.' yazısı görünmüyor; "
                        + "ancak Ameliyat Raporu başlığı da yok, yine de boş kabul ediyoruz.");
            }

            LOGGER.info("[SurgeryReport] Hedef vizitte hiçbir Ameliyat Raporu görünmüyor.");
            return true;
        }

        LOGGER.warn("[SurgeryReport] Vizit listesinde {} numaralı hedef vizit kartı bulunamadı.", createdVisitNumberForSurgeryReport);
        return false;
    }
}

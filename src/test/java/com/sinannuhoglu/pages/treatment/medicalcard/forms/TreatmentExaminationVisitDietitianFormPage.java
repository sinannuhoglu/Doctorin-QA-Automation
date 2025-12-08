package com.sinannuhoglu.pages.treatment.medicalcard.forms;

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
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tıbbi İşlemler > İş Listesi > Detay > Muayene sekmesi
 * Vizit içi "Diyetisyen" Formu Page Object
 */
public class TreatmentExaminationVisitDietitianFormPage {

    private static final Logger LOGGER =
            LogManager.getLogger(TreatmentExaminationVisitDietitianFormPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    /**
     * Diyetisyen formunun hangi vizitte açıldığını takip etmek için vizit numarası (örn: "1767").
     */
    private String createdVisitNumberForDietitianForm;

    public TreatmentExaminationVisitDietitianFormPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ================= VİZİT LİSTESİ LOCATOR'LARI =================

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

    // Popup içindeki "Diyetisyen" butonu
    private final By dietitianButtonInPopup = By.xpath(
            "//button[normalize-space()='Diyetisyen']"
    );

    // Vizit gövdesindeki formlar container'ı (flex flex-col gap-1)
    private final By visitFormsContainerInCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'flex-col') and contains(@class,'gap-1')]"
    );

    // Boş durumda görünen "Form bulunamadı." metni
    private final By emptyFormTextInCard = By.xpath(
            ".//p[contains(@class,'font-semibold') and normalize-space()='Form bulunamadı.']"
    );

    // Vizit kartı içinde "Diyetisyen" başlığı (header satırı)
    private final By dietitianHeaderInVisitCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'items-center')" +
                    " and contains(@class,'justify-between') and contains(@class,'px-3')]" +
                    "//span[normalize-space()='Diyetisyen']"
    );

    // Aynı vizit kartı içinde çöp/sil butonu (tüm formlar için ortak)
    private final By formDeleteButtonInVisitCard = By.xpath(
            ".//span[contains(@class,'e-icons') and contains(@class,'e-trash') and contains(@class,'e-btn-icon')]/ancestor::button[1]"
    );

    // Silme onay dialogu "Evet" butonu
    private final By deleteConfirmYesButton = By.xpath(
            "//div[contains(@class,'e-dlg-container')]//button" +
                    "[contains(@class,'e-primary') and normalize-space()='Evet']"
    );

    // ================= DİYETİSYEN FORM LOCATOR'LARI =================

    // Form root
    private final By dietitianFormRoot = By.xpath("//form[@method='post']");

    // Ziyaret Tarihi input
    private final By visitDateInput = By.xpath(
            "//form[@method='post']//label[normalize-space()='Ziyaret Tarihi']" +
                    "/following::span[contains(@class,'e-date-wrapper')][1]//input"
    );

    // Boy (cm)
    private final By heightInput = By.xpath(
            "//form[@method='post']//label[normalize-space()='Boy (cm)']" +
                    "/following::span[contains(@class,'e-numeric')][1]//input[contains(@class,'e-control')]"
    );

    // Kilo (kg)
    private final By weightInput = By.xpath(
            "//form[@method='post']//label[normalize-space()='Kilo (kg)']" +
                    "/following::span[contains(@class,'e-numeric')][1]//input[contains(@class,'e-control')]"
    );

    // Vücut Kitle İndeksi (VKİ)
    private final By bmiInput = By.xpath(
            "//form[@method='post']//label[normalize-space()='Vücut Kitle İndeksi (VKİ)']" +
                    "/following::span[contains(@class,'e-numeric')][1]//input[contains(@class,'e-control')]"
    );

    // Bel çevresi (cm) – label/span esnek
    private final By waistInput = By.xpath(
            "//form[@method='post']" +
                    "//*[self::label or self::span]" +
                    "[contains(normalize-space(),'Bel çevresi') or contains(normalize-space(),'Bel Çevresi')]" +
                    "/following::span[contains(@class,'e-numeric')][1]//input[contains(@class,'e-control')]"
    );

    // Vücut Yağ Oranı %
    private final By bodyFatInput = By.xpath(
            "//form[@method='post']//label[normalize-space()='Vücut Yağ Oranı %']" +
                    "/following::span[contains(@class,'e-numeric')][1]//input[contains(@class,'e-control')]"
    );

    // Notlar
    private final By notesTextArea = By.xpath(
            "//form[@method='post']//label[normalize-space()='Notlar']" +
                    "/following::textarea[1]"
    );

    // Kaydet butonu
    private final By saveButton = By.xpath(
            "//form[@method='post']//button[@type='submit' and normalize-space()='Kaydet']"
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
                LOGGER.warn("[Dietitian] clearAndType sırasında stale aldı, yeniden denenecek... Deneme: {}", i + 1);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (last != null) throw last;
    }

    private void waitUntilAjaxAndAnimationsFinish() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * DataTable satırları için null-safe hücre okuma helper’ı.
     */
    private String getCell(Map<String, String> row, String key) {
        String value = row.get(key);
        return value == null ? "" : value.trim();
    }

    // ================= VİZİT NUMARASI ÇÖZÜMLEME =================

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
            LOGGER.warn("[Dietitian] Vizit numarası çözümlenemedi: {}", e.getMessage());
            return null;
        }
    }

    // ================= FORM AÇILIŞI =================

    public void openDietitianFormFromPopup() {
        LOGGER.info("[Dietitian] İlk vizitin Diyetisyen formu açılıyor...");

        waitVisible(visitsContainer);

        WebElement card = waitVisible(firstVisitCard);
        scrollIntoView(card);

        createdVisitNumberForDietitianForm = extractVisitNumberFromCard(card);
        LOGGER.info("[Dietitian] Diyetisyen formu açılan vizit numarası: {}", createdVisitNumberForDietitianForm);

        WebElement formsButton = card.findElement(visitFormsButtonInsideCard);

        LOGGER.info("[Dietitian] Vizit içindeki Formlar butonuna tıklanıyor...");
        try {
            scrollIntoView(formsButton);
            wait.until(ExpectedConditions.elementToBeClickable(formsButton)).click();
        } catch (Exception e) {
            LOGGER.warn("[Dietitian] Normal click başarısız, JS click denenecek. Hata: {}", e.getMessage());
            js.executeScript("arguments[0].click();", formsButton);
        }

        try {
            Thread.sleep(700);
        } catch (InterruptedException ignored) {
        }

        WebElement popupRoot = waitPresent(formsPopupRoot);
        scrollIntoView(popupRoot);

        waitPresent(formsPopupGrid);

        LOGGER.info("[Dietitian] Pop-up içindeki Diyetisyen butonu bekleniyor...");
        WebElement dietitianButton = waitPresent(dietitianButtonInPopup);
        scrollIntoView(dietitianButton);

        try {
            js.executeScript("arguments[0].click();", dietitianButton);
        } catch (JavascriptException e) {
            LOGGER.warn("[Dietitian] JS click hatası, fallback normal click denenecek. Hata: {}", e.getMessage());
            dietitianButton.click();
        }

        waitVisible(dietitianFormRoot);
        LOGGER.info("[Dietitian] Diyetisyen formu vizit içinde açıldı.");
    }

    // ================= FORMU DOLDURMA & KAYDETME (DataTable destekli) =================

    /**
     * Diyetisyen formunu DataTable’dan gelen satırlara göre doldurur ve Kaydet’e tıklar.
     *
     * Beklenen kolonlar:
     *  - alan  : Form üzerindeki alan adı (Ziyaret Tarihi, Boy (cm) vb.)
     *  - tip   : date | numeric | textarea
     *  - değer : Alanın değeri (örn. "BUGÜN", "172", "84", "Not metni...")
     *
     * Örnek satırlar:
     *  | alan                      | tip      | değer    |
     *  | Ziyaret Tarihi            | date     | BUGÜN    |
     *  | Boy (cm)                  | numeric  | 172      |
     *  | Notlar                    | textarea | ....     |
     */
    public void fillDietitianFormFromTable(List<Map<String, String>> rows) {
        if (rows == null || rows.isEmpty()) {
            LOGGER.warn("[Dietitian] DataTable boş geldi, Diyetisyen formu doldurulmayacak.");
            return;
        }

        LOGGER.info("[Dietitian] DataTable ile Diyetisyen formu dolduruluyor. Satır sayısı: {}", rows.size());

        for (Map<String, String> row : rows) {
            String field = getCell(row, "alan");
            String type = getCell(row, "tip").toLowerCase(Locale.ROOT);
            String value = getCell(row, "değer");

            if (field.isEmpty()) {
                LOGGER.warn("[Dietitian] Satırda 'alan' boş, satır atlanıyor. Satır: {}", row);
                continue;
            }

            LOGGER.info("[Dietitian] Satır işleniyor -> alan='{}', tip='{}', değer='{}'",
                    field, type, value);

            try {
                switch (type) {
                    case "date":
                        fillDateField(field, value);
                        break;
                    case "numeric":
                        fillNumericField(field, value);
                        break;
                    case "textarea":
                        fillTextareaField(field, value);
                        break;
                    default:
                        LOGGER.warn("[Dietitian] Desteklenmeyen tip ('{}') için işlem yapılmadı. Satır: {}", type, row);
                }
            } catch (Exception e) {
                LOGGER.error("[Dietitian] DataTable satırı işlenirken hata oluştu. Satır: {}, Hata: {}",
                        row, e.getMessage());
                throw e;
            }
        }

        clickSaveAndWait();
    }

    /**
     * Eski step kullanan senaryolar için geriye dönük uyumluluk:
     * Sabit test verisiyle formu doldurup Kaydet'e basar.
     */
    public void fillDietitianFormAndSave() {
        LOGGER.info("[Dietitian] (legacy) Diyetisyen formu alanları sabit verilerle dolduruluyor ve kaydediliyor...");

        fillDateField("Ziyaret Tarihi", "BUGÜN");
        fillNumericField("Boy (cm)", "172");
        fillNumericField("Kilo (kg)", "84");
        fillNumericField("Vücut Kitle İndeksi (VKİ)", "28.4");
        fillNumericField("Bel çevresi (cm)", "98");
        fillNumericField("Vücut Yağ Oranı %", "27");
        fillTextareaField("Notlar", "Son haftalarda düzensiz beslenme ve hafif ödem gözlendi. Su tüketimi düşük.");

        clickSaveAndWait();
    }

    // ---- Field bazlı helper’lar ----

    private void fillDateField(String fieldLabel, String value) {
        LOGGER.info("[Dietitian] Date alanı dolduruluyor -> alan='{}', değer='{}'", fieldLabel, value);
        setVisitDateByValue(value);
    }

    private void fillNumericField(String fieldLabel, String value) {
        LOGGER.info("[Dietitian] Numeric alan dolduruluyor -> alan='{}', değer='{}'", fieldLabel, value);

        if (fieldLabel.toLowerCase(Locale.ROOT).contains("boy")) {
            clearAndType(heightInput, value);
        } else if (fieldLabel.toLowerCase(Locale.ROOT).contains("kilo")) {
            clearAndType(weightInput, value);
        } else if (fieldLabel.toLowerCase(Locale.ROOT).contains("kitle") || fieldLabel.toUpperCase(Locale.ROOT).contains("VKİ")) {
            clearAndType(bmiInput, value);
        } else if (fieldLabel.toLowerCase(Locale.ROOT).contains("bel çevresi")) {
            clearAndType(waistInput, value);
        } else if (fieldLabel.toLowerCase(Locale.ROOT).contains("yağ oranı")) {
            clearAndType(bodyFatInput, value);
        } else {
            LOGGER.warn("[Dietitian] Numeric alan için eşleşen locator bulunamadı. Alan: '{}'", fieldLabel);
        }
    }

    private void fillTextareaField(String fieldLabel, String value) {
        LOGGER.info("[Dietitian] Textarea alanı dolduruluyor -> alan='{}'", fieldLabel);
        if (fieldLabel.toLowerCase(Locale.ROOT).contains("notlar")) {
            setNotes(value);
        } else {
            LOGGER.warn("[Dietitian] Textarea alanı için eşleşen locator bulunamadı. Alan: '{}'", fieldLabel);
        }
    }

    // ---- Ziyaret Tarihi helper’ları ----

    private void setVisitDateByValue(String value) {
        // Değer boş veya "BUGÜN"/"TODAY" ise takvimden bugün seç
        if (value.isEmpty()
                || value.equalsIgnoreCase("BUGÜN")
                || value.equalsIgnoreCase("TODAY")) {
            setVisitDateToToday();
            return;
        }

        // Aksi halde verilen değeri doğrudan input'a yaz (örn. 15.12.2025)
        LOGGER.info("[Dietitian] Ziyaret Tarihi alanına manuel tarih set ediliyor: {}", value);

        WebElement input = waitVisible(visitDateInput);
        scrollIntoView(input);

        input.click();
        try {
            input.clear();
        } catch (InvalidElementStateException ignored) {
        }

        input.sendKeys(value);
        input.sendKeys(Keys.TAB);
    }

    private void setVisitDateToToday() {
        LOGGER.info("[Dietitian] Ziyaret Tarihi için bugün seçiliyor...");

        WebElement input = waitVisible(visitDateInput);
        scrollIntoView(input);
        input.click();

        By todayCell = By.cssSelector(
                "div.e-datepicker, div.e-datetimepicker, div.e-calendar " +
                        "td.e-today span, div.e-datepicker td.e-focused span"
        );

        try {
            WebElement today = wait.until(ExpectedConditions.elementToBeClickable(todayCell));
            today.click();
        } catch (TimeoutException e) {
            LOGGER.warn("[Dietitian] Takvimde 'bugün' hücresi bulunamadı, input'a tarih doğrudan yazılıyor.");
            LocalDate today = LocalDate.now();
            String formatted = today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            input.clear();
            input.sendKeys(formatted);
            input.sendKeys(Keys.TAB);
        }
    }

    private void setNotes(String text) {
        LOGGER.info("[Dietitian] Notlar alanı dolduruluyor.");
        WebElement textarea = waitVisible(notesTextArea);
        scrollIntoView(textarea);
        textarea.clear();
        textarea.sendKeys(text);
    }

    private void clickSaveAndWait() {
        LOGGER.info("[Dietitian] Kaydet butonuna tıklanıyor...");
        safeClick(saveButton);
        waitUntilAjaxAndAnimationsFinish();
    }

    // ================= SİLME: Hedef vizit → bir önceki vizit =================

    /**
     * Formun açıldığı vizit numarasını kullanarak:
     *  1) Önce ilgili vizit kartında Diyetisyen formunu arar ve siler.
     *  2) Bulunamazsa, DOM'daki bir sonraki kartta (bir önceki vizit) arar ve siler.
     *
     * Başka vizitlerde gezinmez, tüm vizitleri dolaşıp silmez.
     */
    public boolean deleteDietitianFormFromTargetOrPreviousVisit() {
        LOGGER.info("[Dietitian] Hedef vizitte (ve gerekirse bir önceki vizitte) Diyetisyen formu silme akışı başlıyor...");

        waitVisible(visitsContainer);

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[Dietitian] Toplam {} vizit kartı bulundu.", cards.size());

        if (cards.isEmpty()) {
            LOGGER.warn("[Dietitian] Hiç vizit kartı yok, silme işlemi yapılamaz.");
            return false;
        }

        int targetIndex = -1;
        if (createdVisitNumberForDietitianForm != null) {
            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                scrollIntoView(card);
                String visitNo = extractVisitNumberFromCard(card);
                LOGGER.info("[Dietitian] {}. vizit kartının numarası: {}", i + 1, visitNo);

                if (createdVisitNumberForDietitianForm.equals(visitNo)) {
                    targetIndex = i;
                    LOGGER.info("[Dietitian] Hedef vizit kartı bulundu. Index: {}, Vizit No: {}", targetIndex, visitNo);
                    break;
                }
            }
        } else {
            LOGGER.warn("[Dietitian] createdVisitNumberForDietitianForm = null, hedef vizit numarası saklanamamış.");
        }

        WebElement targetCard = (targetIndex >= 0 && targetIndex < cards.size()) ? cards.get(targetIndex) : null;
        WebElement previousCard = null;

        if (targetIndex >= 0 && (targetIndex + 1) < cards.size()) {
            previousCard = cards.get(targetIndex + 1);
            LOGGER.info("[Dietitian] Bir önceki vizit kartı index: {}", targetIndex + 1);
        }

        if (targetCard != null) {
            LOGGER.info("[Dietitian] Önce hedef vizit kartında Diyetisyen formu aranıyor...");
            if (deleteDietitianFormInSingleCard(targetCard)) {
                return true;
            }
        }

        if (previousCard != null) {
            LOGGER.info("[Dietitian] Hedef vizitte bulunamadı, bir önceki vizitte Diyetisyen formu aranıyor...");
            if (deleteDietitianFormInSingleCard(previousCard)) {
                return true;
            }
        }

        LOGGER.warn("[Dietitian] Hedef ve bir önceki vizitte Diyetisyen formu bulunamadı, silme yapılmadı.");
        return false;
    }

    private boolean deleteDietitianFormInSingleCard(WebElement card) {
        scrollIntoView(card);

        for (int retry = 0; retry < 3; retry++) {
            List<WebElement> headers = card.findElements(dietitianHeaderInVisitCard);
            if (!headers.isEmpty()) {
                LOGGER.info("[Dietitian] Bu vizit kartında Diyetisyen başlığı bulundu (retry: {}).", retry);

                WebElement deleteBtn = card.findElement(formDeleteButtonInVisitCard);
                scrollIntoView(deleteBtn);

                try {
                    wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
                } catch (Exception e) {
                    LOGGER.warn("[Dietitian] Silme ikonunda normal click hata aldı, JS click denenecek. Hata: {}", e.getMessage());
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

        LOGGER.info("[Dietitian] Bu vizit kartında Diyetisyen başlığı bulunamadı.");
        return false;
    }

    public void confirmDeleteIfDialogVisible() {
        LOGGER.info("[Dietitian] Silme onay penceresi kontrol ediliyor...");

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            WebElement yesBtn = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(deleteConfirmYesButton)
            );

            LOGGER.info("[Dietitian] Silme onay penceresi göründü, 'Evet' butonuna tıklanıyor...");
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
            LOGGER.info("[Dietitian] Silme onay penceresi görünmedi, devam ediliyor.");
        }
    }

    // ================= HEDEF VİZİTTE FORM VAR MI / YOK MU =================

    /**
     * Diyetisyen formunun açıldığı vizitte (createdVisitNumberForDietitianForm)
     * artık hiçbir Diyetisyen formu kalmadı mı?
     *
     *  - Sadece ilgili vizit kartına bakar.
     *  - Başka vizitleri kontrol etmez.
     */
    public boolean isDietitianFormAbsentInCreatedVisit() {
        LOGGER.info("[Dietitian] Hedef vizitte Diyetisyen formu kalmadı mı? (sadece ilgili vizit kontrol ediliyor)");

        if (createdVisitNumberForDietitianForm == null) {
            LOGGER.warn("[Dietitian] createdVisitNumberForDietitianForm = null, hedef vizit numarası bilinmiyor.");
            return false;
        }

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[Dietitian] Toplam {} vizit kartı bulundu.", cards.size());

        for (WebElement card : cards) {
            String visitNo = extractVisitNumberFromCard(card);
            if (!createdVisitNumberForDietitianForm.equals(visitNo)) {
                continue;
            }

            LOGGER.info("[Dietitian] Hedef vizit kartı bulundu. Vizit No: {}", visitNo);
            scrollIntoView(card);

            List<WebElement> headers = card.findElements(dietitianHeaderInVisitCard);
            if (!headers.isEmpty()) {
                LOGGER.info("[Dietitian] Hedef vizitte hâlâ Diyetisyen başlığı görünüyor.");
                return false;
            }

            try {
                WebElement formsContainer = card.findElement(visitFormsContainerInCard);
                scrollIntoView(formsContainer);

                WebElement emptyText = formsContainer.findElement(emptyFormTextInCard);
                LOGGER.info("[Dietitian] Hedef vizitte boş durum yazısı bulundu: {}", emptyText.getText());
            } catch (NoSuchElementException e) {
                LOGGER.info("[Dietitian] Hedef vizitte 'Form bulunamadı.' yazısı görünmüyor; "
                        + "ancak Diyetisyen başlığı da yok, yine de boş kabul ediyoruz.");
            }

            LOGGER.info("[Dietitian] Hedef vizitte hiçbir Diyetisyen formu görünmüyor.");
            return true;
        }

        LOGGER.warn("[Dietitian] Vizit listesinde {} numaralı hedef vizit kartı bulunamadı.",
                createdVisitNumberForDietitianForm);
        return false;
    }

    // ================= STEP'LER İÇİN KULLANILAN 3 METOT =================

    /** Eklenme doğrulaması: hedef vizitte en az bir Diyetisyen formu var mı? */
    public boolean isDietitianFormPresentInOpenedVisit() {
        LOGGER.info("[Dietitian] Hedef vizitte (ve gerekirse bir önceki vizitte) Diyetisyen formu var mı?");

        waitVisible(visitsContainer);

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[Dietitian] isDietitianFormPresentInOpenedVisit -> toplam {} vizit kartı bulundu.", cards.size());

        if (cards.isEmpty()) {
            LOGGER.warn("[Dietitian] isDietitianFormPresentInOpenedVisit -> hiç vizit kartı yok.");
            return false;
        }

        int targetIndex = findTargetVisitIndex(cards);
        if (targetIndex < 0 || targetIndex >= cards.size()) {
            LOGGER.warn("[Dietitian] isDietitianFormPresentInOpenedVisit -> geçersiz targetIndex: {}", targetIndex);
            return false;
        }

        WebElement targetCard = cards.get(targetIndex);
        // Önce oluşturulan vizite bak
        if (hasDietitianFormInCard(targetCard)) {
            LOGGER.info("[Dietitian] isDietitianFormPresentInOpenedVisit -> hedef vizitte Diyetisyen formu bulundu.");
            return true;
        }

        // Sonra, varsa bir önceki vizite bak (DOM'da bir alt satır)
        int previousIndex = targetIndex + 1;
        if (previousIndex < cards.size()) {
            WebElement previousCard = cards.get(previousIndex);
            if (hasDietitianFormInCard(previousCard)) {
                LOGGER.info("[Dietitian] isDietitianFormPresentInOpenedVisit -> bir önceki vizitte Diyetisyen formu bulundu.");
                return true;
            }
        }

        LOGGER.info("[Dietitian] isDietitianFormPresentInOpenedVisit -> hedef ve bir önceki vizitte Diyetisyen formu yok.");
        return false;
    }

    /**
     * STEP: "tüm Diyetisyen formları silinir" için.
     * Sadece hedef vizitte (ve gerekirse bir önceki vizitte) bulunan Diyetisyen formlarını
     * teker teker siler, en az bir tanesi silindiyse true döner.
     */
    public boolean deleteAllDietitianFormsInOpenedVisit() {
        LOGGER.info("[Dietitian] Hedef vizitteki tüm Diyetisyen formları siliniyor (gerekirse bir önceki vizit dahil)...");

        boolean deletedAny = false;

        // Sadece hedef + bir önceki vizitte dönecek; başka vizitlere dokunmaz.
        while (deleteDietitianFormFromTargetOrPreviousVisit()) {
            deletedAny = true;
            waitUntilAjaxAndAnimationsFinish();
        }

        LOGGER.info("[Dietitian] Silme işlemi tamamlandı. Silinen form var mı? {}", deletedAny);
        return deletedAny;
    }

    /** STEP: "hiç Diyetisyen formu kalmadığı doğrulanır" için. */
    public boolean isNoDietitianFormLeftInOpenedVisit() {
        LOGGER.info("[Dietitian] Hedef vizitte ve bir önceki vizitte hiç Diyetisyen formu kalmadı mı?");

        waitVisible(visitsContainer);

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[Dietitian] isNoDietitianFormLeftInOpenedVisit -> toplam {} vizit kartı bulundu.", cards.size());

        if (cards.isEmpty()) {
            LOGGER.warn("[Dietitian] isNoDietitianFormLeftInOpenedVisit -> hiç vizit kartı yok, doğal olarak form da yok kabul ediliyor.");
            return true;
        }

        int targetIndex = findTargetVisitIndex(cards);
        if (targetIndex < 0 || targetIndex >= cards.size()) {
            LOGGER.warn("[Dietitian] isNoDietitianFormLeftInOpenedVisit -> geçersiz targetIndex: {}", targetIndex);
            // Hedefi bulamıyorsak, korumacı davranıp "form olabilir" diyelim
            return false;
        }

        boolean targetHas = hasDietitianFormInCard(cards.get(targetIndex));
        boolean previousHas = false;

        int previousIndex = targetIndex + 1;
        if (previousIndex < cards.size()) {
            previousHas = hasDietitianFormInCard(cards.get(previousIndex));
        }

        boolean noneLeft = !targetHas && !previousHas;
        LOGGER.info("[Dietitian] isNoDietitianFormLeftInOpenedVisit -> hedef vizit: {}, önceki vizit: {}, kalan var mı? {}",
                targetHas, previousHas, !noneLeft);

        return noneLeft;
    }

    // ================= VİZİT KARTI YARDIMCI METOTLARI =================

    /**
     * createdVisitNumberForDietitianForm değerine göre hedef vizit index'ini bulur.
     * - Bulamazsa 0 (ilk vizit) kabul eder.
     */
    private int findTargetVisitIndex(List<WebElement> cards) {
        if (cards == null || cards.isEmpty()) {
            return -1;
        }

        if (createdVisitNumberForDietitianForm == null) {
            LOGGER.warn("[Dietitian] createdVisitNumberForDietitianForm = null, hedef olarak ilk vizit kabul edilecek.");
            return 0;
        }

        for (int i = 0; i < cards.size(); i++) {
            WebElement card = cards.get(i);
            String visitNo = extractVisitNumberFromCard(card);
            LOGGER.info("[Dietitian] findTargetVisitIndex -> {}. kart vizit no: {}", i + 1, visitNo);

            if (createdVisitNumberForDietitianForm.equals(visitNo)) {
                LOGGER.info("[Dietitian] findTargetVisitIndex -> hedef vizit bulundu. Index: {}", i);
                return i;
            }
        }

        LOGGER.warn("[Dietitian] findTargetVisitIndex -> {} numaralı vizit bulunamadı, 0. index kullanılacak.",
                createdVisitNumberForDietitianForm);
        return 0;
    }

    /**
     * Verilen tek bir vizit kartında Diyetisyen formu (başlığı) var mı?
     */
    private boolean hasDietitianFormInCard(WebElement card) {
        if (card == null) return false;

        scrollIntoView(card);
        List<WebElement> headers = card.findElements(dietitianHeaderInVisitCard);

        boolean present = !headers.isEmpty();
        LOGGER.info("[Dietitian] hasDietitianFormInCard -> bu vizit kartında Diyetisyen formu var mı? {}", present);

        return present;
    }
}

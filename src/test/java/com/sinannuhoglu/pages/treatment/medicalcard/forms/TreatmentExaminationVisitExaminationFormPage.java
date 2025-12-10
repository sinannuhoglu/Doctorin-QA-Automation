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
 * Vizit içi "Muayene Formu" Page Object
 */
public class TreatmentExaminationVisitExaminationFormPage {

    private static final Logger LOGGER =
            LogManager.getLogger(TreatmentExaminationVisitExaminationFormPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    /**
     * Formun hangi vizitte açıldığını takip etmek için vizit numarası (örn: "1767").
     */
    private String createdVisitNumberForExaminationForm;

    public TreatmentExaminationVisitExaminationFormPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ================= LOCATORS =================

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

    // "Hiç form yok" durumundaki mesaj
    private final By noFormMessageText = By.xpath(
            "//div[contains(@class,'flex') and contains(@class,'flex-col') and contains(@class,'gap-1')]" +
                    "//p[contains(@class,'text-sm') and contains(@class,'font-semibold') and normalize-space()='Form bulunamadı.']"
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

    // Popup içindeki "Muayene Formu" butonu
    private final By examinationFormButtonInPopup = By.xpath(
            "//button[normalize-space()='Muayene Formu']"
    );

    // Form tag'i (vizit içinde açılan Muayene Formu)
    private final By examinationFormRoot = By.xpath(
            "//form[starts-with(@id,'dataform') and contains(@class,'e-data-form')]"
    );

    // Şikayet textarea (group/complaint)
    private final By complaintTextarea = By.xpath(
            "//form[starts-with(@id,'dataform')]" +
                    "//div[contains(@class,'group/complaint')]//textarea"
    );

    // Geçmiş textarea (group/history)
    private final By historyTextarea = By.xpath(
            "//form[starts-with(@id,'dataform')]" +
                    "//div[contains(@class,'group/history')]//textarea"
    );

    // Muayene textarea (group/examination)
    private final By examinationTextarea = By.xpath(
            "//form[starts-with(@id,'dataform')]" +
                    "//div[contains(@class,'group/examination')]//textarea"
    );

    // Tedavi Planı ve Önerileri textarea (group/treatmentplan)
    private final By treatmentPlanTextarea = By.xpath(
            "//form[starts-with(@id,'dataform')]" +
                    "//div[contains(@class,'group/treatmentplan')]//textarea"
    );

    // Kaydet butonu
    private final By saveButton = By.xpath(
            "//form[starts-with(@id,'dataform')]" +
                    "//button[@type='submit' and (normalize-space()='Kaydet' or .//span[normalize-space()='Kaydet'])]"
    );

    // Tedavi Planı zorunlu alan uyarısı
    private final By treatmentPlanValidationMessage = By.xpath(
            "//form[starts-with(@id,'dataform')]" +
                    "//div[contains(@class,'group/treatmentplan')]//div[contains(@class,'validation-message')]"
    );

    // Vizit kartı içinde "Muayene Formu" başlığı (header satırı)
    private final By examinationFormHeaderInVisitCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'items-center')" +
                    " and contains(@class,'justify-between') and contains(@class,'px-3')]" +
                    "//span[normalize-space()='Muayene Formu']"
    );

    // Aynı vizit kartı içinde çöp/sil butonu
    private final By examinationFormDeleteButtonInVisitCard = By.xpath(
            ".//span[contains(@class,'e-icons') and contains(@class,'e-trash') and contains(@class,'e-btn-icon')]/ancestor::button[1]"
    );

    // Silme onay dialogu "Evet" butonu (varsa)
    private final By deleteConfirmYesButton = By.xpath(
            "//div[contains(@class,'e-dlg-container')]//button" +
                    "[contains(@class,'e-primary') and normalize-space()='Evet']"
    );

    // Vizit gövdesindeki formlar container'ı (flex flex-col gap-1)
    private final By visitFormsContainerInCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'flex-col') and contains(@class,'gap-1')]"
    );

    // Boş durumda görünen "Form bulunamadı." metni
    private final By emptyFormTextInCard = By.xpath(
            ".//p[contains(@class,'font-semibold') and normalize-space()='Form bulunamadı.']"
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
                LOGGER.warn("[ExaminationForm] clearAndType sırasında stale aldı, yeniden denenecek... Deneme: {}", i + 1);
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

    /**
     * DataTable satırı için null-safe hücre okuma helper’ı.
     */
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
            LOGGER.warn("[ExaminationForm] Vizit numarası çözümlenemedi: {}", e.getMessage());
            return null;
        }
    }

    // ================= AKIŞ METOTLARI =================

    public void openFirstVisitExaminationForm() throws InterruptedException {
        LOGGER.info("[ExaminationForm] İlk vizitin Muayene Formu penceresi açılıyor...");

        waitVisible(visitsContainer);

        WebElement card = waitVisible(firstVisitCard);

        scrollIntoView(card);

        createdVisitNumberForExaminationForm = extractVisitNumberFromCard(card);

        LOGGER.info("[ExaminationForm] Muayene Formu açılacak vizit numarası: {}", createdVisitNumberForExaminationForm);

    // ************* ZORUNLU: VIZITI FORCE-SELECT ET *************

        LOGGER.info("[ExaminationForm] Vizit yeniden seçiliyor (force select)...");

        js.executeScript("arguments[0].click();", card);

        // Vizitin gerçekten seçildiğini doğrula

        wait.until(driver -> {
            WebElement refreshed = driver.findElement(firstVisitCard);
            String cls = refreshed.getAttribute("class");
            assert cls != null;
            return cls.contains("animate-highlight-border") || cls.contains("bg-primary");
        });
        LOGGER.info("[ExaminationForm] Vizit başarıyla seçildi.");

    // ************* Formlar butonunu aç *************

        WebElement formsButton = card.findElement(visitFormsButtonInsideCard);

        LOGGER.info("[ExaminationForm] Formlar butonuna tıklanıyor...");

        try {

            scrollIntoView(formsButton);

            wait.until(ExpectedConditions.elementToBeClickable(formsButton)).click();

        } catch (Exception e) {

            LOGGER.warn("[ExaminationForm] Normal click başarısız, JS click deniyor: {}", e.getMessage());

            js.executeScript("arguments[0].click();", formsButton);

        }

        Thread.sleep(700); // popup animasyon için

    // ************* Popup elemanlarını bekle *************

        WebElement popupRoot = waitPresent(formsPopupRoot);

        scrollIntoView(popupRoot);

        waitPresent(formsPopupGrid);

    // ************* Muayene Formu butonuna geç *************

        WebElement muayeneFormButton = waitPresent(examinationFormButtonInPopup);

        scrollIntoView(muayeneFormButton);

    // TIKLAMADAN ÖNCE VIZITI BİR KEZ DAHA KONTROL ET

        LOGGER.info("[ExaminationForm] Muayene Formu açılmadan önce vizit seçimi doğrulanıyor...");

        /*
        if (!card.getAttribute("class").contains("selected")) {

            LOGGER.warn("[ExaminationForm] Vizit seçimi kayboldu, yeniden seçiliyor...");

            js.executeScript("arguments[0].click();", card);

            wait.until(driver -> card.getAttribute("class").contains("selected"));

        }
        */

        if (!card.getAttribute("class").contains("animate-highlight-border")) {
            LOGGER.warn("[ExaminationForm] Vizit seçimi kayboldu, yeniden seçiliyor...");
            js.executeScript("arguments[0].click();", card);

            wait.until(driver -> {
                WebElement refreshed = driver.findElement(firstVisitCard);
                String cls = refreshed.getAttribute("class");
                return cls.contains("animate-highlight-border") || cls.contains("bg-primary");
            });
        }

    // ************* Muayene Formu butonuna tıkla *************

        LOGGER.info("[ExaminationForm] Muayene Formu butonuna tıklanıyor...");

        try {

            js.executeScript("arguments[0].click();", muayeneFormButton);

        } catch (JavascriptException e) {

            LOGGER.warn("[ExaminationForm] JS click hatası, fallback normal click. Hata: {}", e.getMessage());

            muayeneFormButton.click();

        }

    // ************* Form açılana kadar bekle *************

        waitVisible(examinationFormRoot);

        LOGGER.info("[ExaminationForm] Muayene Formu doğru vizitte açıldı.");
    }

    // ---- Alan doldurma metotları ----

    public void fillComplaint(String text) {
        LOGGER.info("[ExaminationForm] Şikayet alanı dolduruluyor...");
        clearAndType(complaintTextarea, text);
    }

    public void fillHistory(String text) {
        LOGGER.info("[ExaminationForm] Geçmiş alanı dolduruluyor...");
        clearAndType(historyTextarea, text);
    }

    public void fillExamination(String text) {
        LOGGER.info("[ExaminationForm] Muayene alanı dolduruluyor...");
        clearAndType(examinationTextarea, text);
    }

    public void fillTreatmentPlan(String text) {
        LOGGER.info("[ExaminationForm] Tedavi Planı ve Önerileri alanı dolduruluyor...");
        clearAndType(treatmentPlanTextarea, text);
    }

    /**
     * Muayene Formu'nu DataTable’dan gelen satırlara göre doldurur ve Kaydet’e tıklar.
     *
     * Beklenen kolonlar:
     *  - alan  : Şikayet | Geçmiş | Muayene | Tedavi Planı ve Önerileri
     *  - tip   : textarea (bilgi amaçlı, alan eşleşmesini alan adına göre yapıyoruz)
     *  - değer : ilgili metin
     */
    public void fillExaminationFormFromTable(List<Map<String, String>> rows) {
        if (rows == null || rows.isEmpty()) {
            LOGGER.warn("[ExaminationForm] DataTable boş geldi, Muayene Formu doldurulmayacak.");
            return;
        }

        LOGGER.info("[ExaminationForm] DataTable ile Muayene Formu dolduruluyor. Satır sayısı: {}", rows.size());

        for (Map<String, String> row : rows) {
            String field = getCell(row, "alan");
            String type = getCell(row, "tip").toLowerCase(Locale.ROOT);
            String value = getCell(row, "değer");

            if (field.isEmpty()) {
                LOGGER.warn("[ExaminationForm] Satırda 'alan' boş, satır atlanıyor. Satır: {}", row);
                continue;
            }

            LOGGER.info("[ExaminationForm] Satır işleniyor -> alan='{}', tip='{}'", field, type);

            String lower = field.toLowerCase(Locale.ROOT);

            try {
                if (lower.contains("şikayet")) {
                    fillComplaint(value);
                } else if (lower.contains("geçmiş")) {
                    fillHistory(value);
                } else if (lower.contains("muayene")) {
                    fillExamination(value);
                } else if (lower.contains("tedavi") || lower.contains("öneri")) {
                    fillTreatmentPlan(value);
                } else {
                    LOGGER.warn("[ExaminationForm] Alan adı için eşleşme bulunamadı, satır atlanıyor. Alan='{}'", field);
                }
            } catch (Exception e) {
                LOGGER.error("[ExaminationForm] DataTable satırı işlenirken hata oluştu. Satır: {}, Hata: {}",
                        row, e.getMessage());
                throw e;
            }
        }

        // Tüm alanlar doldurulduktan sonra kaydet
        clickSaveButton();
    }

    // ---- Kaydet & doğrulama ----

    public void clickSaveButton() {
        LOGGER.info("[ExaminationForm] Kaydet butonuna tıklanıyor...");
        safeClick(saveButton);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }

    public boolean isExaminationFormSavedSuccessfully() {
        LOGGER.info("[ExaminationForm] Kaydın başarıyla oluşturulup oluşturulmadığı kontrol ediliyor...");

        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(treatmentPlanValidationMessage));

            LOGGER.warn("[ExaminationForm] Tedavi Planı zorunlu alan uyarısı hâlâ görünüyor.");
            return false;
        } catch (TimeoutException e) {
            LOGGER.info("[ExaminationForm] Zorunlu alan uyarısı görünmüyor, kayıt başarılı kabul ediliyor.");
            return true;
        }
    }

    // =========================================================
    //  Silme: Hedef vizit → bir önceki vizit → fallback
    // =========================================================

    /**
     * Bütün vizitlerdeki TÜM Muayene Formu kayıtlarını siler.
     * Önce hedef vizit / bir önceki vizit, ardından fallback ile kalanları temizler.
     */
    public boolean deleteAllExaminationFormsFromAllVisits() {
        LOGGER.info("[ExaminationForm] Tüm vizitlerdeki Muayene Formu kayıtları siliniyor...");

        boolean deletedAtLeastOne = false;

        // Önce hedef vizit / önceki vizit / fallback
        if (deleteExaminationFormFromTargetOrPreviousVisit()) {
            deletedAtLeastOne = true;
        }

        // Hâlâ form kaldığı sürece, fallback üzerinden ilk bulduğunu silmeye devam et
        int guard = 0;
        while (hasAnyExaminationFormInVisits() && guard < 20) {
            LOGGER.info("[ExaminationForm] Hâlâ Muayene Formu var, tekrar silme denemesi (guard = {}).", guard);
            if (!deleteFirstExaminationFormFromVisits()) {
                // görünür form var ama silinemiyorsa, infinite loop'a girmemek için kır
                LOGGER.warn("[ExaminationForm] Görünür Muayene Formu olmasına rağmen deleteFirstExaminationFormFromVisits false döndü. Döngü sonlandırılıyor.");
                break;
            }
            deletedAtLeastOne = true;
            guard++;
        }

        return deletedAtLeastOne;
    }

    /**
     * Formun açıldığı vizit numarasını kullanarak:
     *  1) Önce ilgili vizit kartında Muayene Formu'nu arar ve siler.
     *  2) Bulunamazsa, DOM'daki bir sonraki kartta (bir önceki vizit) arar ve siler.
     *  3) Hâlâ bulunamazsa genel arama fallback'i ile ilk bulduğu Muayene Formu'nu siler.
     */
    public boolean deleteExaminationFormFromTargetOrPreviousVisit() {
        LOGGER.info("[ExaminationForm] Hedef vizitte (ve gerekirse bir önceki vizitte) Muayene Formu silme akışı başlıyor...");

        waitVisible(visitsContainer);

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[ExaminationForm] Toplam {} vizit kartı bulundu.", cards.size());

        if (cards.isEmpty()) {
            LOGGER.warn("[ExaminationForm] Hiç vizit kartı yok, silme işlemi yapılamaz.");
            return false;
        }

        int targetIndex = -1;
        if (createdVisitNumberForExaminationForm != null) {
            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                scrollIntoView(card);
                String visitNo = extractVisitNumberFromCard(card);
                LOGGER.info("[ExaminationForm] {}. vizit kartının numarası: {}", i + 1, visitNo);

                if (createdVisitNumberForExaminationForm.equals(visitNo)) {
                    targetIndex = i;
                    LOGGER.info("[ExaminationForm] Hedef vizit kartı bulundu. Index: {}, Vizit No: {}", targetIndex, visitNo);
                    break;
                }
            }
        } else {
            LOGGER.warn("[ExaminationForm] createdVisitNumberForExaminationForm = null, hedef vizit numarası saklanamamış.");
        }

        WebElement targetCard = (targetIndex >= 0 && targetIndex < cards.size()) ? cards.get(targetIndex) : null;
        WebElement previousCard = null;

        if (targetIndex >= 0 && (targetIndex + 1) < cards.size()) {
            previousCard = cards.get(targetIndex + 1);
            LOGGER.info("[ExaminationForm] Bir önceki vizit kartı index: {}", targetIndex + 1);
        }

        if (targetCard != null) {
            LOGGER.info("[ExaminationForm] Önce hedef vizit kartında Muayene Formu aranıyor...");
            if (deleteExaminationFormInSingleCard(targetCard)) {
                return true;
            }
        }

        if (previousCard != null) {
            LOGGER.info("[ExaminationForm] Hedef vizitte bulunamadı, bir önceki vizitte Muayene Formu aranıyor...");
            if (deleteExaminationFormInSingleCard(previousCard)) {
                return true;
            }
        }

        LOGGER.warn("[ExaminationForm] Hedef ve bir önceki vizitte bulunamadı, fallback olarak ilk bulunan Muayene Formu silinecek.");
        return deleteFirstExaminationFormFromVisits();
    }

    private boolean deleteExaminationFormInSingleCard(WebElement card) {
        scrollIntoView(card);

        for (int retry = 0; retry < 3; retry++) {
            List<WebElement> headers = card.findElements(examinationFormHeaderInVisitCard);
            if (!headers.isEmpty()) {
                LOGGER.info("[ExaminationForm] Bu vizit kartında Muayene Formu başlığı bulundu (retry: {}).", retry);

                WebElement deleteBtn = card.findElement(examinationFormDeleteButtonInVisitCard);
                scrollIntoView(deleteBtn);

                try {
                    wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
                } catch (Exception e) {
                    LOGGER.warn("[ExaminationForm] Silme ikonunda normal click hata aldı, JS click denenecek. Hata: {}", e.getMessage());
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

        LOGGER.info("[ExaminationForm] Bu vizit kartında Muayene Formu başlığı bulunamadı.");
        return false;
    }

    public boolean deleteFirstExaminationFormFromVisits() {
        LOGGER.info("[ExaminationForm] (Fallback) Vizitler içinde ekli ilk Muayene Formu aranıyor ve silinecek...");

        waitVisible(visitsContainer);

        long end = System.currentTimeMillis() + 8000L;

        while (System.currentTimeMillis() < end) {
            List<WebElement> cards = driver.findElements(allVisitCards);

            LOGGER.info("[ExaminationForm] (Fallback) Toplam {} vizit kartı bulundu.", cards.size());

            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                scrollIntoView(card);

                List<WebElement> headers = card.findElements(examinationFormHeaderInVisitCard);
                if (!headers.isEmpty()) {
                    LOGGER.info("[ExaminationForm] (Fallback) Muayene Formu {}. vizitte bulundu, silme ikonuna tıklanacak.", i + 1);

                    WebElement deleteBtn = card.findElement(examinationFormDeleteButtonInVisitCard);
                    scrollIntoView(deleteBtn);

                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
                    } catch (Exception e) {
                        LOGGER.warn("[ExaminationForm] (Fallback) Normal click silme ikonunda hata aldı, JS click denenecek. Hata: {}", e.getMessage());
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

        LOGGER.warn("[ExaminationForm] (Fallback) Hiçbir vizitte Muayene Formu bulunamadı, silme işlemi yapılmadı.");
        return false;
    }

    public void confirmDeleteIfDialogVisible() {
        LOGGER.info("[ExaminationForm] Silme onay penceresi kontrol ediliyor...");

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            WebElement yesBtn = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(deleteConfirmYesButton)
            );

            LOGGER.info("[ExaminationForm] Silme onay penceresi göründü, 'Evet' butonuna tıklanıyor...");
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
            LOGGER.info("[ExaminationForm] Silme onay penceresi görünmedi, devam ediliyor.");
        }
    }

    /**
     * Herhangi bir vizit kartında "Muayene Formu" başlığı var mı?
     */
    private boolean hasAnyExaminationFormInVisits() {
        waitVisible(visitsContainer);
        List<WebElement> cards = driver.findElements(allVisitCards);

        for (WebElement card : cards) {
            if (!card.findElements(examinationFormHeaderInVisitCard).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Muayene Formu’nun açıldığı vizitte (createdVisitNumberForExaminationForm)
     * artık hiçbir Muayene Formu kalmadı mı?
     *
     *  - Sadece ilgili vizit kartına bakar.
     *  - Başka vizitleri kontrol etmez.
     *  - Bekleme / uzun döngü kullanmaz, anlık DOM’a bakar.
     */
    public boolean isExaminationFormAbsentInCreatedVisit() {
        LOGGER.info("[ExaminationForm] Hedef vizitte Muayene Formu kalmadı mı? (sadece ilgili vizit kontrol ediliyor)");

        if (createdVisitNumberForExaminationForm == null) {
            LOGGER.warn("[ExaminationForm] createdVisitNumberForExaminationForm = null, hedef vizit numarası bilinmiyor.");
            return false;
        }

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[ExaminationForm] Toplam {} vizit kartı bulundu.", cards.size());

        for (WebElement card : cards) {
            String visitNo = extractVisitNumberFromCard(card);
            if (!createdVisitNumberForExaminationForm.equals(visitNo)) {
                continue;
            }

            LOGGER.info("[ExaminationForm] Hedef vizit kartı bulundu. Vizit No: {}", visitNo);
            scrollIntoView(card);

            List<WebElement> headers = card.findElements(examinationFormHeaderInVisitCard);
            if (!headers.isEmpty()) {
                LOGGER.info("[ExaminationForm] Hedef vizitte hâlâ Muayene Formu başlığı görünüyor.");
                return false;
            }

            try {
                WebElement formsContainer = card.findElement(visitFormsContainerInCard);
                scrollIntoView(formsContainer);

                WebElement emptyText = formsContainer.findElement(emptyFormTextInCard);
                LOGGER.info("[ExaminationForm] Hedef vizitte boş durum yazısı bulundu: {}", emptyText.getText());
            } catch (NoSuchElementException e) {
                LOGGER.info("[ExaminationForm] Hedef vizitte 'Form bulunamadı.' yazısı görünmüyor; "
                        + "ancak Muayene Formu başlığı da yok, yine de boş kabul ediyoruz.");
            }

            LOGGER.info("[ExaminationForm] Hedef vizitte hiçbir Muayene Formu görünmüyor.");
            return true;
        }

        LOGGER.warn("[ExaminationForm] Vizit listesinde {} numaralı hedef vizit kartı bulunamadı.",
                createdVisitNumberForExaminationForm);
        return false;
    }
}

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
 * Vizit içi "Vital Bulgular" Formu Page Object
 */
public class TreatmentExaminationVisitVitalSignsFormPage {

    private static final Logger LOGGER =
            LogManager.getLogger(TreatmentExaminationVisitVitalSignsFormPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    /**
     * Formun hangi vizitte açıldığını takip etmek için vizit numarası (örn: "1767").
     */
    private String createdVisitNumberForVitalForm;

    public TreatmentExaminationVisitVitalSignsFormPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ====================== GENEL VİZİT LOCATORLERİ ======================

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

    // Popup içindeki "Vital Bulgular" butonu
    private final By vitalFormButtonInPopup = By.xpath(
            "//button[normalize-space()='Vital Bulgular']"
    );

    // Vizit kartı içinde "Vital Bulgular" başlığı (header satırı)
    private final By vitalFormHeaderInVisitCard = By.xpath(
            ".//div[contains(@class,'flex') and contains(@class,'items-center')" +
                    " and contains(@class,'justify-between') and contains(@class,'px-3')]" +
                    "//span[normalize-space()='Vital Bulgular']"
    );

    // Aynı vizit kartı içinde Vital Bulgular formunun çöp/sil butonu
    private final By vitalFormDeleteButtonInVisitCard = By.xpath(
            ".//span[contains(@class,'e-icons') and contains(@class,'e-trash') and contains(@class,'e-btn-icon')]/ancestor::button[1]"
    );

    // Silme onay dialogu "Evet" butonu
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

    // ====================== VITAL FORM LOCATORLERİ ======================

    // Vital Bulgular formu – LOC Değeri label'ına göre kök form
    private final By vitalFormRoot = By.xpath(
            "//form[.//label[normalize-space()='LOC Değeri']]"
    );

    // ========================= HELPER METOTLAR ==========================

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

    private String getCell(Map<String, String> row, String key) {
        String value = row.get(key);
        return value == null ? "" : value.trim();
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

    private void safeClickElement(WebElement element) {
        RuntimeException last = null;

        for (int i = 0; i < 3; i++) {
            try {
                scrollIntoView(element);
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return;
            } catch (ElementClickInterceptedException e) {
                last = e;
                LOGGER.warn("[VitalForm] safeClickElement -> ElementClickInterceptedException, JS click denenecek. Deneme: {}", i + 1);
                try {
                    js.executeScript("arguments[0].click();", element);
                    return;
                } catch (JavascriptException jsEx) {
                    last = jsEx;
                }
            } catch (StaleElementReferenceException e) {
                last = e;
                LOGGER.warn("[VitalForm] safeClickElement -> StaleElementReferenceException, yeniden denenecek. Deneme: {}", i + 1);
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
        }

        if (last != null) {
            throw last;
        }
    }

    private void clearAndTypeElement(WebElement el, String text) {
        RuntimeException last = null;
        for (int i = 0; i < 3; i++) {
            try {
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
                LOGGER.warn("[VitalForm] clearAndTypeElement sırasında stale aldı, yeniden denenecek... Deneme: {}", i + 1);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            }
        }
        if (last != null) throw last;
    }

    private WebElement getVitalFormRoot() {
        return waitVisible(vitalFormRoot);
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
            LOGGER.warn("[VitalForm] Vizit numarası çözümlenemedi: {}", e.getMessage());
            return null;
        }
    }

    // =========================================================
    //  FORM AÇILIŞI
    // =========================================================

    public void openFirstVisitVitalForm() {
        LOGGER.info("[VitalForm] İlk vizitin Vital Bulgular formu penceresi açılıyor...");

        waitVisible(visitsContainer);

        WebElement card = waitVisible(firstVisitCard);
        scrollIntoView(card);

        createdVisitNumberForVitalForm = extractVisitNumberFromCard(card);
        LOGGER.info("[VitalForm] Vital Bulgular formu açılan vizit numarası: {}", createdVisitNumberForVitalForm);

        WebElement formsButton = card.findElement(visitFormsButtonInsideCard);

        LOGGER.info("[VitalForm] Vizit içindeki Formlar butonuna tıklanıyor...");
        try {
            scrollIntoView(formsButton);
            wait.until(ExpectedConditions.elementToBeClickable(formsButton)).click();
        } catch (Exception e) {
            LOGGER.warn("[VitalForm] Normal click başarısız, JS click denenecek. Hata: {}", e.getMessage());
            js.executeScript("arguments[0].click();", formsButton);
        }

        try {
            Thread.sleep(700);
        } catch (InterruptedException ignored) {
        }

        WebElement popupRoot = waitPresent(formsPopupRoot);
        scrollIntoView(popupRoot);

        waitPresent(formsPopupGrid);

        LOGGER.info("[VitalForm] Pop-up içindeki Vital Bulgular butonu bekleniyor...");
        WebElement vitalFormButton = waitPresent(vitalFormButtonInPopup);
        scrollIntoView(vitalFormButton);

        try {
            js.executeScript("arguments[0].click();", vitalFormButton);
        } catch (JavascriptException e) {
            LOGGER.warn("[VitalForm] JS click hatası, fallback normal click denenecek. Hata: {}", e.getMessage());
            vitalFormButton.click();
        }

        waitVisible(vitalFormRoot);
        LOGGER.info("[VitalForm] Vital Bulgular formu vizit içinde açıldı.");
    }

    // =========================================================
    //  FORM ALANLARINI DOLDURMA
    // =========================================================

    /**
     * Generic Syncfusion dropdown seçimi (label + option metnine göre).
     * - Popup id: inputId + "_popup"
     * - Önce tam eşleşme, bulunamazsa contains ile fallback
     */
    private void selectDropdownOptionByLabel(String fieldLabelText, String optionLabelText) {
        LOGGER.info("[VitalForm] '{}' alanı için '{}' seçeneği seçiliyor...", fieldLabelText, optionLabelText);

        WebElement formRoot = getVitalFormRoot();

        WebElement label = formRoot.findElement(
                By.xpath(".//label[normalize-space()='" + fieldLabelText + "']")
        );

        WebElement dropdownWrapper = label.findElement(
                By.xpath("following::span[contains(@class,'e-ddl')][1]")
        );

        WebElement input = dropdownWrapper.findElement(
                By.xpath(".//input[contains(@class,'e-dropdownlist')]")
        );
        String inputId = input.getAttribute("id");
        String popupId = inputId + "_popup";

        LOGGER.debug("[VitalForm] '{}' alanı için dropdown inputId={}, popupId={}",
                fieldLabelText, inputId, popupId);

        safeClickElement(dropdownWrapper);

        By popupLocator = By.id(popupId);
        waitVisible(popupLocator);

        By exactOptionLocator = By.xpath(
                "//div[@id='" + popupId + "']//li[contains(@class,'e-list-item') and normalize-space()='" + optionLabelText + "']"
        );
        WebElement optionElement;
        try {
            optionElement = waitVisible(exactOptionLocator);
        } catch (TimeoutException e) {
            LOGGER.warn("[VitalForm] '{}' alanında '{}' için tam eşleşme bulunamadı, contains ile tekrar denenecek.",
                    fieldLabelText, optionLabelText);
            By containsOptionLocator = By.xpath(
                    "//div[@id='" + popupId + "']//li[contains(@class,'e-list-item') and contains(normalize-space(.), '" + optionLabelText + "')]"
            );
            optionElement = waitVisible(containsOptionLocator);
        }

        safeClickElement(optionElement);
    }

    /**
     * Syncfusion dropdown – label’a göre popupId üzerinden İLK seçeneği seçer.
     */
    private void selectFirstDropdownOptionByLabel(String labelText) {
        LOGGER.info("[VitalForm] '{}' alanı için ilk seçenek seçiliyor...", labelText);

        WebElement formRoot = getVitalFormRoot();

        WebElement labelEl = formRoot.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        WebElement dropdownWrapper = labelEl.findElement(
                By.xpath("following::span[contains(@class,'e-ddl')][1]")
        );

        WebElement input = dropdownWrapper.findElement(
                By.xpath(".//input[contains(@class,'e-dropdownlist')]")
        );
        String inputId = input.getAttribute("id");
        String popupId = inputId + "_popup";

        LOGGER.debug("[VitalForm] '{}' alanı için dropdown inputId={}, popupId={}",
                labelText, inputId, popupId);

        safeClickElement(dropdownWrapper);

        By popupLocator = By.id(popupId);
        waitVisible(popupLocator);

        By firstOptionLocator = By.xpath(
                "//div[@id='" + popupId + "']//li[contains(@class,'e-list-item')][1]"
        );
        WebElement firstOption = waitClickable(firstOptionLocator);
        safeClickElement(firstOption);
    }

    /**
     * Syncfusion radio seçenekleri (span.e-label metnine göre).
     */
    private void clickRadioOptionByText(String optionText) {
        WebElement form = getVitalFormRoot();

        By radioLocator = By.xpath(
                ".//span[contains(@class,'e-label') and normalize-space()='" + optionText + "']/ancestor::label[1]"
        );

        LOGGER.info("[VitalForm] Radio seçeneği tıklanıyor: {}", optionText);

        WebElement label = form.findElement(radioLocator);
        safeClickElement(label);
    }

    /**
     * Label metnine göre EJ2 numeric input değer set etme (ATEŞ, SPO2, NABIZ vb.)
     * Bu metod EJ2 instance üzerinden value set ederek formun gerçekten valide olmasını sağlar.
     */
    private void fillNumericInputByLabel(String labelText, String value) {
        WebElement form = getVitalFormRoot();

        By labelLocator = By.xpath(".//label[normalize-space()='" + labelText + "']");
        WebElement labelEl = form.findElement(labelLocator);

        WebElement columnContainer = labelEl.findElement(
                By.xpath("./ancestor::div[contains(@class,'flex') and contains(@class,'flex-col')][1]")
        );

        WebElement numericWrapper = columnContainer.findElement(
                By.xpath(".//span[contains(@class,'e-numeric')]")
        );

        WebElement input = numericWrapper.findElement(
                By.xpath(".//input[@role='spinbutton' and not(@aria-disabled='true')]")
        );

        LOGGER.info("[VitalForm] '{}' alanına EJ2 ile değer atanıyor -> {}", labelText, value);

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", numericWrapper);

        js.executeScript(
                "var wrap = arguments[0]; var val = arguments[1];" +
                        "var inst = wrap.ej2_instances ? wrap.ej2_instances[0] : null;" +
                        "if (inst) {" +
                        "   inst.value = parseFloat(val);" +
                        "   inst.dataBind();" +
                        "}" +
                        "var inp = wrap.querySelector('input[role=\"spinbutton\"]');" +
                        "if (inp) {" +
                        "   inp.value = val;" +
                        "   inp.dispatchEvent(new Event('input', {bubbles:true}));" +
                        "   inp.dispatchEvent(new Event('change', {bubbles:true}));" +
                        "}",
                numericWrapper, value
        );

        try {
            input.click();
            input.sendKeys(Keys.TAB);
        } catch (Exception ignored) {
        }

        LOGGER.info("[VitalForm] '{}' final input değeri: {}", labelText, input.getAttribute("value"));
    }

    /**
     * Önceki hard-coded senaryoyu korumak istersen hâlâ kullanılabilir.
     * Şu an DataTable tabanlı doldurma tercih ediliyor.
     */
    public void fillVitalFormFields() {
        selectDropdownOptionByLabel("LOC Değeri", "Ağrıya Yanıt Veriyor");
        clickRadioOptionByText("Huzursuz ama sakinleştirilebiliyor");
        clickRadioOptionByText("Normal");
        clickRadioOptionByText("Retraksiyon Yok");
        clickRadioOptionByText("Oksijen");
        fillNumericInputByLabel("SpO2", "95");
        selectDropdownOptionByLabel("Cilt Rengi ve Kapiller Dolum Süresi", "Cilt rengi gri KDS: 4 sn");
        selectFirstDropdownOptionByLabel("Kalp Hızı (sayı / dk)");
        fillNumericInputByLabel("Nabız (dk)", "88");
        clickRadioOptionByText("Uyanık / Normal");
        fillNumericInputByLabel("Ateş", "37");
    }

    /**
     * Feature dosyasındaki DataTable'a göre Vital Bulgular formunu doldurur.
     * Kolonlar:
     *  - alan  : Formdaki label metni
     *  - tip   : dropdown | dropdownFirst | radio | numeric
     *  - değer : Seçilecek / yazılacak değer (dropdownFirst için boş kalabilir)
     */
    public void fillVitalFormFieldsFromTable(List<Map<String, String>> rows) {
        if (rows == null || rows.isEmpty()) {
            LOGGER.warn("[VitalForm] DataTable boş geldi, form alanları doldurulmayacak.");
            return;
        }

        for (Map<String, String> row : rows) {
            String field = getCell(row, "alan");
            String type = getCell(row, "tip").toLowerCase(Locale.ROOT);
            String value = getCell(row, "değer");

            if (field.isEmpty() && !"dropdownfirst".equals(type)) {
                LOGGER.warn("[VitalForm] Satırda 'alan' boş, atlanıyor. Satır: {}", row);
                continue;
            }

            LOGGER.info("[VitalForm] DataTable satırı işleniyor -> alan='{}', tip='{}', değer='{}'",
                    field, type, value);

            try {
                switch (type) {
                    case "dropdown":
                        selectDropdownOptionByLabel(field, value);
                        break;
                    case "dropdownfirst":
                        // Bu tipte değer boş olabilir, sadece ilk seçeneği seçiyoruz. (Sayfa üzerinde seçenekler okunmuyor)
                        selectFirstDropdownOptionByLabel(field);
                        break;
                    case "radio":
                        // Radio’lar label’dan bağımsız, direkt seçenek metnine göre seçiliyor.
                        clickRadioOptionByText(value);
                        break;
                    case "numeric":
                        fillNumericInputByLabel(field, value);
                        break;
                    default:
                        LOGGER.warn("[VitalForm] Desteklenmeyen tip ('{}') için işlem yapılmadı. Satır: {}", type, row);
                }
            } catch (Exception e) {
                LOGGER.error("[VitalForm] DataTable satırı işlenirken hata oldu. Satır: {}, Hata: {}",
                        row, e.getMessage());
                throw e;
            }
        }
    }

    // =========================================================
    //  KAYDET & DOĞRULAMA
    // =========================================================

    public void clickSaveButton() {
        LOGGER.info("[VitalForm] Vital Bulgular formu içindeki Kaydet butonuna tıklanıyor...");

        WebElement formRoot = getVitalFormRoot();

        By saveButtonInsideForm = By.xpath(
                ".//button[@type='submit' and " +
                        "(contains(normalize-space(),'Kaydet') or .//span[normalize-space()='Kaydet'])]"
        );

        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement saveButton = localWait.until(d -> {
            try {
                WebElement root = getVitalFormRoot();
                WebElement btn = root.findElement(saveButtonInsideForm);
                return (btn.isDisplayed() && btn.isEnabled()) ? btn : null;
            } catch (NoSuchElementException e) {
                return null;
            }
        });

        scrollIntoView(saveButton);
        safeClickElement(saveButton);

        try {
            waitVisible(visitsContainer);
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }

        LOGGER.info("[VitalForm] Kaydet butonuna başarıyla tıklandı.");
    }

    /**
     * Vital Bulgular formu kaydını doğrular.
     *  - Önce formun açıldığı vizit numarasındaki kartta "Vital Bulgular" başlığını arar.
     *  - Bulamazsa DOM'daki bir önceki vizit kartında arar.
     */
    public boolean isVitalFormSavedSuccessfully() {
        LOGGER.info("[VitalForm] Vital Bulgular formu kaydının başarıyla oluşup oluşmadığı kontrol ediliyor...");

        long deadline = System.currentTimeMillis() + 12_000L;

        while (System.currentTimeMillis() < deadline) {
            waitVisible(visitsContainer);
            List<WebElement> cards = driver.findElements(allVisitCards);
            LOGGER.info("[VitalForm] Kayıt kontrolü için toplam {} vizit kartı bulundu.", cards.size());

            if (cards.isEmpty()) {
                LOGGER.warn("[VitalForm] Vizit kartı yok, tekrar denenecek...");
            } else {
                WebElement targetCard = null;
                WebElement previousCard = null;

                if (createdVisitNumberForVitalForm != null) {
                    int targetIndex = -1;

                    for (int i = 0; i < cards.size(); i++) {
                        WebElement card = cards.get(i);
                        String visitNo = extractVisitNumberFromCard(card);
                        LOGGER.debug("[VitalForm] {}. vizit kartının numarası: {}", i + 1, visitNo);

                        if (createdVisitNumberForVitalForm.equals(visitNo)) {
                            targetIndex = i;
                            LOGGER.info("[VitalForm] Hedef vizit kartı bulundu. Index: {}, Vizit No: {}",
                                    targetIndex, visitNo);
                            break;
                        }
                    }

                    if (targetIndex >= 0) {
                        targetCard = cards.get(targetIndex);
                        if (targetIndex + 1 < cards.size()) {
                            previousCard = cards.get(targetIndex + 1);
                            LOGGER.info("[VitalForm] Bir önceki vizit kartı index: {}", targetIndex + 1);
                        }
                    } else {
                        LOGGER.warn("[VitalForm] Vizit listesinde {} numaralı hedef vizit kartı bulunamadı.",
                                createdVisitNumberForVitalForm);
                    }
                } else {
                    LOGGER.warn("[VitalForm] createdVisitNumberForVitalForm = null, ilk iki vizit üzerinden kontrol yapılacak.");
                    targetCard = cards.get(0);
                    if (cards.size() > 1) {
                        previousCard = cards.get(1);
                    }
                }

                if (targetCard != null && hasVitalFormHeaderInCard(targetCard)) {
                    LOGGER.info("[VitalForm] Hedef vizitte Vital Bulgular formu başlığı bulundu. Kayıt başarılı.");
                    return true;
                }

                if (previousCard != null && hasVitalFormHeaderInCard(previousCard)) {
                    LOGGER.info("[VitalForm] Bir önceki vizitte Vital Bulgular formu başlığı bulundu. Kayıt başarılı.");
                    return true;
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }

        LOGGER.warn("[VitalForm] Hedef vizit ve bir önceki vizitte Vital Bulgular formu başlığı bulunamadı. Kayıt başarısız kabul ediliyor.");
        return false;
    }

    private boolean hasVitalFormHeaderInCard(WebElement card) {
        try {
            scrollIntoView(card);
            List<WebElement> headers = card.findElements(vitalFormHeaderInVisitCard);
            boolean exists = !headers.isEmpty();
            LOGGER.info("[VitalForm] Kart içinde Vital Bulgular başlığı var mı? {}", exists);
            return exists;
        } catch (StaleElementReferenceException e) {
            LOGGER.warn("[VitalForm] hasVitalFormHeaderInCard sırasında stale aldı: {}", e.getMessage());
            return false;
        }
    }

    // =========================================================
    //  SİLME: HEDEF VİZİT → ÖNCEKİ VİZİT → FALLBACK
    // =========================================================

    /**
     * Bütün vizitlerdeki TÜM Vital Bulgular form kayıtlarını siler.
     * Önce hedef vizit / bir önceki vizit, ardından fallback ile kalanları temizler.
     */
    public boolean deleteAllVitalFormsFromAllVisits() {
        LOGGER.info("[VitalForm] Tüm vizitlerdeki Vital Bulgular form kayıtları siliniyor...");

        boolean deletedAtLeastOne = false;

        if (deleteVitalFormFromTargetOrPreviousVisit()) {
            deletedAtLeastOne = true;
        }

        int guard = 0;
        while (hasAnyVitalFormInVisits() && guard < 20) {
            LOGGER.info("[VitalForm] Hâlâ Vital Bulgular formu var, tekrar silme denemesi (guard = {}).", guard);
            if (!deleteFirstVitalFormFromVisits()) {
                LOGGER.warn("[VitalForm] Görünür Vital Bulgular formu olmasına rağmen deleteFirstVitalFormFromVisits false döndü. Döngü sonlandırılıyor.");
                break;
            }
            deletedAtLeastOne = true;
            guard++;
        }

        return deletedAtLeastOne;
    }

    /**
     * Formun açıldığı vizit numarasını kullanarak:
     *  1) Önce ilgili vizit kartında Vital Bulgular formunu arar ve siler.
     *  2) Bulunamazsa, DOM'daki bir sonraki kartta (bir önceki vizit) arar ve siler.
     *  3) Hâlâ bulunamazsa genel arama fallback'i ile ilk bulduğu Vital Bulgular formunu siler.
     */
    public boolean deleteVitalFormFromTargetOrPreviousVisit() {
        LOGGER.info("[VitalForm] Hedef vizitte (ve gerekirse bir önceki vizitte) Vital Bulgular formu silme akışı başlıyor...");

        waitVisible(visitsContainer);

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[VitalForm] Toplam {} vizit kartı bulundu.", cards.size());

        if (cards.isEmpty()) {
            LOGGER.warn("[VitalForm] Hiç vizit kartı yok, silme işlemi yapılamaz.");
            return false;
        }

        int targetIndex = -1;
        if (createdVisitNumberForVitalForm != null) {
            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                scrollIntoView(card);
                String visitNo = extractVisitNumberFromCard(card);
                LOGGER.info("[VitalForm] {}. vizit kartının numarası: {}", i + 1, visitNo);

                if (createdVisitNumberForVitalForm.equals(visitNo)) {
                    targetIndex = i;
                    LOGGER.info("[VitalForm] Hedef vizit kartı bulundu. Index: {}, Vizit No: {}", targetIndex, visitNo);
                    break;
                }
            }
        } else {
            LOGGER.warn("[VitalForm] createdVisitNumberForVitalForm = null, hedef vizit numarası saklanamamış.");
        }

        WebElement targetCard = (targetIndex >= 0 && targetIndex < cards.size()) ? cards.get(targetIndex) : null;
        WebElement previousCard = null;

        if (targetIndex >= 0 && (targetIndex + 1) < cards.size()) {
            previousCard = cards.get(targetIndex + 1);
            LOGGER.info("[VitalForm] Bir önceki vizit kartı index: {}", targetIndex + 1);
        }

        if (targetCard != null) {
            LOGGER.info("[VitalForm] Önce hedef vizit kartında Vital Bulgular formu aranıyor...");
            if (deleteVitalFormInSingleCard(targetCard)) {
                return true;
            }
        }

        if (previousCard != null) {
            LOGGER.info("[VitalForm] Hedef vizitte bulunamadı, bir önceki vizitte Vital Bulgular formu aranıyor...");
            if (deleteVitalFormInSingleCard(previousCard)) {
                return true;
            }
        }

        LOGGER.warn("[VitalForm] Hedef ve bir önceki vizitte bulunamadı, fallback olarak ilk bulunan Vital Bulgular formu silinecek.");
        return deleteFirstVitalFormFromVisits();
    }

    private boolean deleteVitalFormInSingleCard(WebElement card) {
        scrollIntoView(card);

        for (int retry = 0; retry < 3; retry++) {
            List<WebElement> headers = card.findElements(vitalFormHeaderInVisitCard);
            if (!headers.isEmpty()) {
                LOGGER.info("[VitalForm] Bu vizit kartında Vital Bulgular formu başlığı bulundu (retry: {}).", retry);

                WebElement deleteBtn = card.findElement(vitalFormDeleteButtonInVisitCard);
                scrollIntoView(deleteBtn);

                try {
                    wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
                } catch (Exception e) {
                    LOGGER.warn("[VitalForm] Silme ikonunda normal click hata aldı, JS click denenecek. Hata: {}", e.getMessage());
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

        LOGGER.info("[VitalForm] Bu vizit kartında Vital Bulgular formu başlığı bulunamadı.");
        return false;
    }

    public boolean deleteFirstVitalFormFromVisits() {
        LOGGER.info("[VitalForm] (Fallback) Vizitler içinde ekli ilk Vital Bulgular formu aranıyor ve silinecek...");

        waitVisible(visitsContainer);

        long end = System.currentTimeMillis() + 8000L;

        while (System.currentTimeMillis() < end) {
            List<WebElement> cards = driver.findElements(allVisitCards);

            LOGGER.info("[VitalForm] (Fallback) Toplam {} vizit kartı bulundu.", cards.size());

            for (int i = 0; i < cards.size(); i++) {
                WebElement card = cards.get(i);
                scrollIntoView(card);

                List<WebElement> headers = card.findElements(vitalFormHeaderInVisitCard);
                if (!headers.isEmpty()) {
                    LOGGER.info("[VitalForm] (Fallback) Vital Bulgular formu {}. vizitte bulundu, silme ikonuna tıklanacak.", i + 1);

                    WebElement deleteBtn = card.findElement(vitalFormDeleteButtonInVisitCard);
                    scrollIntoView(deleteBtn);

                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
                    } catch (Exception e) {
                        LOGGER.warn("[VitalForm] (Fallback) Normal click silme ikonunda hata aldı, JS click denenecek. Hata: {}", e.getMessage());
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

        LOGGER.warn("[VitalForm] (Fallback) Hiçbir vizitte Vital Bulgular formu bulunamadı, silme işlemi yapılmadı.");
        return false;
    }

    public void confirmDeleteIfDialogVisible() {
        LOGGER.info("[VitalForm] Silme onay penceresi kontrol ediliyor...");

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            WebElement yesBtn = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(deleteConfirmYesButton)
            );

            LOGGER.info("[VitalForm] Silme onay penceresi göründü, 'Evet' butonuna tıklanıyor...");
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
            LOGGER.info("[VitalForm] Silme onay penceresi görünmedi, devam ediliyor.");
        }
    }

    private boolean hasAnyVitalFormInVisits() {
        waitVisible(visitsContainer);
        List<WebElement> cards = driver.findElements(allVisitCards);

        for (WebElement card : cards) {
            if (!card.findElements(vitalFormHeaderInVisitCard).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vital Bulgular formunun açıldığı vizitte artık hiçbir Vital Bulgular formu kalmadı mı?
     *  - Sadece ilgili vizit kartına bakar.
     */
    public boolean isVitalFormAbsentInCreatedVisit() {
        LOGGER.info("[VitalForm] Hedef vizitte Vital Bulgular formu kalmadı mı? (sadece ilgili vizit kontrol ediliyor)");

        if (createdVisitNumberForVitalForm == null) {
            LOGGER.warn("[VitalForm] createdVisitNumberForVitalForm = null, hedef vizit numarası bilinmiyor.");
            return false;
        }

        List<WebElement> cards = driver.findElements(allVisitCards);
        LOGGER.info("[VitalForm] Toplam {} vizit kartı bulundu.", cards.size());

        for (WebElement card : cards) {
            String visitNo = extractVisitNumberFromCard(card);
            if (!createdVisitNumberForVitalForm.equals(visitNo)) {
                continue;
            }

            LOGGER.info("[VitalForm] Hedef vizit kartı bulundu. Vizit No: {}", visitNo);
            scrollIntoView(card);

            List<WebElement> headers = card.findElements(vitalFormHeaderInVisitCard);
            if (!headers.isEmpty()) {
                LOGGER.info("[VitalForm] Hedef vizitte hâlâ Vital Bulgular formu başlığı görünüyor.");
                return false;
            }

            try {
                WebElement formsContainer = card.findElement(visitFormsContainerInCard);
                scrollIntoView(formsContainer);

                WebElement emptyText = formsContainer.findElement(emptyFormTextInCard);
                LOGGER.info("[VitalForm] Hedef vizitte boş durum yazısı bulundu: {}", emptyText.getText());
            } catch (NoSuchElementException e) {
                LOGGER.info("[VitalForm] Hedef vizitte 'Form bulunamadı.' yazısı görünmüyor; " +
                        "ancak Vital Bulgular formu başlığı da yok, yine de boş kabul ediliyor.");
            }

            LOGGER.info("[VitalForm] Hedef vizitte hiçbir Vital Bulgular formu görünmüyor.");
            return true;
        }

        LOGGER.warn("[VitalForm] Vizit listesinde {} numaralı hedef vizit kartı bulunamadı.", createdVisitNumberForVitalForm);
        return false;
    }
}

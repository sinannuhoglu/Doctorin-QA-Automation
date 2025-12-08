package com.sinannuhoglu.pages.admission;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;

import java.text.Normalizer;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Hasta Arama modülü + Hasta Kabul akışında kullanılan Page Object:
 * - Hasta Arama modülüne giriş
 * - Yeni hasta kaydı (Hasta Kabul formu – sidebar)
 * - Vizit Tipi / Departman / Doktor seçimleri
 * - Hasta Arama grid üzerinden kayıt doğrulama
 */
public class AdmissionPatientSearchPage {

    private static final Logger LOGGER = LogManager.getLogger(AdmissionPatientSearchPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Random random = new Random();

    private String lastIdentityNo;

    public AdmissionPatientSearchPage() {
        this.driver = DriverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    // ================== LOCATORS ==================

    private final By newPatientButton     = By.cssSelector("button[data-testid='patient-add-new']");
    private final By admissionSidebarRoot = By.id("AdmissionSidebarId");
    private final By registrationForm     = By.id("admission-sidebar__registration-form");

    private final By detailedFilterButton = By.cssSelector("button[data-testid='detailed-filter-button']");
    private final By detailedFilterDialog = By.cssSelector("div[id^='modal-dialog'][class*='e-dlg-container']");
    private final By toolbarSearchInput   = By.cssSelector("input[name='search-input']");
    private final By gridRoot             = By.id("Grid");
    private final By gridRows             = By.cssSelector("#Grid tbody[role='rowgroup'] tr.e-row");

    private final By globalLoadingOverlay = By.cssSelector("div.backdrop-blur-xs.z-20");

    private final By duplicateIdentityToastLocator = By.xpath(
            "//*[@id='toast_default']//*[contains(normalize-space(),'Bu kimlik numarasına sahip bir hasta var')]"
    );

    // ================== NAVİGASYON ==================

    public void goToPatientSearch(String url) {
        driver.get(url);

        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        pageWait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );

        pageWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(newPatientButton),
                ExpectedConditions.visibilityOfElementLocated(toolbarSearchInput),
                ExpectedConditions.visibilityOfElementLocated(gridRoot)
        ));

        waitForLoadingOverlayToDisappear();
    }

    // ================== GENEL YARDIMCILAR ==================

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element);
    }

    private void typeInto(WebElement input, String text) {
        scrollIntoView(input);
        input.click();
        try {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            input.sendKeys(Keys.DELETE);
        } catch (Exception ignored) {
            try {
                input.clear();
            } catch (Exception ignored2) {
                // input.clear() bazı custom inputlarda exception verebilir, sessiz geçiyoruz
            }
        }
        if (text != null) {
            input.sendKeys(text);
        }
    }

    /**
     * Ekranda global loading overlay (blur katman) varsa kaybolmasını bekler.
     * Overlay hiç yoksa hemen devam eder.
     */
    private void waitForLoadingOverlayToDisappear() {
        try {
            WebDriverWait overlayWait = new WebDriverWait(driver, Duration.ofSeconds(20));
            overlayWait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoadingOverlay));
        } catch (TimeoutException e) {
            LOGGER.warn("Loading overlay beklenen sürede kaybolmadı, akışa devam ediliyor.", e);
        }
    }

    private WebElement findFormGroupByLabel(String labelText) {
        WebElement formRoot = wait.until(
                ExpectedConditions.visibilityOfElementLocated(registrationForm));

        List<WebElement> groups = formRoot.findElements(By.cssSelector("div.e-form-group"));

        for (WebElement group : groups) {
            List<WebElement> labels = group.findElements(By.cssSelector("label.e-form-label"));
            if (labels.isEmpty()) continue;

            String text = labels.get(0).getText().trim();
            if (text.equals(labelText)) return group;
        }

        try {
            WebElement label = formRoot.findElement(By.xpath(
                    ".//label[contains(@class,'e-form-label') and normalize-space(.)='" + labelText + "']"
            ));

            return label.findElement(By.xpath(
                    "./ancestor::div[contains(@class,'e-form-group') or contains(@class,'grid')][1]"
            ));

        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(
                    "Hasta Kabul formunda label '" + labelText + "' bulunamadı.", e);
        }
    }

    /**
     * Türkçe karakterler dahil, accent ve büyük/küçük harf farkını normalize eder.
     * (Örn: "PSİKİYATRİ " -> "psikiyatri")
     */
    private String normalizeText(String text) {
        if (text == null) {
            return "";
        }

        String t = text.trim();

        t = Normalizer.normalize(t, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");

        t = t.replace('İ', 'I')
                .replace('ı', 'i');

        return t.toLowerCase(Locale.ROOT);
    }

    /**
     * Sidebar dışındaki (Hasta Kabul ana ekranı gibi) formlarda,
     * label metnine göre e-form-group alanını döner.
     */
    private WebElement findGlobalFormGroupByLabel(String labelText) {
        WebDriverWait globalWait = new WebDriverWait(driver, Duration.ofSeconds(20));

        String lower = labelText.toLowerCase(Locale.ROOT);

        By strictLocator = By.xpath(
                "//*[self::label or self::span]" +
                        "[contains(@class,'e-form-label') or contains(@class,'e-float-text')]" +
                        "[contains(" +
                        " translate(normalize-space(), " +
                        "  'ABCDEFGHIJKLMNOPQRSTUVWXYZÇĞİÖŞÜ', " +
                        "  'abcdefghijklmnopqrstuvwxyzçğıöşü'" +
                        " ), '" + lower + "')]"
        );

        try {
            WebElement label = globalWait.until(
                    ExpectedConditions.visibilityOfElementLocated(strictLocator)
            );

            return label.findElement(
                    By.xpath("./ancestor::div[" +
                            " contains(@class,'e-form-group') or" +
                            " contains(@class,'e-float-input') or" +
                            " contains(@class,'e-control-wrapper')" +
                            "][1]")
            );
        } catch (TimeoutException e) {
            By fallbackLocator = By.xpath(
                    "//*[self::label or self::span or self::div]" +
                            "[contains(" +
                            " translate(normalize-space(), " +
                            "  'ABCDEFGHIJKLMNOPQRSTUVWXYZÇĞİÖŞÜ', " +
                            "  'abcdefghijklmnopqrstuvwxyzçğıöşü'" +
                            " ), '" + lower + "')]"
            );

            WebElement label = globalWait.until(
                    ExpectedConditions.visibilityOfElementLocated(fallbackLocator)
            );

            return label.findElement(
                    By.xpath("./ancestor::div[" +
                            " contains(@class,'e-form-group') or" +
                            " contains(@class,'e-float-input') or" +
                            " contains(@class,'e-control-wrapper')" +
                            "][1]")
            );
        }
    }

    /**
     * Syncfusion DropDownList popup’ından item seçer.
     * Önce verilen popupLocator ile dener, bulunamazsa generic popup locator ile fallback yapar.
     */
    private void selectFromDropdown(By popupLocator, String optionText) {
        By genericPopupLocator = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open[role='dialog']"
        );

        WebElement popup;

        waitForLoadingOverlayToDisappear();

        try {
            popup = new WebDriverWait(driver, Duration.ofSeconds(12))
                    .until(ExpectedConditions.visibilityOfElementLocated(popupLocator));
        } catch (TimeoutException ex) {
            LOGGER.warn("Popup, özel locator ile bulunamadı: {}. Generic locator ile yeniden denenecek.", popupLocator);

            waitForLoadingOverlayToDisappear();

            popup = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfElementLocated(genericPopupLocator));
        }

        String optionXpath = ".//li[normalize-space()='" + optionText + "']";
        WebElement optionElement = popup.findElement(By.xpath(optionXpath));

        scrollIntoView(optionElement);

        optionElement.click();
    }

    private void waitForGridRowsToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridRoot));

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> {
                    List<WebElement> rows = d.findElements(gridRows);
                    return !rows.isEmpty();
                });
    }

    // ================== YENİ KAYIT (SIDEBAR) ==================

    public void openNewPatientForm() {
        waitForLoadingOverlayToDisappear();

        clickNewPatientButtonSafely();

        waitForLoadingOverlayToDisappear();

        wait.until(ExpectedConditions.visibilityOfElementLocated(admissionSidebarRoot));
        wait.until(ExpectedConditions.visibilityOfElementLocated(registrationForm));
    }

    private void clickNewPatientButtonSafely() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement button = wait.until(
                        ExpectedConditions.elementToBeClickable(newPatientButton));
                scrollIntoView(button);
                button.click();
                return;
            } catch (ElementClickInterceptedException e) {
                attempts++;
                LOGGER.warn("Yeni hasta butonuna tıklanamadı, deneme: {}", attempts, e);
                waitForLoadingOverlayToDisappear();

                if (attempts == 3) {
                    WebElement button = driver.findElement(newPatientButton);
                    scrollIntoView(button);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }

    public void selectNationality(String nationalityText) {
        By popupLocator = By.cssSelector(
                "div[id*='nationality'].e-ddl.e-control.e-lib.e-popup.e-popup-open[role='dialog']"
        );
        selectFromLabeledDropdown("Uyruk", popupLocator, nationalityText);
    }

    public void selectLanguage(String languageText) {
        By popupLocator = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open[role='dialog'][aria-label='dropdownlist']"
        );
        selectFromLabeledDropdown("Dil", popupLocator, languageText);
    }

    public void setIdentityNumber(String identityNo) {
        this.lastIdentityNo = identityNo;

        WebElement formRoot = wait.until(
                ExpectedConditions.visibilityOfElementLocated(registrationForm)
        );

        WebElement label = formRoot.findElement(By.xpath(
                ".//label[contains(normalize-space(),'Kimlik Numarası')]"
        ));

        WebElement input = label.findElement(By.xpath(
                "./following-sibling::span[1]//input[not(@type='hidden')]"
        ));

        scrollIntoView(input);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "var el = arguments[0]; var val = arguments[1];" +
                        "if (el && el.ej2_instances && el.ej2_instances[0]) {" +
                        "  el.ej2_instances[0].value = val;" +
                        "} else {" +
                        "  el.value = val;" +
                        "}" +
                        "el.dispatchEvent(new Event('input',  { bubbles:true }));" +
                        "el.dispatchEvent(new Event('change', { bubbles:true }));",
                input, identityNo
        );

        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(d -> {
                    String value = input.getAttribute("value");
                    if (value == null) return false;
                    String digits = value.replaceAll("\\D", "");
                    return digits.length() == identityNo.length();
                });
    }

    public void setFirstName(String firstName) {
        WebElement group = findFormGroupByLabel("Ad");
        WebElement input = group.findElement(By.tagName("input"));
        typeInto(input, firstName);
    }

    public void setLastName(String lastName) {
        WebElement group = findFormGroupByLabel("Soyad");
        WebElement input = group.findElement(By.cssSelector("input[type='text']"));
        typeInto(input, lastName);
    }

    public void setBirthDate(String dateStr) {
        WebElement group = findFormGroupByLabel("Doğum Tarihi");

        WebElement input = group.findElement(By.xpath(
                ".//input[not(@type='hidden') and " +
                        "(contains(@class,'e-input') or contains(@class,'e-maskedtextbox'))]"
        ));

        scrollIntoView(input);
        wait.until(ExpectedConditions.elementToBeClickable(input));
        input.click();

        Keys cmdCtrl = System.getProperty("os.name").toLowerCase().contains("mac")
                ? Keys.COMMAND : Keys.CONTROL;

        try {
            input.sendKeys(Keys.chord(cmdCtrl, "a"));
            input.sendKeys(Keys.DELETE);
        } catch (Exception ignored) {
            try {
                input.clear();
            } catch (Exception ignored2) {}
        }

        input.sendKeys(dateStr);

        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(d -> !input.getAttribute("value").trim().isEmpty());
    }

    public void selectGender(String genderText) {
        By popupLocator = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open[role='dialog'][aria-label='dropdownlist']"
        );
        selectFromLabeledDropdown("Cinsiyet", popupLocator, genderText);
    }

    public void setMobilePhone(String phone) {
        WebElement formRoot = wait.until(
                ExpectedConditions.visibilityOfElementLocated(registrationForm));

        WebElement label = formRoot.findElement(By.xpath(
                ".//label[contains(@class,'e-form-label') and normalize-space(.)='Cep Telefonu']"
        ));

        WebElement input = label.findElement(By.xpath(
                "./following-sibling::div[contains(@class,'grid')][1]" +
                        "//input[@type='tel' and contains(@class,'e-maskedtextbox')]"
        ));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "var el = arguments[0]; var val = arguments[1];" +
                        "if (el && el.ej2_instances && el.ej2_instances[0]) {" +
                        "  el.ej2_instances[0].value = val;" +
                        "} else {" +
                        "  el.value = val;" +
                        "}" +
                        "el.dispatchEvent(new Event('input',  { bubbles:true }));" +
                        "el.dispatchEvent(new Event('change', { bubbles:true }));",
                input, phone
        );
    }

    public void setEmail(String email) {
        WebElement group = findFormGroupByLabel("E-posta");
        WebElement input = group.findElement(By.tagName("input"));
        typeInto(input, email);
    }

    /**
     * Kaydet sonrası:
     *  - İlk adım kayıt formu kapanırsa: başarı (Kabul adımına geçilmiş demek).
     *  - "Bu kimlik numarasına sahip bir hasta var" toast'ı çıkarsa:
     *      + Kimlik numarasının son 2 hanesi random değiştirilir
     *      + Tekrar Kaydet denemesi yapılır (max 3 deneme).
     */
    public void clickSidebarSave() {
        int attempts = 0;

        while (attempts < 3) {
            attempts++;

            boolean clicked = clickSidebarSaveInternal();
            if (!clicked) {
                LOGGER.warn("Kayıt formu veya Kaydet butonu bulunamadı; muhtemelen zaten Kabul adımına geçildi. Adım atlanıyor.");
                return;
            }

            try {
                WebDriverWait postWait = new WebDriverWait(driver, Duration.ofSeconds(15));
                postWait.until(d -> {
                    boolean regFormClosed = !isRegistrationFormOpen();
                    boolean duplicateToast = isDuplicateIdentityToastVisibleNow(d);
                    return regFormClosed || duplicateToast;
                });

                boolean regFormClosed = !isRegistrationFormOpen();
                boolean duplicateToast = isDuplicateIdentityToastVisibleNow(driver);

                if (regFormClosed) {
                    waitForLoadingOverlayToDisappear();
                    return;
                }

                if (duplicateToast) {
                    LOGGER.warn("Bu kimlik numarasına sahip bir hasta var uyarısı alındı. Yeni kimlik numarası deneniyor. Deneme: {}", attempts);
                    changeIdentityLastTwoDigitsRandom();
                    dismissDuplicateIdentityToastIfPossible();
                    continue;
                }

            } catch (TimeoutException e) {
                LOGGER.warn("Kayıt sonrası ne form kapandı ne de duplicate toast görüldü. Deneme: {}", attempts, e);
                if (attempts >= 3) {
                    throw e;
                }
            }
        }

        throw new RuntimeException("Hasta kaydı için benzersiz kimlik numarası üretilemedi (3 deneme).");
    }

    /**
     * Sidebar içindeki Kaydet butonuna tıklar.
     * Sidebar veya form ya da buton hiç bulunamazsa exception atmaz, false döner.
     */
    private boolean clickSidebarSaveInternal() {
        waitForLoadingOverlayToDisappear();

        List<WebElement> sidebars = driver.findElements(admissionSidebarRoot);
        if (sidebars.isEmpty()) {
            LOGGER.warn("Admission sidebar root (AdmissionSidebarId) bulunamadı; form muhtemelen kapalı.");
            return false;
        }

        WebElement sidebar = sidebars.get(0);

        WebElement container = sidebar;
        try {
            container = sidebar.findElement(By.id("admission-sidebar__registration-form"));
        } catch (NoSuchElementException ex) {
            LOGGER.warn("registrationForm id'li form bulunamadı; Kaydet butonu sidebar kökünden aranacak.");
        }

        List<WebElement> buttons = container.findElements(
                By.xpath(".//button[@type='submit' and normalize-space()='Kaydet']")
        );

        if (buttons.isEmpty()) {
            LOGGER.warn("Sidebar içinde 'Kaydet' butonu bulunamadı.");
            return false;
        }

        WebElement saveButton = buttons.get(0);

        scrollIntoView(saveButton);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(saveButton))
                .click();

        return true;
    }

    /**
     * Sidebar açık mı? (AdmissionSidebarId görünür mü?)
     */
    private boolean isSidebarOpen() {
        try {
            List<WebElement> sidebars = driver.findElements(admissionSidebarRoot);
            for (WebElement sb : sidebars) {
                if (sb.isDisplayed()) {
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    private boolean isDuplicateIdentityToastVisibleNow(WebDriver d) {
        try {
            List<WebElement> elements = d.findElements(duplicateIdentityToastLocator);
            for (WebElement el : elements) {
                if (el.isDisplayed()) {
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    private void changeIdentityLastTwoDigitsRandom() {
        if (lastIdentityNo == null || lastIdentityNo.length() < 2) {
            LOGGER.warn("lastIdentityNo tanımlı değil, kimlik numarası güncellenemiyor.");
            return;
        }

        String prefix = lastIdentityNo.substring(0, lastIdentityNo.length() - 2);
        int suffix = random.nextInt(90) + 10; // 10-99
        String newId = prefix + String.format("%02d", suffix);

        LOGGER.info("Kimlik numarası çakıştığı için yeni kimlik numarası denenecek: {}", newId);
        setIdentityNumber(newId);
    }

    private void dismissDuplicateIdentityToastIfPossible() {
        try {
            WebElement toast = driver.findElement(By.id("toast_default"));
            WebElement closeBtn = toast.findElement(By.cssSelector("button[aria-label='Close']"));
            closeBtn.click();
        } catch (Exception ignored) {
        }
    }

    // ================== KAYIT SONRASI HASTA KABUL EKRANI ==================

    public void selectVisitType(String visitType) {
        selectFromGlobalLabeledDropdown("Vizit Tipi", visitType);
    }

    public void selectDepartment(String departmentName) {
        selectFromGlobalLabeledDropdown("Departman", departmentName);
    }

    public void selectDoctor(String doctorName) {
        selectFromGlobalLabeledDropdown("Doktor", doctorName);
    }


    private WebElement findSidebarFormGroupByLabel(String labelText) {
        By formGroupLocator = By.xpath(
                "//label[contains(normalize-space(), '" + labelText + "')]" +
                        "/ancestor::div[" +
                        "contains(@class,'e-form-group') or " +
                        "contains(@class,'e-float-input') or " +
                        "contains(@class,'e-control-wrapper')" +
                        "][1]"
        );

        return wait.until(ExpectedConditions.visibilityOfElementLocated(formGroupLocator));
    }

    public void clickFinalSaveOnVisitScreen() {
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'e-button-right')]//button[@type='submit' and normalize-space()='Kaydet']")
        ));
        scrollIntoView(saveButton);
        saveButton.click();
    }

    public void clickMainSave() {
        WebElement saveButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//button[@type='submit' and normalize-space()='Kaydet'])[last()]")
                )
        );
        scrollIntoView(saveButton);
        saveButton.click();
    }

    // ================== TOAST (OPSİYONEL, BAŞKA SENARYOLAR İÇİN) ==================

    public void verifySaveSuccessToast() {
        By toastMessageLocator = By.cssSelector(
                "#toast_default .e-toast-message, " +
                        "#toast_default .e-toast-content, " +
                        "#toast_default [role='alert']"
        );

        WebElement toastMessage;
        try {
            toastMessage = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfElementLocated(toastMessageLocator));
        } catch (TimeoutException e) {
            try {
                WebElement container = driver.findElement(By.id("toast_default"));
                String outer = container.getAttribute("outerHTML");
                Assert.fail("Kayıt sonrası beklenen toast mesajı hiç görünmedi. Container HTML: " + outer);
            } catch (NoSuchElementException ne) {
                Assert.fail("Kayıt sonrası beklenen toast mesajı hiç görünmedi ve #toast_default container'ı bulunamadı.");
            }
            return;
        }

        String toastText = toastMessage.getText().trim();

        Assert.assertFalse(
                toastText.isEmpty(),
                "Toast mesajı boş geldi. HTML: " + toastMessage.getAttribute("outerHTML")
        );

        String lower = toastText.toLowerCase(Locale.ROOT);
        boolean containsSuccess =
                lower.contains("başar") ||
                        lower.contains("kaydedildi") ||
                        lower.contains("oluşturuldu");

        Assert.assertTrue(
                containsSuccess,
                "Beklenen başarı mesajı görünmedi. Toast içeriği: " + toastText
        );
    }

    // ================== LABEL'LI DROPDOWN YARDIMCILARI ==================

    /**
     * İlk adım kayıt formu (admission-sidebar__registration-form) açık mı?
     */
    private boolean isRegistrationFormOpen() {
        try {
            List<WebElement> forms = driver.findElements(registrationForm);
            for (WebElement form : forms) {
                if (form.isDisplayed()) {
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    private void selectFromLabeledDropdown(String labelText, By popupLocator, String value) {
        int attempts = 0;

        while (attempts < 3) {
            try {
                waitForLoadingOverlayToDisappear();

                WebElement group = findFormGroupByLabel(labelText);

                WebElement combo;
                try {
                    combo = group.findElement(
                            By.cssSelector("span.e-input-group-icon.e-ddl-icon")
                    );
                } catch (NoSuchElementException ex) {
                    combo = group.findElement(By.cssSelector("span.e-ddl"));
                }

                wait.until(ExpectedConditions.elementToBeClickable(combo));
                scrollIntoView(combo);
                combo.click();
                selectFromDropdown(popupLocator, value);

                return;

            } catch (StaleElementReferenceException | ElementClickInterceptedException | TimeoutException e) {
                attempts++;
                LOGGER.warn("Label '{}' için dropdown seçiminde sorun, deneme: {}", labelText, attempts, e);

                try {
                    driver.findElement(By.tagName("body")).click();
                } catch (Exception ignored2) {}

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored3) {}

                if (attempts >= 3) {
                    throw new RuntimeException(
                            "Dropdown '" + labelText + "' için değer seçilemedi: " + value, e);
                }
            }
        }
    }

    /**
     * Ana ekrandaki Syncfusion dropdown'larını label'a göre açar ve değer seçer.
     */
    private void selectFromGlobalLabeledDropdown(String labelText, String value) {
        int attempts = 0;

        By popupLocator = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open"
        );

        while (attempts < 3) {
            try {
                waitForLoadingOverlayToDisappear();

                WebElement group = findGlobalFormGroupByLabel(labelText);

                WebElement combo;
                try {
                    combo = group.findElement(
                            By.cssSelector("span.e-input-group-icon.e-ddl-icon")
                    );
                } catch (NoSuchElementException ex) {
                    combo = group.findElement(By.cssSelector("span.e-ddl"));
                }

                wait.until(ExpectedConditions.elementToBeClickable(combo));
                scrollIntoView(combo);
                combo.click();

                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {}

                selectFromDropdown(popupLocator, value);
                return;

            } catch (StaleElementReferenceException | ElementClickInterceptedException | TimeoutException e) {
                attempts++;
                LOGGER.warn("Global dropdown '{}' seçiminde sorun, deneme: {}", labelText, attempts, e);

                try {
                    driver.findElement(By.tagName("body")).click();
                } catch (Exception ignored2) {}

                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored3) {}

                if (attempts >= 3) throw new RuntimeException(
                        "Global dropdown '" + labelText + "' için değer seçilemedi: " + value, e);
            }
        }
    }

    // ================== PATIENT SEARCH – DETAYLI ARAMA & GRID DOĞRULAMA ==================

    public void openDetailedFilterDialog() {
        waitForLoadingOverlayToDisappear();

        int attempts = 0;
        while (attempts < 3) {
            WebElement button = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(detailedFilterButton)
            );
            scrollIntoView(button);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(button)).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(detailedFilterDialog));
                return;
            } catch (ElementClickInterceptedException e) {
                attempts++;
                LOGGER.warn("Detaylı filtre butonuna tıklanamadı (overlay olabilir). Deneme: {}", attempts, e);

                waitForLoadingOverlayToDisappear();

                if (attempts >= 3) {
                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(detailedFilterDialog));
                        return;
                    } catch (Exception jsEx) {
                        throw e;
                    }
                } else {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }

    public void clickDetailedFilterClear() {
        WebElement dialog = wait.until(
                ExpectedConditions.visibilityOfElementLocated(detailedFilterDialog)
        );

        WebElement clearButton = dialog.findElement(
                By.xpath(".//button[@type='button' and normalize-space()='Temizle']")
        );

        scrollIntoView(clearButton);
        clearButton.click();
    }

    public void clickDetailedFilterApply() {
        WebElement dialog = wait.until(
                ExpectedConditions.visibilityOfElementLocated(detailedFilterDialog)
        );

        WebElement applyButton = dialog.findElement(
                By.xpath(".//button[@type='submit' and normalize-space()='Uygula']")
        );

        scrollIntoView(applyButton);
        applyButton.click();

        wait.until(ExpectedConditions.invisibilityOf(dialog));
    }

    public void searchPatientByName(String fullName) {
        waitForLoadingOverlayToDisappear();

        WebElement searchInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(toolbarSearchInput)
        );

        scrollIntoView(searchInput);
        searchInput.click();

        Keys cmdCtrl = System.getProperty("os.name").toLowerCase().contains("mac")
                ? Keys.COMMAND : Keys.CONTROL;

        try {
            searchInput.sendKeys(Keys.chord(cmdCtrl, "a"));
            searchInput.sendKeys(Keys.DELETE);
        } catch (Exception ignored) {
            try {
                searchInput.clear();
            } catch (Exception ignored2) {}
        }

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "var el = arguments[0];" +
                            "if (!el) return;" +
                            "el.value = '';" +
                            "if (el.dispatchEvent) {" +
                            "  el.dispatchEvent(new Event('input',  {bubbles:true}));" +
                            "  el.dispatchEvent(new Event('change', {bubbles:true}));" +
                            "}",
                    searchInput
            );
        } catch (Exception ignored) { }

        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(d -> {
                    String v = searchInput.getAttribute("value");
                    return v == null || v.trim().isEmpty();
                });

        searchInput.sendKeys(fullName);
        searchInput.sendKeys(Keys.ENTER);

        waitForGridRowsToLoad();
    }

    public void assertPatientListedInGrid(String fullName) {
        String expected = normalizeText(fullName);

        WebElement grid = wait.until(
                ExpectedConditions.visibilityOfElementLocated(gridRoot)
        );

        long end = System.currentTimeMillis() + 10000;

        while (System.currentTimeMillis() < end) {
            List<WebElement> rows = grid.findElements(gridRows);
            boolean found = false;

            for (WebElement row : rows) {
                try {
                    WebElement firstCell = row.findElement(By.xpath(".//td[@role='gridcell'][1]"));
                    String cellText = firstCell.getText().trim();
                    if (cellText.isEmpty()) continue;

                    String normalizedCell = normalizeText(cellText);

                    if (normalizedCell.contains(expected)) {
                        found = true;
                        break;
                    }
                } catch (NoSuchElementException ignored) {}
            }

            if (found) {
                LOGGER.info("Hasta grid üzerinde bulundu: '{}'", fullName);
                return;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }

        WebElement gridElement = driver.findElement(gridRoot);
        Assert.assertTrue(
                false,
                "Hasta kaydı grid üzerinde bulunamadı: '" + fullName + "'. " +
                        "Grid HTML: " + gridElement.getAttribute("outerHTML")
        );
    }
}

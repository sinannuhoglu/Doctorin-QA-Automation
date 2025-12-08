package com.sinannuhoglu.pages.admission;

import com.sinannuhoglu.core.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.text.Normalizer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Hasta Arama modülü + Hasta Kabul akışında kullanılan Page Object:
 * - Hasta Arama modülüne giriş
 * - Yeni hasta kaydı (Hasta Kabul formu – sidebar)
 * - Vizit Tipi / Departman / Doktor seçimleri
 * - Hasta Arama grid üzerinden kayıt doğrulama
 */
public class AdmissionPatientSearchPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AdmissionPatientSearchPage() {
        this.driver = DriverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    // ================== LOCATORS ==================

    // Yeni hasta ekleme / sidebar
    private final By newPatientButton     = By.cssSelector("button[data-testid='patient-add-new']");
    private final By admissionSidebarRoot = By.id("AdmissionSidebarId");
    private final By registrationForm     = By.id("admission-sidebar__registration-form");

    // Hasta Arama ekranı toolbar / grid
    private final By detailedFilterButton = By.cssSelector("button[data-testid='detailed-filter-button']");
    private final By detailedFilterDialog = By.cssSelector("div[id^='modal-dialog'][class*='e-dlg-container']");
    private final By toolbarSearchInput   = By.cssSelector("input[name='search-input']");
    private final By gridRoot             = By.id("Grid");
    private final By gridRows             = By.cssSelector("#Grid tbody[role='rowgroup'] tr.e-row");

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

        WebElement label = globalWait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[contains(@class,'e-form-label') and " +
                                "contains(normalize-space(),'" + labelText + "')]")
                )
        );

        return label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')][1]")
        );
    }

    private void selectFromDropdown(By popupLocator, String value) {
        int attempts = 0;
        String target = value.trim();
        String normalizedTarget = normalizeText(target);

        while (attempts < 3) {
            try {
                WebElement popup = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(popupLocator)
                );

                List<WebElement> options = popup.findElements(By.cssSelector("ul li"));
                boolean staleHit = false;

                for (int i = 0; i < options.size(); i++) {
                    WebElement opt = options.get(i);

                    String text;
                    String innerHtml;
                    try {
                        text = opt.getText();
                        innerHtml = opt.getAttribute("innerHTML");
                    } catch (StaleElementReferenceException e) {
                        staleHit = true;
                        break;
                    }

                    String candidate = (text != null && !text.trim().isEmpty())
                            ? text.trim()
                            : (innerHtml != null
                            ? innerHtml.replaceAll("<[^>]*>", "").trim()
                            : "");

                    if (candidate.isEmpty()) {
                        continue;
                    }

                    String normalizedOption = normalizeText(candidate);

                    if (normalizedOption.equals(normalizedTarget) ||
                            normalizedOption.contains(normalizedTarget)) {

                        wait.until(ExpectedConditions.elementToBeClickable(opt)).click();
                        return;
                    }
                }

                if (staleHit) {
                    attempts++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) {}
                    continue;
                }

                throw new NoSuchElementException(
                        "Dropdown içinde '" + value + "' seçeneği bulunamadı."
                );

            } catch (StaleElementReferenceException e) {
                attempts++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {}
            }
        }

        throw new NoSuchElementException(
                "Dropdown içinde '" + value + "' seçeneği, birden fazla STALE denemesine rağmen bulunamadı."
        );
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
        clickNewPatientButtonSafely();
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
                if (attempts == 3) {
                    WebElement button = driver.findElement(newPatientButton);
                    scrollIntoView(button);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
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
        WebElement group = findFormGroupByLabel("Kimlik Numarası");
        WebElement input = group.findElement(By.tagName("input"));
        typeInto(input, identityNo);
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

    public void clickSidebarSave() {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(registrationForm)
        );

        WebElement saveButton = form.findElement(
                By.xpath(".//button[@type='submit' and normalize-space()='Kaydet']")
        );

        scrollIntoView(saveButton);
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    // ================== KAYIT SONRASI HASTA KABUL EKRANI ==================

    public void selectVisitType(String visitType) {
        selectFromGlobalLabeledDropdown("Vizit Tipi", visitType);

        // Vizit Tipi değiştiğinde, bağlı alanların yüklenmesi için kısa bekleme
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}
    }

    public void selectDepartment(String departmentName) {
        By popupLocator = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open[role='dialog']"
        );

        int attempts = 0;

        while (attempts < 3) {
            attempts++;

            WebElement formGroup = findGlobalFormGroupByLabel("Departman");

            WebElement dropdownIcon = formGroup.findElement(
                    By.cssSelector("span.e-input-group-icon.e-ddl-icon")
            );

            wait.until(ExpectedConditions.elementToBeClickable(dropdownIcon));
            scrollIntoView(dropdownIcon);
            dropdownIcon.click();

            try {
                selectFromDropdown(popupLocator, departmentName);
                return;
            } catch (NoSuchElementException e) {

                try {
                    driver.findElement(By.tagName("body")).click();
                } catch (Exception ignored2) {}

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored3) {}

                if (attempts >= 3) {
                    throw e;
                }
            }
        }
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

    public void selectDoctor(String doctorName) {
        By popupLocator = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open[role='dialog']"
        );

        int attempts = 0;

        while (attempts < 3) {
            attempts++;

            WebElement formGroup = findGlobalFormGroupByLabel("Doktor");

            WebElement combo;
            try {
                combo = formGroup.findElement(
                        By.cssSelector("span.e-input-group-icon.e-ddl-icon")
                );
            } catch (NoSuchElementException ex) {
                combo = formGroup.findElement(By.cssSelector("span.e-ddl"));
            }

            wait.until(ExpectedConditions.elementToBeClickable(combo));
            scrollIntoView(combo);
            combo.click();

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}

            try {
                selectFromDropdown(popupLocator, doctorName);
                return;
            } catch (TimeoutException | NoSuchElementException e) {

                try {
                    driver.findElement(By.tagName("body")).click();
                } catch (Exception ignored2) {}

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored3) {}

                if (attempts >= 3) {
                    throw e;
                }
            }
        }
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

    private void selectFromLabeledDropdown(String labelText, By popupLocator, String value) {
        int attempts = 0;

        while (attempts < 3) {
            try {
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

            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts >= 3) throw e;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {}
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

            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts >= 3) throw e;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    // ================== PATIENT SEARCH – DETAYLI ARAMA & GRID DOĞRULAMA ==================

    public void openDetailedFilterDialog() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(detailedFilterButton));
        scrollIntoView(button);
        button.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(detailedFilterDialog));
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
        WebElement searchInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(toolbarSearchInput)
        );

        scrollIntoView(searchInput);

        try {
            searchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            searchInput.sendKeys(Keys.DELETE);
        } catch (Exception ignored) {
            try {
                searchInput.clear();
            } catch (Exception ignored2) {}
        }

        searchInput.sendKeys(fullName);
        searchInput.sendKeys(Keys.ENTER);

        waitForGridRowsToLoad();
    }

    public void assertPatientListedInGrid(String fullName) {
        String expected = normalizeText(fullName);

        WebElement grid = wait.until(
                ExpectedConditions.visibilityOfElementLocated(gridRoot)
        );

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
            } catch (NoSuchElementException ignored) {
            }
        }

        Assert.assertTrue(
                found,
                "Hasta kaydı grid üzerinde bulunamadı: '" + fullName + "'. " +
                        "Grid HTML: " + grid.getAttribute("outerHTML")
        );
    }
}

package com.sinannuhoglu.pages.admission.definitions;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.text.Normalizer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdmissionVisitTypesPage {

    private static final Logger LOGGER = LogManager.getLogger(AdmissionVisitTypesPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // CREATE & EDIT senaryolarında kullanılan son auto-index isim
    private String generatedName = null;

    // EDIT + DURUM değiştirme senaryosunda grid’de bulunacak satırın mevcut adı
    private String editSourceName = null;

    public AdmissionVisitTypesPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ==================== LOCATORS ======================

    private final By newVisitTypeButton = By.xpath(
            "//button[contains(@class,'e-btn')]" +
                    "[.//span[normalize-space()='Yeni Ekle'] or normalize-space()='Yeni Ekle']"
    );

    private final By filterButton = By.cssSelector(
            "div.e-toolbar-left button.e-control.e-btn.e-lib.e-icon-btn"
    );

    private final By detailedSearchClearButton = By.xpath("//button[normalize-space()='Temizle']");
    private final By detailedSearchApplyButton = By.xpath("//button[normalize-space()='Uygula']");

    private final By visitTypeDialogForm = By.cssSelector("form.e-data-form");

    private final By gridContent = By.cssSelector("div.e-gridcontent");

    private final By gridRows = By.cssSelector(
            "div.e-gridcontent table.e-table tbody tr.e-row"
    );

    private final By emptyRow = By.cssSelector(
            "div.e-gridcontent table.e-table tbody tr.e-emptyrow"
    );

    private final By toolbarSearchContainer = By.cssSelector(
            "div.e-toolbar-item.e-template#search"
    );

    // ==================== HELPER METODLAR ===========================

    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", el
            );
        } catch (JavascriptException e) {
            LOGGER.warn("scrollIntoView sırasında hata alındı: {}", e.getMessage());
        }
    }

    /**
     * WebElement üzerinden güvenli click – stale / not interactable durumlarında
     * birkaç kez deneyip son denemede exception bırakır.
     */
    private void safeClick(WebElement element) {
        int maxRetries = 3;

        for (int i = 1; i <= maxRetries; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                scrollIntoView(element);
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("[AdmissionVisitTypesPage] safeClick(WebElement) -> StaleElementReference, retry {}", i);
            } catch (ElementNotInteractableException e) {
                LOGGER.warn("[AdmissionVisitTypesPage] safeClick(WebElement) -> Click problemi, retry {}", i);
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    return;
                } catch (JavascriptException jsEx) {
                    LOGGER.warn("JS click denemesi de başarısız: {}", jsEx.getMessage());
                }
            }
        }

        // Hâlâ olmuyorsa son kez tıklayıp exception'ı gör
        element.click();
    }

    /**
     * By locator üzerinden güvenli click – her denemede elementi taze bulur.
     */
    private void safeClick(By locator) {
        int maxRetries = 3;

        for (int i = 1; i <= maxRetries; i++) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                scrollIntoView(el);
                el.click();
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("[AdmissionVisitTypesPage] safeClick(By) -> StaleElementReference, retry {}", i);
            } catch (ElementNotInteractableException e) {
                LOGGER.warn("[AdmissionVisitTypesPage] safeClick(By) -> Click problemi, retry {}", i);
                try {
                    WebElement el = driver.findElement(locator);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    return;
                } catch (JavascriptException jsEx) {
                    LOGGER.warn("JS click denemesi de başarısız: {}", jsEx.getMessage());
                }
            }
        }

        WebElement el = driver.findElement(locator);
        scrollIntoView(el);
        el.click();
    }

    private void typeInto(WebElement el, String text) {
        wait.until(ExpectedConditions.visibilityOf(el));
        scrollIntoView(el);
        el.click();
        el.clear();
        if (text != null) {
            el.sendKeys(text);
        }
    }

    private String normalize(String t) {
        if (t == null) return "";
        return Normalizer
                .normalize(t.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT);
    }

    private void waitForGridLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContent));
        wait.until(d -> {
            boolean hasRows = !d.findElements(gridRows).isEmpty();
            boolean hasEmpty = !d.findElements(emptyRow).isEmpty();
            return hasRows || hasEmpty;
        });
    }

    private WebElement findRowByFirstCellEquals(String exactText) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);
        for (WebElement row : rows) {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            if (tds.isEmpty()) continue;
            String cellText = tds.get(0).getText().trim();
            if (cellText.equals(exactText)) {
                return row;
            }
        }
        throw new NoSuchElementException("İlk sütunda '" + exactText + "' değerine sahip satır bulunamadı.");
    }

    // ==================== NAVIGATION ========================

    public void goToVisitTypes(String url) {
        driver.get(url);

        // Sayfanın tam yüklenmesini bekle
        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        pageWait.until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState"))
        );

        pageWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(newVisitTypeButton),
                ExpectedConditions.visibilityOfElementLocated(toolbarSearchContainer),
                ExpectedConditions.visibilityOfElementLocated(gridContent)
        ));

        waitForGridLoaded();
    }

    // ================= DETAYLI ARAMA ========================

    public void openDetailedSearch() {
        safeClick(filterButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(detailedSearchClearButton));
    }

    public void clickDetailedSearchClear() {
        safeClick(detailedSearchClearButton);
    }

    public void clickDetailedSearchApply() {
        safeClick(detailedSearchApplyButton);
        waitForGridLoaded();
    }

    // ===================== ARAMA ALANI ======================

    public void searchOnToolbar(String text) {
        WebElement container = wait.until(
                ExpectedConditions.visibilityOfElementLocated(toolbarSearchContainer)
        );
        WebElement input = container.findElement(By.cssSelector("input"));
        typeInto(input, text);
        input.sendKeys(Keys.ENTER);
        waitForGridLoaded();
    }

    // =============== AUTO INDEX (CREATE) =====================

    /**
     * Grid isimlerinden en büyük index'i bulup +1 ile yeni kayıt ismini üretir.
     * "Göz Ölçüm Odası", "Göz Ölçüm Odası 1", "Göz Ölçüm Odası (1)" gibi
     * parantezli / parantezsiz formatları devam ettirebilir.
     */
    public String generateAutoIndex(String base) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        int maxIndex = 0;
        boolean useParentheses = false;

        String normBase = normalize(base);

        for (WebElement row : rows) {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            if (tds.isEmpty()) continue;

            String cell = tds.get(0).getText().trim();
            String norm = normalize(cell);

            if (!norm.startsWith(normBase)) continue;

            int idx = 0;
            boolean parenStyle = false;

            if (norm.equals(normBase)) {
                idx = 0;
            } else {
                String suffix = cell.substring(base.length()).trim();
                if (suffix.startsWith("(") && suffix.endsWith(")")) {
                    String num = suffix.substring(1, suffix.length() - 1).trim();
                    try {
                        idx = Integer.parseInt(num);
                        parenStyle = true;
                    } catch (NumberFormatException ignore) {
                        // idx 0 kalır
                    }
                } else {
                    try {
                        idx = Integer.parseInt(suffix);
                    } catch (NumberFormatException ignore) {
                        // idx 0 kalır
                    }
                }
            }

            if (idx > maxIndex) {
                maxIndex = idx;
                useParentheses = parenStyle;
            }
        }

        if (maxIndex == 0) {
            generatedName = useParentheses
                    ? base + " (1)"
                    : base + " 1";
        } else {
            generatedName = useParentheses
                    ? base + " (" + (maxIndex + 1) + ")"
                    : base + " " + (maxIndex + 1);
        }

        return generatedName;
    }

    public String getGeneratedName() {
        return generatedName;
    }

    // =============== AUTO INDEX (EDIT) =======================

    /**
     * Griddeki kayıtlar içinde base'e göre en yüksek index'li kaydı bulur.
     * - editSourceName → griddeki mevcut isim
     * - generatedName  → düzenle penceresinde kullanılacak yeni isim (index + 1)
     */
    public String prepareEditAutoIndex(String base) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        int maxIndex = 0;
        boolean useParentheses = false;
        String maxName = null;

        String normBase = normalize(base);

        for (WebElement row : rows) {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            if (tds.isEmpty()) continue;

            String cell = tds.get(0).getText().trim();
            String norm = normalize(cell);

            if (!norm.startsWith(normBase)) continue;

            int idx = 0;
            boolean parenStyle = false;

            if (norm.equals(normBase)) {
                idx = 0;
            } else {
                String suffix = cell.substring(base.length()).trim();
                if (suffix.startsWith("(") && suffix.endsWith(")")) {
                    String num = suffix.substring(1, suffix.length() - 1).trim();
                    try {
                        idx = Integer.parseInt(num);
                        parenStyle = true;
                    } catch (NumberFormatException ignore) {
                        // idx 0 kalır
                    }
                } else {
                    try {
                        idx = Integer.parseInt(suffix);
                    } catch (NumberFormatException ignore) {
                        // idx 0 kalır
                    }
                }
            }

            if (maxName == null || idx >= maxIndex) {
                maxIndex = idx;
                useParentheses = parenStyle;
                maxName = cell;
            }
        }

        if (maxName == null) {
            maxIndex = 0;
            maxName = base;
            useParentheses = true;
        }

        this.editSourceName = maxName;

        if (maxIndex == 0) {
            this.generatedName = useParentheses
                    ? base + " (1)"
                    : base + " 1";
        } else {
            this.generatedName = useParentheses
                    ? base + " (" + (maxIndex + 1) + ")"
                    : base + " " + (maxIndex + 1);
        }

        return this.generatedName;
    }

    public String getEditSourceName() {
        return editSourceName;
    }

    // =============== 3 NOKTA MENÜSÜ ===================

    private void openActionsMenuForRow(String rowName) {
        WebElement row = findRowByFirstCellEquals(rowName);
        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() < 5) {
            throw new IllegalStateException("Satırda 5. sütun bulunamadı (üç nokta menüsü).");
        }

        WebElement actionsCell = tds.get(4);
        WebElement menuButton = actionsCell.findElement(
                By.xpath(".//button[@aria-haspopup='true' or contains(@class,'e-dropdown-btn')]")
        );

        safeClick(menuButton);
    }

    /**
     * prepareEditAutoIndex sonrasında, editSourceName satırı için
     * üç nokta menüsünden "Düzenle" seçeneğini açar.
     */
    public void openEditPopupForPreparedRow() {
        if (editSourceName == null) {
            throw new IllegalStateException("Önce prepareEditAutoIndex() çağrılmalı.");
        }

        openActionsMenuForRow(editSourceName);

        By editOption = By.xpath(
                "//ul[contains(@class,'e-dropdown-menu')]//li" +
                        "[.//span[contains(@class,'e-edit')] or " +
                        ".//span[normalize-space()='Düzenle'] or " +
                        "normalize-space(.)='Düzenle']"
        );

        WebElement edit = wait.until(ExpectedConditions.elementToBeClickable(editOption));
        safeClick(edit);

        wait.until(ExpectedConditions.visibilityOfElementLocated(visitTypeDialogForm));
    }

    /**
     * prepareEditAutoIndex sonrasında, editSourceName satırının üç nokta menüsünden
     * verilen aksiyonu (Pasif Et / Aktif Et) seçer.
     */
    public void clickStatusChangeForPreparedRow(String actionText) {
        if (editSourceName == null) {
            throw new IllegalStateException("Önce prepareEditAutoIndex() çağrılmalı.");
        }

        openActionsMenuForRow(editSourceName);

        By actionOption = By.xpath(
                "//ul[contains(@class,'e-dropdown-menu')]//li" +
                        "[normalize-space(.)='" + actionText + "' or " +
                        ".//span[normalize-space()='" + actionText + "']]"
        );

        WebElement action = wait.until(ExpectedConditions.elementToBeClickable(actionOption));
        safeClick(action);
    }

    // ===================== FORM ALANLARI ====================

    public void openNewVisitTypeForm() {
        safeClick(newVisitTypeButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(visitTypeDialogForm));
    }

    private WebElement findDialogFormGroupByLabel(String labelText) {
        WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(visitTypeDialogForm));
        WebElement label = form.findElement(By.xpath(".//label[normalize-space()='" + labelText + "']"));
        return label.findElement(By.xpath("./ancestor::div[contains(@class,'e-form-group')]"));
    }

    public void setVisitTypeName(String name) {
        WebElement group = findDialogFormGroupByLabel("Ad");
        WebElement input = group.findElement(By.cssSelector("input"));
        typeInto(input, name);
    }

    public void selectArrivalType(String text) {
        selectFromDialogDropdown("Geliş Tipi", text);
    }

    public void selectSystemType(String text) {
        selectFromDialogDropdown("Sistem Türü", text);
    }

    private void selectFromDialogDropdown(String label, String text) {
        WebElement group = findDialogFormGroupByLabel(label);
        WebElement dropdown = group.findElement(By.cssSelector("span.e-input-group-icon"));

        safeClick(dropdown);

        By popupItems = By.cssSelector("div.e-ddl.e-control.e-lib.e-popup.e-popup-open ul li");
        List<WebElement> items = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(popupItems)
        );

        for (WebElement i : items) {
            if (normalize(i.getText()).equals(normalize(text))) {
                safeClick(i);
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector("div.e-ddl.e-control.e-lib.e-popup.e-popup-open")
                ));
                return;
            }
        }

        throw new NoSuchElementException("Dropdown içinde değer bulunamadı: " + text);
    }

    public void clickSaveVisitType() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Kaydet']")));
        scrollIntoView(btn);
        btn.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(visitTypeDialogForm));
        waitForGridLoaded();
    }

    // ===================== ONAY DİYALOĞU ====================

    /**
     * Pasif Et / Aktif Et sonrasında açılan onay popup’ındaki "Evet" butonuna tıklar.
     */
    public void confirmYesOnDialog() {
        By yesButton = By.xpath(
                "//div[contains(@class,'e-dlg-container') and contains(@class,'e-dlg-center-center')]" +
                        "//button[normalize-space()='Evet']"
        );

        WebElement yes = wait.until(ExpectedConditions.elementToBeClickable(yesButton));
        scrollIntoView(yes);
        yes.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(yesButton));
        waitForGridLoaded();
    }

    // ===================== DOĞRULAMALAR ====================

    public void verifyVisitTypeListed(String text) {
        waitForGridLoaded();

        List<WebElement> rows = driver.findElements(gridRows);
        List<String> seen = new ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            if (tds.isEmpty()) continue;

            String cell = tds.get(0).getText().trim();
            seen.add(cell);

            if (normalize(cell).equals(normalize(text))) {
                return;
            }
        }

        Assert.fail("Gridde '" + text + "' bulunamadı. Görünen değerler: " + seen);
    }

    /**
     * Verilen ad’a sahip satırın 4. sütunundaki durum bilgisinin
     * beklenen değer (Aktif / Pasif) olduğunu doğrular.
     */
    public void verifyStatusForRow(String name, String expectedStatus) {
        WebElement row = findRowByFirstCellEquals(name);
        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() < 4) {
            throw new IllegalStateException("Satırda 4. sütun (Durum) bulunamadı.");
        }

        String statusText = tds.get(3).getText().trim();
        Assert.assertEquals(
                statusText,
                expectedStatus,
                "Durum sütunu beklenen değerle eşleşmiyor. Beklenen: " +
                        expectedStatus + ", Gerçek: " + statusText
        );
    }
}

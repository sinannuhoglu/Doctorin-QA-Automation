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

public class AdmissionTagsPage {

    private static final Logger LOGGER = LogManager.getLogger(AdmissionTagsPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Senaryolar arasında kullanılan son etiket adı (auto-index, düzenleme, silme vs.)
    private String generatedName = null;

    public AdmissionTagsPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ==================== LOCATORS ======================

    private final By filterButton = By.cssSelector(
            "div.e-toolbar-left button.e-control.e-btn.e-lib.e-icon-btn"
    );

    private final By detailedSearchClearButton = By.xpath(
            "//button[normalize-space()='Temizle']"
    );

    private final By detailedSearchApplyButton = By.xpath(
            "//button[normalize-space()='Uygula']"
    );

    private final By firstRowActionMenuButton = By.xpath(
            "//div[contains(@class,'e-gridcontent')]//table[contains(@class,'e-table')]//tbody" +
                    "/tr[contains(@class,'e-row')][1]/td[5]" +
                    "//button[contains(@class,'e-dropdown-btn') and contains(@class,'action-cell') and contains(@class,'e-icon-btn')]"
    );

    private final By newTagButton = By.xpath(
            "//button[contains(@class,'e-btn')]" +
                    "[.//span[normalize-space()='Yeni Ekle'] or normalize-space()='Yeni Ekle']"
    );

    private final By tagDialogForm = By.cssSelector("form.e-data-form");

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

    private final By deleteConfirmYesButton = By.xpath(
            "//div[contains(@class,'e-dlg-container')]//button[normalize-space()='Evet']"
    );

    // ==================== HELPER METODLAR ===============

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
     * Genel amaçlı güvenli click – WebElement üzerinden.
     * Stale / interactable problemlerinde birkaç deneme yapar.
     */
    public void safeClick(WebElement element) {
        int maxRetries = 3;

        for (int i = 1; i <= maxRetries; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("[AdmissionTagsPage] safeClick(WebElement) -> StaleElementReference, retry {}", i);
            } catch (ElementNotInteractableException e) {
                LOGGER.warn("[AdmissionTagsPage] safeClick(WebElement) -> ElementNotInteractable, retry {}", i);
            }
        }

        // Halen tıklanamıyorsa, son denemede hatayı görebilmek adına direkt tıkla
        element.click();
    }

    /**
     * Genel amaçlı güvenli click – By locator üzerinden.
     * Her denemede elementi taze bulur.
     */
    public void safeClick(By locator) {
        int maxRetries = 3;

        for (int i = 1; i <= maxRetries; i++) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("[AdmissionTagsPage] safeClick(By) -> StaleElementReference, retry {}", i);
            } catch (ElementNotInteractableException e) {
                LOGGER.warn("[AdmissionTagsPage] safeClick(By) -> ElementNotInteractable, retry {}", i);
            }
        }

        WebElement element = driver.findElement(locator);
        element.click();
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

    /**
     * Grid alanının yüklendiğinden emin olur:
     * - Grid içeriği görünür
     * - Satırlar veya "Gösterilecek kayıt yok" satırı oluşmuş olur.
     */
    private void waitForGridLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContent));
        wait.until(d -> {
            boolean hasRows = !d.findElements(gridRows).isEmpty();
            boolean hasEmpty = !d.findElements(emptyRow).isEmpty();
            return hasRows || hasEmpty;
        });
    }

    private WebElement findInputByLabel(String labelText) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(tagDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        String forAttr = label.getAttribute("for");
        if (forAttr != null && !forAttr.isEmpty()) {
            return form.findElement(By.id(forAttr));
        }

        return label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-input-group') or contains(@class,'e-float-input')]//input")
        );
    }

    /**
     * Dialog içindeki label metnine göre dropdown açıp seçenek seçim işlemini yapar.
     */
    private void selectFromDialogDropdown(String labelText, String optionText) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(tagDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        WebElement container = label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-ddl') or contains(@class,'e-input-group')]")
        );

        WebElement dropdownIcon = container.findElement(
                By.cssSelector("span.e-input-group-icon")
        );

        safeClick(dropdownIcon);

        By popupLocator = By.cssSelector("div.e-ddl.e-control.e-lib.e-popup.e-popup-open");
        wait.until(ExpectedConditions.visibilityOfElementLocated(popupLocator));

        By optionLocator = By.xpath(
                "//div[contains(@class,'e-ddl') and contains(@class,'e-popup-open')]//ul//li[normalize-space()='" +
                        optionText + "']"
        );

        safeClick(optionLocator);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(popupLocator));
    }

    // ==================== NAVIGATION ========================

    /**
     * Etiketler sayfasına gider ve gridin yüklenmesini bekler.
     */
    public void goToTags(String url) {
        driver.get(url);

        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        pageWait.until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState"))
        );

        pageWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(newTagButton),
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

    // =============== AUTO INDEX (ŞİDDET vb.) ================

    /**
     * Grid üzerinde "Şiddet / Şiddet (1) / Şiddet (2) ..." benzeri kayıtlar varsa
     * en büyük index’i bulup +1 ile yeni etiket adını üretir.
     * Hiç kayıt yoksa veya "Gösterilecek kayıt yok" mesajı varsa base değeri kullanır.
     */
    public String generateTagAutoIndex(String base) {
        waitForGridLoaded();

        List<WebElement> emptyRows = driver.findElements(emptyRow);
        if (!emptyRows.isEmpty()) {
            String msg = emptyRows.get(0).getText();
            if (msg != null && msg.contains("Gösterilecek kayıt yok")) {
                this.generatedName = base;
                return this.generatedName;
            }
        }

        List<WebElement> rows = driver.findElements(gridRows);
        int maxIndex = -1;
        boolean anyMatch = false;

        String normBase = normalize(base);

        for (WebElement row : rows) {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            if (tds.isEmpty()) continue;

            String cellText = tds.get(0).getText().trim();
            String norm = normalize(cellText);

            if (!norm.startsWith(normBase)) {
                continue;
            }

            anyMatch = true;
            int idx = 0;

            if (norm.equals(normBase)) {
                idx = 0;
            } else {
                String suffix = cellText.substring(base.length()).trim();

                if (suffix.startsWith("(") && suffix.endsWith(")")) {
                    String num = suffix.substring(1, suffix.length() - 1).trim();
                    try {
                        idx = Integer.parseInt(num);
                    } catch (NumberFormatException ignore) {
                        idx = 0;
                    }
                } else {
                    try {
                        idx = Integer.parseInt(suffix);
                    } catch (NumberFormatException ignore) {
                        idx = 0;
                    }
                }
            }

            if (idx > maxIndex) {
                maxIndex = idx;
            }
        }

        if (!anyMatch) {
            this.generatedName = base;
        } else {
            int next = maxIndex + 1;
            if (next == 0) {
                this.generatedName = base;
            } else {
                this.generatedName = base + " (" + next + ")";
            }
        }

        return this.generatedName;
    }

    public String getGeneratedName() {
        return generatedName;
    }

    // ===================== FORM ALANLARI ====================

    public void openNewTagForm() {
        safeClick(newTagButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(tagDialogForm));
    }

    public void setTagTitle(String title) {
        WebElement input = findInputByLabel("Başlık");
        typeInto(input, title);
        this.generatedName = title;
    }

    public void setTagDescription(String description) {
        WebElement input = findInputByLabel("Açıklama");
        typeInto(input, description);
    }

    public void selectTagType(String type) {
        selectFromDialogDropdown("Tür", type);
    }

    public void selectTagColor(String color) {
        selectFromDialogDropdown("Renk", color);
    }

    public void clickSaveTag() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Kaydet']")
        ));
        scrollIntoView(btn);
        btn.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(tagDialogForm));
        waitForGridLoaded();
    }

    /**
     * Düzenle dialogundaki "Başlık" alanını okuyup sondaki index'i +1 yapar.
     */
    public String incrementTagTitleIndexInDialog() {
        WebElement input = findInputByLabel("Başlık");

        String current = input.getAttribute("value");
        if (current == null) current = "";
        current = current.trim();

        String base = current;
        int idx = 0;

        int openIdx = current.lastIndexOf('(');
        int closeIdx = current.lastIndexOf(')');

        if (openIdx != -1 && closeIdx == current.length() - 1 && openIdx < closeIdx) {
            base = current.substring(0, openIdx).trim();
            String numStr = current.substring(openIdx + 1, closeIdx).trim();
            try {
                idx = Integer.parseInt(numStr);
            } catch (NumberFormatException ignored) {
                idx = 0;
            }
        }

        int next = idx + 1;
        String newTitle;

        if (base.isEmpty()) {
            newTitle = String.valueOf(next);
        } else {
            newTitle = base + " (" + next + ")";
        }

        typeInto(input, newTitle);
        this.generatedName = newTitle;

        return newTitle;
    }

    // ================== GRID ACTION MENÜSÜ ===================

    public void openActionMenuOnRow() {
        waitForGridLoaded();
        safeClick(firstRowActionMenuButton);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.e-dropdown-popup.e-popup-open")
        ));
    }

    public void clickEditFromActionMenu() {
        By editIcon = By.cssSelector(
                "div.e-dropdown-popup.e-popup-open li span.e-menu-icon.e-icons.e-edit"
        );

        WebElement icon = wait.until(ExpectedConditions.visibilityOfElementLocated(editIcon));
        WebElement li = icon.findElement(By.xpath("./ancestor::li"));
        safeClick(li);

        wait.until(ExpectedConditions.visibilityOfElementLocated(tagDialogForm));
    }

    public void clickDeleteFromActionMenu() {
        By deleteIcon = By.cssSelector(
                "div.e-dropdown-popup.e-popup-open li span.e-menu-icon.e-icons.e-trash"
        );

        WebElement icon = wait.until(ExpectedConditions.visibilityOfElementLocated(deleteIcon));
        WebElement li = icon.findElement(By.xpath("./ancestor::li"));
        safeClick(li);

        wait.until(ExpectedConditions.visibilityOfElementLocated(deleteConfirmYesButton));
    }

    public void confirmDeleteYes() {
        WebElement yesButton = wait.until(
                ExpectedConditions.elementToBeClickable(deleteConfirmYesButton)
        );
        scrollIntoView(yesButton);
        yesButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteConfirmYesButton));
        waitForGridLoaded();
    }

    // ===================== DOĞRULAMALAR =====================

    public void verifyTagListed(String text) {
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

        Assert.fail("Gridde '" + text + "' etiket kaydı bulunamadı. Görünen değerler: " + seen);
    }

    /**
     * Verilen etiket adının gridde listelenmediğini doğrular.
     * - "Gösterilecek kayıt yok" varsa PASS
     * - Aksi halde satırlar içinde aranan değer bulunmamalıdır.
     */
    public void verifyTagNotListed(String text) {
        waitForGridLoaded();

        List<WebElement> emptyRows = driver.findElements(emptyRow);
        if (!emptyRows.isEmpty()) {
            String msg = emptyRows.get(0).getText();
            if (msg != null && msg.contains("Gösterilecek kayıt yok")) {
                return;
            }
        }

        List<WebElement> rows = driver.findElements(gridRows);
        List<String> seen = new ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            if (tds.isEmpty()) continue;

            String cell = tds.get(0).getText().trim();
            seen.add(cell);

            if (normalize(cell).equals(normalize(text))) {
                Assert.fail("Gridde silinmiş olması beklenen '" + text +
                        "' etiket kaydı hâlâ listeleniyor. Görünen değerler: " + seen);
            }
        }
    }

    /**
     * Grid üzerindeki ilk satırın 1. sütunundaki etiket adını döner.
     * Silme senaryosunda kullanılmaktadır.
     */
    public String readFirstRowTitle() {
        waitForGridLoaded();

        List<WebElement> rows = driver.findElements(gridRows);
        if (rows.isEmpty()) {
            throw new IllegalStateException("Grid üzerinde satır bulunamadı, ilk satır okunamıyor.");
        }

        WebElement firstRow = rows.get(0);
        List<WebElement> tds = firstRow.findElements(By.tagName("td"));
        if (tds.isEmpty()) {
            throw new IllegalStateException("İlk satırda sütun bulunamadı.");
        }

        String cellText = tds.get(0).getText().trim();
        this.generatedName = cellText;
        return cellText;
    }

}

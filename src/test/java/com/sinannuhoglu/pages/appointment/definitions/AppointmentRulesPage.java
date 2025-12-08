package com.sinannuhoglu.pages.appointment.definitions;

import com.sinannuhoglu.core.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Randevu > Tanımlar > Kurallar ekranı
 * URL: /appointment-service/appointment-rules
 *
 * Bu Page Object:
 *  - Kurallar gridinin yüklenmesini bekler,
 *  - Yeni kural oluşturma / düzenleme / silme akışlarını yönetir,
 *  - Detaylı Arama ve toolbar arama fonksiyonlarını kapsar,
 *  - Grid üzerinde doğrulama yardımcılarını içerir.
 */
public class AppointmentRulesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;
    private final Actions actions;

    public AppointmentRulesPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
    }

    // ==================== LOCATORS ======================

    private final By newRuleButton = By.xpath(
            "//button[contains(@class,'e-btn')]" +
                    "[.//span[normalize-space()='Yeni Ekle'] or normalize-space()='Yeni Ekle']"
    );

    private final By filterButton = By.xpath(
            "//div[contains(@class,'e-toolbar-item') and contains(@class,'e-template')" +
                    " and (@id='filter' or contains(@id,'filter'))]" +
                    "//button"
    );

    private final By detailedSearchClearButton = By.xpath("//button[normalize-space()='Temizle']");
    private final By detailedSearchApplyButton = By.xpath("//button[normalize-space()='Uygula']");

    private final By ruleDialogForm = By.cssSelector("form.e-data-form");

    private final By dialogOverlay = By.cssSelector("div.e-dlg-overlay");

    private final By toolbarSearchContainer = By.cssSelector("div.e-toolbar-item.e-template#search");

    private final By gridContent = By.cssSelector("div.e-gridcontent");
    private final By gridRows = By.cssSelector("#Grid_content_table tbody tr.e-row");
    private final By emptyRow = By.cssSelector("#Grid_content_table tbody tr.e-emptyrow");

    private final By deleteConfirmDialog = By.cssSelector("div[id^='modal-dialog'][class*='e-dlg-container']");

    // ==================== GENEL HELPERLAR ======================

    private void scrollIntoView(WebElement el) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
        } catch (Exception ignored) {
        }
    }

    /**
     * Locator ile güvenli tıklama:
     *  - Tıklanabilir olana kadar bekler,
     *  - Ortaya scroll eder,
     *  - Normal click, olmazsa JS click dener.
     */
    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(el);
        safeClick(el);
    }

    /**
     * WebElement ile güvenli tıklama.
     */
    private void safeClick(WebElement el) {
        scrollIntoView(el);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (ElementClickInterceptedException | TimeoutException e) {
            try {
                js.executeScript("arguments[0].click();", el);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Input alanına güvenli yazma:
     *  - Overlay varsa kaybolmasını bekler,
     *  - Click / JS click ile focus almaya çalışır,
     *  - Clear + sendKeys ile yeni metni yazar.
     */
    private void typeInto(WebElement el, String text) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }

        scrollIntoView(el);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            try {
                js.executeScript("arguments[0].click();", el);
            } catch (Exception ignored) {
            }
        }
        try {
            el.clear();
        } catch (Exception ignored) {
        }
        el.sendKeys(text);
    }

    /**
     * Grid içeriğinin yüklendiğini bekler:
     *  - Grid content görünür,
     *  - Satır ya da boş satır görünene kadar beklenir.
     */
    private void waitForGridLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContent));
        wait.until(d -> {
            boolean hasRows = !d.findElements(gridRows).isEmpty();
            boolean hasEmpty = !d.findElements(emptyRow).isEmpty();
            return hasRows || hasEmpty;
        });
    }

    private String getCellText(WebElement row, int cellIndexOneBased) {
        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() >= cellIndexOneBased) {
            return tds.get(cellIndexOneBased - 1).getText().trim();
        }
        return "";
    }

    /**
     * Dialog içindeki label metnine göre input'u döndürür.
     */
    private WebElement findDialogInputByLabel(String labelText) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(ruleDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        return label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')]//input")
        );
    }

    // ==================== NAVIGATION ========================

    /**
     * İlgili Kurallar sayfasına gider ve gridin yüklenmesini bekler.
     */
    public void goToAppointmentRules(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "[AppointmentRulesPage] URL null/boş. testdata.properties içinde 'appointmentRulesUrl' tanımlı mı?"
            );
        }

        driver.get(url);

        // Sayfa yüklenmesini ve gridin hazır olmasını bekle
        try {
            wait.until(d -> "complete".equals(
                    ((JavascriptExecutor) d).executeScript("return document.readyState")
            ));
        } catch (TimeoutException ignored) {
        }

        waitForGridLoaded();
    }

    // ===================== DETAYLI ARAMA ====================

    public void openDetailedSearch() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }

        WebElement filter;

        try {
            filter = wait.until(
                    ExpectedConditions.elementToBeClickable(filterButton)
            );
        } catch (TimeoutException e) {
            By altFilterButton = By.xpath(
                    "//button[@aria-label='Detaylı Arama' or @title='Detaylı Arama' " +
                            "or .//span[contains(@class,'e-icons') and contains(@class,'e-search')]]"
            );

            filter = wait.until(
                    ExpectedConditions.elementToBeClickable(altFilterButton)
            );
        }

        safeClick(filter);

        wait.until(ExpectedConditions.visibilityOfElementLocated(detailedSearchClearButton));
    }

    public void clickDetailedSearchClear() {
        safeClick(detailedSearchClearButton);
    }

    public void clickDetailedSearchApply() {
        safeClick(detailedSearchApplyButton);
        waitForGridLoaded();
    }

    // ===================== TOOLBAR ARAMA ====================

    public void searchOnToolbar(String text) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }

        WebElement container = wait.until(
                ExpectedConditions.visibilityOfElementLocated(toolbarSearchContainer)
        );
        WebElement input = container.findElement(By.cssSelector("input"));
        typeInto(input, text);
        input.sendKeys(Keys.ENTER);
        waitForGridLoaded();
    }

    // ===================== YENİ KURAL POPUP =================

    public void openNewRuleForm() {
        safeClick(newRuleButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(ruleDialogForm));
    }

    /**
     * "Randevu Kural Türü" dropdown'undan istenen değeri seçer.
     * (Takvim Kontrolü, Ara Randevu vb.)
     */
    public void selectRuleType(String ruleType) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(ruleDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='Randevu Kural Türü']")
        );

        WebElement group = label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')]")
        );

        WebElement dropdownIcon = group.findElement(By.cssSelector("span.e-input-group-icon"));
        safeClick(dropdownIcon);

        By popupItems = By.cssSelector("div.e-ddl.e-control.e-popup-open ul li");

        List<WebElement> items = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(popupItems)
        );

        String target = ruleType.trim().toLowerCase(Locale.ROOT);

        for (WebElement item : items) {
            String itemText = item.getText().trim();
            String itemLower = itemText.toLowerCase(Locale.ROOT);

            if (itemText.equals(ruleType) || itemLower.equals(target) || itemLower.contains(target)) {
                safeClick(item);
                return;
            }
        }

        throw new NoSuchElementException("Randevu Kural Türü dropdown içinde değer bulunamadı: " + ruleType);
    }

    /**
     * "Kaynak" multi-select alanından istenen kaynağı (checkbox) seçer.
     * Yeni Ekle ve Düzenle pencerelerinde aynı yapıyı kullanır.
     */
    public void selectResource(String resourceName) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(ruleDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[contains(normalize-space(),'Kaynak')]")
        );

        WebElement group = label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')]")
        );

        WebElement multiselectWrapper = group.findElement(
                By.cssSelector("div.e-multiselect")
        );
        scrollIntoView(multiselectWrapper);

        WebElement openButton = multiselectWrapper.findElement(
                By.cssSelector("span.e-input-group-icon")
        );
        safeClick(openButton);

        By popupLocator = By.cssSelector(
                "div.e-ddl.e-multi-select-list-wrapper.e-popup-open"
        );
        WebElement popup = wait.until(
                ExpectedConditions.visibilityOfElementLocated(popupLocator)
        );

        try {
            WebElement searchInput = multiselectWrapper.findElement(
                    By.cssSelector("span.e-multi-searcher input")
            );
            searchInput.clear();
            searchInput.sendKeys(resourceName);
            Thread.sleep(500);
        } catch (Exception ignored) {
        }

        List<WebElement> items = popup.findElements(By.cssSelector("ul li"));

        String target = resourceName.trim().toLowerCase(Locale.ROOT);
        boolean found = false;

        for (WebElement item : items) {
            String itemText = item.getText().trim();
            String itemLower = itemText.toLowerCase(Locale.ROOT);

            if (itemLower.contains(target)) {
                safeClick(item);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("Kaynak listesinde istenen değer bulunamadı: " + resourceName);
        }

        actions.moveToElement(label).click().perform();
    }

    /**
     * "Açıklama" alanına kural açıklamasını yazar.
     * (Yeni Ekle ve Düzenle penceresinde ortak)
     */
    public void setDescription(String description) {
        WebElement input = findDialogInputByLabel("Açıklama");
        typeInto(input, description);
    }

    /**
     * Kaydet butonuna tıklar. (Yeni Ekle & Düzenle için ortak)
     *  - Dialog overlay'in kaybolmasını,
     *  - Ardından gridin yüklenmesini bekler.
     */
    public void clickSaveRule() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//form[contains(@class,'e-data-form')]//button[normalize-space()='Kaydet']")
        ));
        safeClick(btn);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }

        waitForGridLoaded();
    }

    // ===================== GRID / AKSİYON MENÜSÜ ============

    /**
     * Verilen açıklamaya göre satırı bulup aksiyon sütunundaki
     * üç nokta (dropdown) menüsünü açar ve popup elementini döndürür.
     */
    private WebElement openActionsPopupForRowByDescription(String description) {
        waitForGridLoaded();

        String target = description.trim().toLowerCase(Locale.ROOT);
        List<WebElement> rows = driver.findElements(gridRows);

        WebElement targetRow = null;

        for (WebElement row : rows) {
            String desc = getCellText(row, 2);
            if (desc.trim().toLowerCase(Locale.ROOT).equals(target)) {
                targetRow = row;
                break;
            }
        }

        if (targetRow == null) {
            throw new NoSuchElementException(
                    "Açıklaması verilen satır gridde bulunamadı: " + description
            );
        }

        List<WebElement> tds = targetRow.findElements(By.tagName("td"));
        if (tds.size() < 3) {
            throw new IllegalStateException("Beklenen aksiyon sütunu (3. sütun) bulunamadı.");
        }

        WebElement actionCell = tds.get(2);
        scrollIntoView(actionCell);

        WebElement dropdownButton = actionCell.findElement(
                By.cssSelector("button[aria-haspopup='true']")
        );

        safeClick(dropdownButton);

        By popupLocator = By.cssSelector("div.e-dropdown-popup.e-popup-open");
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(popupLocator)
        );
    }

    /**
     * Gridde açıklaması verilen satırın üç nokta menüsünden “Düzenle”
     * seçeneğine tıklar ve düzenleme formunun açılmasını bekler.
     */
    public void openEditForRowByDescription(String description) {
        WebElement popup = openActionsPopupForRowByDescription(description);

        List<WebElement> items = popup.findElements(By.cssSelector("li.e-item"));

        for (WebElement item : items) {
            String text = item.getText().trim();

            boolean hasEditIcon = !item.findElements(By.cssSelector("span.e-edit")).isEmpty()
                    || !item.findElements(By.cssSelector("span.e-menu-icon.e-icons.e-edit")).isEmpty();

            if (text.contains("Düzenle") || hasEditIcon) {
                safeClick(item);

                wait.until(ExpectedConditions.visibilityOfElementLocated(ruleDialogForm));
                return;
            }
        }

        throw new NoSuchElementException("Üç nokta menüsünde 'Düzenle' seçeneği bulunamadı.");
    }

    /**
     * Gridde açıklaması verilen satırın üç nokta menüsünden “Sil”
     * seçeneğine tıklar (onay popup’ı açılır).
     */
    public void deleteRowByDescription(String description) {
        WebElement popup = openActionsPopupForRowByDescription(description);

        List<WebElement> items = popup.findElements(By.cssSelector("li.e-item"));

        for (WebElement item : items) {
            String text = item.getText().trim();

            boolean hasTrashIcon = !item.findElements(By.cssSelector("span.e-trash")).isEmpty()
                    || !item.findElements(By.cssSelector("span.e-menu-icon.e-icons.e-trash")).isEmpty();

            if (text.contains("Sil") || hasTrashIcon) {
                safeClick(item);
                wait.until(ExpectedConditions.visibilityOfElementLocated(deleteConfirmDialog));
                return;
            }
        }

        throw new NoSuchElementException("Üç nokta menüsünde 'Sil' seçeneği bulunamadı.");
    }

    /**
     * Sil onay penceresindeki “Evet” butonuna tıklar
     * ve silme sonrası gridin yeniden yüklendiğini doğrular.
     */
    public void confirmDeleteYes() {
        WebElement dialog = wait.until(
                ExpectedConditions.visibilityOfElementLocated(deleteConfirmDialog)
        );

        WebElement yesButton = dialog.findElement(
                By.xpath(".//button[normalize-space()='Evet']")
        );

        safeClick(yesButton);

        try {
            wait.until(ExpectedConditions.invisibilityOf(dialog));
        } catch (TimeoutException ignored) {
        }

        waitForGridLoaded();
    }

    /**
     * Grid üzerindeki satırlarda;
     * 1. sütun: Randevu Kural Tipi
     * 2. sütun: Açıklama
     * olacak şekilde, beklenen değerlerle kayıt arar.
     */
    public void verifyRuleListed(String expectedRuleType, String expectedDescription) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        List<String> seen = new ArrayList<>();

        String expectedRuleTypeNorm = expectedRuleType.trim().toLowerCase(Locale.ROOT);
        String expectedDescNorm = expectedDescription.trim().toLowerCase(Locale.ROOT);

        for (WebElement row : rows) {
            String ruleType = getCellText(row, 1);
            String description = getCellText(row, 2);

            seen.add(ruleType + " | " + description);

            if (ruleType.isEmpty() && description.isEmpty()) {
                continue;
            }

            String ruleTypeNorm = ruleType.trim().toLowerCase(Locale.ROOT);
            String descNorm = description.trim().toLowerCase(Locale.ROOT);

            boolean ruleMatches =
                    ruleTypeNorm.equals(expectedRuleTypeNorm) ||
                            ruleTypeNorm.startsWith(expectedRuleTypeNorm);

            boolean descMatches = descNorm.equals(expectedDescNorm);

            if (ruleMatches && descMatches) {
                return; // Kayıt bulundu → test başarılı
            }
        }

        Assert.fail("Gridde beklenen randevu kuralı kaydı bulunamadı. Beklenen: " +
                "[Kural Tipi: " + expectedRuleType + ", Açıklama: " + expectedDescription + "]" +
                " - Görünen kayıtlar: " + seen);
    }

    /**
     * Silme senaryosu için:
     * – Verilen açıklamaya sahip hiçbir satırın listelenmediğini
     * – Gridde boş durum mesajı olarak beklenen metnin gösterildiğini doğrular.
     */
    public void verifyRuleDeletedWithEmptyMessage(String description, String expectedMessage) {
        waitForGridLoaded();

        String target = description.trim().toLowerCase(Locale.ROOT);
        List<WebElement> rows = driver.findElements(gridRows);

        for (WebElement row : rows) {
            String desc = getCellText(row, 2);
            if (desc.trim().toLowerCase(Locale.ROOT).equals(target)) {
                Assert.fail("Silinmiş olması beklenen kayıt hâlâ gridde görünüyor. Açıklama: " + description);
            }
        }

        WebElement empty = wait.until(
                ExpectedConditions.visibilityOfElementLocated(emptyRow)
        );
        String emptyText = empty.getText().trim();

        Assert.assertTrue(
                emptyText.contains(expectedMessage),
                "Boş grid mesajı beklenen değerle eşleşmiyor. Beklenen: '" +
                        expectedMessage + "', Gerçek: '" + emptyText + "'"
        );
    }
}

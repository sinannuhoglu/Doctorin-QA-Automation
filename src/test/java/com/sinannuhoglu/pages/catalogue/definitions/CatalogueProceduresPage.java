package com.sinannuhoglu.pages.catalogue.definitions;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

/**
 * Katalog > Tanımlar > Hizmet Tanımları ekranı
 * URL: /catalog-service/procedures
 *
 * - Yeni hizmet oluşturma / düzenleme
 * - Fiyat grid işlemleri
 * - Filtreleme ve arama
 * - Durum (Aktif/Pasif) ve 3 nokta menüsü aksiyonları
 */
public class CatalogueProceduresPage {

    private static final Logger LOGGER = LogManager.getLogger(CatalogueProceduresPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private final By procedureDialogForm = By.cssSelector("form.e-data-form");
    private final By dialogOverlay = By.cssSelector("div.e-dlg-overlay");

    private final By priceGridContainer = By.cssSelector("div.e-gridcontent, div[id^='acrdn_panel']");

    private final By detailedFilterButton = By.cssSelector("button[data-testid='detailed-filter-button']");

    private final By toolbarSearchInput = By.cssSelector("input#search-input[name='search-input']");

    private final By mainGridContent = By.cssSelector("div.e-gridcontent");
    private final By mainGridRows = By.cssSelector("div.e-gridcontent tbody tr.e-row");
    private final By mainGridEmptyRow = By.cssSelector("div.e-gridcontent tbody tr.e-emptyrow");

    private final By firstRowStatusCell = By.xpath(
            "//div[contains(@class,'e-gridcontent')]//tbody/tr[1]/td[7]"
    );

    private final By firstRowActionsButton = By.xpath(
            "//div[contains(@class,'e-gridcontent')]//tbody/tr[1]/td[8]" +
                    "//button[contains(@class,'e-dropdown-btn')]"
    );

    public CatalogueProceduresPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ===================== GENEL HELPERS =======================

    private void scrollIntoView(WebElement el) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {
        }
    }

    private void safeClick(WebElement el) {
        scrollIntoView(el);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (ElementNotInteractableException e) {
            try {
                js.executeScript("arguments[0].click();", el);
            } catch (Exception ignored) {
            }
        }
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        safeClick(el);
    }

    private void waitForOverlayToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }
    }

    /**
     * Ana gridin yüklenmesini bekler:
     * - grid content görünür
     * - en az bir satır veya boş satır (empty row) oluşmuş olmalı
     */
    private void waitForMainGridLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mainGridContent));
        wait.until(d -> {
            boolean hasRows = !d.findElements(mainGridRows).isEmpty();
            boolean hasEmpty = !d.findElements(mainGridEmptyRow).isEmpty();
            return hasRows || hasEmpty;
        });
    }

    /**
     * Input/textarea alanlarına sağlam metin yazma helper’ı:
     * - Tıklar
     * - CTRL/CMD + A ile temizler
     * - DELETE ile siler
     * - Gerekirse tekrar dener
     */
    private void typeInto(WebElement el, String text) {
        scrollIntoView(el);

        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                try {
                    el.click();
                } catch (ElementClickInterceptedException e) {
                    waitForOverlayToDisappear();
                    js.executeScript("arguments[0].click();", el);
                }

                try {
                    el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                } catch (Exception ignore) {
                }
                try {
                    el.sendKeys(Keys.chord(Keys.COMMAND, "a"));
                } catch (Exception ignore) {
                }

                el.sendKeys(Keys.DELETE);

                try {
                    el.clear();
                } catch (Exception ignore) {
                }

                el.sendKeys(text);

                String current = el.getAttribute("value");
                if (text.equals(current)) {
                    return;
                }
            } catch (StaleElementReferenceException e) {
                // element yenilenmiş olabilir, döngü tekrar edecektir
            }
        }

        js.executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                el, text
        );
    }

    /**
     * Dialog içindeki label metnine göre input/textarea döndürür.
     * Birden fazla olası label değeri verilebilir (ör: "Hizmet adı", "Hizmet Adı", "Ad").
     */
    private WebElement findDialogInputByLabel(String... possibleLabels) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(procedureDialogForm)
        );

        List<WebElement> labels = form.findElements(By.tagName("label"));

        for (WebElement label : labels) {
            String actual = label.getText();
            if (actual == null) continue;

            String actualNorm = actual.trim().toLowerCase(Locale.ROOT);

            for (String expected : possibleLabels) {
                if (expected == null) continue;
                String expectedNorm = expected.trim().toLowerCase(Locale.ROOT);

                if (actualNorm.equals(expectedNorm) || actualNorm.contains(expectedNorm)) {
                    WebElement group = label.findElement(
                            By.xpath("./ancestor::div[contains(@class,'e-form-group')]")
                    );
                    return group.findElement(By.cssSelector("input, textarea"));
                }
            }
        }

        throw new NoSuchElementException(
                "Form içinde label bulunamadı. Beklenenler: " + String.join(", ", possibleLabels)
        );
    }

    /**
     * Syncfusion popup listeden text'e göre seçim yapar.
     * DOM’un yeniden render olması ihtimaline karşı stale-safe ve retry’li çalışır.
     */
    private void selectFromPopup(String valueToSelect) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String target = valueToSelect.trim();
        By popupLocator = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open[role='dialog'], " +
                        "div[id$='_popup'].e-popup-open[role='dialog']"
        );
        By optionsLocator = By.cssSelector("li.e-list-item[role='option'], li.e-list-item");

        LOGGER.info("[CatalogueProceduresPage] selectFromPopup -> '{}'", target);

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                shortWait.until(ExpectedConditions.presenceOfElementLocated(popupLocator));

                List<WebElement> options = shortWait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(optionsLocator)
                );

                StringBuilder existing = new StringBuilder();

                for (WebElement option : options) {
                    String text;
                    try {
                        text = option.getText().trim();
                    } catch (StaleElementReferenceException e) {
                        LOGGER.warn("[CatalogueProceduresPage] selectFromPopup -> option stale, attempt {}", attempt);
                        throw e;
                    }

                    if (!existing.isEmpty()) existing.append(", ");
                    existing.append(text);

                    if (text.equalsIgnoreCase(target)) {
                        js.executeScript(
                                "arguments[0].scrollIntoView({block:'nearest',inline:'start'});",
                                option
                        );
                        shortWait.until(ExpectedConditions.elementToBeClickable(option)).click();
                        LOGGER.info("[CatalogueProceduresPage] selectFromPopup -> '{}' bulundu ve tıklandı", target);
                        return;
                    }
                }

                throw new NoSuchElementException(
                        "Popup listesinde değer bulunamadı: " + valueToSelect +
                                " | Mevcut değerler: " + existing
                );

            } catch (StaleElementReferenceException e) {
                LOGGER.warn("[CatalogueProceduresPage] selectFromPopup -> StaleElementReferenceException (attempt {}/3)", attempt);
                if (attempt == 3) {
                    throw e;
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    // ===================== NAVIGATION ====================

    public void goToProcedures(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "[CatalogueProceduresPage] URL null/boş. testdata.properties içinde 'catalogueProceduresUrl' tanımlı mı?"
            );
        }

        driver.get(url);

        try {
            wait.until(d -> js.executeScript("return document.readyState").equals("complete"));
        } catch (TimeoutException ignored) {
        }

        waitForOverlayToDisappear();
        waitForMainGridLoaded();
    }

    // ===================== FORM ALANLARI =================

    /**
     * "Yeni Hizmet" veya "Yeni Ekle" butonuna tıklar ve formu açar.
     */
    public void openNewProcedureForm() {
        By newButton = By.xpath(
                "//button[contains(@class,'e-btn')]" +
                        "[.//span[normalize-space()='Yeni Hizmet'] or " +
                        ".//span[normalize-space()='Yeni Ekle'] or " +
                        "normalize-space()='Yeni Hizmet' or normalize-space()='Yeni Ekle']"
        );
        safeClick(newButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(procedureDialogForm));
        waitForOverlayToDisappear();
    }

    public void setName(String name) {
        WebElement input = findDialogInputByLabel("Hizmet adı", "Hizmet Adı", "Ad");
        typeInto(input, name);
    }

    public void selectSubType(String subType) {
        WebElement input = findDialogInputByLabel("Alt tür", "Alt Tür", "Alt türü");
        safeClick(input);
        selectFromPopup(subType);
    }

    // [KDV Oranı] dropdown’ı
    public void selectVatRate(String percentText) {
        selectPercentFromPopup("KDV Oranı", percentText);
    }

    private void selectPercentFromPopup(String labelText, String percentText) {
        String normalizedValue = percentText.replace("%", "").trim();

        WebElement formGroup = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(
                                "//div[contains(@class,'e-form-group') and " +
                                        ".//label[normalize-space()='" + labelText + "']]"
                        )
                )
        );

        WebElement arrowIcon = formGroup.findElement(
                By.cssSelector("span.e-input-group-icon.e-ddl-icon")
        );

        wait.until(ExpectedConditions.elementToBeClickable(arrowIcon));
        try {
            arrowIcon.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", arrowIcon);
        }

        String optionXpath =
                "//ul[contains(@class,'e-list-parent')]//li[contains(normalize-space(), '" +
                        normalizedValue + "')]";

        WebElement option = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(optionXpath)));

        js.executeScript("arguments[0].scrollIntoView(true);", option);
        option.click();
    }

    // ===================== FİYAT GRIDİ ====================

    private WebElement getFirstRow() {
        By rowLocator = By.cssSelector(
                "div[id^='acrdn_panel'] tbody tr:first-child, " +
                        "div.e-gridcontent tbody tr:first-child"
        );

        wait.until(ExpectedConditions.visibilityOfElementLocated(priceGridContainer));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));
    }

    public void setFirstRowPrice(String price) {
        LOGGER.info("[CatalogueProceduresPage] setFirstRowPrice -> '{}'", price);

        By firstRowPriceInputLocator = By.xpath(
                "//div[@role='dialog']" +
                        "//tbody/tr[1]/td[2]//input[contains(@class,'e-numerictextbox')]"
        );

        WebElement priceInput = wait.until(
                ExpectedConditions.elementToBeClickable(firstRowPriceInputLocator)
        );

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", priceInput);
        js.executeScript("arguments[0].focus();", priceInput);

        String currentValue = priceInput.getAttribute("value");
        if (currentValue != null && !currentValue.isEmpty()) {
            for (int i = 0; i < currentValue.length(); i++) {
                priceInput.sendKeys(Keys.BACK_SPACE);
            }
        }

        priceInput.sendKeys(price);
        priceInput.sendKeys(Keys.TAB);

        String afterValue = priceInput.getAttribute("value");
        LOGGER.info("[CatalogueProceduresPage] priceInput value after sendKeys: '{}'", afterValue);
    }

    public void selectFirstRowCurrency(String currencyCode) {
        LOGGER.info("[CatalogueProceduresPage] selectFirstRowCurrency -> '{}'", currencyCode);

        By cellLocator = By.xpath(
                "//div[@role='dialog']" +
                        "//tbody/tr[1]/td[3]"
        );

        WebElement cell = wait.until(
                ExpectedConditions.elementToBeClickable(cellLocator)
        );
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", cell);

        try {
            cell.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", cell);
        }

        List<WebElement> icons = cell.findElements(
                By.cssSelector(".e-input-group-icon, .e-ddl-icon, button")
        );
        if (!icons.isEmpty()) {
            WebElement icon = icons.get(0);
            try {
                icon.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", icon);
            }
        }

        selectFromPopup(currencyCode);
    }

    // ===================== KAYDET ====================

    public void clickSaveProcedure() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//form[contains(@class,'e-data-form')]//button[normalize-space()='Kaydet']")
                )
        );
        safeClick(btn);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(procedureDialogForm));
        } catch (TimeoutException ignored) {
        }

        waitForOverlayToDisappear();
        waitForMainGridLoaded();
    }

    public boolean isProcedureDialogClosed() {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(procedureDialogForm));
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ===================== FİLTRE & ARAMA ====================

    public void clickToolbarFilterButton() {
        LOGGER.info("[CatalogueProceduresPage] clickToolbarFilterButton");

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(detailedFilterButton)
        );
        safeClick(btn);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class,'e-dlg-container')]//button[normalize-space()='Temizle']")
                )
        );
    }

    public void clearAndApplyDetailedFilter() {
        LOGGER.info("[CatalogueProceduresPage] clearAndApplyDetailedFilter");

        By clearButton = By.xpath(
                "//div[contains(@class,'e-dlg-container')]//button[normalize-space()='Temizle']"
        );
        By applyButton = By.xpath(
                "//div[contains(@class,'e-dlg-container')]//button[normalize-space()='Uygula']"
        );

        WebElement clear = wait.until(ExpectedConditions.elementToBeClickable(clearButton));
        safeClick(clear);

        WebElement apply = wait.until(ExpectedConditions.elementToBeClickable(applyButton));
        safeClick(apply);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(applyButton));
        } catch (TimeoutException ignored) {
        }

        waitForOverlayToDisappear();
        waitForMainGridLoaded();
    }

    /**
     * Toolbar üzerindeki arama alanına Adı/Kodu değeri girer ve ENTER ile arama yapar.
     */
    public void searchByCode(String text) {
        LOGGER.info("[CatalogueProceduresPage] searchByCode (Adı/Kodu) -> '{}'", text);

        WebElement searchInput = wait.until(
                ExpectedConditions.elementToBeClickable(toolbarSearchInput)
        );

        scrollIntoView(searchInput);

        for (int i = 0; i < 2; i++) {
            try {
                searchInput.click();
                break;
            } catch (StaleElementReferenceException e) {
                searchInput = wait.until(
                        ExpectedConditions.elementToBeClickable(toolbarSearchInput)
                );
            }
        }

        try {
            searchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            searchInput.sendKeys(Keys.DELETE);
        } catch (Exception e) {
            String currentValue = searchInput.getAttribute("value");
            if (currentValue != null && !currentValue.isEmpty()) {
                for (int i = 0; i < currentValue.length(); i++) {
                    searchInput.sendKeys(Keys.BACK_SPACE);
                }
            }
        }

        searchInput.sendKeys(text);

        try {
            WebElement finalSearchInput = searchInput;
            wait.until(d -> text.equals(finalSearchInput.getAttribute("value")));
        } catch (TimeoutException ignored) {
        }

        searchInput.sendKeys(Keys.ENTER);

        By loadingSpinner = By.cssSelector("div.e-spinner-pane[role='alert'][aria-label='Loading']");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (TimeoutException ignored) {
        }

        waitForMainGridLoaded();
    }

    // ===================== GRID DOĞRULAMA ====================

    /**
     * Grid üzerinde 2. sütunda (Adı alanı) verilen değere sahip kayıt olup olmadığını kontrol eder.
     */
    public boolean isRecordWithCodeVisible(String value) {
        LOGGER.info("[CatalogueProceduresPage] isRecordWithCodeVisible (Adı sütunu) -> '{}'", value);

        waitForMainGridLoaded();

        By nameCellLocator = By.xpath(
                "//div[contains(@class,'e-gridcontent')]//tbody/tr/td[2][normalize-space()='" + value + "']"
        );

        try {
            WebElement cell = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(nameCellLocator)
            );
            scrollIntoView(cell);
            return cell.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Grid ilk satırdaki durum alanı text'ini döndürür. (7. sütun)
     */
    public String getFirstRowStatus() {
        waitForMainGridLoaded();
        WebElement statusCell = wait.until(
                ExpectedConditions.visibilityOfElementLocated(firstRowStatusCell)
        );
        scrollIntoView(statusCell);
        return statusCell.getText().trim();
    }

    /**
     * Grid ilk satırdaki durum alanının beklenen değerde olup olmadığını döndürür.
     */
    public boolean isFirstRowStatusEquals(String expectedStatus) {
        String actual = getFirstRowStatus();
        LOGGER.info("[CatalogueProceduresPage] first row status -> actual='{}', expected='{}'", actual, expectedStatus);
        return actual.equalsIgnoreCase(expectedStatus);
    }

    /**
     * İlk satır için 3 nokta (aksiyon) menüsünü açar.
     */
    public void openFirstRowActionsMenu() {
        LOGGER.info("[CatalogueProceduresPage] openFirstRowActionsMenu");

        waitForMainGridLoaded();

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(firstRowActionsButton)
        );
        safeClick(btn);

        By dropdownMenu = By.cssSelector("ul.e-dropdown-menu[role='menu']");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownMenu));
        } catch (TimeoutException ignored) {
        }
    }

    /**
     * Açık olan aksiyon menüsünden verilen metne sahip seçeneğe tıklar.
     * Örn: "Pasif Et" veya "Aktif Et" ya da "Düzenle".
     */
    public void clickFirstRowActionsMenuItem(String itemText) {
        LOGGER.info("[CatalogueProceduresPage] clickFirstRowActionsMenuItem -> '{}'", itemText);

        By optionLocator = By.xpath(
                "//ul[contains(@class,'e-dropdown-menu') and @role='menu']" +
                        "//li[@role='menuitem'][normalize-space()='" + itemText + "']"
        );

        WebElement option = wait.until(
                ExpectedConditions.elementToBeClickable(optionLocator)
        );
        safeClick(option);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(optionLocator));
        } catch (TimeoutException ignored) {
        }

        By toastLocator = By.id("toast_default");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(toastLocator));
        } catch (TimeoutException ignored) {
        }

        waitForMainGridLoaded();
    }

    /**
     * Açılan onay popup'ında "Evet" butonuna tıklar ve popup'ın kapanmasını bekler.
     * (modal-dialog-... içindeki Evet butonu)
     */
    public void confirmYesOnDialog() {
        LOGGER.info("[CatalogueProceduresPage] confirmYesOnDialog -> 'Evet' butonuna tıklanıyor");

        By yesButtonLocator = By.xpath(
                "//div[starts-with(@id,'dialog') and contains(@class,'e-dialog') and @role='dialog']" +
                        "//button[normalize-space()='Evet']"
        );

        WebElement yesBtn = wait.until(
                ExpectedConditions.elementToBeClickable(yesButtonLocator)
        );
        safeClick(yesBtn);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(yesButtonLocator));
        } catch (TimeoutException ignored) {
        }

        waitForOverlayToDisappear();

        By toastLocator = By.id("toast_default");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(toastLocator));
        } catch (TimeoutException ignored) {
        }

        waitForMainGridLoaded();
    }
}

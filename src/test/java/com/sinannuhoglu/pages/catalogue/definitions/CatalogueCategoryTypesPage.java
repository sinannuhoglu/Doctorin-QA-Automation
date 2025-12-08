package com.sinannuhoglu.pages.catalogue.definitions;

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
 * Katalog > Tanımlar > Kategori Tipleri ekranı Page Object
 */
public class CatalogueCategoryTypesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public CatalogueCategoryTypesPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        this.js = (JavascriptExecutor) driver;
    }

    // ================== LOCATORLAR ==================

    private final By newCategoryButton = By.xpath(
            "//button[contains(@class,'e-btn')]" +
                    "[.//span[normalize-space()='Yeni Ekle'] or normalize-space()='Yeni Ekle']"
    );

    private final By categoryDialogForm = By.cssSelector("form.e-data-form");
    private final By dialogOverlay = By.cssSelector("div.e-dlg-overlay");

    private final By toolbarSearchContainer = By.cssSelector("div.e-toolbar-item.e-template#search");

    private final By gridContent = By.cssSelector("div.e-gridcontent");
    private final By gridRows = By.cssSelector("#Grid_content_table tbody tr.e-row");
    private final By emptyRow = By.cssSelector("#Grid_content_table tbody tr.e-emptyrow");

    // ================== GENEL HELPER METOTLAR ==================

    private void scrollIntoView(WebElement el) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {
        }
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        safeClick(el);
    }

    /**
     * ElementClickIntercepted gibi durumlar için JS fallback ile güvenli tıklama.
     */
    private void safeClick(WebElement el) {
        scrollIntoView(el);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (ElementClickInterceptedException e) {
            try {
                js.executeScript("arguments[0].click();", el);
            } catch (Exception ignored) {
            }
        }
    }

    private void waitForOverlayToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }
    }

    /**
     * Alanı temizleyip verilen metni yazan, gerekirse JS ile değer set eden sağlam input helper’ı.
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
                // Element yenilenmiş olabilir, döngü tekrar deneyecek.
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
     * Grid’in yüklenmesini bekler:
     * - Grid content görünür
     * - En az bir satır veya boş satır (empty row) oluşmuş olmalı
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

    private WebElement findDialogInputByLabel(String labelText) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(categoryDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        WebElement group = label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')]")
        );

        List<WebElement> candidates = new ArrayList<>();
        candidates.addAll(group.findElements(By.cssSelector("input")));
        candidates.addAll(group.findElements(By.cssSelector("textarea")));

        for (WebElement c : candidates) {
            if (c.isDisplayed() && c.isEnabled()) {
                return c;
            }
        }

        throw new NoSuchElementException("Label için input/textarea bulunamadı: " + labelText);
    }

    private WebElement findDialogFormGroupByLabel(String labelText) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(categoryDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        return label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')]")
        );
    }

    // ================== NAVİGASYON ==================

    /**
     * Kategori Tipleri URL’ine gider, sayfa yüklemesini ve gridin hazır olmasını bekler.
     */
    public void goToCategoryTypes(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "[CatalogueCategoryTypesPage] URL null/boş. testdata.properties içinde 'catalogueCategoryTypesUrl' tanımlı mı?"
            );
        }

        driver.get(url);

        try {
            wait.until(d -> "complete".equals(
                    ((JavascriptExecutor) d).executeScript("return document.readyState")
            ));
        } catch (TimeoutException ignored) {
        }

        waitForOverlayToDisappear();
        waitForGridLoaded();
    }

    // ================== YENİ KATEGORİ OLUŞTURMA ==================

    public void openNewCategoryForm() {
        safeClick(newCategoryButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryDialogForm));
        waitForOverlayToDisappear();
    }

    public void setName(String name) {
        WebElement nameInput = findDialogInputByLabel("Adı");
        typeInto(nameInput, name);
    }

    public void selectServiceType(String serviceType) {
        WebElement group = findDialogFormGroupByLabel("Servis Türü");

        WebElement trigger;
        List<WebElement> icons = group.findElements(By.cssSelector("span.e-input-group-icon"));
        if (!icons.isEmpty()) {
            trigger = icons.get(0);
        } else {
            trigger = group.findElement(By.cssSelector("input[role='combobox'], input.e-dropdownlist"));
        }

        safeClick(trigger);

        By popupItems = By.cssSelector("div.e-ddl.e-control.e-popup-open ul li");

        List<WebElement> items = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(popupItems)
        );

        String target = serviceType.trim().toLowerCase(Locale.ROOT);

        for (WebElement item : items) {
            String itemText = item.getText().trim();
            String itemLower = itemText.toLowerCase(Locale.ROOT);

            if (itemText.equals(serviceType) || itemLower.equals(target) || itemLower.contains(target)) {
                safeClick(item);
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector("div.e-ddl.e-control.e-popup-open")));
                } catch (TimeoutException ignored) {
                }
                return;
            }
        }

        throw new NoSuchElementException("Servis Türü dropdown içinde değer bulunamadı: " + serviceType);
    }

    private void clearAllBranches(WebElement popup) {
        try {
            WebElement selectAllCheckbox = popup.findElement(
                    By.xpath(".//li[contains(@class,'e-list-item')][.//span[normalize-space()='Hepsini seç']]//input[@type='checkbox']")
            );
            safeClick(selectAllCheckbox);
            safeClick(selectAllCheckbox);
            return;
        } catch (NoSuchElementException ignore) {
        }

        List<WebElement> activeItems = popup.findElements(By.cssSelector("li.e-list-item.e-active"));
        for (WebElement item : activeItems) {
            try {
                safeClick(item);
            } catch (Exception ignored) {
            }
        }
    }

    public void selectBranch(String branchName) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(categoryDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='Branşlar']")
        );

        WebElement group = label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')]")
        );

        WebElement multiselectWrapper = group.findElement(
                By.cssSelector("div.e-multiselect")
        );
        scrollIntoView(multiselectWrapper);

        WebElement openTarget;
        try {
            openTarget = multiselectWrapper.findElement(
                    By.cssSelector("span.e-input-group-icon")
            );
        } catch (NoSuchElementException e) {
            openTarget = multiselectWrapper.findElement(
                    By.cssSelector("div.e-multi-select-wrapper")
            );
        }

        By popupLocator = By.cssSelector(
                "div.e-ddl.e-multi-select-list-wrapper.e-popup-open"
        );
        if (driver.findElements(popupLocator).isEmpty()) {
            safeClick(openTarget);
        }

        WebElement popup = wait.until(
                ExpectedConditions.visibilityOfElementLocated(popupLocator)
        );

        clearAllBranches(popup);

        try {
            WebElement searchInput = multiselectWrapper.findElement(
                    By.cssSelector("span.e-multi-searcher input")
            );
            searchInput.clear();
            searchInput.sendKeys(branchName);

            wait.until(d ->
                    !popup.findElements(By.cssSelector("ul li")).isEmpty()
            );
        } catch (Exception ignored) {
        }

        List<WebElement> items = popup.findElements(By.cssSelector("ul li"));

        String target = branchName.trim().toLowerCase(Locale.ROOT);
        boolean found = false;

        for (WebElement item : items) {
            String itemText = item.getText().trim();
            String itemLower = itemText.toLowerCase(Locale.ROOT);

            if (!itemLower.contains(target)) {
                continue;
            }

            scrollIntoView(item);
            safeClick(item);

            found = true;
            break;
        }

        if (!found) {
            throw new NoSuchElementException("Branş listesinde istenen değer bulunamadı: " + branchName);
        }

        // Popup dışına tıklayarak kapat
        Actions actions = new Actions(driver);
        actions.moveToElement(label).click().perform();

        try {
            wait.until(ExpectedConditions.invisibilityOf(popup));
        } catch (TimeoutException ignored) {
        }
    }

    public void setDescription(String description) {
        WebElement input = findDialogInputByLabel("Açıklama");
        typeInto(input, description);
    }

    public void clickSaveCategory() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//form[contains(@class,'e-data-form')]//button[normalize-space()='Kaydet']")
        ));
        safeClick(btn);

        waitForOverlayToDisappear();
        waitForGridLoaded();
    }

    // ================== TOOLBAR ARAMA ==================

    public void searchOnToolbar(String text) {
        waitForOverlayToDisappear();

        WebElement container = wait.until(
                ExpectedConditions.visibilityOfElementLocated(toolbarSearchContainer)
        );
        WebElement input = container.findElement(By.cssSelector("input"));

        typeInto(input, text);
        input.sendKeys(Keys.ENTER);

        waitForGridLoaded();
    }

    // ================== GRID DOĞRULAMA ==================

    public void verifyCategoryTypeListed(
            String expectedName,
            String expectedServiceType,
            String expectedBranch,
            String expectedDescription
    ) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        List<String> seen = new ArrayList<>();

        String expectedNameNorm = expectedName.trim().toLowerCase(Locale.ROOT);
        String expectedServiceTypeNorm = expectedServiceType.trim().toLowerCase(Locale.ROOT);
        String expectedBranchNorm = expectedBranch.trim().toLowerCase(Locale.ROOT);
        String expectedDescNorm = expectedDescription.trim().toLowerCase(Locale.ROOT);

        for (WebElement row : rows) {
            String name = getCellText(row, 1);
            String serviceType = getCellText(row, 2);
            String branch = getCellText(row, 3);
            String description = getCellText(row, 4);

            seen.add(name + " | " + serviceType + " | " + branch + " | " + description);

            if (name.isEmpty() && serviceType.isEmpty() && branch.isEmpty() && description.isEmpty()) {
                continue;
            }

            String nameNorm = name.trim().toLowerCase(Locale.ROOT);
            String serviceTypeNorm = serviceType.trim().toLowerCase(Locale.ROOT);
            String branchNorm = branch.trim().toLowerCase(Locale.ROOT);
            String descNorm = description.trim().toLowerCase(Locale.ROOT);

            boolean nameMatches = nameNorm.equals(expectedNameNorm);
            boolean serviceMatches = serviceTypeNorm.equals(expectedServiceTypeNorm);
            boolean branchMatches = branchNorm.equals(expectedBranchNorm);
            boolean descMatches = descNorm.equals(expectedDescNorm);

            if (nameMatches && serviceMatches && branchMatches && descMatches) {
                return;
            }
        }

        Assert.fail("Gridde beklenen kategori tipi kaydı bulunamadı. Beklenen: " +
                "[Adı: " + expectedName +
                ", Servis Türü: " + expectedServiceType +
                ", Branş: " + expectedBranch +
                ", Açıklama: " + expectedDescription + "]" +
                " - Görünen kayıtlar: " + seen);
    }

    // ================== ÜÇ NOKTA MENÜ / DÜZENLE & SİL ==================

    private WebElement openActionsPopupForRowByName(String name) {
        waitForGridLoaded();

        String target = name.trim().toLowerCase(Locale.ROOT);
        List<WebElement> rows = driver.findElements(gridRows);

        WebElement targetRow = null;

        for (WebElement row : rows) {
            String rowName = getCellText(row, 1);
            if (rowName.trim().toLowerCase(Locale.ROOT).equals(target)) {
                targetRow = row;
                break;
            }
        }

        if (targetRow == null) {
            throw new NoSuchElementException(
                    "Adı verilen kategori tipi satırı gridde bulunamadı: " + name
            );
        }

        List<WebElement> tds = targetRow.findElements(By.tagName("td"));
        if (tds.size() < 5) {
            throw new IllegalStateException("Beklenen aksiyon sütunu (5. sütun) bulunamadı.");
        }

        WebElement actionCell = tds.get(4);
        scrollIntoView(actionCell);

        WebElement dropdownButton = null;
        List<WebElement> buttons = actionCell.findElements(By.cssSelector("button"));

        for (WebElement btn : buttons) {
            String classes = btn.getAttribute("class");
            String ariaHasPopup = btn.getAttribute("aria-haspopup");
            if ("true".equalsIgnoreCase(ariaHasPopup)
                    || (classes != null && classes.contains("e-dropdown-btn"))) {
                dropdownButton = btn;
                break;
            }
        }

        if (dropdownButton == null) {
            throw new NoSuchElementException("Aksiyon hücresinde dropdown (üç nokta) butonu bulunamadı.");
        }

        safeClick(dropdownButton);

        By popupLocator = By.cssSelector("div.e-dropdown-popup.e-popup-open");
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(popupLocator)
        );
    }

    public void openEditForRowByName(String name) {
        WebElement popup = openActionsPopupForRowByName(name);

        List<WebElement> items = popup.findElements(By.cssSelector("li.e-item"));

        for (WebElement item : items) {
            String text = item.getText().trim();

            boolean hasEditIcon = !item.findElements(By.cssSelector("span.e-edit")).isEmpty()
                    || !item.findElements(By.cssSelector("span.e-menu-icon.e-icons.e-edit")).isEmpty();

            if (text.contains("Düzenle") || hasEditIcon) {
                safeClick(item);
                wait.until(ExpectedConditions.visibilityOfElementLocated(categoryDialogForm));
                waitForOverlayToDisappear();
                return;
            }
        }

        throw new NoSuchElementException("Üç nokta menüsünde 'Düzenle' seçeneği bulunamadı.");
    }

    public void clickDeleteForRowByName(String name) {
        WebElement popup = openActionsPopupForRowByName(name);

        List<WebElement> items = popup.findElements(By.cssSelector("li.e-item"));

        for (WebElement item : items) {
            String text = item.getText().trim();

            boolean hasDeleteIcon =
                    !item.findElements(By.cssSelector("span.e-menu-icon.e-icons.e-trash")).isEmpty();

            if (text.contains("Sil") || hasDeleteIcon) {
                safeClick(item);
                return;
            }
        }

        throw new NoSuchElementException("Üç nokta menüsünde 'Sil' seçeneği bulunamadı.");
    }

    public void confirmDeleteOnDialog() {
        By yesBtnLocator = By.xpath(
                "//div[contains(@id,'modal-dialog') or contains(@class,'e-dlg-container')]" +
                        "//button[normalize-space()='Evet']"
        );

        WebElement yesBtn = wait.until(
                ExpectedConditions.elementToBeClickable(yesBtnLocator)
        );

        safeClick(yesBtn);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(yesBtnLocator));
        } catch (TimeoutException ignored) {
        }

        waitForOverlayToDisappear();
        waitForGridLoaded();
    }

    public void verifyCategoryNotListedByName(String name) {
        waitForGridLoaded();

        String target = name.trim().toLowerCase(Locale.ROOT);
        List<WebElement> rows = driver.findElements(gridRows);

        for (WebElement row : rows) {
            String rowName = getCellText(row, 1);
            if (rowName.trim().toLowerCase(Locale.ROOT).equals(target)) {
                Assert.fail("Silinmiş olması beklenen kayıt hâlâ gridde görünüyor: " + rowName);
            }
        }
    }
}

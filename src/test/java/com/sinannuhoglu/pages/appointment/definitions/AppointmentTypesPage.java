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
 * Randevu > Tanımlar > Randevu Tipleri ekranı
 */
public class AppointmentTypesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AppointmentTypesPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // ==================== LOCATORS ======================

    private final By newAppointmentTypeButton = By.xpath(
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

    private final By appointmentTypeDialogForm = By.cssSelector("form.e-data-form");

    private final By dialogOverlay = By.cssSelector("div.e-dlg-overlay");

    private final By gridContent = By.cssSelector("div.e-gridcontent");
    private final By gridRows = By.cssSelector("#Grid_content_table tbody tr.e-row");
    private final By emptyRow = By.cssSelector("#Grid_content_table tbody tr.e-emptyrow");

    private final By toolbarSearchContainer = By.cssSelector("div.e-toolbar-item.e-template#search");

    private final By statusChangeConfirmYesButton = By.xpath(
            "//div[contains(@class,'e-dialog')]//button[contains(@class,'e-primary') and normalize-space()='Evet']"
    );

    // ==================== STATE FIELDS ===================

    private String autoAppointmentTypeName;

    private WebElement preparedEditRow;

    // ==================== HELPERS ===========================

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el
        );
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(el);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void typeInto(WebElement el, String text) {
        scrollIntoView(el);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
            } catch (TimeoutException ignored) {
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
        el.clear();
        el.sendKeys(text);
    }

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
     * Belirli ada sahip satırı (1. sütun) bulur.
     */
    private WebElement findRowByName(String name) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);
        List<String> seen = new ArrayList<>();

        for (WebElement row : rows) {
            String cellText = getCellText(row, 1);
            if (!cellText.isEmpty()) {
                seen.add(cellText);
            }
            if (cellText.equals(name)) {
                return row;
            }
        }

        Assert.fail("Gridde beklenen randevu tipi bulunamadı. Ad: " + name +
                " - Görünen kayıtlar: " + seen);
        return null;
    }

    /**
     * Verilen satır için üç nokta aksiyon menüsünü açar.
     */
    private void openActionMenuForRow(WebElement row) {
        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() < 5) {
            Assert.fail("Beklenen aksiyon hücresi (5. sütun) bulunamadı.");
        }

        WebElement actionCell = tds.get(4);

        WebElement dropdownButton = actionCell.findElement(
                By.cssSelector("button[aria-label='dropdownbutton'], button.e-dropdown-btn")
        );

        scrollIntoView(dropdownButton);
        dropdownButton.click();
    }

    /**
     * Açık aksiyon menüsünden istenen item'a tıklar.
     */
    private void clickActionMenuItem(String itemText) {
        By menuItemLocator = By.xpath(
                "//ul[contains(@class,'e-dropdown') or contains(@class,'e-menu') or contains(@class,'e-contextmenu')]" +
                        "//li[.//span[normalize-space()='" + itemText + "'] or normalize-space()='" + itemText + "']"
        );

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement item = shortWait.until(
                ExpectedConditions.elementToBeClickable(menuItemLocator)
        );
        scrollIntoView(item);
        item.click();
    }

    /**
     * Dialog içindeki label metnine göre input'u döndürür.
     */
    private WebElement findDialogInputByLabel(String labelText) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(appointmentTypeDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        return label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')]//input")
        );
    }

    /**
     * Dialog içindeki label metnine göre dropdown seçimi yapar.
     */
    private void selectDropdownByLabel(String labelText, String optionText) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(appointmentTypeDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        WebElement group = label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')]")
        );

        WebElement dropdownIcon = group.findElement(By.cssSelector("span.e-input-group-icon"));
        dropdownIcon.click();

        By popupItems = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open ul li"
        );
        List<WebElement> items = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(popupItems)
        );

        String target = optionText.trim().toLowerCase(Locale.ROOT);

        for (WebElement item : items) {
            String itemText = item.getText().trim();
            String itemLower = itemText.toLowerCase(Locale.ROOT);

            if (itemText.equals(optionText)) {
                item.click();
                return;
            }

            if (itemLower.equals(target)) {
                item.click();
                return;
            }

            if (itemLower.contains(target)) {
                item.click();
                return;
            }
        }

        throw new NoSuchElementException(
                "Dropdown (" + labelText + ") içinde değer bulunamadı: " + optionText
        );
    }

    // ==================== NAVIGATION ========================

    public void goToAppointmentTypes(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "[AppointmentTypesPage] URL null/boş. testdata.properties içinde 'appointmentTypesUrl' tanımlı mı?"
            );
        }

        driver.get(url);

        try {
            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState")
                    .equals("complete"));
        } catch (TimeoutException ignored) {
        }
    }

    // ================= DETAYLI ARAMA ========================

    public void openDetailedSearch() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }

        WebElement filter;

        try {
            filter = wait.until(
                    ExpectedConditions.presenceOfElementLocated(filterButton)
            );
        } catch (TimeoutException e) {
            By altFilterButton = By.xpath(
                    "//button[@aria-label='Detaylı Arama' or @title='Detaylı Arama' " +
                            "or .//span[contains(@class,'e-icons') and contains(@class,'e-search')]]"
            );

            filter = wait.until(
                    ExpectedConditions.presenceOfElementLocated(altFilterButton)
            );
        }

        scrollIntoView(filter);

        try {
            filter.click();
        } catch (ElementNotInteractableException ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", filter);
        }

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

    // =================== YENİ EKLE POPUP ====================

    public void openNewAppointmentTypeForm() {
        safeClick(newAppointmentTypeButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(appointmentTypeDialogForm));
    }

    public void setAppointmentTypeName(String name) {
        WebElement input = findDialogInputByLabel("Ad");
        typeInto(input, name);
    }

    public void selectType(String tip) {
        selectDropdownByLabel("Tip", tip);
    }

    public void selectColor(String renk) {
        selectDropdownByLabel("Renk", renk);
    }

    public void clickSaveAppointmentType() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//form[contains(@class,'e-data-form')]//button[normalize-space()='Kaydet']")
        ));
        scrollIntoView(btn);
        btn.click();

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }

        waitForGridLoaded();
    }

    // ===================== AUTO INDEX (CREATE) ==============

    /**
     * Grid’in ilk sütunundaki kayıtları dolaşır, baseName ile başlayanları sayar.
     * Hiç yoksa → baseName
     * n adet varsa → baseName + " " + n
     */
    public String generateAutoIndexName(String baseName) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        int count = 0;
        String baseLower = baseName.toLowerCase(Locale.ROOT);

        for (WebElement row : rows) {
            String cellText = getCellText(row, 1);
            if (cellText.toLowerCase(Locale.ROOT).startsWith(baseLower)) {
                count++;
            }
        }

        if (count == 0) {
            autoAppointmentTypeName = baseName;
        } else {
            autoAppointmentTypeName = baseName + " " + count;
        }
        return autoAppointmentTypeName;
    }

    public String getAutoAppointmentTypeName() {
        return autoAppointmentTypeName;
    }

    // ===================== AUTO INDEX (EDIT) =================

    /**
     * Düzenleme için hedef satırı hazırlar ve yeni auto-index ismini üretir.
     * - Grid daha önce baseName ile filtrelenmiş olmalıdır.
     * - baseName ile başlayan ilk satır düzenlenecek satır olarak seçilir.
     * - Aynı baseName ile başlayan kayıt sayısı n ise yeni isim baseName + " " + n olur.
     */
    public String prepareEditAutoIndex(String baseName) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        int countSameBase = 0;
        String baseLower = baseName.toLowerCase(Locale.ROOT);
        preparedEditRow = null;

        for (WebElement row : rows) {
            String name = getCellText(row, 1);
            String nameLower = name.toLowerCase(Locale.ROOT);

            if (nameLower.startsWith(baseLower)) {
                countSameBase++;

                if (preparedEditRow == null) {
                    preparedEditRow = row;
                }
            }
        }

        if (preparedEditRow == null) {
            Assert.fail("Düzenleme için hedef satır bulunamadı. Ad (base): " + baseName);
        }

        if (countSameBase == 0) {
            autoAppointmentTypeName = baseName;
        } else {
            autoAppointmentTypeName = baseName + " " + countSameBase;
        }

        return autoAppointmentTypeName;
    }

    /**
     * Hazırlanan satır için üç nokta menüsünden "Düzenle" seçeneğini açar.
     */
    public void openEditPopupForPreparedRow() {
        if (preparedEditRow == null) {
            Assert.fail("Düzenleme yapılacak satır hazırlanmadı (preparedEditRow null).");
        }

        List<WebElement> tds = preparedEditRow.findElements(By.tagName("td"));
        if (tds.size() < 5) {
            Assert.fail("Beklenen aksiyon hücresi (5. sütun) bulunamadı.");
        }

        WebElement actionCell = tds.get(4);

        WebElement dropdownButton = actionCell.findElement(
                By.cssSelector("button[aria-label='dropdownbutton'], button.e-dropdown-btn")
        );

        scrollIntoView(dropdownButton);
        dropdownButton.click();

        try {
            By editMenuItem = By.xpath(
                    "//ul[contains(@class,'e-dropdown') or contains(@class,'e-menu') or contains(@class,'e-contextmenu')]" +
                            "//li[.//span[normalize-space()='Düzenle'] or normalize-space()='Düzenle']"
            );

            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement editItem = shortWait.until(
                    ExpectedConditions.elementToBeClickable(editMenuItem)
            );
            editItem.click();
        } catch (TimeoutException e) {
            Actions actions = new Actions(driver);
            scrollIntoView(preparedEditRow);
            actions.doubleClick(preparedEditRow).perform();
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(appointmentTypeDialogForm));
    }

    // ===================== SON DOĞRULAMA (CREATE) ===========

    public void verifyAppointmentTypeListed(String expectedName) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);
        List<String> seen = new ArrayList<>();

        for (WebElement row : rows) {
            String name = getCellText(row, 1);
            if (!name.isEmpty()) {
                seen.add(name);
            }

            if (name.equals(expectedName)) {
                return;
            }
        }

        Assert.fail("Gridde beklenen randevu tipi bulunamadı. Beklenen: "
                + expectedName + " - Görünen kayıtlar: " + seen);
    }

    public void verifyAppointmentTypeExists(String name) {
        verifyAppointmentTypeListed(name);
    }

    // ===================== SON DOĞRULAMA (EDIT) =============

    /**
     * Ad (1. sütun), Tip (2. sütun) ve Renk (3. sütun) alanlarıyla doğrulama.
     */
    public void verifyAppointmentTypeListedWithAllFields(String expectedName,
                                                         String expectedType,
                                                         String expectedColor) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        List<String> seen = new ArrayList<>();

        String expectedTypeNorm = expectedType.trim().toLowerCase(Locale.ROOT);
        String expectedColorNorm = expectedColor.trim().toLowerCase(Locale.ROOT);

        for (WebElement row : rows) {
            String name  = getCellText(row, 1);
            String type  = getCellText(row, 2);
            String color = getCellText(row, 3);

            seen.add(name + " | " + type + " | " + color);

            if (!name.equals(expectedName)) {
                continue;
            }

            String typeNorm  = type.trim().toLowerCase(Locale.ROOT);
            String colorNorm = color.trim().toLowerCase(Locale.ROOT);

            boolean typeMatches =
                    typeNorm.equals(expectedTypeNorm) ||      // tam eşitlik
                            typeNorm.startsWith(expectedTypeNorm);    // "Kontrol Muayenesi" vs "Kontrol Muayene"

            boolean colorMatches = colorNorm.equals(expectedColorNorm);

            if (typeMatches && colorMatches) {
                return;
            }
        }

        Assert.fail("Gridde beklenen düzenlenmiş randevu tipi bulunamadı. Beklenen: " +
                "[Ad: " + expectedName + ", Tip: " + expectedType + ", Renk: " + expectedColor + "]" +
                " - Görünen kayıtlar: " + seen);
    }

    // ===================== AKTİF / PASİF İŞLEMLERİ ==========

    /**
     * Aktif/pasif senaryosu için hedef kaydı hazırlar:
     * - Grid yüklendikten sonra 1. sütundaki isimleri dolaşır,
     * - baseName ile başlayan ilk kaydı bulur,
     * - Bu kaydın gerçek adını (ör: "Sabah Seansı 4") döner.
     */
    public String prepareStatusToggleTarget(String baseName) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        String baseLower = baseName.toLowerCase(Locale.ROOT);
        List<String> seen = new ArrayList<>();
        WebElement targetRow = null;

        for (WebElement row : rows) {
            String name = getCellText(row, 1);
            if (name == null || name.trim().isEmpty()) {
                continue;
            }

            seen.add(name);

            String nameLower = name.toLowerCase(Locale.ROOT);
            if (nameLower.startsWith(baseLower)) {
                targetRow = row;
                break;
            }
        }

        if (targetRow == null) {
            Assert.fail("Aktif/pasif işlemi için '" + baseName +
                    "' ile başlayan bir randevu tipi kaydı bulunamadı. Görünen kayıtlar: " + seen);
        }

        return getCellText(targetRow, 1);
    }

    /**
     * Verilen randevu tipi kaydının durumu Aktif ise üç nokta menüsünden "Pasif Et" seçeneğini uygular.
     */
    public void changeStatusFromActiveToPassive(String name) {
        WebElement row = findRowByName(name);

        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() < 4) {
            Assert.fail("Beklenen durum sütunu (4. sütun) bulunamadı.");
        }

        String statusText = tds.get(3).getText().trim().toLowerCase(Locale.ROOT);
        if (!statusText.startsWith("aktif")) {
            Assert.fail("Kayıt Aktif durumda değil. Mevcut durum: " + tds.get(3).getText());
        }

        openActionMenuForRow(row);
        clickActionMenuItem("Pasif Et");

        waitForGridLoaded();
    }

    /**
     * Verilen randevu tipi kaydının durumu Pasif ise üç nokta menüsünden "Aktif Et" seçeneğini uygular.
     */
    public void changeStatusFromPassiveToActive(String name) {
        WebElement row = findRowByName(name);

        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() < 4) {
            Assert.fail("Beklenen durum sütunu (4. sütun) bulunamadı.");
        }

        String statusText = tds.get(3).getText().trim().toLowerCase(Locale.ROOT);
        if (!statusText.startsWith("pasif")) {
            Assert.fail("Kayıt Pasif durumda değil. Mevcut durum: " + tds.get(3).getText());
        }

        openActionMenuForRow(row);
        clickActionMenuItem("Aktif Et");

        waitForGridLoaded();
    }

    /**
     * Aktif/Pasif değişimi için açılan onay penceresindeki "Evet" butonuna tıklar.
     */
    public void clickStatusChangeConfirmYes() {
        safeClick(statusChangeConfirmYesButton);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }
        waitForGridLoaded();
    }

    /**
     * Verilen randevu tipi için 4. sütunda görünen durum bilgisini doğrular.
     */
    public void verifyStatusForAppointmentType(String name, String expectedStatus) {
        WebElement row = findRowByName(name);

        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() < 4) {
            Assert.fail("Beklenen durum sütunu (4. sütun) bulunamadı.");
        }

        String actual = tds.get(3).getText().trim();
        String actualNorm = actual.toLowerCase(Locale.ROOT);
        String expectedNorm = expectedStatus.trim().toLowerCase(Locale.ROOT);

        if (!actualNorm.startsWith(expectedNorm)) {
            Assert.fail("Durum alanı beklenen değerle uyuşmuyor. Beklenen: " +
                    expectedStatus + " - Gerçek: " + actual);
        }
    }
}

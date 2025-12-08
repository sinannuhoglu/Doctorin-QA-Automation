package com.sinannuhoglu.pages.treatment.definitions;

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
 * Tıbbi İşlemler > Tanımlar > Özel İlaçlar ekranı Page Object
 */
public class TreatmentCustomDrugsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public TreatmentCustomDrugsPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // ==================== LOCATORS ======================

    private final By newCustomDrugButton = By.xpath(
            "//button[contains(@class,'e-btn')]" +
                    "[.//span[normalize-space()='Yeni Ekle'] or normalize-space()='Yeni Ekle']"
    );

    private final By filterButton = By.cssSelector("div.e-toolbar-item.e-template#filter button");

    private final By detailedSearchClearButton = By.xpath("//button[normalize-space()='Temizle']");
    private final By detailedSearchApplyButton = By.xpath("//button[normalize-space()='Uygula']");

    private final By customDrugDialogForm = By.cssSelector("form.e-data-form");

    private final By dialogOverlay = By.cssSelector("div.e-dlg-overlay");

    private final By gridContent = By.cssSelector("div.e-gridcontent");
    private final By gridRows = By.cssSelector("#Grid_content_table tbody tr.e-row");
    private final By emptyRow = By.cssSelector("#Grid_content_table tbody tr.e-emptyrow");

    private final By toolbarSearchContainer = By.cssSelector("div.e-toolbar-item.e-template#search");

    // ==================== STATE FIELDS ===================

    private WebElement preparedEditRow;

    private WebElement preparedStatusRow;
    private String preparedStatusBaseName;
    private String preparedStatusBarcode;

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

    /**
     * Herhangi bir input alanına güvenli yazma.
     */
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
        try {
            el.clear();
        } catch (Exception ignored) {
        }
        if (text != null) {
            el.sendKeys(text);
        }
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
     * Verilen label metnine göre, aynı form-grup içindeki input elementini döner.
     * Örn: "Ad" veya "Barkod" veya "Reçete Türü".
     */
    private WebElement findDialogInputByLabel(String labelText) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(customDrugDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='" + labelText + "']")
        );

        return label.findElement(
                By.xpath("./ancestor::div[contains(@class,'e-form-group')]//input")
        );
    }

    private WebElement findRowByNameAndBarcodeOrNull(String baseName, String barcode) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);
        String baseLower = baseName.toLowerCase(Locale.ROOT);

        for (WebElement row : rows) {
            String name = getCellText(row, 1);
            String rowBarcode = getCellText(row, 2);
            if (name == null) name = "";
            if (rowBarcode == null) rowBarcode = "";

            if (name.toLowerCase(Locale.ROOT).startsWith(baseLower)
                    && rowBarcode.equals(barcode)) {
                return row;
            }
        }
        return null;
    }

    // ==================== NAVIGATION ========================

    public void goToCustomDrugs(String url) {
        driver.get(url);

        try {
            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState")
                    .equals("complete"));
        } catch (TimeoutException ignored) {
        }

        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(25));
        pageWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(newCustomDrugButton),
                ExpectedConditions.visibilityOfElementLocated(gridContent),
                ExpectedConditions.visibilityOfElementLocated(toolbarSearchContainer)
        ));
    }

    // ================= DETAYLI ARAMA ========================

    public void openDetailedSearch() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }

        WebElement filter = wait.until(
                ExpectedConditions.presenceOfElementLocated(filterButton)
        );
        scrollIntoView(filter);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(filter)).click();
        } catch (Exception e) {
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

    // =================== YENİ EKLE / DÜZENLE POPUP =========

    public void openNewCustomDrugForm() {
        safeClick(newCustomDrugButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(customDrugDialogForm));
    }

    public void setDrugName(String name) {
        WebElement input = findDialogInputByLabel("Ad");
        typeInto(input, name);
    }

    public void setBarcode(String barcode) {
        WebElement input = findDialogInputByLabel("Barkod");
        typeInto(input, barcode);
    }

    public void selectPrescriptionType(String text) {
        WebElement form = wait.until(
                ExpectedConditions.visibilityOfElementLocated(customDrugDialogForm)
        );

        WebElement label = form.findElement(
                By.xpath(".//label[normalize-space()='Reçete Türü']")
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

        for (WebElement item : items) {
            if (item.getText().trim().equals(text)) {
                item.click();
                return;
            }
        }

        throw new NoSuchElementException("Reçete Türü dropdown içinde değer bulunamadı: " + text);
    }

    public void clickSaveCustomDrug() {
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

    // ===================== AUTO INDEX =======================

    /**
     * Grid’in ilk sütunundaki kayıtları dolaşır, baseName ile başlayanları sayar.
     * Hiç yoksa → baseName
     * n adet varsa → baseName + " " + n
     */
    public String generateAutoIndexName(String baseName) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        int count = 0;
        for (WebElement row : rows) {
            String cellText = getCellText(row, 1);
            if (cellText.toLowerCase().startsWith(baseName.toLowerCase())) {
                count++;
            }
        }

        if (count == 0) {
            return baseName;
        }
        return baseName + " " + count;
    }

    // ===================== EDIT AUTO INDEX ==================

    public String prepareEditAutoIndex(String baseName, String barcode) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        int countSameBase = 0;
        String baseLower = baseName.toLowerCase(Locale.ROOT);
        preparedEditRow = null;

        for (WebElement row : rows) {
            String name = getCellText(row, 1);
            String rowBarcode = getCellText(row, 2);
            String nameLower = name.toLowerCase(Locale.ROOT);

            if (nameLower.startsWith(baseLower)) {
                countSameBase++;

                if (rowBarcode.equals(barcode) && preparedEditRow == null) {
                    preparedEditRow = row;
                }
            }
        }

        if (preparedEditRow == null) {
            Assert.fail("Düzenleme için hedef satır bulunamadı. Ad (base): "
                    + baseName + ", Barkod: " + barcode);
        }

        if (countSameBase == 0) {
            return baseName;
        }
        return baseName + " " + countSameBase;
    }

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

        try {
            wait.until(ExpectedConditions.elementToBeClickable(dropdownButton)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownButton);
        }

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

        wait.until(ExpectedConditions.visibilityOfElementLocated(customDrugDialogForm));
    }

    // ===================== SON DOĞRULAMA ====================

    public void verifyDrugListed(String expectedName, String expectedBarcode) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);
        List<String> seen = new ArrayList<>();

        for (WebElement row : rows) {
            String name = getCellText(row, 1);
            String barcode = getCellText(row, 2);
            if (!name.isEmpty()) {
                seen.add(name + " | " + barcode);
            }

            if (name.equals(expectedName) && barcode.equals(expectedBarcode)) {
                return;
            }
        }

        Assert.fail("Gridde beklenen özel ilaç bulunamadı. Beklenen: [Ad: "
                + expectedName + ", Barkod: " + expectedBarcode + "]"
                + " - Görünen kayıtlar: " + seen);
    }

    public void verifyDrugListedWithAllFields(String expectedName,
                                              String expectedBarcode,
                                              String expectedPrescriptionType) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);
        List<String> seen = new ArrayList<>();

        for (WebElement row : rows) {
            String name = getCellText(row, 1);
            String barcode = getCellText(row, 2);
            String prescriptionType = getCellText(row, 3);
            if (!name.isEmpty()) {
                seen.add(name + " | " + barcode + " | " + prescriptionType);
            }

            if (name.equals(expectedName)
                    && barcode.equals(expectedBarcode)
                    && prescriptionType.equals(expectedPrescriptionType)) {
                return;
            }
        }

        Assert.fail("Gridde beklenen düzenlenmiş özel ilaç bulunamadı. Beklenen: [Ad: "
                + expectedName + ", Barkod: " + expectedBarcode + ", Reçete Türü: "
                + expectedPrescriptionType + "] - Görünen kayıtlar: " + seen);
    }

    // ===================== DURUM (AKTİF/PASİF) ==============

    /**
     * Adı baseName ile başlayan ve barkodu barcode olan satırı bulur.
     * Bulamazsa aynı barkodla yeni kayıt oluşturup tekrar dener.
     */
    public void prepareStatusRow(String baseName, String barcode) {
        preparedStatusBaseName = baseName;
        preparedStatusBarcode = barcode;

        preparedStatusRow = findRowByNameAndBarcodeOrNull(baseName, barcode);

        if (preparedStatusRow == null) {
            String autoName = generateAutoIndexName(baseName);

            openNewCustomDrugForm();
            setDrugName(autoName);
            setBarcode(barcode);
            selectPrescriptionType("Kırmızı"); // varsayılan
            clickSaveCustomDrug();

            preparedStatusRow = findRowByNameAndBarcodeOrNull(baseName, barcode);
            if (preparedStatusRow == null) {
                Assert.fail("Durum değişikliği için hedef satır bulunamadı ya da oluşturulamadı. Ad: "
                        + baseName + ", Barkod: " + barcode);
            }
        }
    }

    /**
     * Hazırlanan satırın aksiyon (üç nokta) butonunu açar.
     */
    private void openStatusDropdownForPreparedRow() {
        if (preparedStatusRow == null) {
            Assert.fail("Durum değiştirme yapılacak satır hazırlanmadı (preparedStatusRow null).");
        }

        List<WebElement> tds = preparedStatusRow.findElements(By.tagName("td"));
        if (tds.isEmpty()) {
            Assert.fail("Durum aksiyon butonu için satırda hiç hücre bulunamadı.");
        }

        WebElement dropdownButton = tds.stream()
                .flatMap(td -> td.findElements(
                        By.cssSelector(
                                "button.action-cell, " +
                                        "button[aria-label='Bölme Düğmesi'], " +
                                        "button[id^='sfpdropdownbutton']"
                        )
                ).stream())
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Durum aksiyon butonu satırda bulunamadı. Hücre sayısı: " + tds.size())
                );

        scrollIntoView(dropdownButton);

        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        localWait.until(ExpectedConditions.elementToBeClickable(dropdownButton));

        try {
            dropdownButton.click();
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownButton);
        }
    }

    /**
     * Durum menüsünden verilen metne sahip seçeneği seçer (örn: "Pasif Et", "Aktif Et").
     * Onay (Evet) tıklaması burada yapılmaz, ayrı step'te yapılır.
     */
    public void changeStatusFromMenu(String menuText) {
        openStatusDropdownForPreparedRow();

        By menuItemLocator = By.xpath(
                "//ul[contains(@class,'e-dropdown') or contains(@class,'e-menu') or contains(@class,'e-contextmenu')]" +
                        "//li[.//span[normalize-space()='" + menuText + "'] or normalize-space()='" + menuText + "']"
        );

        WebElement menuItem = wait.until(
                ExpectedConditions.elementToBeClickable(menuItemLocator)
        );
        scrollIntoView(menuItem);
        menuItem.click();
    }

    /**
     * "Onay" popup'ındaki "Evet" butonuna tıklar, overlay kaybolana ve grid yeniden yüklenene kadar bekler.
     */
    public void confirmStatusChange() {
        By yesButton = By.xpath("//button[normalize-space()='Evet']");

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(yesButton)
        );
        scrollIntoView(btn);
        btn.click();

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }

        waitForGridLoaded();

        if (preparedStatusBaseName != null && preparedStatusBarcode != null) {
            preparedStatusRow = findRowByNameAndBarcodeOrNull(preparedStatusBaseName, preparedStatusBarcode);
        }
    }

    /**
     * Hazırlanan kaydın durum bilgisini (4. sütun) beklenen değerle karşılaştırır.
     * Örn: "Aktif" veya "Pasif".
     */
    public void verifyPreparedRowStatus(String expectedStatus) {
        if (preparedStatusBaseName == null || preparedStatusBarcode == null) {
            Assert.fail("Durum doğrulaması için kayıt bilgileri hazırlanmadı.");
        }

        waitForGridLoaded();
        WebElement row = findRowByNameAndBarcodeOrNull(preparedStatusBaseName, preparedStatusBarcode);
        if (row == null) {
            Assert.fail("Durum doğrulaması sırasında hedef satır bulunamadı. Ad: "
                    + preparedStatusBaseName + ", Barkod: " + preparedStatusBarcode);
        }

        String statusText = getCellText(row, 4);
        Assert.assertEquals(statusText, expectedStatus,
                "Hazırlanan kaydın durum değeri beklenenden farklı.");

        preparedStatusRow = row;
    }
}

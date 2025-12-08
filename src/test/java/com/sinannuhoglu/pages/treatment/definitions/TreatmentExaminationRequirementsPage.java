package com.sinannuhoglu.pages.treatment.definitions;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Tıbbi İşlemler > Tanımlar > Muayene Zorunlu Alanlar ekranı Page Object
 */
public class TreatmentExaminationRequirementsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String EXAMINATION_REQUIREMENTS_URL =
            "https://testapp.biklinik.com/treatment-service/examination-requirements";

    public TreatmentExaminationRequirementsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // =========================
    // LOCATORS
    // =========================

    private final By newButton = By.xpath(
            "//div[contains(@class,'e-toolbar-right')]//div[@id='add']//button[contains(@class,'e-btn')]"
    );

    private final By dataFormLocator = By.cssSelector("form.e-data-form");

    private final By filterButton = By.xpath(
            "//div[contains(@class,'e-toolbar')]//button[.//span[contains(@class,'e-filter')]]"
    );

    private final By filterClearButton = By.xpath(
            "//form[contains(@class,'e-data-form')]//button[normalize-space()='Temizle']"
    );

    private final By filterApplyButton = By.xpath(
            "//form[contains(@class,'e-data-form')]//button[normalize-space()='Uygula']"
    );

    private final By toolbarSearchInput = By.xpath(
            "//div[contains(@class,'e-toolbar-item') and @id='search']//input[contains(@class,'e-textbox')]"
    );

    private final By toolbarSearchIcon = By.xpath(
            "//div[contains(@class,'e-toolbar-item') and @id='search']//span[contains(@class,'e-search')]"
    );

    private final By gridContainer = By.cssSelector("div.e-grid");
    private final By gridRows = By.cssSelector("div.e-grid tbody tr");

    private final By openSingleSelectPopup =
            By.cssSelector("div.e-ddl.e-popup-open");

    private final By openMultiSelectPopup =
            By.cssSelector("div.e-multi-select-list-wrapper.e-popup-open");

    private final By actionDropdownMenu = By.cssSelector("ul.e-dropdown-menu");

    private final By deleteConfirmYesButton = By.xpath(
            "//div[contains(@class,'e-dlg-container')]//button[normalize-space()='Evet']"
    );

    // =========================
    // PUBLIC – STEP'LERİN KULLANDIĞI METOTLAR
    // =========================

    /**
     * Muayene Zorunlu Alanlar ekranına gider, sayfanın ve gridin yüklenmesini bekler.
     */
    public void goToExaminationRequirements() {
        driver.get(EXAMINATION_REQUIREMENTS_URL);

        // Document.readyState = complete olana kadar bekle (farklı ortamlar için stabilite)
        try {
            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState")
                    .equals("complete"));
        } catch (TimeoutException ignored) {
        }

        // Grid container görünür olsun (boş da olsa sayfa hazır)
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContainer));
    }

    public void openNewRequirementForm() {
        clickNew();
    }

    public void openDetailedSearch() {
        clickFilterIcon();
    }

    public void clickDetailedSearchClear() {
        clickFilterClear();
    }

    public void clickDetailedSearchApply() {
        clickFilterApply();
    }

    public void searchOnToolbar(String fieldName) {
        searchByFieldName(fieldName);
    }

    public boolean isRecordDisplayedInGrid(String alan, String sube, String departman) {
        return isRecordDisplayedInternal(alan, sube, departman);
    }

    public void openEditMenuForRecord(String alan, String sube, String departman) {
        openEditMenu(alan, sube, departman);
    }

    public void openDeleteMenuForRecord(String alan, String sube, String departman) {
        openDeleteMenu(alan, sube, departman);
    }

    // =========================
    // LOW LEVEL / HELPER METOTLAR
    // =========================

    private WebElement waitForVisibleAndEnabled(By locator) {
        return wait.until(d -> {
            List<WebElement> els = d.findElements(locator);
            for (WebElement el : els) {
                try {
                    if (el.isDisplayed() && el.isEnabled()) {
                        return el;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
            return null;
        });
    }

    private void scrollIntoCenter(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", el);
        } catch (JavascriptException ignored) {
        }
    }

    // Güvenli click – By
    private void safeClick(By locator) {
        WebElement element = waitForVisibleAndEnabled(locator);
        safeClick(element);
    }

    // Güvenli click – WebElement
    private void safeClick(WebElement element) {
        scrollIntoCenter(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Form içinde label’a göre input bul (Şube / Alan / Departmanlar).
     */
    private WebElement findInputByLabel(String labelText) {
        By locator = By.xpath(
                "//form[contains(@class,'e-data-form') and not(contains(@style,'display: none'))]" +
                        "//div[contains(@class,'e-form-group') and .//label[normalize-space()='" + labelText + "']]" +
                        "//input"
        );
        return waitForVisibleAndEnabled(locator);
    }

    // =========================
    // YENİ EKLE / KAYDET
    // =========================

    /**
     * Toolbar'daki Yeni Ekle butonuna tıklar ve form açılana kadar bekler.
     */
    public void clickNew() {
        safeClick(newButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(dataFormLocator));
    }

    /**
     * Şube dropdown'ından verilen şubeyi seçer.
     */
    public void selectBranch(String branchName) {
        WebElement input = findInputByLabel("Şube");
        safeClick(input);

        By optionLocator = By.xpath(
                "//li[contains(@class,'e-list-item') and normalize-space()='" + branchName + "']"
        );
        WebElement option = waitForVisibleAndEnabled(optionLocator);
        safeClick(option);

        closeSingleSelectDropdownIfOpen();
    }

    /**
     * Alan dropdown'ından verilen alanı seçer.
     */
    public void selectField(String fieldName) {
        WebElement input = findInputByLabel("Alan");
        safeClick(input);

        By optionLocator = By.xpath(
                "//li[contains(@class,'e-list-item') and normalize-space()='" + fieldName + "']"
        );
        WebElement option = waitForVisibleAndEnabled(optionLocator);
        safeClick(option);

        closeSingleSelectDropdownIfOpen();
    }

    /**
     * Departmanlar multi-select listesinden ilgili departmanı seçer.
     */
    public void selectDepartment(String departmentName) {
        WebElement input = findInputByLabel("Departmanlar");
        safeClick(input);

        By optionLocator = By.xpath(
                "//li[contains(@class,'e-list-item') and normalize-space()='" + departmentName + "']"
        );
        WebElement option = waitForVisibleAndEnabled(optionLocator);
        safeClick(option);

        // ESC + popup kapanmasını bekle
        input.sendKeys(Keys.ESCAPE);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(openMultiSelectPopup));
    }

    /**
     * Düzenle penceresinde departmanları günceller:
     * önce belirtilen departmanı kaldırır, sonra yenisini seçer.
     */
    public void updateDepartments(String deselectDepartment, String selectDepartment) {
        WebElement input = findInputByLabel("Departmanlar");
        safeClick(input);

        // Mevcut departman seçimini kaldır
        By deselectLocator = By.xpath(
                "//li[contains(@class,'e-list-item') and normalize-space()='" + deselectDepartment + "']"
        );
        WebElement deselectOpt = waitForVisibleAndEnabled(deselectLocator);
        safeClick(deselectOpt);

        // Yeni departmanı seç
        By selectLocator = By.xpath(
                "//li[contains(@class,'e-list-item') and normalize-space()='" + selectDepartment + "']"
        );
        WebElement selectOpt = waitForVisibleAndEnabled(selectLocator);
        safeClick(selectOpt);

        // Dropdown kapat
        input.sendKeys(Keys.ESCAPE);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(openMultiSelectPopup));
    }

    /**
     * Kaydet butonuna tıklar ve formun kapanmasını bekler.
     */
    public void clickSave() {
        closeSingleSelectDropdownIfOpen();
        closeMultiSelectDropdownIfOpen();

        By saveButton = By.xpath(
                "//form[contains(@class,'e-data-form') and not(contains(@style,'display: none'))]" +
                        "//button[normalize-space()='Kaydet']"
        );
        WebElement button = waitForVisibleAndEnabled(saveButton);

        safeClick(button);

        // Form kapanmasını bekle
        wait.until(ExpectedConditions.invisibilityOfElementLocated(dataFormLocator));
        // Sonrasında grid container görünür olsun
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContainer));
    }

    // =========================
    // DETAYLI ARAMA
    // =========================

    public void clickFilterIcon() {
        safeClick(filterButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(dataFormLocator));
    }

    public void clickFilterClear() {
        safeClick(filterClearButton);
    }

    public void clickFilterApply() {
        safeClick(filterApplyButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(dataFormLocator));
        // Filtre sonrası gridin tekrar görünmesini bekle
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContainer));
    }

    // =========================
    // TOOLBAR ARAMA
    // =========================

    public void searchByFieldName(String fieldName) {
        WebElement input = waitForVisibleAndEnabled(toolbarSearchInput);
        input.clear();
        input.sendKeys(fieldName);
        input.sendKeys(Keys.ENTER);

        // Alternatif tetikleme: ikon ile
        // WebElement icon = waitForVisibleAndEnabled(toolbarSearchIcon);
        // safeClick(icon);

        // Sonuçların yüklenmesi için grid container'ı bekle
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContainer));
    }

    // =========================
    // GRID KONTROLLERİ
    // =========================

    private boolean isRecordDisplayedInternal(String alan, String sube, String departman) {
        // Grid en az bir kez yüklenmiş olsun
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContainer));

        List<WebElement> rows = driver.findElements(gridRows);
        if (rows.isEmpty()) {
            return false;
        }

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() < 3) continue;

            String alanCell = cells.get(0).getText().trim();
            String subeCell = cells.get(1).getText().trim();
            String departmanCell = cells.get(2).getText().trim();

            if (alanCell.equals(alan)
                    && subeCell.equals(sube)
                    && departmanCell.contains(departman)) {
                return true;
            }
        }
        return false;
    }

    // =========================
    // GRID ACTION MENÜSÜ (DÜZENLE / SİL)
    // =========================

    /**
     * Verilen kriterlere uyan satırın üç nokta menüsünden "Düzenle" seçeneğini açar.
     */
    private void openEditMenu(String alan, String sube, String departman) {
        WebElement actionCell = findActionCellForRecord(alan, sube, departman);

        WebElement dropdownButton = actionCell.findElement(
                By.cssSelector("button[aria-label='dropdownbutton']")
        );
        safeClick(dropdownButton);

        WebElement menu = waitForVisibleAndEnabled(actionDropdownMenu);

        WebElement editItem = menu.findElement(
                By.xpath(".//li[.//span[contains(@class,'e-edit')]]")
        );
        safeClick(editItem);

        wait.until(ExpectedConditions.visibilityOfElementLocated(dataFormLocator));
    }

    /**
     * Verilen kriterlere uyan satırın üç nokta menüsünden "Sil" seçeneğini açar.
     * Sil onay popup'ının görünmesini bekler.
     */
    private void openDeleteMenu(String alan, String sube, String departman) {
        WebElement actionCell = findActionCellForRecord(alan, sube, departman);

        WebElement dropdownButton = actionCell.findElement(
                By.cssSelector("button[aria-label='dropdownbutton']")
        );
        safeClick(dropdownButton);

        WebElement menu = waitForVisibleAndEnabled(actionDropdownMenu);

        WebElement deleteItem = menu.findElement(
                By.xpath(".//li[.//span[contains(@class,'e-trash')]]")
        );
        safeClick(deleteItem);

        waitForVisibleAndEnabled(deleteConfirmYesButton);
    }

    /**
     * Sil onay popup'ında "Evet" butonuna tıklar ve popup'ın kapanmasını bekler.
     */
    public void confirmDelete() {
        WebElement yesBtn = waitForVisibleAndEnabled(deleteConfirmYesButton);
        safeClick(yesBtn);

        // Popup kapanana kadar bekle
        wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteConfirmYesButton));
        // Grid tekrar görünür olsun
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContainer));
    }

    /**
     * Kayıt satırının 4. sütunundaki action (üç nokta) hücresini döner.
     */
    private WebElement findActionCellForRecord(String alan, String sube, String departman) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContainer));
        List<WebElement> rows = driver.findElements(gridRows);

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() < 4) {
                continue;
            }

            String alanCell = cells.get(0).getText().trim();
            String subeCell = cells.get(1).getText().trim();
            String departmanCell = cells.get(2).getText().trim();

            if (alanCell.equals(alan)
                    && subeCell.equals(sube)
                    && departmanCell.contains(departman)) {
                return cells.get(3); // 4. sütun (üç nokta menüsü)
            }
        }

        throw new NoSuchElementException(
                "Kayıt gridde bulunamadı. Alan=" + alan +
                        ", Şube=" + sube +
                        ", Departman=" + departman
        );
    }

    // =========================
    // DROPDOWN HELPER’LARI
    // =========================

    private void closeSingleSelectDropdownIfOpen() {
        List<WebElement> openPopups = driver.findElements(openSingleSelectPopup);
        if (!openPopups.isEmpty()) {
            try {
                openPopups.get(0).sendKeys(Keys.ESCAPE);
            } catch (Exception ignored) {
            }
            wait.until(ExpectedConditions.invisibilityOfElementLocated(openSingleSelectPopup));
        }
    }

    private void closeMultiSelectDropdownIfOpen() {
        List<WebElement> openPopups = driver.findElements(openMultiSelectPopup);
        if (!openPopups.isEmpty()) {
            try {
                openPopups.get(0).sendKeys(Keys.ESCAPE);
            } catch (Exception ignored) {
            }
            wait.until(ExpectedConditions.invisibilityOfElementLocated(openMultiSelectPopup));
        }
    }
}

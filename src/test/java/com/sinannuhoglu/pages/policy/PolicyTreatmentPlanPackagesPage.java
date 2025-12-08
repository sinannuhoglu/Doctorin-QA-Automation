package com.sinannuhoglu.pages.policy;

import com.sinannuhoglu.core.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Poliçe > Tedavi Planı Paketler ekranı Page Object
 */
public class PolicyTreatmentPlanPackagesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PolicyTreatmentPlanPackagesPage() {
        this.driver = DriverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ================== LOCATORS ==================

    private final By actionMenuPopup =
            By.cssSelector("div.e-dropdown-popup.action-cell.e-popup-open[role='navigation']");

    private final By panelWrapper = By.cssSelector(
            "div.bg-white.rounded-xl.dark\\:bg-surface-800.panel__wrapper-shadow-default.panel-padding-dense"
    );

    private final By detailsButtonInToolbar = By.cssSelector(
            "div.bg-white.rounded-xl.dark\\:bg-surface-800.panel__wrapper-shadow-default.panel-padding-dense " +
                    "button.e-control.e-btn.e-lib.e-icon-btn"
    );

    private final By newAddButtonInToolbar = By.xpath(
            "//div[contains(@class,'panel__wrapper-shadow-default')]" +
                    "//button[contains(@class,'e-control') and contains(@class,'e-btn') and contains(@class,'e-primary')]" +
                    "[contains(normalize-space(.),'Yeni Ekle')]"
    );

    private final By detailsDialog = By.cssSelector("div.e-dialog[role='dialog']");

    private final By clearButtonInDialog = By.xpath(
            "//div[contains(@class,'e-dialog')]//div[contains(@class,'e-button-right')]" +
                    "//button[contains(.,'Temizle')]"
    );

    private final By applyButtonInDialog = By.xpath(
            "//div[contains(@class,'e-dialog')]//div[contains(@class,'e-button-right')]" +
                    "//button[contains(.,'Uygula')]"
    );

    private final By toolbarSearchWrapper = By.cssSelector(
            "div#search span.e-input-group.e-control-container.e-control-wrapper.e-control-wrapper.e-append, " +
                    "div#search span.e-input-group.e-control-container.e-control-wrapper.e-append"
    );

    private final By toolbarSearchInput = By.cssSelector(
            "div#search input.e-control.e-textbox"
    );

    private final By toolbarSearchIcon = By.cssSelector(
            "div#search span.e-input-group-icon.e-icons.e-search"
    );

    // Grid
    private final By gridRoot = By.id("Grid");

    private final By gridContent = By.cssSelector(
            "#Grid div.e-gridcontent"
    );

    private final By gridRows = By.cssSelector(
            "#Grid div.e-gridcontent table tbody tr[role='row']:not(.e-emptyrow)"
    );

    private final By emptyRowCell = By.cssSelector(
            "#Grid div.e-gridcontent table tbody tr.e-emptyrow td"
    );

    private final By dropdownMenuPopup = By.cssSelector(
            "div.e-dropdown-popup.e-popup-open"
    );

    private final By editDialogRoot = By.cssSelector(
            "div.e-dlg-container[id^='modal-dialog']"
    );

    private final By deleteDialogRoot = By.cssSelector(
            "div.e-dlg-container[id^='dialog-']"
    );

    private final By policyMainMenu = By.xpath(
            "//span[contains(@class,'menu-text') and contains(normalize-space(.),'Poliçe')]"
    );

    private final By treatmentPlanPackagesMenuItem = By.xpath(
            "//a[contains(@href,'treatment-plan-packages') or contains(normalize-space(.),'Tedavi Planı Paketler')]"
    );

    // ================== ORTAK YARDIMCI METOTLAR ==================

    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignore) {
        }
    }

    private void waitForGridLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridRoot));
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContent));

        wait.until(d -> {
            try {
                List<WebElement> rows = d.findElements(gridRows);
                List<WebElement> empties = d.findElements(
                        By.cssSelector("#Grid div.e-gridcontent table tbody tr.e-emptyrow")
                );

                int rowCount = rows.size();
                int emptyCount = empties.size();

                System.out.println("[PolicyPage] Grid state - rows: " + rowCount +
                        ", emptyRows: " + emptyCount);

                return rowCount > 0 || emptyCount > 0;
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
    }

    private void safeClick(By locator) {
        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                scrollIntoView(el);
                el.click();
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println("[PolicyPage] safeClick() -> StaleElementReferenceException, retry: " + (i + 1));
            } catch (ElementClickInterceptedException e) {
                System.out.println("[PolicyPage] safeClick() -> ElementClickInterceptedException, JS click denenecek");
                try {
                    WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    scrollIntoView(el);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    return;
                } catch (Exception ignored) {
                }
            }
        }

        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(el);
        el.click();
    }

    private void setInputValue(WebElement input, String text) {
        scrollIntoView(input);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(input)).click();
        } catch (ElementNotInteractableException e) {
            System.out.println("[PolicyPage] Input click ElementNotInteractable, JS focus denenecek: " + e);
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", input);
            } catch (Exception ignore) {
            }
        } catch (Exception e) {
            System.out.println("[PolicyPage] Input normal tıklanamadı, JS focus denenecek: " + e);
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", input);
            } catch (Exception ignore) {
            }
        }

        try {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        } catch (Exception ignore) {
        }

        try {
            input.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } catch (Exception ignore) {
        }

        try {
            input.sendKeys(Keys.DELETE);
        } catch (Exception ignore) {
        }

        try {
            input.clear();
        } catch (Exception ignore) {
        }

        try {
            input.sendKeys(text);
        } catch (ElementNotInteractableException e) {
            System.out.println("[PolicyPage] Input sendKeys ElementNotInteractable, JS ile value atanacak: " + e);
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1];", input, text
            );
        }

        System.out.println("[PolicyPage] Input value after set: " + input.getAttribute("value"));
    }

    // ================== NAVİGASYON ==================

    public void goToTreatmentPlanPackages(String url) {
        System.out.println("[PolicyPage] Poliçe Tedavi Planı Paketler URL'ine gidiliyor: " + url);
        driver.get(url);

        // Sayfa load (readyState)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(d -> {
                        try {
                            return "complete".equals(
                                    ((JavascriptExecutor) d).executeScript("return document.readyState")
                            );
                        } catch (Exception e) {
                            return false;
                        }
                    });
        } catch (TimeoutException ignored) {
        }

        System.out.println("[PolicyPage] Aktif URL: " + driver.getCurrentUrl());

        boolean panelLoadedByUrl = true;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(12))
                    .until(ExpectedConditions.visibilityOfElementLocated(panelWrapper));
        } catch (Exception e) {
            panelLoadedByUrl = false;
            System.out.println("[PolicyPage] Panel URL ile gelmedi, sol menü üzerinden açılacak. Sebep: " + e);
        }

        if (!panelLoadedByUrl) {
            openTreatmentPlanPackagesFromMenu();
        }

        waitForGridLoaded();
    }

    private void openTreatmentPlanPackagesFromMenu() {
        WebElement policyMenu = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(policyMainMenu));
        scrollIntoView(policyMenu);
        policyMenu.click();

        WebElement tpMenuItem = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(treatmentPlanPackagesMenuItem));
        scrollIntoView(tpMenuItem);
        tpMenuItem.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(panelWrapper));

        System.out.println("[PolicyPage] Poliçe Tedavi Planı Paketler sayfası menü üzerinden açıldı. Aktif URL: "
                + driver.getCurrentUrl());
    }

    // ================== DETAYLI ARAMA ==================

    public void openDetailsPopup() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(panelWrapper));

        List<WebElement> buttons = driver.findElements(detailsButtonInToolbar);
        System.out.println("[PolicyPage] Toolbar icon button count: " + buttons.size());
        Assert.assertTrue(
                !buttons.isEmpty(),
                "[PolicyPage] Toolbar içinde detaylı arama (filter) butonu bulunamadı."
        );

        WebElement detailsButton = buttons.get(buttons.size() - 1);
        scrollIntoView(detailsButton);
        System.out.println("[PolicyPage] Detaylı arama butonu tıklanıyor...");

        try {
            wait.until(ExpectedConditions.elementToBeClickable(detailsButton)).click();
        } catch (Exception ex) {
            System.out.println("[PolicyPage] Detaylı arama butonu normal tıklanamadı, JS click denenecek: " + ex);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", detailsButton);
        }

        System.out.println("[PolicyPage] Dialog bekleniyor...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(detailsDialog));
        System.out.println("[PolicyPage] Detaylı arama dialogu göründü.");
    }

    public void clickClearInDetailsDialog() {
        WebElement clearBtn = wait.until(
                ExpectedConditions.elementToBeClickable(clearButtonInDialog)
        );
        scrollIntoView(clearBtn);
        clearBtn.click();
    }

    public void clickApplyInDetailsDialog() {
        WebElement applyBtn = wait.until(
                ExpectedConditions.elementToBeClickable(applyButtonInDialog)
        );
        scrollIntoView(applyBtn);
        applyBtn.click();

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(detailsDialog));
        } catch (TimeoutException ignored) {
        }
        waitForGridLoaded();
    }

    // ================== ARAMA & LİSTE KONTROL ==================

    public void searchTreatmentPlanPackageAllowEmpty(String text) {
        waitForGridLoaded();

        safeClick(toolbarSearchWrapper);

        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(toolbarSearchInput)
        );

        setInputValue(input, text);

        System.out.println("[PolicyPage] (AllowEmpty) Search input value: " +
                input.getAttribute("value"));

        safeClick(toolbarSearchIcon);

        new WebDriverWait(driver, Duration.ofSeconds(12)).until(d -> {
            try {
                List<WebElement> rows = d.findElements(gridRows);
                List<WebElement> emptyCells = d.findElements(emptyRowCell);

                System.out.println("[PolicyPage] (AllowEmpty) rows: " + rows.size() +
                        ", emptyCells: " + emptyCells.size());

                if (!rows.isEmpty()) {
                    WebElement firstRow = rows.get(0);
                    List<WebElement> cells = firstRow.findElements(By.tagName("td"));
                    if (!cells.isEmpty()) {
                        String firstCellText = cells.get(0).getText().trim();
                        System.out.println("[PolicyPage] (AllowEmpty) first row first cell: '" +
                                firstCellText + "'");
                    }
                    return true;
                }

                if (!emptyCells.isEmpty()) {
                    String emptyText = emptyCells.get(0).getText().trim().toLowerCase();
                    System.out.println("[PolicyPage] (AllowEmpty) empty row text: '" + emptyText + "'");
                    return emptyText.contains("gösterilecek kayıt yok");
                }

                return false;
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public void verifyListFilteredBy(String packageName) {
        waitForGridLoaded();

        List<WebElement> rows = driver.findElements(gridRows);
        List<WebElement> emptyCells = driver.findElements(emptyRowCell);

        String expectedLower = packageName.toLowerCase().trim();

        if (rows.isEmpty()) {
            Assert.assertFalse(
                    emptyCells.isEmpty(),
                    "[PolicyPage] Gridde satır yok ancak 'Gösterilecek kayıt yok' satırı bulunamadı."
            );

            String emptyText = emptyCells.get(0).getText().trim().toLowerCase();
            System.out.println("[PolicyPage] verifyListFilteredBy - empty row text: '" + emptyText + "'");

            Assert.assertTrue(
                    emptyText.contains("gösterilecek kayıt yok"),
                    "[PolicyPage] Boş liste mesajı bekleniyor ama farklı bir metin görünüyor: " + emptyText
            );

            System.out.println("[PolicyPage] Liste boş ve 'Gösterilecek kayıt yok' mesajı görünüyor.");
            return;
        }

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            Assert.assertFalse(
                    cells.isEmpty(),
                    "[PolicyPage] Satırda hiç hücre bulunamadı."
            );

            String firstCellText = cells.get(0).getText().trim();
            String firstCellLower = firstCellText.toLowerCase();

            System.out.println("[PolicyPage] verifyListFilteredBy - row first cell: '" + firstCellText + "'");

            Assert.assertEquals(
                    firstCellLower,
                    expectedLower,
                    "[PolicyPage] Filtre sonrası satır ilk hücresi beklenen ile eşleşmiyor."
            );
        }

        System.out.println("[PolicyPage] Tüm satırlar '" + packageName +
                "' değerine göre doğru şekilde filtrelenmiş görünüyor.");
    }

    public void verifyListEmpty() {
        waitForGridLoaded();

        List<WebElement> rows = driver.findElements(gridRows);
        List<WebElement> emptyCells = driver.findElements(emptyRowCell);

        Assert.assertTrue(
                rows.isEmpty(),
                "[PolicyPage] Liste boş beklenirken satır bulundu."
        );

        Assert.assertFalse(
                emptyCells.isEmpty(),
                "[PolicyPage] 'Gösterilecek kayıt yok' satırı bulunamadı."
        );

        String emptyText = emptyCells.get(0).getText().trim().toLowerCase();
        System.out.println("[PolicyPage] verifyListEmpty - empty row text: '" + emptyText + "'");

        Assert.assertTrue(
                emptyText.contains("gösterilecek kayıt yok"),
                "[PolicyPage] Boş liste mesajı bekleniyor ama farklı bir metin görünüyor: " + emptyText
        );
    }

    /**
     * Grid’de mevcut kayıtları kontrol ederek, verilen baz ad üzerinden benzersiz
     * bir paket adı üretir.
     */
    public String generateUniquePackageName(String baseName) {
        waitForGridLoaded();

        List<WebElement> rows = driver.findElements(gridRows);

        String trimmedBase = baseName.trim();
        String regex = "^" + Pattern.quote(trimmedBase) + "(?:\\s+(\\d+))?$";
        Pattern pattern = Pattern.compile(regex);

        boolean baseExists = false;
        int maxSuffix = 0;

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.isEmpty()) continue;

            String firstCellText = cells.get(0).getText().trim();
            Matcher m = pattern.matcher(firstCellText);

            if (m.matches()) {
                if (m.group(1) == null) {
                    baseExists = true;
                } else {
                    try {
                        int suffix = Integer.parseInt(m.group(1));
                        if (suffix > maxSuffix) {
                            maxSuffix = suffix;
                        }
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
        }

        if (!baseExists && maxSuffix == 0) {
            System.out.println("[PolicyPage] Baz ad için kayıt yok, '" + trimmedBase + "' kullanılacak.");
            return trimmedBase;
        }

        String newName = trimmedBase + " " + (maxSuffix + 1);
        System.out.println("[PolicyPage] Baz ad için maxSuffix=" + maxSuffix +
                ", yeni ad='" + newName + "'");
        return newName;
    }

    // ================== ÜÇ NOKTA MENÜ & AKSİYONLAR ==================

    public void openActionMenuForFirstRow() {
        waitForGridLoaded();

        List<WebElement> rows = driver.findElements(gridRows);
        List<WebElement> emptyCells = driver.findElements(emptyRowCell);

        if (rows.isEmpty()) {
            String emptyText = emptyCells.isEmpty()
                    ? ""
                    : emptyCells.get(0).getText().trim();

            System.out.println("[PolicyPage] Aksiyon menüsü için satır bulunamadı. Grid boş. Mesaj='" + emptyText + "'");

            if (!emptyCells.isEmpty() &&
                    emptyText.toLowerCase().contains("gösterilecek kayıt yok")) {

                throw new AssertionError(
                        "[PolicyPage] Aksiyon menüsü açılamadı: grid 'Gösterilecek kayıt yok' durumunda. " +
                                "Düzenleme / silme adımlarından önce ilgili paket kaydının test verisinde oluşturulduğundan " +
                                "ve arama sonucunda listelendiğinden emin olun."
                );
            }

            throw new AssertionError(
                    "[PolicyPage] Aksiyon menüsü için satır bulunamadı; grid boş fakat 'Gösterilecek kayıt yok' mesajı görünmüyor."
            );
        }

        WebElement row = rows.get(0);
        List<WebElement> cells = row.findElements(By.tagName("td"));
        Assert.assertTrue(
                cells.size() >= 6,
                "[PolicyPage] Aksiyon hücresi (6. sütun) bulunamadı."
        );

        WebElement actionCell = cells.get(5);
        WebElement menuButton = actionCell.findElement(
                By.cssSelector("button.e-dropdown-btn")
        );

        scrollIntoView(menuButton);
        System.out.println("[PolicyPage] Üç nokta (aksiyon) butonuna tıklanıyor...");

        try {
            wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
        } catch (Exception ex) {
            System.out.println("[PolicyPage] Aksiyon butonu normal tıklanamadı, JS click denenecek: " + ex);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuButton);
        }

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownMenuPopup));
            System.out.println("[PolicyPage] Aksiyon menüsü açıldı.");
        } catch (TimeoutException e) {
            System.out.println("[PolicyPage] Aksiyon menüsü beklenen sürede açılmadı: " + e.getMessage());
            throw e;
        }
    }

    public void clickActionMenuOption(String optionText) {
        System.out.println("[PolicyPage] Açılır menüden '" + optionText + "' seçeneği seçiliyor...");

        WebElement popup = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(actionMenuPopup));

        System.out.println("[PolicyPage] Aksiyon popup bulundu. id=" + popup.getAttribute("id"));

        List<WebElement> items = popup.findElements(
                By.cssSelector("ul.e-dropdown-menu li.e-item")
        );

        for (WebElement item : items) {
            String text = item.getText().trim();
            System.out.println("[PolicyPage] Menü item text = '" + text + "'");

            if (text.equalsIgnoreCase(optionText.trim())) {
                scrollIntoView(item);
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(item));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);

                System.out.println("[PolicyPage] Menü opsiyonu tıklandı: '" + text + "'");
                return;
            }
        }

        throw new NoSuchElementException(
                "Aksiyon menüsünde '" + optionText + "' seçeneği bulunamadı."
        );
    }

    // ================== TOOLBAR "YENİ EKLE" ==================

    public void clickToolbarYeniEkle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(panelWrapper));

        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(newAddButtonInToolbar)
        );
        scrollIntoView(button);

        System.out.println("[PolicyPage] Toolbar 'Yeni Ekle' butonuna tıklanıyor...");
        button.click();

        System.out.println("[PolicyPage] Yeni kayıt dialogu bekleniyor...");
        getEditDialog();
    }

    // ================== DÜZENLEME / YENİ KAYIT PENCERESİ ==================

    private WebElement getEditDialog() {
        WebElement dialog = wait.until(
                ExpectedConditions.visibilityOfElementLocated(editDialogRoot)
        );
        System.out.println("[PolicyPage] Düzenleme/Yeni kayıt dialogu bulundu. id=" + dialog.getAttribute("id"));
        return dialog;
    }

    public void fillEditDialogAd(String value) {
        WebElement dialog = getEditDialog();

        WebElement adInput = dialog.findElement(By.xpath(
                ".//div[contains(@class,'e-form-group')]" +
                        "[.//label[contains(normalize-space(.),'Ad')]]" +
                        "//input[contains(@class,'e-textbox')]"
        ));

        setInputValue(adInput, value);
    }

    // ----------- FATURA YÖNETİMİ -----------

    public void openFaturaYonetimiDropdown() {
        System.out.println("[PolicyPage] Düzenle/Yeni dialogunda 'Fatura Yöntemi' dropdownu açılıyor...");

        WebElement dialog = getEditDialog();

        WebElement formGroup = dialog.findElement(By.xpath(
                ".//div[contains(@class,'e-form-group')]" +
                        "[.//label[normalize-space(.)='Fatura Yöntemi']]"
        ));

        WebElement icon = formGroup.findElement(By.cssSelector(
                "span.e-input-group-icon.e-ddl-icon"
        ));

        scrollIntoView(icon);
        wait.until(ExpectedConditions.elementToBeClickable(icon)).click();
    }

    public void selectFaturaYonetimi(String option) {
        System.out.println("[PolicyPage] 'Fatura Yöntemi' için '" + option + "' seçeneği seçiliyor...");

        By popupLocator = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open[aria-label='dropdownlist']"
        );

        WebElement popup = wait.until(
                ExpectedConditions.visibilityOfElementLocated(popupLocator)
        );

        WebElement item = popup.findElement(By.xpath(
                ".//li[normalize-space(.)='" + option + "']"
        ));

        scrollIntoView(item);
        wait.until(ExpectedConditions.elementToBeClickable(item)).click();

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(popupLocator));
            System.out.println("[PolicyPage] Fatura Yöntemi dropdown'u kapandı.");
        } catch (TimeoutException e) {
            System.out.println("[PolicyPage] Fatura Yöntemi popup beklenen sürede kapanmadı, devam ediliyor: " + e.getMessage());
        }

        System.out.println("[PolicyPage] Fatura Yöntemi seçildi: " + option);
    }

    // ----------- PARA BİRİMİ -----------

    public void openParaBirimiDropdown() {
        System.out.println("[PolicyPage] Düzenle/Yeni dialogunda 'Para Birimi' dropdownu açılıyor...");

        WebElement dialog = getEditDialog();

        WebElement formGroup = dialog.findElement(By.xpath(
                ".//div[contains(@class,'e-form-group')]" +
                        "[.//label[normalize-space(.)='Para Birimi']]"
        ));

        WebElement combo = formGroup.findElement(By.cssSelector("span.e-ddl"));

        scrollIntoView(combo);
        wait.until(ExpectedConditions.elementToBeClickable(combo)).click();
    }

    public void selectParaBirimi(String option) {
        System.out.println("[PolicyPage] Para Birimi seçeneği seçiliyor: " + option);

        By itemLocator = By.xpath(
                "//li[contains(@class,'e-list-item')][normalize-space(.)='" + option + "']"
        );

        WebElement item = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(itemLocator));

        scrollIntoView(item);
        wait.until(ExpectedConditions.elementToBeClickable(item)).click();

        System.out.println("[PolicyPage] Para Birimi seçildi: " + option);
    }

    // ----------- SERVİS ÖĞESİ -----------

    public void openServiceItemDropdown() {
        System.out.println("[PolicyPage] Yeni kayıt dialogunda Servis Öğesi dropdownu açılıyor...");

        WebElement dialog = getEditDialog();

        WebElement wrapper = dialog.findElement(By.xpath(
                ".//div[contains(@class,'col-span-2')]//div[contains(@class,'search-dropdown-wrapper')]"
        ));

        WebElement form = wrapper.findElement(By.tagName("form"));
        WebElement input = form.findElement(By.tagName("input"));

        scrollIntoView(input);
        input.click();
        input.sendKeys(Keys.ENTER);
    }

    public void selectServiceItem(String serviceName) {
        System.out.println("[PolicyPage] Servis Öğesi seçiliyor: " + serviceName);

        By containerLocator = By.id("00000000-0000-0000-0000-000000000001");

        WebElement container = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(containerLocator));

        List<WebElement> rows = container.findElements(By.xpath(
                ".//div[contains(@class,'flex') and contains(@class,'items-center') and contains(@class,'cursor-pointer')]"
        ));

        for (WebElement row : rows) {
            List<WebElement> ps = row.findElements(By.tagName("p"));
            for (WebElement p : ps) {
                String text = p.getText().trim();
                if (text.equals(serviceName)) {
                    scrollIntoView(p);
                    wait.until(ExpectedConditions.elementToBeClickable(p)).click();
                    System.out.println("[PolicyPage] Servis Öğesi bulundu ve seçildi: " + text);
                    return;
                }
            }
        }

        throw new NoSuchElementException("Servis Öğesi listesinde '" + serviceName + "' bulunamadı.");
    }

    // ================== KAYDET ==================

    public void clickEditSave() {
        WebElement dialog = getEditDialog();

        WebElement saveBtn = dialog.findElement(By.xpath(
                ".//div[contains(@class,'e-button-right')]" +
                        "//button[contains(@class,'e-primary') and normalize-space(text())='Kaydet']"
        ));

        scrollIntoView(saveBtn);
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();

        try {
            wait.until(ExpectedConditions.invisibilityOf(dialog));
        } catch (TimeoutException | StaleElementReferenceException ignored) {
        }

        waitForGridLoaded();
    }

    // ================== SİLME ONAY PENCERESİ ==================

    public void confirmDelete() {
        System.out.println("[PolicyPage] Silme onay dialogu bekleniyor...");

        By primaryLocator = deleteDialogRoot;
        By fallbackLocator = By.cssSelector(
                "div.e-dialog[role='dialog'], div.e-dialog[role='alertdialog']"
        );

        WebElement dialog;

        try {
            dialog = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfElementLocated(primaryLocator));
            System.out.println("[PolicyPage] Silme onay dialogu (primary locator) bulundu.");
        } catch (TimeoutException e) {
            System.out.println("[PolicyPage] 'dialog-' id'li container bulunamadı, alternatif dialog locator denenecek...");

            try {
                dialog = new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(fallbackLocator));
                System.out.println("[PolicyPage] Silme onay dialogu (fallback locator) bulundu.");
            } catch (TimeoutException ex) {
                System.out.println("[PolicyPage] Silme onay dialogu hiç görünmedi, kayıt muhtemelen onaysız silindi. Grid yenilenmesi beklenecek.");
                waitForGridLoaded();
                return;
            }
        }

        WebElement footer;
        try {
            footer = dialog.findElement(By.cssSelector("div.e-footer-content"));
        } catch (NoSuchElementException ex) {
            footer = driver.findElement(By.cssSelector("div.e-footer-content"));
        }

        WebElement yesBtn = footer.findElement(By.xpath(
                ".//button[contains(@class,'e-primary') and normalize-space(text())='Evet']"
        ));

        scrollIntoView(yesBtn);
        System.out.println("[PolicyPage] Silme onay dialogunda 'Evet' butonu tıklanıyor...");
        wait.until(ExpectedConditions.elementToBeClickable(yesBtn)).click();

        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOf(dialog));
        } catch (StaleElementReferenceException | TimeoutException ignore) {
        }

        waitForGridLoaded();
    }
}

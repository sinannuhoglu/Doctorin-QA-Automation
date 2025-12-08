package com.sinannuhoglu.pages.resources;

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

public class ResourcesHealthcareResourceManagementPage {

    private static final String LOG = "[ResourcesHealthcare] ";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private String lastCreatedResourceName;

    // ================== LOCATORLAR ==================

    private final By detailsButton = By.cssSelector("button.e-control.e-btn.e-lib.e-icon-btn");

    private final By fullPageOverlay = By.cssSelector(
            "div.absolute.inset-0.m-auto.grid.h-full.w-full.place-items-center.backdrop-blur-xs.z-20"
    );

    private final By detailsDialog = By.cssSelector("div.e-dlg-container.e-dlg-center-center");

    private final By clearButtonInDialog = By.xpath(
            "//div[contains(@class,'e-dlg-container') and contains(@class,'e-dlg-center-center')]//button[normalize-space()='Temizle']"
    );

    private final By applyButtonInDialog = By.xpath(
            "//div[contains(@class,'e-dlg-container') and contains(@class,'e-dlg-center-center')]//button[normalize-space()='Uygula']"
    );

    private final By searchWrapper = By.cssSelector(
            "form[method='post'] span.e-input-group.e-control-container.e-control-wrapper.e-append"
    );

    private final By searchInput = By.cssSelector(
            "input.e-control.e-textbox.e-lib.e-input[name='search-input']"
    );

    private final By searchIcon = By.cssSelector(
            "span.e-input-group-icon.e-icons.e-search"
    );

    private final By gridContent = By.cssSelector("div.e-gridcontent");

    private final By gridRows = By.cssSelector("div.e-gridcontent table.e-table tbody tr");

    private final By confirmYesButton = By.xpath(
            "//div[contains(@class,'e-dlg-container')]//button[normalize-space()='Evet']"
    );

    // ============ YENİ KAYNAK OLUŞTURMA LOCATORLARI ============

    private final By newResourceButtons = By.xpath(
            "//button[contains(normalize-space(.), 'Yeni Ekle')]"
    );

    private final By newResourceDialog = By.xpath(
            "//div[contains(@id,'dialog') and contains(@class,'e-dialog')]" +
                    "[.//form[contains(@id,'dataform')]]"
    );

    private final By dialogSaveButton = By.xpath(
            "//div[contains(@id,'dialog') and contains(@class,'e-dialog')]" +
                    "//button[@type='submit' and contains(@class,'e-primary') and normalize-space()='Kaydet']"
    );

    private final By roomDeviceMenuLink =
            By.cssSelector("a[href*='/resource-service/healthcare-resource-management']");

    private final By toastContainer = By.cssSelector("div[id^='toast_'].e-toast");

    public ResourcesHealthcareResourceManagementPage() {
        this.driver = DriverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ================== COMMON HELPERS ==================

    private void waitForGridLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContent));
        wait.until(d -> {
            List<WebElement> rows = d.findElements(gridRows);
            System.out.println(LOG + "Grid yüklendi. Satır sayısı: " + rows.size());
            return !rows.isEmpty();
        });
    }

    private void waitForToastsToDisappear() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(toastContainer));
        } catch (TimeoutException e) {
            // 10 sn içinde kaybolmazsa test akmaya devam etsin, ama logla:
            System.out.println("[ResourcesHealthcare] Toast 10 sn içinde kaybolmadı, devam ediliyor.");
        }
    }

    private void safeClick(By locator) {
        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                el.click();
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println(LOG + "Stale element, tekrar denenecek: " + locator + " (deneme " + (i + 1) + ")");
            } catch (ElementClickInterceptedException e) {
                System.out.println(LOG + "Click intercepted, JS ile tıklanıyor: " + locator);
                try {
                    WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    return;
                } catch (Exception ignored) {
                }
            }
        }
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        el.click();
    }

    private void safeClick(WebElement element) {
        for (int i = 0; i < 3; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println(LOG + "Stale element (WebElement), tekrar denenecek. Deneme: " + (i + 1));
            } catch (ElementClickInterceptedException e) {
                System.out.println(LOG + "Click intercepted (WebElement), JS ile tıklanıyor...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                return;
            }
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void setSearchInputValue(WebElement input, String text) {
        waitForToastsToDisappear();

        try {
            input.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("[ResourcesHealthcare] Input click intercepted, toast bekleniyor...");
            waitForToastsToDisappear();
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        }

        try {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        } catch (Exception ignore) {}

        try {
            input.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } catch (Exception ignore) {}

        input.sendKeys(Keys.DELETE);

        try {
            input.clear();
        } catch (Exception ignore) {}

        input.sendKeys(text);

        System.out.println("[ResourcesHealthcare] Search input value after set: " +
                input.getAttribute("value"));
    }

    // ================== NAVIGATION ==================

    public void goToHealthcareResourceManagement(String urlFromConfig) {
        System.out.println(LOG + "Oda & Cihaz Yönetimi sayfasına gidiliyor...");

        String current = driver.getCurrentUrl();
        if (current.contains("/resource-service/healthcare-resource-management")) {
            System.out.println(LOG + "Zaten Oda & Cihaz sayfasındayız.");
            waitForGridLoaded();
            return;
        }

        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            WebElement link = localWait.until(
                    ExpectedConditions.elementToBeClickable(roomDeviceMenuLink)
            );
            link.click();
            System.out.println(LOG + "Menüden Oda & Cihaz linkine tıklandı.");
        } catch (TimeoutException e) {
            System.out.println(
                    LOG + "Menüde Oda & Cihaz linki bulunamadı, URL ile açılacak: " + urlFromConfig
            );
            driver.get(urlFromConfig);
        }

        localWait.until(ExpectedConditions.urlContains(
                "/resource-service/healthcare-resource-management"
        ));

        System.out.println(LOG + "Aktif URL: " + driver.getCurrentUrl());

        waitForGridLoaded();
    }

    // ================== DETAYLI ARAMA ==================

    public void openDetailsPopup() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(fullPageOverlay));
        } catch (TimeoutException e) {
        }

        WebElement detailsBtn = wait.until(
                ExpectedConditions.elementToBeClickable(detailsButton)
        );

        try {
            detailsBtn.click();
        } catch (ElementClickInterceptedException ex) {
            System.out.println(LOG + "detailsButton click intercepted, JS ile tıklanıyor...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", detailsBtn);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(detailsDialog));
    }

    public void clickDetailsClear() {
        WebElement clearBtn = wait.until(
                ExpectedConditions.elementToBeClickable(clearButtonInDialog)
        );
        clearBtn.click();
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(detailsDialog));
        } catch (TimeoutException e) {
        }
    }

    public void clickDetailsApply() {
        WebElement applyBtn = wait.until(
                ExpectedConditions.elementToBeClickable(applyButtonInDialog)
        );
        applyBtn.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(detailsDialog));
        waitForGridLoaded();
    }

    // ================== ARAMA & FİLTRE ==================

    public void searchForResource(String text) {
        waitForGridLoaded();

        safeClick(searchWrapper);

        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(searchInput)
        );

        setSearchInputValue(input, text);

        input.sendKeys(Keys.ENTER);

        try {
            safeClick(searchIcon);
        } catch (Exception e) {
            System.out.println(LOG + "Search icon click opsiyonel, hata yok sayıldı: " + e.getMessage());
        }

        String expectedLower = text.toLowerCase(Locale.ROOT).trim();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
            List<WebElement> rows = d.findElements(gridRows);
            if (rows.isEmpty()) {
                System.out.println(LOG + "Filtre sonrası satır yok.");
                return false;
            }

            System.out.println(LOG + "Filtre sonrası satır sayısı: " + rows.size());
            boolean anyMatch = false;

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.isEmpty()) {
                    return false;
                }

                String nameCellText = cells.get(0).getText().trim().toLowerCase(Locale.ROOT);
                System.out.println(LOG + "Satır kaynak adı (filtre sonrası): '" + nameCellText + "'");

                if (nameCellText.equals(expectedLower)) {
                    anyMatch = true;
                }
            }

            return anyMatch;
        });
    }

    public boolean searchForResourceAllowEmpty(String resourceName) {
        System.out.println(LOG + "(AllowEmpty) Aranan kaynak adı: " + resourceName);

        waitForGridLoaded();

        safeClick(searchWrapper);

        WebElement searchInputEl = wait.until(
                ExpectedConditions.elementToBeClickable(searchInput)
        );

        setSearchInputValue(searchInputEl, resourceName);

        searchInputEl.sendKeys(Keys.ENTER);

        try {
            safeClick(searchIcon);
        } catch (Exception e) {
            System.out.println(LOG + "(AllowEmpty) Search icon click opsiyonel, hata yok sayıldı: " + e.getMessage());
        }

        waitForGridLoaded();

        List<WebElement> rows = driver.findElements(gridRows);
        System.out.println(LOG + "(AllowEmpty) Filtre sonrası satır sayısı: " + rows.size());

        if (rows.isEmpty()) {
            System.out.println(LOG + "(AllowEmpty) Grid boş, yeni kayıt index=1 olarak başlayabilir.");
            return false;
        }

        boolean found = false;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.isEmpty()) {
                continue;
            }
            String name = cells.get(0).getText().trim();
            System.out.println(LOG + "Satır kaynak adı (allowEmpty): '" + name + "'");
            if (name.equalsIgnoreCase(resourceName)) {
                found = true;
            }
        }

        System.out.println(LOG + "(AllowEmpty) '" + resourceName + "' bulundu mu? " + found);
        return found;
    }

    // ================== GRID HELPERLARI ==================

    private WebElement findResourceRow(String resourceName) {
        waitForGridLoaded();

        String targetLower = resourceName.toLowerCase(Locale.ROOT).trim();

        List<WebElement> rows = driver.findElements(gridRows);
        System.out.println(LOG + "Satır sayısı (findResourceRow): " + rows.size());

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.isEmpty()) continue;

            String nameCellText = cells.get(0).getText().trim();
            System.out.println(LOG + "Satır kaynak adı: '" + nameCellText + "'");

            String rowLower = nameCellText.toLowerCase(Locale.ROOT).trim();
            if (rowLower.equals(targetLower)) {
                return row;
            }
        }

        return null;
    }

    private String generateIndexedResourceName(String baseName) {
        List<WebElement> rows = driver.findElements(gridRows);
        if (rows.isEmpty()) {
            return baseName + " 1";
        }

        int maxIndex = 0;
        String baseLower = baseName.toLowerCase(Locale.ROOT).trim();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.isEmpty()) {
                continue;
            }

            String rowName = cells.get(0).getText().trim();
            if (rowName.isEmpty()) {
                continue;
            }

            String rowLower = rowName.toLowerCase(Locale.ROOT).trim();

            if (rowLower.equals(baseLower)) {
                if (maxIndex < 0) {
                    maxIndex = 0;
                }
            } else if (rowLower.startsWith(baseLower + " ")) {
                String suffix = rowLower.substring(baseLower.length()).trim();
                try {
                    int num = Integer.parseInt(suffix);
                    if (num > maxIndex) {
                        maxIndex = num;
                    }
                } catch (NumberFormatException ignore) {
                }
            }
        }

        int newIndex = (maxIndex == 0 ? 1 : maxIndex + 1);
        return baseName + " " + newIndex;
    }

    private String getResourceStatus(String resourceName) {
        WebElement row = findResourceRow(resourceName);
        Assert.assertNotNull(
                row,
                LOG + "Durum okunmak üzere kaynak bulunamadı: " + resourceName
        );

        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() < 5) {
            Assert.fail(LOG + "Durum hücresi (5. sütun) bulunamadı.");
        }

        WebElement statusCell = cells.get(4);
        String actualStatus = statusCell.getText().trim();
        System.out.println(LOG + "Satır durum değeri: '" + actualStatus + "'");
        return actualStatus;
    }

    // ================== DURUM GÜNCELLEME ==================

    public void ensureResourceStatus(String resourceName, String expectedStatus) {
        searchForResource(resourceName);
        waitForGridLoaded();

        String actualStatus = getResourceStatus(resourceName);
        System.out.println(LOG + "İlk okunan durum: '" + actualStatus + "'");

        if (actualStatus.equalsIgnoreCase(expectedStatus)) {
            System.out.println(LOG + "Kaynak zaten '" + expectedStatus + "' durumda.");
        } else if (expectedStatus.equalsIgnoreCase("Aktif")
                && actualStatus.equalsIgnoreCase("Pasif")) {

            System.out.println(LOG + "Kaynak pasif, 'Aktif Et' akışı başlatılıyor...");
            activateResource(resourceName);
            actualStatus = getResourceStatus(resourceName);
        } else {
            Assert.fail(LOG + "Beklenmeyen durum kombinasyonu. Beklenen: '" +
                    expectedStatus + "', ilk okunan: '" + actualStatus + "'");
        }

        Assert.assertTrue(
                actualStatus.equalsIgnoreCase(expectedStatus),
                LOG + "Beklenen durum '" + expectedStatus +
                        "', fakat ekranda '" + actualStatus + "' görünüyor."
        );
    }

    public void verifyResourceStatusIs(String resourceName, String expectedStatus) {
        searchForResource(resourceName);

        String actualStatus = getResourceStatus(resourceName);

        Assert.assertTrue(
                actualStatus.equalsIgnoreCase(expectedStatus),
                LOG + "Beklenen durum '" + expectedStatus +
                        "', fakat ekranda '" + actualStatus + "' görünüyor."
        );
    }

    private void activateResource(String resourceName) {
        waitForGridLoaded();

        WebElement row = findResourceRow(resourceName);
        Assert.assertNotNull(
                row,
                LOG + "Aktif etme için kaynak bulunamadı: " + resourceName
        );

        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() < 6) {
            Assert.fail(LOG + "Aksiyon hücresi (6. sütun) bulunamadı.");
        }

        WebElement actionCell = cells.get(5);

        List<WebElement> buttons = actionCell.findElements(By.tagName("button"));
        Assert.assertFalse(buttons.isEmpty(), LOG + "Aksiyon hücresinde buton bulunamadı.");

        WebElement dropdownBtn = buttons.get(buttons.size() - 1);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                dropdownBtn
        );

        System.out.println(LOG + "Dropdown butonuna tıklanıyor...");
        safeClick(dropdownBtn);
        System.out.println(LOG + "Dropdown butonuna tıklandı, menu item'lar bekleniyor...");

        By menuItemLocator = By.cssSelector("li.e-item, li[role='menuitem']");
        List<WebElement> visibleItems = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            List<WebElement> all = driver.findElements(menuItemLocator);
            visibleItems.clear();

            for (WebElement el : all) {
                try {
                    if (el.isDisplayed()) {
                        visibleItems.add(el);
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }

            System.out.println(LOG + "Iterasyon " + i
                    + " - görünen menu item sayısı: " + visibleItems.size());

            if (!visibleItems.isEmpty()) {
                break;
            }

            if (i < 3 && visibleItems.isEmpty()) {
                waitForGridLoaded();
                row = findResourceRow(resourceName);
                if (row != null) {
                    cells = row.findElements(By.tagName("td"));
                    if (cells.size() >= 6) {
                        actionCell = cells.get(5);
                        buttons = actionCell.findElements(By.tagName("button"));
                        if (!buttons.isEmpty()) {
                            dropdownBtn = buttons.get(buttons.size() - 1);
                            System.out.println(LOG + "Menü görünmedi, dropdown tekrar tıklanıyor (deneme " + i + ").");
                            safeClick(dropdownBtn);
                        }
                    }
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }

        Assert.assertFalse(
                visibleItems.isEmpty(),
                LOG + "10 sn içinde hiçbir dropdown menu item bulunamadı."
        );

        WebElement targetItem = null;

        for (WebElement item : visibleItems) {
            String text = "";
            try {
                Object jsText = ((JavascriptExecutor) driver)
                        .executeScript("return arguments[0].innerText;", item);
                if (jsText != null) {
                    text = jsText.toString().trim();
                }
            } catch (Exception ignored) {
            }

            System.out.println(LOG + "Menu item text: '" + text + "'");

            if (!text.isEmpty() &&
                    text.toLowerCase(Locale.ROOT).contains("aktif")) {
                targetItem = item;
                System.out.println(LOG + "'Aktif' içeren item bulundu.");
                break;
            }
        }

        if (targetItem == null) {
            targetItem = visibleItems.get(visibleItems.size() - 1);
            System.out.println(LOG + "'Aktif' içeren bulunamadı, fallback olarak son item seçildi.");
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetItem);
        System.out.println(LOG + "Seçilen dropdown item'e tıklandı.");

        WebElement yesBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(confirmYesButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesBtn);
        System.out.println(LOG + "'Evet' butonuna JS ile tıklandı.");

        waitUntilResourceStatusIs(resourceName, "Aktif");
        waitForGridLoaded();
    }

    private void waitUntilResourceStatusIs(String resourceName, String expectedStatus) {
        String expectedLower = expectedStatus.toLowerCase(Locale.ROOT);

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> {
                    try {
                        waitForGridLoaded();

                        WebElement row = findResourceRow(resourceName);
                        if (row == null) {
                            System.out.println(LOG + "Durum beklenirken satır bulunamadı.");
                            return false;
                        }

                        List<WebElement> cells = row.findElements(By.tagName("td"));
                        if (cells.size() < 5) {
                            System.out.println(LOG + "Durum hücresi okunamadı.");
                            return false;
                        }

                        WebElement statusCell = cells.get(4);

                        String statusText = "";
                        try {
                            Object jsText = ((JavascriptExecutor) d)
                                    .executeScript("return arguments[0].innerText;", statusCell);
                            if (jsText != null) {
                                statusText = jsText.toString().trim();
                            }
                        } catch (Exception ignoredInner) {
                        }

                        String statusLower = statusText.toLowerCase(Locale.ROOT);
                        System.out.println(LOG + "Durum güncellemesi bekleniyor: '" + statusLower + "'");

                        if (statusLower.equals(expectedLower)) {
                            System.out.println(LOG + "Durum hedef değere ulaştı: '" + expectedStatus + "'");
                            return true;
                        }

                        try {
                            List<WebElement> successToasts =
                                    d.findElements(By.xpath("//*[contains(text(),'Kaynak aktif edildi')]"));
                            for (WebElement toast : successToasts) {
                                if (toast.isDisplayed()) {
                                    System.out.println(LOG + "Başarı toast görüldü: 'Kaynak aktif edildi'");
                                    return statusLower.equals(expectedLower);
                                }
                            }
                        } catch (Exception ignoredToast) {
                        }

                        return false;
                    } catch (StaleElementReferenceException sere) {
                        System.out.println(LOG + "Durum kontrolünde stale element; tekrar denenecek.");
                        return false;
                    } catch (Exception e) {
                        System.out.println(LOG + "Durum kontrolünde beklenmeyen hata: " + e.getMessage());
                        return false;
                    }
                });
    }

    // ============= YENİ HELPER: YENİ SATIRIN GELMESİNİ BEKLE =============

    private void waitUntilResourceRowExists(String resourceName) {
        String targetLower = resourceName.toLowerCase(Locale.ROOT).trim();

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> {
                    try {
                        waitForGridLoaded();

                        List<WebElement> rows = d.findElements(gridRows);
                        System.out.println(LOG + "Yeni kayıt bekleniyor, satır sayısı: " + rows.size());

                        for (WebElement row : rows) {
                            List<WebElement> cells = row.findElements(By.tagName("td"));
                            if (cells.isEmpty()) continue;

                            String name = cells.get(0).getText().trim();
                            String lower = name.toLowerCase(Locale.ROOT).trim();
                            System.out.println(LOG + "Satır kaynak adı (wait): '" + name + "'");

                            if (lower.equals(targetLower)) {
                                System.out.println(LOG + "Yeni kaynak satırı bulundu: '" + name + "'");
                                return true;
                            }
                        }
                        return false;
                    } catch (StaleElementReferenceException e) {
                        System.out.println(LOG + "Yeni kaynak beklerken stale, tekrar denenecek.");
                        return false;
                    }
                });
    }

    // ================== YENİ ODA & CİHAZ KAYDI ==================

    private WebElement findInputInDialogByLabel(String labelText) {
        return driver.findElement(By.xpath(
                "//div[contains(@id,'dialog') and contains(@class,'e-dialog')]" +
                        "//label[normalize-space()='" + labelText + "']/following::input[1]"
        ));
    }

    private void selectFromDropdownInDialog(String labelText, String optionText) {
        WebElement formGroup = driver.findElement(By.xpath(
                "//div[contains(@id,'dialog') and contains(@class,'e-dialog')]" +
                        "//label[normalize-space()='" + labelText + "']/ancestor::div[contains(@class,'e-form-group')]"
        ));

        WebElement icon = formGroup.findElement(
                By.cssSelector(".e-ddl .e-ddl-icon, span.e-input-group-icon.e-ddl-icon, span.e-input-group-icon")
        );

        wait.until(ExpectedConditions.elementToBeClickable(icon)).click();

        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement option = localWait.until(d -> {
            List<WebElement> candidates = d.findElements(
                    By.xpath("//li[normalize-space()='" + optionText + "']")
            );
            for (WebElement li : candidates) {
                try {
                    if (li.isDisplayed()) {
                        return li;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
            return null;
        });

        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", option
            );
        } catch (Exception ignored) {
        }

        try {
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();
        } catch (Exception ex) {
            System.out.println(LOG + "Dropdown option normal tıklanamadı, JS ile tıklanıyor: " + ex);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        }

        try {
            localWait.until(ExpectedConditions.invisibilityOf(option));
        } catch (TimeoutException ignored) {
        }
    }

    private void selectFromMultiSelectInDialog(String labelText, String optionText) {
        WebElement formGroup = driver.findElement(By.xpath(
                "//div[contains(@id,'dialog') and contains(@class,'e-dialog')]" +
                        "//label[normalize-space()='" + labelText + "']/ancestor::div[contains(@class,'e-form-group')]"
        ));

        WebElement input = formGroup.findElement(
                By.cssSelector("input.e-dropdownbase.e-control.e-multiselect")
        );

        try {
            new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        } catch (Exception ignored) { }

        safeClick(input);

        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement option = localWait.until(d -> {
            List<WebElement> allOptions = d.findElements(By.xpath("//li[not(@class='e-hide')]"));
            System.out.println("[ResourcesHealthcare] Multi-select '" + labelText + "' için görünen seçenekler:");
            WebElement target = null;

            for (WebElement li : allOptions) {
                try {
                    if (!li.isDisplayed()) {
                        continue;
                    }
                    String text = li.getText().trim();
                    if (!text.isEmpty()) {
                        System.out.println("  -> '" + text + "'");
                    }
                    if (text.equals(optionText)) {
                        target = li;
                    }
                } catch (StaleElementReferenceException ignored) { }
            }

            if (target != null) {
                System.out.println("[ResourcesHealthcare] Hedef seçenek bulundu: '" + optionText + "'");
            }

            return target;
        });

        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", option
            );
        } catch (Exception ignored) { }

        try {
            safeClick(option);
        } catch (Exception ex) {
            System.out.println("[ResourcesHealthcare] Multi-select option click sırasında hata, JS ile tıklanıyor: " + ex);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        }

        try {
            new Actions(driver).sendKeys(Keys.ESCAPE).perform();
            Thread.sleep(200);
        } catch (Exception ignored) { }
    }

    private void openNewResourceDialog() {
        waitForGridLoaded();

        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(fullPageOverlay));
        } catch (TimeoutException e) {
            System.out.println(LOG + "Overlay görünmedi veya zamanında kaybolmadı, akış devam ediyor.");
        }

        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement button = localWait.until(d -> {
            List<WebElement> candidates = d.findElements(newResourceButtons);
            System.out.println(LOG + "Yeni Ekle buton adayı sayısı: " + candidates.size());
            for (WebElement b : candidates) {
                try {
                    if (b.isDisplayed() && b.isEnabled()) {
                        return b;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
            return null;
        });

        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", button
            );
        } catch (Exception ignore) {
        }

        try {
            button.click();
        } catch (Exception ex) {
            System.out.println(LOG + "Yeni Ekle butonu normal tıklanamadı, JS ile tıklanıyor: " + ex);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(newResourceDialog));
    }

    private void fillNewResourceForm(String name,
                                     String tip,
                                     String altTip,
                                     String department,
                                     String sube) {

        WebElement nameInput = findInputInDialogByLabel("Ad");
        nameInput.click();
        nameInput.clear();
        nameInput.sendKeys(name);

        selectFromDropdownInDialog("Tip", tip);
        selectFromDropdownInDialog("Alt Tip", altTip);
        selectFromMultiSelectInDialog("Departmanlar", department);
        selectFromMultiSelectInDialog("Şube", sube);
    }

    public void clickResourceDialogSave() {
        By dialogLocator = By.xpath(
                "//div[contains(@id,'dialog') and contains(@class,'e-dialog')]" +
                        "[.//form[contains(@id,'dataform')]]"
        );

        long endTime = System.currentTimeMillis() + Duration.ofSeconds(15).toMillis();
        int attempt = 0;

        while (System.currentTimeMillis() < endTime) {
            attempt++;

            WebElement dialog;
            WebElement saveButton;

            try {
                dialog = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(dialogLocator)
                );
            } catch (TimeoutException e) {
                System.out.println(LOG + "Kaydet denemesi #" + attempt + " sonrasında dialog zaten görünmez durumda.");
                return;
            }

            try {
                saveButton = dialog.findElement(By.xpath(
                        ".//button[@type='submit' and contains(@class,'e-primary') and normalize-space()='Kaydet']"
                ));
            } catch (NoSuchElementException e) {
                System.out.println(LOG + "Kaydet butonu bulunamadı, dialog kapanmış olabilir. Deneme #" + attempt);
                return;
            }

            System.out.println(LOG + "Kaydet butonuna tıklama denemesi: " + attempt);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
            } catch (ElementClickInterceptedException e) {
                System.out.println(LOG + "Kaydet normal click sırasında intercept edildi, ESC ile popup kapatılacak ve tekrar denenecek...");
                try {
                    new Actions(driver).sendKeys(Keys.ESCAPE).perform();
                    wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
                } catch (ElementClickInterceptedException e2) {
                    System.out.println(LOG + "Kaydet hâlâ intercept ediliyor, JS ile tıklanacak: " + e2.getMessage());
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
                } catch (Exception e2) {
                    System.out.println(LOG + "Kaydet click denemesinde beklenmeyen hata (ESC sonrası): " + e2.getMessage());
                }
            } catch (StaleElementReferenceException e) {
                System.out.println(LOG + "Kaydet butonu stale oldu, bir sonraki iterasyonda yeniden denenecek. Deneme #" + attempt);
            } catch (Exception e) {
                System.out.println(LOG + "Kaydet click denemesinde beklenmeyen hata: " + e.getMessage());
            }

            try {
                boolean closed = new WebDriverWait(driver, Duration.ofSeconds(2))
                        .until(ExpectedConditions.invisibilityOfElementLocated(dialogLocator));
                if (closed) {
                    System.out.println(LOG + "Kaydet tıklaması sonrası dialog başarıyla kapandı. Toplam deneme: " + attempt);
                    return;
                }
            } catch (TimeoutException ignored) {
                System.out.println(LOG + "Dialog henüz kapanmadı, tekrar denenecek. Deneme #" + attempt);
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
        }

        Assert.fail(LOG + "Kaydet butonuna birden çok kez tıklanmasına rağmen dialog 15 sn içinde kapanmadı.");
    }

    public void createResourceWithAutoIndex(String baseResourceName,
                                            String tip,
                                            String altTip,
                                            String department,
                                            String sube) {

        String baseName = baseResourceName;

        searchForResourceAllowEmpty(baseName);

        WebElement existing = findResourceRow(baseName);
        String actualName = baseName;

        if (existing != null) {
            System.out.println(LOG + "'" + baseName +
                    "' zaten mevcut, indexli yeni kayıt oluşturulacak.");
            actualName = generateIndexedResourceName(baseName);
            System.out.println(LOG + "Yeni kaynak adı: '" + actualName + "'");
        } else {
            System.out.println(LOG + "'" + baseName +
                    "' bulunamadı, bu adla ilk kayıt oluşturulacak.");
        }

        openNewResourceDialog();
        fillNewResourceForm(actualName, tip, altTip, department, sube);
        clickResourceDialogSave();

        searchForResourceAllowEmpty(actualName);

        try {
            waitUntilResourceRowExists(actualName);
        } catch (TimeoutException e) {
            System.out.println(LOG + "Yeni kaynak satırı zamanında bulunamadı, validation mesajları taranıyor...");

            try {
                List<WebElement> validationMsgs = driver.findElements(
                        By.xpath("//*[contains(@class,'e-error') or " +
                                "contains(@class,'validation') or " +
                                "contains(@class,'text-danger') or " +
                                "contains(@class,'alert')]")
                );
                for (WebElement msg : validationMsgs) {
                    try {
                        if (msg.isDisplayed()) {
                            System.out.println(LOG + "Validation / hata mesajı: " + msg.getText());
                        }
                    } catch (StaleElementReferenceException ignored) {
                    }
                }
            } catch (Exception ignoredOuter) {
            }

            Assert.fail(LOG + "Yeni oluşturulan kaynak gridde 15 sn içinde görünmedi: " + actualName);
        }

        WebElement created = findResourceRow(actualName);
        Assert.assertNotNull(
                created,
                LOG + "Yeni oluşturulan kaynak gridde bulunamadı (post-wait): " + actualName
        );

        this.lastCreatedResourceName = actualName;
    }

    // ================== GETTER ==================

    public String getLastCreatedResourceName() {
        return lastCreatedResourceName;
    }
}

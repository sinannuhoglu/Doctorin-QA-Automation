package com.sinannuhoglu.pages.resources;

import com.sinannuhoglu.core.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResourcesStaffManagementPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ================== LOCATORS ==================

    private final By detailsButton = By.cssSelector("button.e-control.e-btn.e-lib.e-icon-btn");

    private final By fullPageOverlay = By.cssSelector(
            "div.absolute.inset-0.m-auto.grid.h-full.w-full.place-items-center.backdrop-blur-xs.z-20"
    );

    private final By detailsDialog = By.xpath("//div[contains(@id,'modal-dialog')]");

    private final By clearButtonInDialog = By.xpath(
            "//div[contains(@id,'modal-dialog')]//div[contains(@class,'e-button-right')]//button[contains(.,'Temizle')]"
    );

    private final By applyButtonInDialog = By.xpath(
            "//div[contains(@id,'modal-dialog')]//div[contains(@class,'e-button-right')]//button[contains(.,'Uygula')]"
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

    private final By gridRows = By.cssSelector(
            "div.e-gridcontent table.e-table tbody tr"
    );

    private final By confirmYesButton = By.xpath(
            "//div[contains(@id,'dialog') and contains(@class,'e-dialog')]//button[normalize-space()='Evet']"
    );

    private final By toastContainer = By.cssSelector("div.e-toast");

    // ================== CTOR ==================

    public ResourcesStaffManagementPage() {
        this.driver = DriverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ================== COMMON HELPERS ==================

    private void waitForGridLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContent));
        wait.until(d -> {
            List<WebElement> rows = d.findElements(gridRows);
            System.out.println("[ResourcesStaff] Grid yüklendi. Satır sayısı: " + rows.size());
            return !rows.isEmpty();
        });
    }

    private void safeClick(By locator) {
        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                el.click();
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println("[ResourcesStaff] StaleElementReferenceException, tekrar denenecek: " + locator);
            } catch (ElementClickInterceptedException e) {
                System.out.println("[ResourcesStaff] ElementClickInterceptedException, JS ile tıklanacak: " + locator);
                try {
                    WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    return;
                } catch (Exception ex) {
                    // yeniden denenecek
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
                System.out.println("[ResourcesStaff] StaleElementReferenceException, dropdown buton tekrar bulunacak.");
                return;
            } catch (ElementClickInterceptedException e) {
                System.out.println("[ResourcesStaff] Dropdown click intercepted, JS ile tıklanıyor...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                return;
            }
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Arama input’unu OS bağımsız şekilde temizler ve yeni metni yazar.
     */
    private void setSearchInputValue(WebElement input, String text) {
        waitForToastsToDisappear();

        try {
            input.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("[ResourcesStaff] Search input click intercepted, JS ile tıklanıyor...");
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

        System.out.println("[ResourcesStaff] Search input value after set: " +
                input.getAttribute("value"));
    }

    private WebElement findStaffRow(String fullName) {
        waitForGridLoaded();

        String targetLower = fullName.toLowerCase(Locale.ROOT).trim();

        List<WebElement> rows = driver.findElements(gridRows);
        System.out.println("[ResourcesStaff] Satır sayısı (findStaffRow): " + rows.size());

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.isEmpty()) {
                continue;
            }

            String nameCellText = cells.get(0).getText().trim();
            System.out.println("[ResourcesStaff] Satır personel adı: '" + nameCellText + "'");

            String rowLower = nameCellText.toLowerCase(Locale.ROOT).trim();
            if (rowLower.equals(targetLower)) {
                return row;
            }
        }

        return null;
    }

    private String getStaffStatus(String fullName) {
        WebElement row = findStaffRow(fullName);
        Assert.assertNotNull(
                row,
                "[ResourcesStaff] Durum okunmak üzere personel bulunamadı: " + fullName
        );

        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() < 5) {
            Assert.fail("[ResourcesStaff] Durum hücresi (5. sütun) bulunamadı.");
        }

        WebElement statusCell = cells.get(4);
        String actualStatus = statusCell.getText().trim();
        System.out.println("[ResourcesStaff] Satır durum değeri: '" + actualStatus + "'");
        return actualStatus;
    }

    private void waitForToastsToDisappear() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOfElementLocated(toastContainer));
        } catch (TimeoutException e) {
            System.out.println("[ResourcesStaff] Toast hala görünüyor, test devam edecek.");
        }
    }

    // ================== NAVIGATION ==================

    public void goToStaffManagement(String url) {
        System.out.println("[ResourcesStaff] Personel Yönetimi URL'ine gidiliyor: " + url);
        driver.get(url);

        WebDriverWait urlWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        urlWait.until(ExpectedConditions.urlContains("/resource-service/staff-management"));

        System.out.println("[ResourcesStaff] Aktif URL: " + driver.getCurrentUrl());

        try {
            urlWait.until(ExpectedConditions.invisibilityOfElementLocated(fullPageOverlay));
        } catch (TimeoutException e) {
            // overlay her zaman çıkmayabilir
        }

        waitForGridLoaded();
    }

    // ================== DETAYLI ARAMA ==================

    public void openDetailsPopup() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(fullPageOverlay));
        } catch (TimeoutException e) {
            // overlay yoksa sorun değil
        }

        WebElement detailsBtn = wait.until(
                ExpectedConditions.elementToBeClickable(detailsButton)
        );

        try {
            detailsBtn.click();
        } catch (ElementClickInterceptedException ex) {
            System.out.println("[ResourcesStaff] detailsButton click intercepted, JS ile tıklanıyor...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", detailsBtn);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(detailsDialog));
    }

    public void clickDetailsClear() {
        WebElement clearBtn = wait.until(
                ExpectedConditions.elementToBeClickable(clearButtonInDialog)
        );
        clearBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(detailsDialog));
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

    public void searchForStaff(String text) {
        waitForGridLoaded();
        waitForToastsToDisappear();

        safeClick(searchWrapper);

        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(searchInput));

        setSearchInputValue(input, text);

        input.sendKeys(Keys.ENTER);

        try {
            safeClick(searchIcon);
        } catch (Exception e) {
            System.out.println("[ResourcesStaff] Search icon click opsiyonel, hata yok sayıldı: " + e.getMessage());
        }

        String expectedLower = text.toLowerCase(Locale.ROOT).trim();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
            List<WebElement> rows = d.findElements(gridRows);
            if (rows.isEmpty()) {
                System.out.println("[ResourcesStaff] Filtre sonrası satır yok.");
                return false;
            }

            System.out.println("[ResourcesStaff] Filtre sonrası satır sayısı: " + rows.size());
            boolean anyMatch = false;

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.isEmpty()) {
                    return false;
                }

                String nameCellText = cells.get(0).getText().trim().toLowerCase(Locale.ROOT);
                System.out.println("  -> Satır personel adı (filtre sonrası): '" + nameCellText + "'");

                if (nameCellText.contains(expectedLower)) {
                    anyMatch = true;
                }
            }

            return anyMatch;
        });
    }

    // ================== DURUM GÜNCELLEME (PASİF -> AKTİF) ==================

    /**
     * Verilen personelin durumunu beklenen değere getirir.
     * Bu metot bir *hazırlık (Given)* adımı olarak kullanıldığı için
     * backend işlemi başarılı olduğu sürece (toast görülürse) PASS sayılır.
     */
    public void ensureStaffStatus(String fullName, String expectedStatus) {
        searchForStaff(fullName);
        waitForGridLoaded();

        String actualStatus = getStaffStatus(fullName);
        System.out.println("[ResourcesStaff] İlk okunan durum: '" + actualStatus + "'");

        if (actualStatus.equalsIgnoreCase(expectedStatus)) {
            System.out.println("[ResourcesStaff] Personel zaten '" + expectedStatus +
                    "' durumda, aksiyon alınmayacak.");
            return;
        }

        if (expectedStatus.equalsIgnoreCase("Aktif")
                && actualStatus.equalsIgnoreCase("Pasif")) {

            System.out.println("[ResourcesStaff] Personel pasif, 'Aktif Et' akışı başlatılıyor...");
            activateStaff(fullName);
            return;
        }

        Assert.fail("[ResourcesStaff] Beklenmeyen durum kombinasyonu. Beklenen: '" +
                expectedStatus + "', ilk okunan: '" + actualStatus + "'");
    }

    /**
     * Personelin mevcut durumunu, herhangi bir aksiyon almadan doğrular.
     */
    public void verifyStaffStatusIs(String fullName, String expectedStatus) {
        searchForStaff(fullName);

        String expectedLower = expectedStatus.toLowerCase(Locale.ROOT);

        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> {
                        String current = getStaffStatus(fullName).toLowerCase(Locale.ROOT);
                        System.out.println("[ResourcesStaff] Verify bekleniyor. Hedef: '"
                                + expectedLower + "', mevcut: '" + current + "'");
                        return current.equals(expectedLower);
                    });

            System.out.println("[ResourcesStaff] Verify tarafında durum hedef değere ulaştı: '"
                    + expectedStatus + "'");

        } catch (TimeoutException e) {
            String last = getStaffStatus(fullName);
            Assert.fail("[ResourcesStaff] 10 sn içinde durum '" + expectedStatus +
                    "' değerine güncellenmedi. Son okunan değer: '" + last + "'");
        }
    }

    /**
     * "Aktif Et" dropdown akışını ve onay penceresini çalıştırır.
     */
    private void activateStaff(String fullName) {
        waitForGridLoaded();

        WebElement row = findStaffRow(fullName);
        Assert.assertNotNull(
                row,
                "[ResourcesStaff] Aktif etme için personel bulunamadı: " + fullName
        );

        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() < 6) {
            Assert.fail("[ResourcesStaff] Aksiyon hücresi (6. sütun) bulunamadı.");
        }

        WebElement actionCell = cells.get(5);

        List<WebElement> buttons = actionCell.findElements(By.tagName("button"));
        Assert.assertFalse(buttons.isEmpty(), "[ResourcesStaff] Aksiyon hücresinde buton bulunamadı.");

        WebElement dropdownBtn = buttons.get(buttons.size() - 1);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                dropdownBtn
        );

        System.out.println("[ResourcesStaff] Dropdown butonuna tıklanıyor...");
        safeClick(dropdownBtn);
        System.out.println("[ResourcesStaff] Dropdown butonuna tıklandı, menu item'lar bekleniyor...");

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

            System.out.println("[ResourcesStaff] Iterasyon " + i
                    + " - görünen menu item sayısı: " + visibleItems.size());

            if (!visibleItems.isEmpty()) {
                break;
            }

            try {
                row = findStaffRow(fullName);
                if (row != null) {
                    cells = row.findElements(By.tagName("td"));
                    if (cells.size() >= 6) {
                        WebElement newActionCell = cells.get(5);
                        List<WebElement> newButtons = newActionCell.findElements(By.tagName("button"));
                        if (!newButtons.isEmpty()) {
                            dropdownBtn = newButtons.get(newButtons.size() - 1);
                            ((JavascriptExecutor) driver).executeScript(
                                    "arguments[0].scrollIntoView({block: 'center'});",
                                    dropdownBtn
                            );
                            ((JavascriptExecutor) driver).executeScript(
                                    "arguments[0].click();",
                                    dropdownBtn
                            );
                            System.out.println("[ResourcesStaff] Menü görünmedi, dropdown tekrar tıklandı (deneme "
                                    + i + ").");
                        }
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                System.out.println("[ResourcesStaff] Menü beklerken stale row, sonraki iterasyonda tekrar denenecek.");
            } catch (Exception e) {
                System.out.println("[ResourcesStaff] Menü için tekrar tıklama denemesinde hata: " + e.getMessage());
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) { }
        }

        if (visibleItems.isEmpty()) {
            Assert.fail("[ResourcesStaff] Dropdown açılmadı, 10 sn içinde hiç menu item bulunamadı.");
        }

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

            System.out.println("[ResourcesStaff] Menu item text: '" + text + "'");

            if (!text.isEmpty() &&
                    text.toLowerCase(Locale.ROOT).contains("aktif")) {
                targetItem = item;
                System.out.println("[ResourcesStaff] 'Aktif' içeren item bulundu.");
                break;
            }
        }

        if (targetItem == null) {
            targetItem = visibleItems.get(visibleItems.size() - 1);
            System.out.println("[ResourcesStaff] 'Aktif' içeren bulunamadı, fallback olarak son item seçildi.");
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetItem);
        System.out.println("[ResourcesStaff] Seçilen dropdown item'e tıklandı.");

        WebElement yesBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(confirmYesButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesBtn);
        System.out.println("[ResourcesStaff] 'Evet' butonuna JS ile tıklandı.");

        waitUntilStaffStatusIs(fullName, "Aktif");
        waitForGridLoaded();
    }

    /**
     * Verilen personel kaydının durumunun istenen değere geldiğini bekler.
     * Önce "Kaynak aktif edildi" toast'ını arar, yoksa durum hücresini izler.
     */
    private void waitUntilStaffStatusIs(String fullName, String expectedStatus) {
        String expectedLower = expectedStatus.toLowerCase(Locale.ROOT);

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> {
                    try {
                        List<WebElement> successToasts =
                                d.findElements(By.xpath("//*[contains(text(),'Kaynak aktif edildi')]"));
                        for (WebElement toast : successToasts) {
                            if (toast.isDisplayed()) {
                                System.out.println("[ResourcesStaff] Başarı toast görüldü: 'Kaynak aktif edildi'");
                                return true;
                            }
                        }
                    } catch (Exception ignored) {
                    }

                    try {
                        waitForGridLoaded();

                        WebElement row = findStaffRow(fullName);
                        if (row == null) {
                            System.out.println("[ResourcesStaff] Durum beklenirken satır bulunamadı.");
                            return false;
                        }

                        List<WebElement> cells = row.findElements(By.tagName("td"));
                        if (cells.size() < 5) {
                            System.out.println("[ResourcesStaff] Durum hücresi okunamadı.");
                            return false;
                        }

                        WebElement statusCell = cells.get(cells.size() - 1);

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
                        System.out.println("[ResourcesStaff] Durum güncellemesi bekleniyor: '" + statusLower + "'");

                        return statusLower.equals(expectedLower);
                    } catch (StaleElementReferenceException sere) {
                        System.out.println("[ResourcesStaff] Durum kontrolünde stale element; tekrar denenecek.");
                        return false;
                    } catch (Exception e) {
                        System.out.println("[ResourcesStaff] Durum kontrolünde beklenmeyen hata: " + e.getMessage());
                        return false;
                    }
                });

        System.out.println("[ResourcesStaff] Durum hedef değere ulaştı: '" + expectedStatus + "'");
    }

}

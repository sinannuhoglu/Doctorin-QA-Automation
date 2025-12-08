package com.sinannuhoglu.pages.resources;

import com.sinannuhoglu.core.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class ResourcesDepartmentsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private String lastCreatedDepartmentName;

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
            "#search span.e-input-group.e-control-container.e-control-wrapper.e-append"
    );

    private final By searchInput = By.cssSelector(
            "#search input.e-control.e-textbox.e-lib.e-input"
    );

    private final By searchIcon = By.cssSelector(
            "#search span.e-input-group-icon.e-icons.e-search"
    );

    private final By gridContent = By.cssSelector("div.e-gridcontent");

    private final By gridRows = By.cssSelector(
            "div.e-gridcontent table#Grid_content_table tbody tr"
    );

    private final By rowActionDropdownButton = By.cssSelector("button.e-dropdown-btn");

    private final By dropdownPopupContainer = By.cssSelector(
            "div[id^='e-dropdown-popup'][class*='e-popup-open']"
    );

    private final By confirmOverlay = By.cssSelector("div.e-dlg-overlay");

    private final By confirmYesButton = By.xpath(
            "//div[contains(@id,'dialog') and contains(@class,'e-dialog')]//button[normalize-space()='Evet']"
    );

    private final By newDepartmentButton = By.xpath(
            "//button[.//span[normalize-space()='Yeni Ekle']]"
    );

    private final By toolbar = By.cssSelector("div.e-control.e-toolbar");
    private final By newDepartmentButtonContainer = By.id("add");

    private final By newDepartmentDialog = By.xpath(
            "//div[contains(@id,'modal-dialog') and .//form[contains(@id,'dataform')]]"
    );

    private final By dialogSaveButton = By.xpath(
            "//div[contains(@id,'modal-dialog')]//button[@type='submit' and contains(@class,'e-primary') and normalize-space()='Kaydet']"
    );

    private final By mainMenuResources = By.xpath("//span[normalize-space()='Kaynaklar']");
    private final By resourcesDepartmentsLink = By.xpath("//a[contains(@href,'/resource-service/departments')]");

    // ================== CTOR ==================

    public ResourcesDepartmentsPage() {
        this.driver = DriverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ================== COMMON HELPERS ==================

    private void waitForGridLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContent));
        wait.until(d -> {
            List<WebElement> rows = d.findElements(gridRows);
            System.out.println("[ResourcesPage] Grid yüklendi. Satır sayısı: " + rows.size());
            return !rows.isEmpty();
        });
    }

    private WebElement findDepartmentRow(String departmentName) {
        waitForGridLoaded();

        String targetLower = departmentName.toLowerCase().trim();
        List<WebElement> rows = driver.findElements(gridRows);
        System.out.println("[ResourcesPage] Satır sayısı (findDepartmentRow): " + rows.size());

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() < 2) {
                continue;
            }

            String deptName = cells.get(1).getText().trim();
            System.out.println("[ResourcesPage] Satır departman adı: '" + deptName + "'");

            if (deptName.toLowerCase().trim().equals(targetLower)) {
                return row;
            }
        }

        return null;
    }

    /**
     * Aynı departman adından grid'de kaç tane olduğunu inceleyip,
     * sonuna eklenecek index'i üretir.
     */
    private String generateIndexedDepartmentName(String baseName) {
        List<WebElement> rows = driver.findElements(gridRows);
        if (rows.isEmpty()) {
            return baseName + " 1";
        }

        int maxIndex = 0;
        String baseLower = baseName.toLowerCase(Locale.ROOT).trim();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() < 2) {
                continue;
            }

            String rowName = cells.get(1).getText().trim();
            if (rowName.isEmpty()) {
                continue;
            }

            String rowLower = rowName.toLowerCase(Locale.ROOT).trim();

            if (rowLower.equals(baseLower)) {
                maxIndex = Math.max(maxIndex, 0);
            } else if (rowLower.startsWith(baseLower + " ")) {
                String suffix = rowLower.substring(baseLower.length()).trim();
                try {
                    int num = Integer.parseInt(suffix);
                    if (num > maxIndex) {
                        maxIndex = num;
                    }
                } catch (NumberFormatException ignore) {
                    // sayı değilse yok say
                }
            }
        }

        int newIndex = (maxIndex == 0 ? 1 : maxIndex + 1);
        return baseName + " " + newIndex;
    }

    private String getDepartmentStatus(String departmentName) {
        WebElement row = findDepartmentRow(departmentName);
        Assert.assertNotNull(
                row,
                "[ResourcesPage] Durum okunmak üzere departman bulunamadı: " + departmentName
        );

        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() < 4) {
            Assert.fail("[ResourcesPage] Durum hücresi (4. sütun) bulunamadı.");
        }

        String actualStatus = cells.get(3).getText().trim();
        System.out.println("[ResourcesPage] Satır durum değeri: '" + actualStatus + "'");
        return actualStatus;
    }

    /**
     * StaleElementReference / click intercepted durumlarına karşı güvenli click.
     */
    private void safeClick(By locator) {
        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                el.click();
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println("[ResourcesPage] StaleElementReferenceException, tekrar denenecek: " + locator);
            } catch (ElementClickInterceptedException e) {
                System.out.println("[ResourcesPage] ElementClickInterceptedException, JS ile tıklanacak: " + locator);
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

    /**
     * Arama input'unu OS bağımsız temizleyip yeni metni yazar.
     */
    private void setSearchInputValue(WebElement input, String text) {
        input.click();

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

        System.out.println("[ResourcesPage] Search input value after set: " +
                input.getAttribute("value"));
    }

    // ================== NAVIGATION ==================

    public void goToResourcesDepartments(String url) {
        System.out.println("[ResourcesPage] Kaynaklar URL'ine gidiliyor: " + url);
        driver.get(url);

        WebDriverWait urlWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            urlWait.until(ExpectedConditions.urlContains("/resource-service/departments"));
        } catch (TimeoutException e) {
            System.out.println("[ResourcesPage] Direct URL ile gidilemedi, menü üzerinden gidilmeye çalışılacak...");

            WebElement resources = urlWait.until(
                    ExpectedConditions.elementToBeClickable(mainMenuResources)
            );
            resources.click();

            WebElement departments = urlWait.until(
                    ExpectedConditions.elementToBeClickable(resourcesDepartmentsLink)
            );
            departments.click();

            urlWait.until(ExpectedConditions.urlContains("/resource-service/departments"));
        }

        System.out.println("[ResourcesPage] Aktif URL: " + driver.getCurrentUrl());

        try {
            urlWait.until(ExpectedConditions.invisibilityOfElementLocated(fullPageOverlay));
        } catch (TimeoutException ignored) {
        }

        waitForGridLoaded();
    }

    // ================== DETAYLI ARAMA ==================

    public void openDetailsPopup() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(fullPageOverlay));
        } catch (TimeoutException ignored) {
        }

        WebElement detailsBtn = wait.until(
                ExpectedConditions.elementToBeClickable(detailsButton)
        );

        try {
            detailsBtn.click();
        } catch (ElementClickInterceptedException ex) {
            System.out.println("[ResourcesPage] detailsButton click intercepted, JS ile tıklanıyor...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", detailsBtn);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(detailsDialog));
    }

    public void clickClear() {
        WebElement clearBtn = wait.until(
                ExpectedConditions.elementToBeClickable(clearButtonInDialog)
        );
        clearBtn.click();
    }

    public void clickApply() {
        WebElement applyBtn = wait.until(
                ExpectedConditions.elementToBeClickable(applyButtonInDialog)
        );
        applyBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(detailsDialog));
        waitForGridLoaded();
    }

    // ================== ARAMA & FİLTRE ==================

    public void searchForDepartment(String text) {
        waitForGridLoaded();

        safeClick(searchWrapper);

        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(searchInput));

        setSearchInputValue(input, text);

        safeClick(searchIcon);

        String expectedLower = text.toLowerCase().trim();

        wait.until(d -> {
            List<WebElement> rows = d.findElements(gridRows);
            if (rows.isEmpty()) {
                System.out.println("[ResourcesPage] Filtre sonrası satır yok.");
                return false;
            }

            System.out.println("[ResourcesPage] Filtre sonrası satır sayısı: " + rows.size());
            boolean anyMatch = false;

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() < 2) {
                    return false;
                }

                String deptName = cells.get(1).getText().trim().toLowerCase();
                System.out.println("  -> Satır departman adı (filtre sonrası): '" + deptName + "'");

                if (!deptName.contains(expectedLower)) {
                    return false;
                } else {
                    anyMatch = true;
                }
            }

            return anyMatch;
        });
    }

    /**
     * Arama yapılır ve:
     *  - Gridde yalnızca aranan kayıt(lar) kalıncaya,
     *  - ya da grid "Gösterilecek kayıt yok" durumuna gelinceye kadar bekler.
     */
    public void searchForDepartmentAllowEmpty(String text) {
        waitForGridLoaded();

        safeClick(searchWrapper);

        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(searchInput));

        setSearchInputValue(input, text);

        System.out.println("[ResourcesPage] (AllowEmpty) Search input value: " +
                input.getAttribute("value"));

        safeClick(searchIcon);

        String expectedLower = text.toLowerCase().trim();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
            try {
                List<WebElement> rows = d.findElements(gridRows);
                if (rows.isEmpty()) {
                    System.out.println("[ResourcesPage] (AllowEmpty) Henüz satır yok, tekrar denenecek.");
                    return false;
                }

                System.out.println("[ResourcesPage] (AllowEmpty) Filtre sonrası satır sayısı: " + rows.size());

                if (rows.size() == 1) {
                    WebElement row = rows.get(0);
                    String rowClass = row.getAttribute("class");
                    String rowText  = row.getText().trim().toLowerCase();

                    if ((rowClass != null && rowClass.contains("e-emptyrow")) ||
                            rowText.contains("gösterilecek kayıt yok")) {

                        System.out.println("[ResourcesPage] (AllowEmpty) Gösterilecek kayıt yok satırı tespit edildi.");
                        return true;
                    }
                }

                boolean anyMatch = false;

                for (WebElement row : rows) {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (cells.size() < 2) {
                        return false;
                    }

                    String deptName = cells.get(1).getText().trim().toLowerCase();
                    System.out.println("  -> Satır departman adı (allowEmpty): '" + deptName + "'");

                    if (!deptName.contains(expectedLower)) {
                        return false;
                    } else {
                        anyMatch = true;
                    }
                }

                return anyMatch;
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
    }

    // ================== PASİF → AKTİF ==================

    private void waitUntilDepartmentStatusIsActive(String departmentName) {
        String target = departmentName.toLowerCase().trim();

        new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> {
            List<WebElement> rows = d.findElements(gridRows);
            if (rows.isEmpty()) return false;

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() < 4) continue;

                String deptName = cells.get(1).getText().trim().toLowerCase();
                String status   = cells.get(3).getText().trim();

                if (deptName.equals(target)) {
                    System.out.println("[ResourcesPage] Durum güncellemesi bekleniyor: '" + status + "'");
                    return status.equalsIgnoreCase("Aktif");
                }
            }
            return false;
        });
    }

    private void activateDepartment(String departmentName) {
        waitForGridLoaded();

        WebElement row = findDepartmentRow(departmentName);
        Assert.assertNotNull(
                row,
                "[ResourcesPage] Aktif etme için departman bulunamadı: " + departmentName
        );

        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() < 5) {
            Assert.fail("[ResourcesPage] Aksiyon hücresi (5. sütun) bulunamadı.");
        }

        WebElement actionCell = cells.get(4);
        WebElement dropdownBtn = actionCell.findElement(rowActionDropdownButton);

        wait.until(ExpectedConditions.elementToBeClickable(dropdownBtn)).click();

        WebElement popup = wait.until(
                ExpectedConditions.visibilityOfElementLocated(dropdownPopupContainer)
        );

        List<WebElement> menuItems = wait.until(d -> {
            List<WebElement> items = popup.findElements(By.cssSelector("li.e-item"));
            return items.isEmpty() ? null : items;
        });

        WebElement aktifItem = null;
        for (WebElement item : menuItems) {
            String text = item.getText().trim();
            System.out.println("[ResourcesPage] Dropdown item text: '" + text + "'");
            if (text.toLowerCase().contains("aktif")) {
                aktifItem = item;
                break;
            }
        }

        Assert.assertNotNull(
                aktifItem,
                "[ResourcesPage] Dropdown içinde 'Aktif' içeren bir seçenek bulunamadı."
        );

        wait.until(ExpectedConditions.elementToBeClickable(aktifItem)).click();

        WebElement yesBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(confirmYesButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesBtn);
        System.out.println("[ResourcesPage] 'Evet' butonuna JS ile tıklandı.");

        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(confirmOverlay));
        } catch (TimeoutException ignored) {
        }

        waitUntilDepartmentStatusIsActive(departmentName);
        waitForGridLoaded();
    }

    // ================== YENİ DEPARTMAN ==================

    private WebElement findInputInDialogByLabel(String labelText) {
        return driver.findElement(By.xpath(
                "//div[contains(@id,'modal-dialog')]//label[normalize-space()='" + labelText + "']" +
                        "/following::input[1]"
        ));
    }

    /**
     * DROPDOWN SEÇİMİNİ DAHA DAYANIKLI HALE GETİRİLMİŞ HALİ
     */
    private void selectFromDropdownInDialog(String labelText, String optionText) {
        WebElement formGroup = driver.findElement(By.xpath(
                "//div[contains(@id,'modal-dialog')]//label[normalize-space()='" + labelText + "']" +
                        "/ancestor::div[contains(@class,'e-form-group')]"
        ));

        WebElement icon = null;
        try {
            icon = formGroup.findElement(By.cssSelector("span.e-ddl .e-ddl-icon"));
        } catch (NoSuchElementException e) {
            icon = formGroup.findElement(By.cssSelector("span.e-input-group-icon.e-ddl-icon"));
        }

        try {
            wait.until(ExpectedConditions.elementToBeClickable(icon)).click();
        } catch (Exception ex) {
            System.out.println("[ResourcesPage] Dropdown ikonu normal tıklanamadı, JS click denenecek: " + ex);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", icon);
        }

        By optionLocator = By.xpath(
                "//li[contains(@class,'e-list-item')][normalize-space()='" + optionText + "']"
        );

        WebElement option = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(optionLocator));

        option.click();

        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOfElementLocated(optionLocator));
        } catch (TimeoutException ignored) {
            // Kapanış kontrolü kritik değil; grid zaten bir sonraki adımda yeniden okunuyor
        }
    }

    private void openNewDepartmentDialog() {
        waitForGridLoaded();

        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(fullPageOverlay));
        } catch (TimeoutException ignored) {
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(toolbar));

        WebElement addContainer = wait.until(
                ExpectedConditions.presenceOfElementLocated(newDepartmentButtonContainer)
        );

        WebElement button;
        try {
            button = addContainer.findElement(By.tagName("button"));
        } catch (NoSuchElementException e) {
            button = wait.until(ExpectedConditions.elementToBeClickable(newDepartmentButton));
        }

        try {
            wait.until(ExpectedConditions.elementToBeClickable(button));
            button.click();
        } catch (Exception ex) {
            System.out.println("[ResourcesPage] Yeni Ekle butonu normal tıklanamadı, scroll + JS click denenecek: " + ex);
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", button
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(newDepartmentDialog));
    }

    private void fillNewDepartmentForm(String code, String name, String departmentType, String branch) {
        WebElement codeInput = findInputInDialogByLabel("Kod");
        codeInput.click();
        codeInput.clear();
        codeInput.sendKeys(code);

        WebElement nameInput = findInputInDialogByLabel("Ad");
        nameInput.click();
        nameInput.clear();
        nameInput.sendKeys(name);

        selectFromDropdownInDialog("Departman Türü", departmentType);
        selectFromDropdownInDialog("Branş", branch);
    }

    private void clickDialogSave() {
        WebElement saveBtn = wait.until(
                ExpectedConditions.elementToBeClickable(dialogSaveButton)
        );
        saveBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(newDepartmentDialog));
        waitForGridLoaded();
    }

    public void createDepartment(String code,
                                 String name,
                                 String departmentType,
                                 String branch) {

        openNewDepartmentDialog();
        fillNewDepartmentForm(code, name, departmentType, branch);
        clickDialogSave();
    }

    /**
     * Verilen isimde departman listede yoksa yeni kayıt oluşturur.
     */
    public void createDepartmentIfNotExists(String departmentName,
                                            String code,
                                            String name,
                                            String departmentType,
                                            String branch) {

        searchForDepartmentAllowEmpty(departmentName);

        WebElement existing = findDepartmentRow(departmentName);
        if (existing != null) {
            System.out.println("[ResourcesPage] '" + departmentName +
                    "' zaten mevcut, yeni kayıt oluşturulmayacak.");
            return;
        }

        System.out.println("[ResourcesPage] '" + departmentName +
                "' bulunamadı, yeni departman kaydı oluşturulacak.");

        openNewDepartmentDialog();
        fillNewDepartmentForm(code, name, departmentType, branch);
        clickDialogSave();

        searchForDepartment(departmentName);
        WebElement created = findDepartmentRow(departmentName);
        Assert.assertNotNull(
                created,
                "[ResourcesPage] Yeni oluşturulan departman gridde bulunamadı: " + departmentName
        );
    }

    /**
     * Her çalıştırmada gerekirse sonuna 1,2,3... ekleyerek yeni kayıt oluşturur.
     */
    public void createDepartmentWithAutoIndex(String departmentName,
                                              String code,
                                              String name,
                                              String departmentType,
                                              String branch) {

        String baseName = name;

        searchForDepartmentAllowEmpty(baseName);

        WebElement existing = findDepartmentRow(baseName);
        String actualName = baseName;

        if (existing != null) {
            System.out.println("[ResourcesPage] '" + baseName +
                    "' zaten mevcut, indexli yeni kayıt oluşturulacak.");
            actualName = generateIndexedDepartmentName(baseName);
            System.out.println("[ResourcesPage] Yeni departman adı: '" + actualName + "'");
        } else {
            System.out.println("[ResourcesPage] '" + baseName +
                    "' bulunamadı, bu adla ilk kayıt oluşturulacak.");
        }

        openNewDepartmentDialog();
        fillNewDepartmentForm(code, actualName, departmentType, branch);
        clickDialogSave();

        searchForDepartmentAllowEmpty(actualName);
        WebElement created = findDepartmentRow(actualName);
        Assert.assertNotNull(
                created,
                "[ResourcesPage] Yeni oluşturulan departman gridde bulunamadı: " + actualName
        );

        this.lastCreatedDepartmentName = actualName;
    }

    // ================== DURUM DOĞRULAMA & HAZIRLAMA ==================

    /**
     * Departmanın durumunu istenen hale getirir; gerekirse aktif/pasif akışını tetikler.
     */
    public void ensureDepartmentStatus(String departmentName, String expectedStatus) {
        searchForDepartmentAllowEmpty(departmentName);
        waitForGridLoaded();

        String actualStatus = getDepartmentStatus(departmentName);
        System.out.println("[ResourcesPage] İlk okunan durum: '" + actualStatus + "'");

        if (actualStatus.equalsIgnoreCase(expectedStatus)) {
            System.out.println("[ResourcesPage] Departman zaten '" + expectedStatus +
                    "' durumda, ek aksiyon alınmayacak.");
        } else if (expectedStatus.equalsIgnoreCase("Aktif")
                && actualStatus.equalsIgnoreCase("Pasif")) {

            System.out.println("[ResourcesPage] Departman pasif, 'Aktif Et' akışı başlatılıyor...");
            activateDepartment(departmentName);
            actualStatus = getDepartmentStatus(departmentName);
        } else if (expectedStatus.equalsIgnoreCase("Pasif")
                && actualStatus.equalsIgnoreCase("Aktif")) {

            System.out.println("[ResourcesPage] Departman aktif, 'Pasif Et' akışı ileride eklenebilir.");
        } else {
            Assert.fail("[ResourcesPage] Beklenmeyen durum kombinasyonu. Beklenen: '" +
                    expectedStatus + "', ilk okunan: '" + actualStatus + "'");
        }

        Assert.assertTrue(
                actualStatus.equalsIgnoreCase(expectedStatus),
                "[ResourcesPage] Beklenen durum '" + expectedStatus +
                        "', fakat ekranda '" + actualStatus + "' görünüyor."
        );

        if (expectedStatus.equalsIgnoreCase("Aktif")) {
            System.out.println("[ResourcesPage] Sayfa yenilenerek durum tekrar kontrol ediliyor...");

            driver.navigate().refresh();
            waitForGridLoaded();

            searchForDepartment(departmentName);

            String statusAfterRefresh = getDepartmentStatus(departmentName);
            System.out.println("[ResourcesPage] Yenileme sonrası durum: '" + statusAfterRefresh + "'");

            Assert.assertTrue(
                    statusAfterRefresh.equalsIgnoreCase(expectedStatus),
                    "[ResourcesPage] Yenileme sonrası beklenen durum '" + expectedStatus +
                            "', fakat ekranda '" + statusAfterRefresh + "' görünüyor."
            );
        }
    }

    /**
     * Sadece mevcut durumu kontrol eder, aksiyon almaz.
     */
    public void verifyDepartmentStatusIs(String departmentName, String expectedStatus) {
        searchForDepartmentAllowEmpty(departmentName);

        String actualStatus = getDepartmentStatus(departmentName);

        Assert.assertTrue(
                actualStatus.equalsIgnoreCase(expectedStatus),
                "[ResourcesPage] Beklenen durum '" + expectedStatus +
                        "', fakat ekranda '" + actualStatus + "' görünüyor."
        );
    }

    @Deprecated
    public void verifyDepartmentStatus(String departmentName, String expectedStatus) {
        ensureDepartmentStatus(departmentName, expectedStatus);
    }

    public String getLastCreatedDepartmentName() {
        return lastCreatedDepartmentName;
    }
}

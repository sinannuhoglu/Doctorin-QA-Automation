package com.sinannuhoglu.pages.treatment.finanscard;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Tıbbi İşlemler > Finans Kartı
 */
public class TreatmentFinanceCardPage {

    private static final Logger LOGGER = LogManager.getLogger(TreatmentFinanceCardPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private String lastServiceDate;
    private String lastServiceTime;

    public TreatmentFinanceCardPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ==================== GENEL YARDIMCI METOTLAR ====================

    private void scrollIntoView(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        } catch (JavascriptException ignored) {
        }
    }

    private void safeClick(WebElement element) {
        scrollIntoView(element);
        try {
            element.click();
        } catch (ElementNotInteractableException e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    private void waitForLoadingToFinish() {
        By loadingSpinner = By.cssSelector(
                "div.e-spinner-pane[role='alert'][aria-label='Loading']"
        );
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (TimeoutException ignored) {
        }
    }

    // ==================== LOCATORLAR ====================

    /**
     * HİZMET ÖĞELERİ penceresinin dialog container'ı
     * (id: modal-dialog-..., içinde drug-search-section bulunuyor)
     */
    private final By serviceDialogContainer = By.xpath(
            "//div[contains(@id,'modal-dialog') and contains(@class,'e-dlg-container')" +
                    " and .//div[@data-testid='drug-search-section']]"
    );

    private final By servicesTabLabel = By.xpath(
            "//div[@data-testid='drug-search-section']" +
                    "//div[contains(@class,'template-tab-buttons')]" +
                    "//label[normalize-space()='Hizmetler']"
    );

    private final By searchDropdownWrapper = By.cssSelector("div.search-dropdown-wrapper");
    private final By serviceSearchInput = By.cssSelector(
            "div.search-dropdown-wrapper input[data-testid='admission-procedure-search']"
    );

    private final By procedureDropdownContainer = By.id("00000000-0000-0000-0000-000000000001");

    private By procedureItemByName(String serviceName) {
        return By.xpath(
                "//div[@id='00000000-0000-0000-0000-000000000001']" +
                        "//p[contains(@data-testid,'procedure-name') and normalize-space()='" + serviceName + "']" +
                        "/ancestor::div[contains(@data-testid,'procedure-')]"
        );
    }

    private final By firstRowDeleteButtonInDialog = By.xpath(
            "//div[contains(@id,'modal-dialog') and contains(@class,'e-dlg-container')" +
                    " and .//div[@data-testid='drug-search-section']]" +
                    "//div[contains(@class,'e-gridcontent')]" +
                    "//table//tbody/tr[1]/td[7]" +
                    "//button[contains(@class,'e-icon-btn')]" +
                    "[.//span[contains(@class,'e-icons') and contains(@class,'e-trash')]]"
    );

    private final By firstRowDateTimeInputInDialog = By.xpath(
            "//div[contains(@id,'modal-dialog') and contains(@class,'e-dlg-container')" +
                    " and .//div[@data-testid='drug-search-section']]" +
                    "//div[contains(@class,'e-gridcontent')]" +
                    "//table//tbody/tr[1]/td[3]" +
                    "//input[contains(@id,'datetimepicker')]"
    );

    private final By serviceDialogSaveButton = By.xpath(
            "//div[contains(@id,'modal-dialog') and contains(@class,'e-dlg-container')" +
                    " and .//div[@data-testid='drug-search-section']]" +
                    "//button[@type='submit' and " +
                    "(normalize-space()='Kaydet' or .//span[normalize-space()='Kaydet'])]"
    );

    private final By financeServiceRows = By.xpath(
            "//div[contains(@class,'flex-col') and contains(@class,'bg-white')" +
                    " and contains(@class,'dark:bg-surface-800') and contains(@class,'p-2')" +
                    " and contains(@class,'rounded-xl')]" +
                    "//table//tbody/tr"
    );

    private final By dotMenuButtonInRow = By.xpath(
            "./td[4]//button[contains(@class,'e-dropdown-btn') or @aria-haspopup='true']"
    );

    private final By actionsDropdownContainer = By.xpath(
            "//div[starts-with(@id,'e-dropdown-popup') and contains(@class,'e-dropdown-popup')]"
    );

    private final By editServiceMenuItem = By.xpath(
            "//div[starts-with(@id,'e-dropdown-popup') and contains(@class,'e-dropdown-popup')]" +
                    "//li[contains(@class,'e-item') and contains(normalize-space(),'Servis Öğesini Düzenle')]"
    );

    private final By cancelServiceMenuItem = By.xpath(
            "//div[starts-with(@id,'e-dropdown-popup') and contains(@class,'e-dropdown-popup')]" +
                    "//li[contains(@class,'e-item') and contains(normalize-space(),'Servis Öğesini İptal Et')]"
    );

    private final By editServiceDialogContainer = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]"
    );

    private final By editServiceDialogSaveButton = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//button[@type='submit' and (normalize-space()='Kaydet' or .//span[normalize-space()='Kaydet'])]"
    );

    private final By cancelConfirmDialogContainer = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]"
    );

    private final By cancelConfirmYesButton = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//button[contains(@class,'e-primary') and " +
                    "(normalize-space()='Evet' or .//span[normalize-space()='Evet'])]"
    );

    private final By emptyServiceTextInVisit = By.xpath(
            "//p[contains(@class,'font-semibold') and normalize-space()='Servis Öğesi Bulunamadı.']"
    );

    private final By activeVisitCard = By.xpath(
            "//div[@id='overview-admission-content']" +
                    "//div[contains(@id,'widget-')" +
                    " and .//div[contains(@class,'max-h-[462px]')]]"
    );

    private final By serviceItemsButtonInVisit = By.xpath(
            "//div[@id='overview-admission-content']" +
                    "//div[contains(@id,'widget-')" +
                    " and .//div[contains(@class,'max-h-[462px]')]]" +
                    "//div[contains(@class,'visit-card-table-tab')]" +
                    "//button[contains(@class,'e-btn') and contains(@class,'e-icon-btn')]"
    );

    private final By emptyServiceTile = By.xpath(
            "//p[normalize-space()='Servis Öğesi Bulunamadı.']" +
                    "/ancestor::div[contains(@class,'drop-shadow-[0_0_10px]')" +
                    " and contains(@class,'drop-shadow-primary-200/60')]"
    );

    // ==================== HİZMET ÖĞELERİ PENCERESİNİ AÇMA ====================

    /**
     * Finans kartında Hizmet Öğeleri penceresini açar.
     *
     * Durumlar:
     *  - Eğer vizit içinde Hizmet Listesi tablosu varsa: "Hizmet Listesi" başlığının yanındaki
     *    + (plus) butonuna tıklar.
     *  - Eğer hiç kayıt yoksa: "Servis Öğesi Bulunamadı." kartına tıklar.
     */
    public void openServiceItemsDialog() {
        LOGGER.info("[FinanceCard] Hizmet Öğeleri penceresi açılıyor...");

        waitForLoadingToFinish();

        try {
            WebElement visitCardContainer = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath(
                                    "//label[normalize-space()='Hizmet Listesi']" +
                                            "/ancestor::div[contains(@class,'rounded-xl')]" +
                                            "[contains(@class,'bg-white') and contains(@class,'dark:bg-surface-800')]"
                            )
                    )
            );
            scrollIntoView(visitCardContainer);

            List<WebElement> plusButtons = visitCardContainer.findElements(
                    By.xpath(".//button[.//span[contains(@class,'e-plus')]]")
            );

            if (!plusButtons.isEmpty()) {
                LOGGER.info("[FinanceCard] Vizit içinde + (Hizmet Listesi) butonu bulundu, tıklanıyor...");
                safeClick(plusButtons.get(0));
            } else {
                LOGGER.info("[FinanceCard] + butonu bulunamadı, 'Servis Öğesi Bulunamadı.' kartı aranıyor...");

                WebElement emptyTile = visitCardContainer.findElement(
                        By.xpath(
                                ".//p[normalize-space()='Servis Öğesi Bulunamadı.']" +
                                        "/ancestor::div[contains(@class,'bg-white') and contains(@class,'dark:bg-surface-800')]"
                        )
                );
                scrollIntoView(emptyTile);
                safeClick(emptyTile);
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(serviceDialogContainer));
            LOGGER.info("[FinanceCard] Hizmet Öğeleri penceresi açıldı.");

        } catch (TimeoutException | NoSuchElementException e) {
            LOGGER.error("[FinanceCard] Hizmet Öğeleri penceresi açılamadı (vizit kartı / + butonu / boş kart bulunamadı).", e);
            throw new TimeoutException("Hizmet Öğeleri penceresi açılamadı (vizit kartı / + butonu / boş kart bulunamadı).", e);
        }
    }

    // ==================== HİZMETLER SEKME ====================

    public void selectServicesTab() {
        LOGGER.info("[FinanceCard] 'Hizmetler' sekmesine tıklanıyor...");

        WebElement label = wait.until(
                ExpectedConditions.elementToBeClickable(servicesTabLabel)
        );
        scrollIntoView(label);
        safeClick(label);

        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {
        }
    }

    // ==================== HİZMET ARAMA & SEÇİM ====================

    public void searchAndSelectServiceByName(String serviceName) {
        LOGGER.info("[FinanceCard] Hizmet aranıyor ve seçiliyor -> '{}'", serviceName);

        WebElement wrapper = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchDropdownWrapper)
        );
        WebElement input = wrapper.findElement(serviceSearchInput);
        scrollIntoView(input);

        if (input.getAttribute("disabled") != null) {
            LOGGER.info("[FinanceCard] Hizmet arama input'u disabled görünüyor, JS ile enable ediliyor.");
            js.executeScript("arguments[0].removeAttribute('disabled');", input);
        }

        try {
            input.click();
        } catch (ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", input);
        }

        try {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            input.sendKeys(Keys.DELETE);
        } catch (Exception e) {
            js.executeScript("arguments[0].value='';", input);
        }

        input.sendKeys(serviceName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(procedureDropdownContainer));

        By serviceLocator = procedureItemByName(serviceName);
        WebElement item = wait.until(
                ExpectedConditions.elementToBeClickable(serviceLocator)
        );
        scrollIntoView(item);
        safeClick(item);

        waitForLoadingToFinish();
        LOGGER.info("[FinanceCard] Hizmet seçimi tamamlandı -> '{}'", serviceName);
    }

    // ==================== DIALOG İÇİ GRID İŞLEMLERİ ====================

    public void deleteFirstServiceRowIfExists() {
        LOGGER.info("[FinanceCard] Dialog içindeki ilk hizmet satırı siliniyor (varsa).");

        try {
            WebElement deleteBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(firstRowDeleteButtonInDialog)
            );
            scrollIntoView(deleteBtn);
            safeClick(deleteBtn);

            waitForLoadingToFinish();
            LOGGER.info("[FinanceCard] İlk hizmet satırı silindi.");
        } catch (TimeoutException e) {
            LOGGER.info("[FinanceCard] Silinecek hizmet satırı bulunamadı, işlem atlandı.");
        }
    }

    public void addServiceAgainAndCaptureDateTime(String serviceName) {
        LOGGER.info("[FinanceCard] Hizmet tekrar ekleniyor ve tarih-saat okunuyor -> '{}'", serviceName);

        searchAndSelectServiceByName(serviceName);

        WebElement dateTimeInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(firstRowDateTimeInputInDialog)
        );
        scrollIntoView(dateTimeInput);

        String value = dateTimeInput.getAttribute("value");
        LOGGER.info("[FinanceCard] Dialog grid tarih-saat değeri -> '{}'", value);

        parseAndStoreDateTime(value);
    }

    private void parseAndStoreDateTime(String dateTimeValue) {
        if (dateTimeValue == null || dateTimeValue.isBlank()) {
            LOGGER.warn("[FinanceCard] parseAndStoreDateTime -> Boş değer geldi.");
            lastServiceDate = null;
            lastServiceTime = null;
            return;
        }

        String[] parts = dateTimeValue.trim().split("\\s+");
        if (parts.length < 2) {
            LOGGER.warn("[FinanceCard] parseAndStoreDateTime -> Beklenen formatta değil: {}", dateTimeValue);
            lastServiceDate = dateTimeValue.trim();
            lastServiceTime = "";
            return;
        }

        String datePart = parts[0];
        String timePart = parts[1];

        try {
            String[] d = datePart.split("\\.");
            if (d.length == 3) {
                int day = Integer.parseInt(d[0]);
                int month = Integer.parseInt(d[1]);
                int year = Integer.parseInt(d[2]);
                lastServiceDate = day + "." + month + "." + year;
            } else {
                lastServiceDate = datePart;
            }
        } catch (Exception e) {
            LOGGER.warn("[FinanceCard] Tarih parse edilemedi, ham değer kullanılacak: {}", datePart);
            lastServiceDate = datePart;
        }

        lastServiceTime = timePart;
        LOGGER.info("[FinanceCard] Son hizmet tarihi: {}, saati: {}", lastServiceDate, lastServiceTime);
    }

    // ==================== KAYDET BUTONU (HİZMET ÖĞELERİ DİALOG) ====================

    public void clickServiceDialogSave() {
        LOGGER.info("[FinanceCard] Hizmet Öğeleri penceresinde Kaydet butonuna tıklanıyor...");

        WebElement saveBtn = wait.until(
                ExpectedConditions.elementToBeClickable(serviceDialogSaveButton)
        );
        scrollIntoView(saveBtn);
        safeClick(saveBtn);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(serviceDialogContainer));
        } catch (TimeoutException ignored) {
        }

        waitForLoadingToFinish();
        LOGGER.info("[FinanceCard] Hizmet Öğeleri penceresi Kaydet ile kapatıldı.");
    }

    // ==================== ORTAK: VİZİT SATIRI BULMA ====================

    private WebElement findVisitRowForLastService(String serviceName) {
        waitForLoadingToFinish();

        List<WebElement> rows = driver.findElements(financeServiceRows);
        LOGGER.info("[FinanceCard] Vizit içi hizmet listesinde toplam {} satır bulundu.", rows.size());

        for (WebElement row : rows) {
            try {
                WebElement firstTd = row.findElement(By.xpath("./td[1]"));
                String nameText = firstTd.getText().trim();

                if (!nameText.contains(serviceName)) {
                    continue;
                }

                WebElement thirdTd = row.findElement(By.xpath("./td[3]"));
                List<WebElement> ps = thirdTd.findElements(By.tagName("p"));
                if (ps.size() < 2) {
                    continue;
                }

                String dateText = ps.get(0).getText().trim();
                String timeText = ps.get(1).getText().trim();

                if (lastServiceDate == null || lastServiceTime == null) {
                    return row;
                }

                if (dateText.equals(lastServiceDate) && timeText.equals(lastServiceTime)) {
                    return row;
                }
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("[FinanceCard] Satır aranırken stale oluştu, diğer satıra geçiliyor.");
            }
        }

        LOGGER.warn("[FinanceCard] Eşleşen vizit satırı bulunamadı. (serviceName={}, date={}, time={})",
                serviceName, lastServiceDate, lastServiceTime);
        return null;
    }

    // ==================== VİZİT İÇİ DOĞRULAMA ====================

    public boolean isServiceShownInVisitWithLastDateTime(String serviceName) {
        LOGGER.info("[FinanceCard] Vizit içi hizmet listesinde '{}' ve tarih-saat doğrulanıyor...", serviceName);

        if (lastServiceDate == null || lastServiceTime == null) {
            LOGGER.warn("[FinanceCard] lastServiceDate/Time null, dialog tarafında tarih-saat okunamamış.");
            return false;
        }

        WebElement row = findVisitRowForLastService(serviceName);
        if (row == null) {
            return false;
        }

        try {
            WebElement thirdTd = row.findElement(By.xpath("./td[3]"));
            List<WebElement> ps = thirdTd.findElements(By.tagName("p"));

            if (ps.size() < 2) {
                LOGGER.warn("[FinanceCard] 3. TD'de beklenen 2 <p> elementi bulunamadı.");
                return false;
            }

            String dateText = ps.get(0).getText().trim();
            String timeText = ps.get(1).getText().trim();

            LOGGER.info("[FinanceCard] Satır tarih-saat -> '{}', '{}'", dateText, timeText);
            LOGGER.info("[FinanceCard] Beklenen tarih-saat -> '{}', '{}'", lastServiceDate, lastServiceTime);

            return dateText.equals(lastServiceDate) && timeText.equals(lastServiceTime);
        } catch (StaleElementReferenceException e) {
            LOGGER.warn("[FinanceCard] Doğrulama sırasında satır stale oldu.");
            return false;
        }
    }

    // ==================== VİZİTTE DÜZENLE ====================

    public void openEditServiceFromVisitActions(String serviceName) {
        LOGGER.info("[FinanceCard] Vizit hizmet satırında '{}' için Servis Öğesini Düzenle seçiliyor...", serviceName);

        WebElement row = findVisitRowForLastService(serviceName);
        if (row == null) {
            throw new NoSuchElementException("Vizit hizmet satırı bulunamadı: " + serviceName);
        }

        WebElement menuButton = row.findElement(dotMenuButtonInRow);
        scrollIntoView(menuButton);
        safeClick(menuButton);

        wait.until(ExpectedConditions.visibilityOfElementLocated(actionsDropdownContainer));

        WebElement editItem = wait.until(
                ExpectedConditions.elementToBeClickable(editServiceMenuItem)
        );
        scrollIntoView(editItem);
        safeClick(editItem);

        wait.until(ExpectedConditions.visibilityOfElementLocated(editServiceDialogContainer));
        LOGGER.info("[FinanceCard] Servis Öğesini Düzenle penceresi açıldı.");
    }

    public void clickEditServiceDialogSave() {
        LOGGER.info("[FinanceCard] Servis Öğesini Düzenle penceresinde Kaydet butonuna tıklanıyor...");

        WebElement saveBtn = wait.until(
                ExpectedConditions.elementToBeClickable(editServiceDialogSaveButton)
        );
        scrollIntoView(saveBtn);
        safeClick(saveBtn);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(editServiceDialogContainer));
        } catch (TimeoutException ignored) {
        }

        waitForLoadingToFinish();
        LOGGER.info("[FinanceCard] Servis Öğesini Düzenle penceresi Kaydet ile kapatıldı.");
    }

    // ==================== VİZİTTE İPTAL ET ====================

    public void openCancelServiceFromVisitActions(String serviceName) {
        LOGGER.info("[FinanceCard] Vizit hizmet satırında '{}' için Servis Öğesini İptal Et seçiliyor...", serviceName);

        WebElement row = findVisitRowForLastService(serviceName);
        if (row == null) {
            throw new NoSuchElementException("Vizit hizmet satırı bulunamadı: " + serviceName);
        }

        WebElement menuButton = row.findElement(dotMenuButtonInRow);
        scrollIntoView(menuButton);
        safeClick(menuButton);

        wait.until(ExpectedConditions.visibilityOfElementLocated(actionsDropdownContainer));

        WebElement cancelItem = wait.until(
                ExpectedConditions.elementToBeClickable(cancelServiceMenuItem)
        );
        scrollIntoView(cancelItem);
        safeClick(cancelItem);

        wait.until(ExpectedConditions.visibilityOfElementLocated(cancelConfirmDialogContainer));
        LOGGER.info("[FinanceCard] Servis Öğesini İptal Et onay penceresi açıldı.");
    }

    public void clickCancelConfirmYes() {
        LOGGER.info("[FinanceCard] Servis Öğesini İptal Et onay penceresinde Evet butonuna tıklanıyor...");

        WebElement yesBtn = wait.until(
                ExpectedConditions.elementToBeClickable(cancelConfirmYesButton)
        );
        scrollIntoView(yesBtn);
        safeClick(yesBtn);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(cancelConfirmDialogContainer));
        } catch (TimeoutException ignored) {
        }

        waitForLoadingToFinish();
        LOGGER.info("[FinanceCard] Servis Öğesini İptal Et işlemi tamamlandı.");
    }

    // ==================== VİZİTTE SİLİNDİ Mİ? ====================

    public boolean isServiceRemovedFromVisit(String serviceName) {
        LOGGER.info("[FinanceCard] Vizit içi hizmet listesinde '{}' kaydının silindiği kontrol ediliyor...",
                serviceName);

        waitForLoadingToFinish();

        try {
            WebElement emptyText = driver.findElement(emptyServiceTextInVisit);
            if (emptyText.isDisplayed()) {
                LOGGER.info("[FinanceCard] Vizit hizmet listesi boş, 'Servis Öğesi Bulunamadı.' görüldü.");
                return true;
            }
        } catch (NoSuchElementException ignored) {
        }

        List<WebElement> rows = driver.findElements(financeServiceRows);
        LOGGER.info("[FinanceCard] Vizit içi hizmet listesinde iptal sonrası {} satır bulundu.", rows.size());

        for (WebElement row : rows) {
            try {
                WebElement firstTd = row.findElement(By.xpath("./td[1]"));
                String nameText = firstTd.getText().trim();

                if (!nameText.contains(serviceName)) {
                    continue;
                }

                WebElement thirdTd = row.findElement(By.xpath("./td[3]"));
                List<WebElement> ps = thirdTd.findElements(By.tagName("p"));
                if (ps.size() < 2) {
                    continue;
                }

                String dateText = ps.get(0).getText().trim();
                String timeText = ps.get(1).getText().trim();

                LOGGER.info("[FinanceCard] Kalan satır -> ad='{}', tarih='{}', saat='{}'",
                        nameText, dateText, timeText);

                if (lastServiceDate != null && lastServiceTime != null &&
                        dateText.equals(lastServiceDate) && timeText.equals(lastServiceTime)) {
                    LOGGER.warn("[FinanceCard] İptal edilen kayıt vizit listesinde halen görünüyor.");
                    return false;
                }

            } catch (StaleElementReferenceException e) {
                LOGGER.warn("[FinanceCard] Silinme kontrolü sırasında stale oluştu, diğer satıra geçiliyor.");
            }
        }

        LOGGER.info("[FinanceCard] İptal edilen hizmet kaydı vizit listesinde bulunamadı, silinmiş görünüyor.");
        return true;
    }
}

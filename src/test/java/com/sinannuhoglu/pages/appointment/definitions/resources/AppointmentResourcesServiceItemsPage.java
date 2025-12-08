package com.sinannuhoglu.pages.appointment.definitions.resources;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Randevu Kaynakları > Servis Öğeleri sekmesi için Page Object
 */
public class AppointmentResourcesServiceItemsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AppointmentResourcesServiceItemsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // ----------------------------------------------------------------
    // GENEL HELPER'LAR
    // ----------------------------------------------------------------

    private JavascriptExecutor js() {
        return (JavascriptExecutor) driver;
    }

    private void scrollIntoViewCenter(WebElement element) {
        try {
            js().executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        } catch (Exception ignored) {
        }
    }

    /**
     * Elemanı görünür alana kaydırır, tıklanabilir olmasını bekler ve tıklar.
     * Gerekirse JS click ile fallback yapar.
     */
    private void safeClick(WebElement element) {
        scrollIntoViewCenter(element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            try {
                js().executeScript("arguments[0].click();", element);
            } catch (Exception ignored) {
            }
        }
    }

    // ----------------------------------------------------------------
    // LOCATORS – Genel
    // ----------------------------------------------------------------

    private final By toolbarItems =
            By.cssSelector("div.e-toolbar div.e-toolbar-items div.e-toolbar-item");

    private static final String SERVICE_ITEMS_TAB_TEXT = "Servis Öğeleri";

    private final By serviceItemsGridTable = By.xpath(
            "//div[contains(@class,'e-grid')]//table[contains(@class,'e-table')]"
    );

    private final By addNewButton = By.xpath(
            "//button[.//span[normalize-space()='Yeni Ekle'] or normalize-space()='Yeni Ekle']"
    );

    private final By serviceItemsForm = By.xpath(
            "//form[contains(@class,'e-data-form')]"
    );

    private final By serviceItemLabel = By.xpath(
            "//form[contains(@class,'e-data-form')]//label[normalize-space()='Servis Öğesi']"
    );

    private final By serviceItemDropdownWrapper = By.xpath(
            "//form[contains(@class,'e-data-form')]" +
                    "//div[contains(@class,'e-form-group')]" +
                    "[.//label[normalize-space()='Servis Öğesi']]" +
                    "//*[contains(@class,'e-control-wrapper') and contains(@class,'e-ddl')]"
    );

    private final By appointmentTypeDropdownWrapper = By.xpath(
            "//form[contains(@class,'e-data-form')]" +
                    "//div[contains(@class,'e-form-group')]" +
                    "[.//label[normalize-space()='Randevu Tipi']]" +
                    "//*[contains(@class,'e-control-wrapper') and contains(@class,'e-ddl')]"
    );

    private final By saveButtonBy = By.xpath(
            "//form[contains(@class,'e-data-form')]" +
                    "//button[.//span[normalize-space()='Kaydet'] or normalize-space()='Kaydet']"
    );

    // ----------------------------------------------------------------
    // SEKME İŞLEMLERİ
    // ----------------------------------------------------------------

    /**
     * Toolbar'daki "Servis Öğeleri" sekmesine geçer.
     * Burada sadece sekmenin aktif olmasını ve gridin görünmesini bekleriz.
     */
    public void openServiceItemsTab() {

        List<WebElement> tabs = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(toolbarItems)
        );

        WebElement serviceItemsTab = null;

        for (WebElement item : tabs) {
            try {
                WebElement textEl = item.findElement(By.cssSelector(".e-tab-text"));
                String text = textEl.getText().trim();
                System.out.println("[ServiceItems] Tab text: '" + text + "'");
                if (SERVICE_ITEMS_TAB_TEXT.equals(text)) {
                    serviceItemsTab = item;
                    break;
                }
            } catch (StaleElementReferenceException | NoSuchElementException ignored) {
            }
        }

        if (serviceItemsTab == null) {
            throw new NoSuchElementException(
                    "[ServiceItems] Toolbar'da '" + SERVICE_ITEMS_TAB_TEXT + "' sekmesi bulunamadı."
            );
        }

        safeClick(serviceItemsTab);

        wait.until(d -> {
            try {
                List<WebElement> items = d.findElements(toolbarItems);
                for (WebElement item : items) {
                    WebElement textEl = item.findElement(By.cssSelector(".e-tab-text"));
                    String text = textEl.getText().trim();
                    if (SERVICE_ITEMS_TAB_TEXT.equals(text)
                            && item.getAttribute("class") != null
                            && item.getAttribute("class").contains("e-active")) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        });

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(serviceItemsGridTable));
        } catch (TimeoutException ignored) {
        }

        System.out.println("[ServiceItems] 'Servis Öğeleri' sekmesi açıldı ve grid yüklendi.");
    }

    // ----------------------------------------------------------------
    // YENİ EKLE BUTONU
    // ----------------------------------------------------------------

    /**
     * Servis Öğeleri sekmesinde "Yeni Ekle" butonuna tıklar.
     */
    public void clickAddNewServiceItem() {

        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(addNewButton)
        );

        safeClick(button);

        System.out.println("[ServiceItems] 'Yeni Ekle' butonuna tıklandı.");

        // Yeni Ekle’den sonra formun gelmesini bekle
        waitForServiceItemsForm();
    }

    // ----------------------------------------------------------------
    // FORM & DROPDOWN HELPER'LARI
    // ----------------------------------------------------------------

    /** Servis Öğeleri formunun ve "Servis Öğesi" label'ının görünmesini bekler. */
    private void waitForServiceItemsForm() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(serviceItemsForm));
        wait.until(ExpectedConditions.visibilityOfElementLocated(serviceItemLabel));

        System.out.println("[ServiceItems] Servis Öğeleri formu açıldı (Servis Öğesi label göründü).");
    }

    /** Verilen dropdown wrapper içindeki ikon'a tıklar. */
    private void openDropdown(WebElement wrapper) {
        WebElement icon = wrapper.findElement(
                By.cssSelector("span.e-input-group-icon.e-ddl-icon")
        );
        safeClick(icon);
    }

    /** Açık durumdaki dropdown popup'ından ilgili option'ı seçer. */
    private void selectFromOpenDropdown(String optionText) {

        By popupBy = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open[role='dialog']"
        );

        WebElement popup = wait.until(
                ExpectedConditions.visibilityOfElementLocated(popupBy)
        );

        WebElement option = popup.findElement(
                By.xpath(".//li[normalize-space()='" + optionText + "']")
        );

        safeClick(option);
    }

    // ----------------------------------------------------------------
    // FORM İŞLEMLERİ (Servis Öğesi + Randevu Tipi)
    // ----------------------------------------------------------------

    /**
     * Servis Öğeleri formunda "Servis Öğesi" dropdown'ından istenen seçeneği seçer.
     */
    public void selectServiceItem(String optionText) {

        waitForServiceItemsForm();

        WebElement wrapper = wait.until(
                ExpectedConditions.visibilityOfElementLocated(serviceItemDropdownWrapper)
        );

        scrollIntoViewCenter(wrapper);
        openDropdown(wrapper);
        selectFromOpenDropdown(optionText);

        System.out.println("[ServiceItems] Servis Öğesi seçildi: " + optionText);
    }

    /**
     * Servis Öğeleri formunda "Randevu Tipi" dropdown'ından istenen seçeneği seçer.
     */
    public void selectAppointmentType(String appointmentTypeText) {

        WebElement wrapper = wait.until(
                ExpectedConditions.visibilityOfElementLocated(appointmentTypeDropdownWrapper)
        );

        scrollIntoViewCenter(wrapper);
        openDropdown(wrapper);
        selectFromOpenDropdown(appointmentTypeText);

        System.out.println("[ServiceItems] Randevu Tipi seçildi: " + appointmentTypeText);
    }

    /** Servis Öğeleri formunda Kaydet butonuna tıklar. */
    public void clickSaveButton() {

        WebElement saveBtn = wait.until(
                ExpectedConditions.elementToBeClickable(saveButtonBy)
        );

        safeClick(saveBtn);

        System.out.println("[ServiceItems] Kaydet butonuna tıklandı.");

        // Formun kapanmasını bekleyebiliriz (ortamdan ortama gecikme olabilir)
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(serviceItemsForm));
        } catch (TimeoutException ignored) {
        }
    }

    // ----------------------------------------------------------------
    // GRID HELPER – satır xpath'i
    // ----------------------------------------------------------------

    /**
     * Grid_content_table içinde, 1. sütunda servis öğesi, 3. sütunda randevu tipi olan satırı bulmak için xpath üretir.
     */
    private String buildServiceItemRowXpath(String serviceItemName, String appointmentType) {
        return String.format(
                "//table[@id='Grid_content_table']//tbody/tr[" +
                        ".//td[@role='gridcell' and @aria-colindex='1' and normalize-space()='%s'] and " +
                        ".//td[@role='gridcell' and @aria-colindex='3' and normalize-space()='%s']" +
                        "]",
                serviceItemName,
                appointmentType
        );
    }

    // ----------------------------------------------------------------
    // GRID DOĞRULAMA
    // ----------------------------------------------------------------

    /**
     * Servis Öğeleri grid'inde:
     *  1. sütunda expectedServiceItem
     *  3. sütunda expectedAppointmentType
     *  değerlerine sahip satırın oluştuğunu doğrular.
     */
    public void verifyServiceItemRow(String expectedServiceItem, String expectedAppointmentType) {
        String logPrefix = "[ServiceItems] ";

        System.out.printf(
                "%sGrid satırı doğrulanıyor. Beklenen Servis Öğesi='%s', Randevu Tipi='%s'%n",
                logPrefix, expectedServiceItem, expectedAppointmentType
        );

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div.e-dialog, div[role='dialog']"))
            );
        } catch (TimeoutException ignored) {
        }

        try {
            By loadingMask = By.cssSelector(
                    "div.e-grid .e-gridcontent .e-spinner-pane, " +
                            "div.e-grid .e-gridcontent .e-spinner-overlay"
            );
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingMask));
        } catch (Exception ignored) {
        }

        String rowXpath = buildServiceItemRowXpath(expectedServiceItem, expectedAppointmentType);
        By rowLocator = By.xpath(rowXpath);

        try {
            WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));
            scrollIntoViewCenter(row);
            System.out.printf("%sBeklenen satır bulundu.%n", logPrefix);

            WebElement serviceCell = row.findElement(
                    By.xpath(".//td[@role='gridcell' and @aria-colindex='1']")
            );
            WebElement appointmentTypeCell = row.findElement(
                    By.xpath(".//td[@role='gridcell' and @aria-colindex='3']")
            );

            System.out.printf(
                    "%sRow -> [1.sütun='%s', 3.sütun='%s']%n",
                    logPrefix,
                    serviceCell.getText().trim(),
                    appointmentTypeCell.getText().trim()
            );

        } catch (TimeoutException e) {
            throw new AssertionError(String.format(
                    "%sGrid'de beklenen satır bulunamadı. Servis Öğesi='%s', Randevu Tipi='%s'",
                    logPrefix, expectedServiceItem, expectedAppointmentType
            ), e);
        }
    }

    /**
     * Servis Öğeleri grid'inde ilgili satırın artık görünmediğini (silindiğini) doğrular.
     */
    public void verifyServiceItemRowDeleted(String expectedServiceItem, String expectedAppointmentType) {
        String logPrefix = "[ServiceItems] ";

        String rowXpath = buildServiceItemRowXpath(expectedServiceItem, expectedAppointmentType);
        By rowLocator = By.xpath(rowXpath);

        try {
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(rowLocator));
            if (!invisible) {
                throw new AssertionError(String.format(
                        "%sGrid'de kayıt halen görünüyor. Servis Öğesi='%s', Randevu Tipi='%s'",
                        logPrefix, expectedServiceItem, expectedAppointmentType
                ));
            }
            System.out.printf(
                    "%sGrid'de ilgili satırın silindiği doğrulandı. Servis Öğesi='%s', Randevu Tipi='%s'%n",
                    logPrefix, expectedServiceItem, expectedAppointmentType
            );
        } catch (TimeoutException e) {
            throw new AssertionError(String.format(
                    "%sGrid'de ilgili satırın silinmesi bekleniyordu ancak görünür durumda. Servis Öğesi='%s', Randevu Tipi='%s'",
                    logPrefix, expectedServiceItem, expectedAppointmentType
            ), e);
        }
    }

    // ----------------------------------------------------------------
    // GRID – ÜÇ NOKTA MENÜSÜ & SİLME
    // ----------------------------------------------------------------

    /**
     * Verilen servis öğesi + randevu tipi kombinasyonuna sahip satırdaki üç nokta menüsünü açar.
     */
    public void openActionMenuForServiceItem(String serviceItemName, String appointmentType) {
        String logPrefix = "[ServiceItems] ";

        String rowXpath = buildServiceItemRowXpath(serviceItemName, appointmentType);
        String actionButtonXpath = rowXpath
                + "//td[@role='gridcell' and @aria-colindex='4']"
                + "//button[contains(@class,'e-dropdown-btn')]";

        By actionButtonLocator = By.xpath(actionButtonXpath);

        WebElement actionButton = wait.until(
                ExpectedConditions.elementToBeClickable(actionButtonLocator)
        );

        scrollIntoViewCenter(actionButton);
        safeClick(actionButton);

        System.out.printf(
                "%sServis Öğesi satırı için üç nokta menüsü açıldı. Servis Öğesi='%s', Randevu Tipi='%s'%n",
                logPrefix, serviceItemName, appointmentType
        );
    }

    /**
     * Açık durumdaki üç nokta menüsünden "Sil" seçeneğine tıklar.
     */
    public void clickDeleteOnActionMenu() {
        By deleteOptionLocator = By.xpath(
                "//ul[contains(@class,'e-dropdown-menu') and @role='menu']" +
                        "//li[@role='menuitem' and normalize-space()='Sil']"
        );

        WebElement deleteOption = wait.until(
                ExpectedConditions.visibilityOfElementLocated(deleteOptionLocator)
        );
        safeClick(deleteOption);
        System.out.println("[ServiceItems] Üç nokta menüsünden 'Sil' seçeneğine tıklandı.");
    }

    /**
     * Silme onay dialog'unda "Evet" butonuna tıklar.
     */
    public void confirmDeletionYes() {
        By yesButtonLocator = By.xpath(
                "//div[contains(@class,'e-dlg-container')]//button[normalize-space()='Evet']"
        );

        WebElement yesButton = wait.until(
                ExpectedConditions.elementToBeClickable(yesButtonLocator)
        );

        safeClick(yesButton);
        System.out.println("[ServiceItems] Silme onay penceresinde 'Evet' butonuna tıklandı.");
    }
}

package com.sinannuhoglu.pages.treatment.finanscard;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class TreatmentFinanceCardTreatmentPlanPage {

    private static final Logger LOGGER = LogManager.getLogger(TreatmentFinanceCardTreatmentPlanPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public TreatmentFinanceCardTreatmentPlanPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ---------- Locators ----------

    private final By treatmentPlansHeader = By.xpath(
            "//p[contains(@class,'text-sm') and contains(normalize-space(),'Tedavi Planları')]"
    );

    private final By treatmentPlansToggleButton = By.xpath(
            "//p[contains(@class,'text-sm') and contains(normalize-space(),'Tedavi Planları')]" +
                    "/ancestor::div[contains(@class,'flex items-center justify-between')][1]" +
                    "//i[contains(@class,'hio-chevron-right')]"
    );

    private final By treatmentPlansDrawerButton = By.xpath(
            "//p[contains(@class,'text-sm') and contains(normalize-space(),'Tedavi Planları')]" +
                    "/ancestor::div[contains(@class,'flex items-center justify-between')][1]" +
                    "//button[.//span[contains(@class,'hio-arrow-rtl') and contains(@class,'e-btn-icon')]]"
    );

    private final By treatmentPlansDrawerTable = By.xpath(
            "//div[contains(@class,'e-gridcontent')]//table[contains(@class,'e-table')]"
    );

    private final By treatmentPlanDialog = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]"
    );

    private final By allServiceItemsTab = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//label[contains(normalize-space(),'Tüm Servis Öğeleri')]"
    );

    private final By serviceSearchInput = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//input[@type='text' and @placeholder='Servis Öğesi Ara']"
    );

    private final By serviceSearchResultContainer = By.id("00000000-0000-0000-0000-000000000001");

    private final By planNameInput = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//div[contains(@class,'e-form-group') and .//label[contains(normalize-space(),'Plan Adı')]]" +
                    "//input"
    );

    private final By discountInput = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//tr[.//p[normalize-space()='İndirim']]" +
                    "//input[@type='number' or @role='spinbutton']"
    );

    private final By discountApplyButton = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//tr[.//p[normalize-space()='İndirim']]" +
                    "//button[.//span[contains(@class,'his-check')]]"
    );

    private final By treatmentPlanSaveButton = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//button[@type='submit' and contains(normalize-space(),'Kaydet')]"
    );

    private final By invoicePlusButton = By.xpath(
            "//form[@id='advance-create-form']" +
                    "//button[@type='submit' and .//span[contains(@class,'e-plus')]]"
    );

    private final By invoiceSaveButton = By.xpath(
            "//button[contains(@class,'e-btn') and contains(normalize-space(),'Fatura Oluştur')]"
    );

    private final By treatmentPlansNewButton = By.xpath(
            "//div[contains(@class,'bg-white') and .//p[contains(normalize-space(),'Tedavi Planları')]]" +
                    "//button[.//span[contains(@class,'hio-plus')] or contains(normalize-space(),'Yeni Ekle')]"
    );

    private final By treatmentPlansListContainer = By.xpath(
            "//div[contains(@class,'panel') and .//p[contains(normalize-space(),'Tedavi Planları')]]"
    );

    // ---------- Helpers ----------

    private void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    private void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    // ================= TEDAVİ PLANLARI PANELİ =================

    /** Sol taraftaki Tedavi Planları panelini açık hale getirir (Yeni Ekle butonunu gösterir). */
    public void openTreatmentPlansPanel() {
        LOGGER.info("[FinansKart][TedaviPlanları] paneli açılıyor...");

        WebElement header = wait.until(
                ExpectedConditions.visibilityOfElementLocated(treatmentPlansHeader)
        );
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", header);

        if (driver.findElements(treatmentPlansNewButton).isEmpty()) {
            LOGGER.info("[FinansKart][TedaviPlanları] panel kapalı, açmak için chevron/başlık tıklanıyor...");

            try {
                WebElement chevron = header.findElement(By.xpath(
                        "./ancestor::div[contains(@class,'flex items-center justify-between')][1]" +
                                "//i[contains(@class,'hio-chevron-right')]"
                ));
                js.executeScript("arguments[0].click();", chevron);
            } catch (NoSuchElementException e) {
                js.executeScript("arguments[0].click();", header);
            }
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(treatmentPlansNewButton));
        LOGGER.info("[FinansKart][TedaviPlanları] panel açık ve 'Yeni Ekle' butonu görünüyor.");
    }

    /** Panel içindeki Yeni Ekle butonuna tıklayıp servis seçtiğimiz Tedavi Planı modalini açar. */
    public void openNewTreatmentPlanDialog() {
        LOGGER.info("[FinansKart][TedaviPlanları] 'Yeni Ekle' butonuna tıklanıyor...");

        openTreatmentPlansPanel();

        WebElement yeniEkle = wait.until(
                ExpectedConditions.elementToBeClickable(treatmentPlansNewButton)
        );
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", yeniEkle);
        js.executeScript("arguments[0].click();", yeniEkle);

        wait.until(ExpectedConditions.visibilityOfElementLocated(treatmentPlanDialog));

        LOGGER.info("[FinansKart][TedaviPlanları] Tedavi Planı oluşturma penceresi açıldı.");
    }

    // ================= TEDAVİ PLANI OLUŞTURMA =================

    /**
     * Tedavi Planı dialog'u içinde:
     * - 'Tüm Servis Öğeleri' sekmesine geçer
     * - Servis Ara alanına serviceName yazar ve ENTER ile aramayı tetikler, listeden seçer
     * - Plan Adı = planName
     * - İndirim = discountRate
     * - Kaydet butonuna basar
     */
    public void createDiscountedTreatmentPlan(String serviceName, String planName, int discountRate) {
        LOGGER.info("[FinansKart][TedaviPlanları] Tedavi Planı oluşturuluyor. Servis: {}, Plan: {}, İndirim: {}",
                serviceName, planName, discountRate);

        click(allServiceItemsTab);

        WebElement searchInput = wait.until(
                ExpectedConditions.elementToBeClickable(serviceSearchInput)
        );
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", searchInput);
        searchInput.click();
        searchInput.clear();
        searchInput.sendKeys(serviceName);
        searchInput.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.visibilityOfElementLocated(serviceSearchResultContainer));

        By serviceOption = By.xpath(
                "//div[@id='00000000-0000-0000-0000-000000000001']" +
                        "//p[contains(@class,'font-normal') and normalize-space()='" + serviceName + "']"
        );
        WebElement option = wait.until(
                ExpectedConditions.elementToBeClickable(serviceOption)
        );
        js.executeScript("arguments[0].click();", option);
        LOGGER.info("[FinansKart][TedaviPlanları] '{}' servisi listeden seçildi.", serviceName);

        type(planNameInput, planName);

        type(discountInput, String.valueOf(discountRate));
        click(discountApplyButton);

        click(treatmentPlanSaveButton);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(treatmentPlanDialog));
        LOGGER.info("[FinansKart][TedaviPlanları] Tedavi Planı başarıyla kaydedildi.");
    }

    // ================= FATURA PENCERESİ =================

    /** Fatura penceresinde '+' ikonuna tıklar ve belirtilen süre kadar bekler. */
    public void clickInvoicePlusAndWait(int seconds) {
        LOGGER.info("[FinansKart][TedaviPlanları] Fatura penceresinde '+' ikonuna tıklanıyor ve {} saniye bekleniyor...", seconds);
        click(invoicePlusButton);

        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Fatura penceresindeki 'Fatura Oluştur' (Kaydet) butonuna tıklar. */
    public void clickInvoiceSaveButton() {
        LOGGER.info("[FinansKart][TedaviPlanları] Fatura penceresinde 'Fatura Oluştur' butonuna tıklanıyor...");
        click(invoiceSaveButton);

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ================= DOĞRULAMA =================

    /** Eski panel içi basit doğrulama – başka senaryolar kullanıyorsa kalsın. */
    public void assertTreatmentPlanExists(String planName) {
        LOGGER.info("[FinansKart][TedaviPlanları] Panel listesinde '{}' planının oluştuğu doğrulanıyor...", planName);

        By planRowLocator = By.xpath(
                "//div[contains(@class,'panel') and .//p[contains(normalize-space(),'Tedavi Planları')]]" +
                        "//p[contains(normalize-space(),'" + planName + "')]"
        );

        WebElement planElement = wait.until(ExpectedConditions.visibilityOfElementLocated(planRowLocator));
        Assert.assertTrue(planElement.isDisplayed(),
                "Tedavi Planları listesinde beklenen plan bulunamadı: " + planName);
    }

    /**
     * Nihai doğrulama:
     *  - Sağdan açılan Tedavi Planları penceresini (drawer) span.hio-arrow-rtl e-btn-icon butonuyla açar
     *  - Grid içindeki <tr> satırında:
     *      3. <td> altındaki ilk <p> == planName
     *      4. <td> içinde statusText ('Faturalandı') geçen bir element
     *    olup olmadığını kontrol eder.
     */
    public void assertTreatmentPlanInDialogWithStatus(String planName, String statusText) {
        LOGGER.info("[FinansKart][TedaviPlanları] Tedavi Planı penceresinde '{}' planı için '{}' statüsü kontrol ediliyor...",
                planName, statusText);

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(treatmentPlansHeader));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", header);

        WebElement drawerButton = wait.until(
                ExpectedConditions.elementToBeClickable(treatmentPlansDrawerButton)
        );
        js.executeScript("arguments[0].click();", drawerButton);

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(treatmentPlansDrawerTable));

        By rowLocator = By.xpath(
                "//div[contains(@class,'e-gridcontent')]//table[contains(@class,'e-table')]/tbody/tr" +
                        "[.//td[3]//p[normalize-space()='" + planName + "'] " +
                        " and .//td[4]//*[contains(normalize-space(),'" + statusText + "')]]"
        );

        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));

        WebElement planCell = row.findElement(By.xpath(".//td[3]//p"));
        String actualPlanName = planCell.getText().trim();
        Assert.assertEquals(actualPlanName, planName,
                "3. sütundaki plan adı beklenenle uyuşmuyor. Beklenen: " + planName + ", Gerçek: " + actualPlanName);

        WebElement statusElement = row.findElement(
                By.xpath(".//td[4]//*[contains(normalize-space(),'" + statusText + "')]")
        );
        Assert.assertTrue(statusElement.isDisplayed(),
                "4. sütunda beklenen statü bulunamadı: " + statusText);

        LOGGER.info("[FinansKart][TedaviPlanları] Grid satırında '{}' planı için '{}' statüsü başarıyla doğrulandı.",
                planName, statusText);
    }
}

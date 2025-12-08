package com.sinannuhoglu.pages.treatment.medicalcard;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Tıbbi İşlemler > İş Listesi > Detay > Muayene sekmesi
 * "Hasta Özgeçmişi" (Patient History) alanı Page Object
 *
 * URL:
 * - Doğrudan URL ile gidilmez; Work List üzerinden hasta detayı açılarak erişilir.
 */
public class TreatmentExaminationPatientHistoryPage {

    private static final Logger LOGGER = LogManager.getLogger(TreatmentExaminationPatientHistoryPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public TreatmentExaminationPatientHistoryPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ===================== LOCATORS ======================

    /**
     * Muayene sekmesindeki kart container (Hasta Özgeçmişi kartı vb.)
     */
    private final By examinationCardsContainer = By.xpath(
            "//div[contains(@class,'relative') and contains(@class,'h-full') and contains(@class,'overflow-auto')]"
    );

    /**
     * "Hasta Özgeçmişi" kartı:
     *  - bg-white rounded-xl dark:bg-surface-800 overflow-visible p-3
     *  - İçinde ikon: span.shrink-0.hgo.hgo-health-mask ...
     */
    private final By patientHistoryCard = By.xpath(
            "//div[contains(@class,'bg-white') and contains(@class,'rounded-xl') and contains(@class,'overflow-visible')]" +
                    "[.//span[contains(@class,'hgo-health-mask')]]"
    );

    /**
     * Hasta Özgeçmişi kartı içindeki slide-over açan e-icon-btn butonu
     */
    private final By patientHistoryCardIconButtonInsideCard = By.xpath(
            ".//button[contains(@class,'e-icon-btn')]"
    );

    /**
     * Hasta Özgeçmişi içindeki "Alerjiler" alanı
     */
    private final By allergiesSection = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//div[contains(@class,'break-inside-avoid-column') and contains(@class,'mb-4')]" +
                    "[.//h4[contains(normalize-space(),'Alerjiler')]]"
    );

    /**
     * Alerjiler form grid (grid grid-cols-4 gap-2)
     */
    private final By allergiesFormGrid = By.xpath(
            ".//div[contains(@class,'grid') and contains(@class,'grid-cols-4') and contains(@class,'gap-2')]"
    );

    /**
     * Alerjiler formundaki "Ekle" butonu
     */
    private final By allergiesAddButton = By.xpath(
            ".//button[@type='submit' and (normalize-space()='Ekle' or .//span[contains(normalize-space(),'Ekle')])]"
    );

    // ===================== HELPER METOTLAR ======================

    private void scrollIntoView(WebElement el) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (JavascriptException ignored) {
        }
    }

    private By buildAllergyRowLocator(String allergen, String description) {
        // NOT: td[1] = Alerjen, td[3] = Açıklama (UI yapısına göre)
        String xpath = "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]"
                + "//div[contains(@class,'break-inside-avoid-column') and contains(@class,'mb-4')]"
                + "[.//h4[contains(normalize-space(),'Alerjiler')]]"
                + "//table[contains(@class,'table-fixed')]//tbody//tr"
                + "[.//td[1]//p[normalize-space()='" + allergen + "']"
                + " and .//td[3]//p[normalize-space()='" + description + "']]";
        return By.xpath(xpath);
    }

    /**
     * Tıklama işlemi için küçük retry içeren güvenli metot.
     */
    private void safeClick(By locator) {
        RuntimeException lastException = null;

        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                scrollIntoView(el);
                el.click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                lastException = e;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (lastException != null) {
            throw lastException;
        } else {
            throw new RuntimeException("safeClick: element could not be clicked for locator: " + locator);
        }
    }

    private void safeClick(WebElement el) {
        scrollIntoView(el);
        try {
            el.click();
        } catch (ElementNotInteractableException e) {
            js.executeScript("arguments[0].click();", el);
        }
    }

    private void clearAndType(WebElement el, String text) {
        scrollIntoView(el);

        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", el);
        }

        // CTRL+A / CMD+A + DELETE
        try {
            el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            el.sendKeys(Keys.chord(Keys.COMMAND, "a"));
            el.sendKeys(Keys.DELETE);
            el.sendKeys(Keys.BACK_SPACE);
        } catch (Exception ignored) {
        }

        try {
            String current = el.getAttribute("value");
            if (current != null && !current.isEmpty()) {
                for (int i = 0; i < current.length(); i++) {
                    el.sendKeys(Keys.BACK_SPACE);
                }
            }
        } catch (Exception ignored) {
        }

        el.sendKeys(text);
    }

    // ===================== ACTIONS – HASTA ÖZGEÇMİŞİ PANELİ ======================

    /**
     * Muayene sekmesinde "Hasta Özgeçmişi" kartındaki butona tıklayarak
     * Hasta Özgeçmişi slide-over penceresini açar.
     */
    public void openPatientHistoryPanel() {
        LOGGER.info("[TreatmentExaminationPatientHistoryPage] openPatientHistoryPanel");

        // Muayene kartlarının yüklendiğini bekle
        wait.until(ExpectedConditions.visibilityOfElementLocated(examinationCardsContainer));

        // Hasta Özgeçmişi kartını ikonuna göre bul
        WebElement card = wait.until(
                ExpectedConditions.visibilityOfElementLocated(patientHistoryCard)
        );
        scrollIntoView(card);

        // Kart içindeki e-icon-btn (sağdaki ok butonu) tıklanır
        WebElement openButton = card.findElement(patientHistoryCardIconButtonInsideCard);
        safeClick(openButton);

        // Panel açıldı mı? "Alerjiler" bölümünün gelmesini beklemek yeterli
        wait.until(ExpectedConditions.visibilityOfElementLocated(allergiesSection));
    }

    // ===================== ACTIONS – ALERJİLER ALANI ======================

    /**
     * Hasta Özgeçmişi penceresinde Alerjiler alanı için
     * yeni kayıt ekleme formunu açar (plus ikonlu buton).
     */
    public void openAllergiesNewRecordForm() {
        LOGGER.info("[TreatmentExaminationPatientHistoryPage] openAllergiesNewRecordForm");

        // Küçük retry: DOM içeriği add butonuna tıklayınca yeniden çizildiği için
        RuntimeException lastException = null;

        for (int i = 0; i < 3; i++) {
            try {
                // 1) Alerjiler bölümünü bul
                WebElement section = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(allergiesSection)
                );

                scrollIntoView(section);

                // 2) Plus ikonlu "yeni ekle" butonuna tıkla
                WebElement addButton = section.findElement(
                        By.xpath(".//button[.//i[contains(@class,'hio-plus')]]")
                );
                safeClick(addButton);

                // 3) Form grid'in yeni DOM'da görünmesini tam xpath ile bekle
                By gridLocator = By.xpath(
                        "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                                "//div[contains(@class,'break-inside-avoid-column') and contains(@class,'mb-4')]" +
                                "[.//h4[contains(normalize-space(),'Alerjiler')]]" +
                                "//div[contains(@class,'grid') and contains(@class,'grid-cols-4') and contains(@class,'gap-2')]"
                );

                wait.until(ExpectedConditions.visibilityOfElementLocated(gridLocator));
                LOGGER.info("[TreatmentExaminationPatientHistoryPage] Alerjiler form grid açıldı.");
                return;

            } catch (StaleElementReferenceException e) {
                lastException = e;
                LOGGER.warn("[TreatmentExaminationPatientHistoryPage] openAllergiesNewRecordForm -> stale, retry={}", i + 1);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (lastException != null) {
            throw lastException;
        } else {
            throw new RuntimeException("[TreatmentExaminationPatientHistoryPage] Alerjiler formu açılamadı.");
        }
    }

    /**
     * Alerjiler formunda; Alerji tipi (dropdown), Alerjen (combobox) ve Açıklama alanlarını doldurur.
     */
    public void fillAllergiesForm(String allergyType, String allergen, String description) {
        LOGGER.info("[TreatmentExaminationPatientHistoryPage] fillAllergiesForm -> type='{}', allergen='{}', desc='{}'",
                allergyType, allergen, description);

        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]")
        ));

        WebElement allergiesCard = modal.findElement(By.xpath(
                ".//div[contains(@class,'break-inside-avoid-column') and .//h4[contains(normalize-space(),'Alerjiler')]]"
        ));

        // Grid'in gerçekten render olmasını bekle
        wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(
                allergiesCard, allergiesFormGrid
        ));

        // ==== 1) Alerji tipi (ilk dropdownlist/combobox) ====
        WebElement allergyTypeInput = allergiesCard.findElement(By.xpath(
                ".//div[contains(@class,'grid')]/div[contains(@class,'e-form-group')][1]" +
                        "//input[(starts-with(@id,'dropdownlist') or starts-with(@id,'combobox')) " +
                        "and contains(@class,'e-input')]"
        ));
        selectFromCombobox(allergyTypeInput, allergyType);   // Örn: "Mevsimsel"

        // ==== 2) Alerjen (tek combobox input) ====
        WebElement allergenInput = allergiesCard.findElement(By.xpath(
                ".//div[contains(@class,'grid')]" +
                        "//input[starts-with(@id,'combobox') and contains(@class,'e-input')]"
        ));
        selectFromCombobox(allergenInput, allergen);         // Örn: "Polen"

        // ==== 3) Açıklama alanı ====
        WebElement descriptionInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        allergiesCard.findElement(
                                By.xpath(".//input[contains(@class,'e-textbox') and @placeholder='Açıklama']")
                        )
                )
        );
        clearAndType(descriptionInput, description);
    }

    /**
     * Alerjiler formundaki "Ekle" butonuna tıklar.
     * Kayıt doğrulaması ayrı adımda (isAllergyRecordVisible) yapılır.
     */
    public void clickAllergiesAddButton() {
        LOGGER.info("[Hasta Özgeçmişi > Alerjiler] Ekle butonuna tıklanıyor...");

        WebElement section = wait.until(
                ExpectedConditions.visibilityOfElementLocated(allergiesSection)
        );

        WebElement addButton = section.findElement(allergiesAddButton);
        safeClick(addButton);

        try {
            Thread.sleep(500); // satır render bekleme
        } catch (InterruptedException ignored) {
        }
    }

    private String getAllergyRowXpath(String allergen, String description) {
        // Alerjiler bölümündeki table-fixed tablo içinde:
        return "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                "//div[contains(@class,'break-inside-avoid-column') and contains(@class,'mb-4')]" +
                "[.//h4[contains(normalize-space(),'Alerjiler')]]" +
                "//table[contains(@class,'table-fixed')]//tbody//tr[" +
                ".//td[1]//p[normalize-space()='" + allergen + "'] and " +
                ".//td[3]//p[normalize-space()='" + description + "']" +
                "]";
    }

    /**
     * Alerjiler tablosunda verilen allergen + açıklama kombinasyonuna ait kaydın
     * görüntülendiğini doğrulamak için kullanılır.
     */
    public boolean isAllergyRecordVisible(String allergen, String description) {
        LOGGER.info("[TreatmentExaminationPatientHistoryPage] isAllergyRecordVisible -> allergen='{}', desc='{}'",
                allergen, description);

        String rowXpath = getAllergyRowXpath(allergen, description);

        try {
            WebElement row = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXpath))
            );
            scrollIntoView(row);
            return row.isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("[TreatmentExaminationPatientHistoryPage] Beklenen alerji kaydı bulunamadı. Mevcut satırlar loglanacak.");

            try {
                driver.findElements(
                        By.xpath(
                                "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                                        "//div[contains(@class,'break-inside-avoid-column') and contains(@class,'mb-4')]" +
                                        "[.//h4[contains(normalize-space(),'Alerjiler')]]" +
                                        "//table[contains(@class,'table-fixed')]//tbody//tr"
                        )
                ).forEach(tr -> LOGGER.warn("ROW TEXT => {}", tr.getText()));
            } catch (Exception ignored) {
            }

            return false;
        }
    }

    /**
     * Alerjiler tablosunda verilen allergen + açıklamaya ait satır için
     * sil butonuna tıklar.
     */
    public void deleteAllergyRecord(String allergen, String description) {
        LOGGER.info("[TreatmentExaminationPatientHistoryPage] deleteAllergyRecord -> allergen='{}', desc='{}'",
                allergen, description);

        String rowXpath = getAllergyRowXpath(allergen, description);
        WebElement row = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXpath))
        );

        WebElement deleteButton = row.findElement(
                By.xpath(".//button[.//i[contains(@class,'e-trash')]]")
        );

        safeClick(deleteButton);

        // Satırın kaybolmasını bekle
        try {
            wait.until(ExpectedConditions.stalenessOf(row));
        } catch (TimeoutException e) {
            LOGGER.warn("[TreatmentExaminationPatientHistoryPage] Silinen alerji satırı için staleness beklemesi timeout oldu.");
        }
    }

    /**
     * Alerjiler tablosunda verilen allergen'e ait herhangi bir kayıt kalmadığını
     * doğrulamak için kullanılır.
     */
    public boolean isAllergyRecordNotVisible(String allergen) {
        LOGGER.info("[TreatmentExaminationPatientHistoryPage] isAllergyRecordNotVisible -> allergen='{}'", allergen);

        String rowXpath =
                "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                        "//div[contains(@class,'break-inside-avoid-column') and contains(@class,'mb-4')]" +
                        "[.//h4[contains(normalize-space(),'Alerjiler')]]" +
                        "//table[contains(@class,'table-fixed')]//tbody//tr[.//td[1]//p[normalize-space()='" + allergen + "']]";

        try {
            driver.findElement(By.xpath(rowXpath));
            // Hâlâ bir satır varsa false
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    /**
     * Syncfusion dropdownlist / combobox için genel seçim metodu.
     * - Mümkünse ok ikonuna tıklar
     * - Açılan popup içinden istenen li text'ini seçer
     * - Popup açılamazsa input'a yazıp ENTER ile seçer (combobox fallback)
     */
    private void selectFromCombobox(WebElement inputElement, String valueToSelect) {
        LOGGER.info("[selectFromCombobox] value='{}'", valueToSelect);

        // 1) Görünür alana kaydır
        scrollIntoView(inputElement);

        // 2) Önce ok ikonunu bulup tıklamayı dene (combobox'larda daha güvenli)
        WebElement clickableElement = inputElement;
        try {
            WebElement wrapper = inputElement.findElement(
                    By.xpath("./ancestor::span[contains(@class,'e-ddl')][1]")
            );
            WebElement icon = wrapper.findElement(
                    By.xpath(".//span[contains(@class,'e-input-group-icon') and contains(@class,'e-ddl-icon')]")
            );
            clickableElement = icon;
        } catch (NoSuchElementException ignore) {
            // Ok ikonu bulunamazsa input üzerinden devam ederiz
        }

        wait.until(ExpectedConditions.elementToBeClickable(clickableElement)).click();

        // 3) Popup açılmaya çalışılsın
        WebElement popup = null;
        try {
            popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'e-ddl') and contains(@class,'e-popup-open')]")
            ));
        } catch (TimeoutException e) {
            LOGGER.warn("[selectFromCombobox] Popup açılamadı, combobox için yaz+ENTER fallback kullanılacak.");

            // --- Fallback: sadece combobox ise yaz + ENTER ile seçim ---
            clearAndType(inputElement, valueToSelect);
            try {
                inputElement.sendKeys(Keys.ENTER);
            } catch (Exception ignored2) {}

            // Değerin input'ta gerçekten set olduğundan emin ol
            try {
                wait.until(driver ->
                        valueToSelect.equals(inputElement.getAttribute("value"))
                );
            } catch (Exception ex) {
                LOGGER.warn("[selectFromCombobox] Fallback sonrası input değer doğrulaması yapılamadı.");
            }
            return;
        }

        // 4) Popup içinden ilgili seçeneği tıkla
        WebElement option = popup.findElement(
                By.xpath(".//li[normalize-space()='" + valueToSelect + "']")
        );
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();

        // 5) Popup'ın kapanmasını bekle (dropdowlist senaryosu)
        try {
            wait.until(ExpectedConditions.invisibilityOf(popup));
        } catch (TimeoutException e) {
            LOGGER.warn("[selectFromCombobox] Popup görünürlüğü kapanma beklemesinde timeout oldu, devam ediliyor.");
        }
    }
}

package com.sinannuhoglu.pages.treatment.finanscard;

import com.sinannuhoglu.core.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Tıbbi İşlemler > Servis Kartı > Finans Kartı
 */
public class TreatmentFinanceCardDiscountPage {

    private static final Logger LOGGER = LogManager.getLogger(TreatmentFinanceCardDiscountPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private String lastEnteredAmountText = null;

    private static final By DISCOUNT_LIST_TABLE = By.xpath(
            "//div[contains(@class,'e-gridcontent')]//table[" +
                    "contains(@class,'e-table') and " +
                    "contains(@id,'sfgrid') and " +
                    "contains(@id,'content_table')]"
    );

    /**
     * Sağdan açılan İndirim paneli:
     * - fixed, top-0, flex-col yapısında
     * - içerisinde "İndirim Yöntemi" yazan kart bulunuyor.
     * Bu container altında hem içerik hem de alttaki Kaydet butonu var.
     */
    private static final By DISCOUNT_CREATE_PANEL = By.xpath(
            "//div[contains(@class,'fixed') and contains(@class,'top-0') and contains(@class,'flex-col')" +
                    " and .//p[contains(normalize-space(),'İndirim Yöntemi')]]"
    );

    // Açılan menüdeki "İptal Et" seçeneği
    private static final By CANCEL_MENU_ITEM = By.xpath(
            "//ul[contains(@class,'e-dropdown-menu')]//li[normalize-space()='İptal Et']"
    );

    // Onay popup’ındaki "Evet" butonu
    private static final By CONFIRM_YES_BUTTON = By.xpath(
            "//div[starts-with(@id,'modal-dialog') and contains(@class,'e-dlg-container')]" +
                    "//button[normalize-space()='Evet']"
    );

    public TreatmentFinanceCardDiscountPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ================== PRIVATE HELPER METODLAR ==================

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * "İndirim (x)" başlığını içeren kartın root container’ını döner.
     * Kart: bg-white rounded-xl ... yapısındaki beyaz kart.
     */
    private WebElement getDiscountCardRoot() {
        By cardContainer = By.xpath(
                "//p[contains(@class,'text-sm') and " +
                        "contains(@class,'font-medium') and " +
                        "contains(normalize-space(),'İndirim')]" +
                        "/ancestor::div[contains(@class,'bg-white') and " +
                        "contains(@class,'rounded-xl')][1]"
        );

        WebElement card = wait.until(
                ExpectedConditions.visibilityOfElementLocated(cardContainer)
        );

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", card);
        return card;
    }

    /**
     * Sağdan açılan panelin (fixed top-0 flex-col) kök elementini döner.
     */
    private WebElement getDiscountPanelRoot() {
        WebElement panel = wait.until(ExpectedConditions.visibilityOfElementLocated(DISCOUNT_CREATE_PANEL));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", panel);
        return panel;
    }

    /**
     * Verilen elemente güvenli click (gerekirse JS ve tekrar denemeli).
     */
    private void safeClick(WebElement element) {
        for (int i = 0; i < 3; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                LOGGER.warn("[Finans/İndirim] safeClick -> stale element, deneme: {}", i + 1);
                sleep(300);
            } catch (ElementNotInteractableException e) {
                LOGGER.warn("[Finans/İndirim] safeClick -> normal click başarısız, JS denenecek. Deneme: {} Hata: {}",
                        i + 1, e.getClass().getSimpleName());
                try {
                    js.executeScript("arguments[0].click();", element);
                    return;
                } catch (Exception ignore) {
                    sleep(300);
                }
            } catch (TimeoutException e) {
                LOGGER.warn("[Finans/İndirim] safeClick -> clickable timeout, JS click fallback. Deneme: {}", i + 1);
                try {
                    js.executeScript("arguments[0].click();", element);
                    return;
                } catch (Exception ignore) {
                    sleep(300);
                }
            }
        }

        // Son çare
        LOGGER.warn("[Finans/İndirim] safeClick -> tüm denemeler başarısız, son kez JS click deneniyor.");
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * İndirim kartı üzerindeki + butonuna tıklar.
     */
    private void clickDiscountPlusButton() {
        LOGGER.info("[Finans/İndirim] Kart üzerindeki + butonuna tıklanıyor...");

        WebElement card = getDiscountCardRoot();

        WebElement plusButton = card.findElement(By.xpath(
                ".//button[contains(@class,'e-control') and contains(@class,'e-btn') " +
                        "and contains(@class,'e-icon-btn')]" +
                        "[.//span[contains(@class,'hio-plus') and contains(@class,'e-btn-icon')]]"
        ));

        LOGGER.info("[Finans/İndirim] + butonu bulundu, safeClick ile tıklanacak.");
        safeClick(plusButton);

        LOGGER.info("[Finans/İndirim] + butonuna tıklandı.");
    }


    /**
     * İndirim kartı üzerindeki sağ ok (liste) butonuna tıklar.
     * (aynı bg-white rounded-xl container altındaki hio-arrow-rtl ikonlu buton)
     */
    private void clickDiscountArrowButton() {
        LOGGER.info("[Finans/İndirim] Kart üzerindeki sağ ok butonuna tıklanıyor...");

        WebElement card = getDiscountCardRoot();

        WebElement arrowButton = card.findElement(By.xpath(
                ".//button[contains(@class,'e-control') and contains(@class,'e-btn') " +
                        "and contains(@class,'e-icon-btn')]" +
                        "[.//span[contains(@class,'hio-arrow-rtl') and contains(@class,'e-btn-icon')]]"
        ));

        safeClick(arrowButton);

        LOGGER.info("[Finans/İndirim] Sağ ok butonuna tıklandı.");
    }

    private String getTodayDateText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy", new Locale("tr", "TR"));
        return LocalDate.now().format(formatter);
    }

    private WebElement getDiscountListTable() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(DISCOUNT_LIST_TABLE));
    }

    private WebElement findDiscountRowOnce(String expectedDateText, String expectedAmountText) {
        WebElement table = driver.findElement(DISCOUNT_LIST_TABLE);
        List<WebElement> rows = table.findElements(By.cssSelector("tbody tr.e-row"));

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.cssSelector("td"));
            if (cells.size() < 4) continue;

            String dateText = cells.get(1).getText().trim();
            String amountText = cells.get(2).getText().trim();

            LOGGER.debug("[Finans/İndirim] Satır -> Tarih: {}, Tutar: {}", dateText, amountText);

            boolean dateMatches = dateText.contains(expectedDateText);
            boolean amountMatches = amountText.replace(".", "").contains(expectedAmountText);

            if (dateMatches && amountMatches) {
                LOGGER.info("[Finans/İndirim] Aranan satır bulundu. Tarih: {}, Tutar: {}", dateText, amountText);
                return row;
            }
        }

        LOGGER.info("[Finans/İndirim] Kriterlere uygun satır bulunamadı. Tarih: {}, Tutar: {}",
                expectedDateText, expectedAmountText);
        return null;
    }

    private WebElement waitAndFindDiscountRow(String expectedDateText, String expectedAmountText) {
        return wait.until(drv -> {
            try {
                return findDiscountRowOnce(expectedDateText, expectedAmountText);
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return null;
            }
        });
    }

    /**
     * Numeric textbox içindeki value ile beklenen amount eşleşiyor mu?
     * (200 → "200", "200,00", "200,0" vs. hepsini normalize ederek kontrol ediyoruz)
     */
    private boolean numericValueMatches(String currentValue, String expectedPlain) {
        if (currentValue == null) return false;

        // Sadece rakamları karşılaştıralım (format farklarını göz ardı etmek için)
        String curDigits = currentValue.replaceAll("\\D", "");
        String expDigits = expectedPlain.replaceAll("\\D", "");

        // Son kısımda beklediğimiz tutar olmalı (örn. 00200 vs 200;)
        return !curDigits.isEmpty() && curDigits.endsWith(expDigits);
    }

    /**
     * Panel içinde "Tutar" radio input’unun gerçekten seçili olduğundan emin olur.
     * Seçili değilse ilgili label’a tıklayıp seçer ve selected olana kadar bekler.
     */
    private void ensureAmountMethodSelected(WebElement panel) {
        try {
            WebElement amountRadioInput = panel.findElement(
                    By.xpath(".//input[@type='radio' and @name='discount-method' and @value='Amount']")
            );

            if (!amountRadioInput.isSelected()) {
                String id = amountRadioInput.getAttribute("id");
                WebElement amountRadioLabel = panel.findElement(
                        By.xpath(".//label[@for='" + id + "']")
                );
                LOGGER.info("[Finans/İndirim] 'Tutar' radio seçili değil, label'a tıklanacak...");
                safeClick(amountRadioLabel);

                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(drv -> {
                            try {
                                return amountRadioInput.isSelected();
                            } catch (StaleElementReferenceException e) {
                                return false;
                            }
                        });

                LOGGER.info("[Finans/İndirim] 'Tutar' radio artık seçili.");
            } else {
                LOGGER.info("[Finans/İndirim] 'Tutar' radio zaten seçili.");
            }
        } catch (NoSuchElementException e) {
            LOGGER.warn("[Finans/İndirim] 'Tutar' radio input bulunamadı, locator kontrol edilmeli.", e);
        }
    }

    // ================== PUBLIC ADIM ADIM METODLAR ==================

    /** 1) İndirim penceresini aç (+ butonuna tıkla). */
    public void openDiscountCreatePanel() {
        LOGGER.info("[Finans/İndirim] İndirim oluşturma penceresi açılıyor...");
        clickDiscountPlusButton();

        getDiscountPanelRoot(); // panelin gerçekten açıldığından emin ol
        sleep(700L); // animasyon buffer
        LOGGER.info("[Finans/İndirim] İndirim oluşturma penceresi açıldı.");
    }

    /** 2) İndirim penceresinde yöntem olarak 'Tutar' seçeneğini işaretler. */
    public void selectAmountMethodInDiscountPanel() {
        LOGGER.info("[Finans/İndirim] 'Tutar' indirim yöntemi seçiliyor...");
        WebElement panel = getDiscountPanelRoot();
        ensureAmountMethodSelected(panel);
        LOGGER.info("[Finans/İndirim] 'Tutar' yöntemi seçildi.");
    }

    /** 3) İndirim penceresinde Tutar alanına değer yazar. (ör: "200") */
    public void enterDiscountAmount(String amountText) {
        LOGGER.info("[Finans/İndirim] Tutar alanına {} yazılıyor...", amountText);
        WebElement panel = getDiscountPanelRoot();

        WebElement amountInput = panel.findElement(
                By.xpath(".//span[contains(@class,'e-numeric') and contains(@class,'e-input-group')]" +
                        "//input[contains(@class,'e-numerictextbox')]")
        );

        amountInput.click();

        amountInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        amountInput.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        amountInput.sendKeys(Keys.DELETE);

        amountInput.sendKeys(amountText);
        lastEnteredAmountText = amountText;

        boolean valueOk = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(drv -> {
                    try {
                        String val = amountInput.getAttribute("value");
                        LOGGER.info("[Finans/İndirim] Numeric input value: '{}'", val);
                        return numericValueMatches(val, amountText);
                    } catch (StaleElementReferenceException e) {
                        return false;
                    }
                });

        Assert.assertTrue(
                valueOk,
                String.format("[Finans/İndirim] Tutar alanına beklenen değer yazılamadı. Beklenen: %s", amountText)
        );

        LOGGER.info("[Finans/İndirim] Tutar alanı dolduruldu ve doğrulandı: {}", amountText);
    }

    /** 4) İndirim penceresinde tekrar 'Tutar' seçeneğine tıklayarak değeri günceller. */
    public void reselectAmountMethodToRefresh() {
        LOGGER.info("[Finans/İndirim] Değerin güncellenmesi için önce panelde bir alana, ardından 'Tutar' seçeneğine tekrar tıklanıyor...");

        WebElement panel = getDiscountPanelRoot();

        try {
            WebElement header = panel.findElement(
                    By.xpath(".//p[contains(normalize-space(),'İndirim Yöntemi')]")
            );
            safeClick(header);
            sleep(300L);
            LOGGER.info("[Finans/İndirim] 'İndirim Yöntemi' başlığına tıklanarak Tutar alanının focus'u kaldırıldı.");
        } catch (NoSuchElementException e) {
            LOGGER.warn("[Finans/İndirim] 'İndirim Yöntemi' başlığı bulunamadı, blur adımı atlanıyor.", e);
        }

        try {
            WebElement amountRadioInput = panel.findElement(
                    By.xpath(".//input[@type='radio' and @name='discount-method' and @value='Amount']")
            );
            String id = amountRadioInput.getAttribute("id");

            WebElement amountRadioLabel = panel.findElement(
                    By.xpath(".//label[@for='" + id + "']")
            );
            safeClick(amountRadioLabel);

            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(drv -> {
                        try {
                            WebElement refreshedInput = panel.findElement(
                                    By.xpath(".//input[@type='radio' and @name='discount-method' and @value='Amount']")
                            );
                            return refreshedInput.isSelected();
                        } catch (StaleElementReferenceException ex) {
                            return false;
                        }
                    });

            LOGGER.info("[Finans/İndirim] 'Tutar' seçeneği tekrar tıklandı ve seçili hale getirildi.");
        } catch (NoSuchElementException e) {
            LOGGER.warn("[Finans/İndirim] 'Tutar' radio/label bulunamadı (reselect). Locator kontrol edilmeli.", e);
        }
    }

    /** 5) İndirim penceresinde Kaydet butonuna tıklar. */
    public void clickDiscountSaveButton() {
        LOGGER.info("[Finans/İndirim] Kaydet butonuna tıklanacak... Son girilen tutar: {}", lastEnteredAmountText);
        WebElement panel = getDiscountPanelRoot();

        ensureAmountMethodSelected(panel);

        WebElement saveButton = panel.findElement(
                By.xpath(".//button[normalize-space()='Kaydet']")
        );

        safeClick(saveButton);

        sleep(1500L);

        LOGGER.info("[Finans/İndirim] Kaydet butonuna tıklandı, işlem uygulamaya bırakıldı.");
    }

    /** 6) İndirim listesi panelini açmak için sağ ok butonuna tıklar. */
    public void openDiscountListPanel() {
        LOGGER.info("[Finans/İndirim] İndirim listesi paneli açılıyor...");
        clickDiscountArrowButton();

        getDiscountListTable();
        sleep(1500L);
        LOGGER.info("[Finans/İndirim] İndirim listesi paneli açıldı ve grid yüklendi.");
    }

    /** 7) Listede bugünün tarihli ve verilen tutarda kaydı bulup İptal Et -> Evet akışını çalıştırır. */
    public void cancelDiscountForToday(String amountText) {
        String today = getTodayDateText();
        LOGGER.info("[Finans/İndirim] Bugünün tarihli ({}) ve {} tutarlı indirim kaydı iptal edilecek.",
                today, amountText);

        openDiscountListPanel();

        WebElement targetRow = waitAndFindDiscountRow(today, amountText);
        Assert.assertNotNull(
                targetRow,
                String.format("[Finans/İndirim] İptal edilecek satır bulunamadı. Tarih: %s, Tutar: %s",
                        today, amountText)
        );

        List<WebElement> cells = targetRow.findElements(By.cssSelector("td"));
        String actualDate = cells.get(1).getText().trim();
        String actualAmount = cells.get(2).getText().trim();

        LOGGER.info("[Finans/İndirim] Bulunan satır -> Tarih: {}, Tutar: {}", actualDate, actualAmount);

        Assert.assertTrue(
                actualDate.contains(today),
                String.format("[Finans/İndirim] Tarih beklenenden farklı. Beklenen: %s, Gerçek: %s",
                        today, actualDate)
        );

        LOGGER.info("[Finans/İndirim] Satırdaki üç nokta menüsüne tıklanıyor...");
        WebElement dropdownButton = targetRow.findElement(
                By.xpath(".//td[4]//button[contains(@class,'e-dropdown-btn')]")
        );
        safeClick(dropdownButton);

        LOGGER.info("[Finans/İndirim] Açılan menüden 'İptal Et' seçeneği seçiliyor...");
        WebElement cancelItem = wait.until(ExpectedConditions.elementToBeClickable(CANCEL_MENU_ITEM));
        cancelItem.click();

        LOGGER.info("[Finans/İndirim] Onay penceresinde 'Evet' butonuna tıklanıyor...");
        WebElement yesButton = wait.until(ExpectedConditions.elementToBeClickable(CONFIRM_YES_BUTTON));
        yesButton.click();

        LOGGER.info("[Finans/İndirim] İptal sonrası satırın silinmesi bekleniyor...");
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean deleted = shortWait.until(drv -> findDiscountRowOnce(today, amountText) == null);

        Assert.assertTrue(
                deleted,
                String.format("[Finans/İndirim] İptal sonrası satır hala listede görünüyor. Tarih: %s, Tutar: %s",
                        today, amountText)
        );

        LOGGER.info("[Finans/İndirim] İndirim satırı başarıyla iptal edildi ve listeden silindi.");
    }

    /** 8) Ek güvenlik: belirli tutar + bugünün tarihli satırın artık listede olmadığını doğrular. */
    public void verifyDiscountDeletedForToday(String amountText) {
        String today = getTodayDateText();
        LOGGER.info("[Finans/İndirim] Silinmenin doğrulanması yapılıyor. Tarih: {}, Tutar: {}",
                today, amountText);

        getDiscountListTable();

        WebElement row = findDiscountRowOnce(today, amountText);
        Assert.assertNull(
                row,
                String.format("[Finans/İndirim] Silinmiş olması gereken kayıt hala görünüyor. Tarih: %s, Tutar: %s",
                        today, amountText)
        );

        LOGGER.info("[Finans/İndirim] Bugünün tarihli {} tutarlı indirim kaydının silindiği doğrulandı.", amountText);
    }
}

package com.sinannuhoglu.pages.appointment.definitions.resources;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Randevu Kaynakları > Resmi Tatiller sekmesi için Page Object.
 * - Resmi Tatiller sekmesine geçiş
 * - Grid üzerinde ilgili resmi tatil satırının bulunması
 * - Durum switch'inin aktif konuma getirilmesi ve doğrulanması
 */
public class AppointmentResourcesOfficialHolidaysPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AppointmentResourcesOfficialHolidaysPage(WebDriver driver) {
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

    private static final String OFFICIAL_HOLIDAYS_TAB_TEXT = "Resmi Tatiller";

    private final By officialHolidaysGridTable = By.xpath(
            "//div[contains(@class,'e-grid')]//table[contains(@class,'e-table')]"
    );

    private final By gridRowsLocator = By.xpath(
            "//div[contains(@class,'e-grid')]//div[contains(@class,'e-gridcontent')]//tbody/tr[not(contains(@class,'e-emptyrow'))]"
    );

    // ----------------------------------------------------------------
    // SEKME İŞLEMLERİ
    // ----------------------------------------------------------------

    /**
     * Toolbar'daki "Resmi Tatiller" sekmesine geçer ve grid'in yüklendiğini bekler.
     */
    public void openOfficialHolidaysTab() {

        List<WebElement> tabs = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(toolbarItems)
        );

        WebElement officialHolidaysTab = null;

        for (WebElement item : tabs) {
            try {
                WebElement textEl = item.findElement(By.cssSelector(".e-tab-text"));
                String text = textEl.getText().trim();
                System.out.println("[OfficialHolidays] Tab text: '" + text + "'");
                if (OFFICIAL_HOLIDAYS_TAB_TEXT.equals(text)) {
                    officialHolidaysTab = item;
                    break;
                }
            } catch (StaleElementReferenceException ignored) {
            } catch (NoSuchElementException ignored) {
            }
        }

        if (officialHolidaysTab == null) {
            throw new NoSuchElementException(
                    "[OfficialHolidays] Toolbar'da '" + OFFICIAL_HOLIDAYS_TAB_TEXT + "' sekmesi bulunamadı."
            );
        }

        safeClick(officialHolidaysTab);

        wait.until(d -> {
            try {
                List<WebElement> items = d.findElements(toolbarItems);
                for (WebElement item : items) {
                    WebElement textEl = item.findElement(By.cssSelector(".e-tab-text"));
                    String text = textEl.getText().trim();
                    if (OFFICIAL_HOLIDAYS_TAB_TEXT.equals(text)
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
            wait.until(ExpectedConditions.visibilityOfElementLocated(officialHolidaysGridTable));
        } catch (TimeoutException ignored) {
        }

        System.out.println("[OfficialHolidays] 'Resmi Tatiller' sekmesi açıldı ve grid yüklendi.");
    }

    // ----------------------------------------------------------------
    // GRID HELPER'LARI
    // ----------------------------------------------------------------

    /**
     * Verilen tatil adı için satır locator'ını üretir.
     */
    private By buildHolidayRowByNameLocator(String holidayName) {
        String xpath =
                "//div[contains(@class,'e-grid')]//div[contains(@class,'e-gridcontent')]" +
                        "//tbody/tr[not(contains(@class,'e-emptyrow'))]" +
                        "[td[1][normalize-space()='" + holidayName + "']]";
        return By.xpath(xpath);
    }

    /**
     * Grid içinde, ilk sütununda verilen resmi tatil adını
     * (örn: "23 Nisan") içeren satırı döner.
     */
    private WebElement findHolidayRowByName(String holidayName) {
        // Önce grid’in hazır olduğundan emin ol
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(officialHolidaysGridTable));
        } catch (TimeoutException ignored) {
        }

        By rowBy = buildHolidayRowByNameLocator(holidayName);

        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowBy));
        scrollIntoViewCenter(row);

        System.out.printf("[OfficialHolidays] '%s' isimli resmi tatil satırı bulundu.%n", holidayName);
        return row;
    }

    /**
     * Verilen satırdan, 3. sütundaki switch (Durum) bileşenini döner.
     */
    private WebElement getStatusSwitchWrapper(WebElement row) {

        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() < 3) {
            throw new IllegalStateException(
                    "[OfficialHolidays] Satırda beklenen (en az 3) sütun yok."
            );
        }

        WebElement statusCell = cells.get(2);
        scrollIntoViewCenter(statusCell);

        return statusCell.findElement(By.cssSelector("div.e-switch-wrapper"));
    }

    /**
     * Switch'in aktif (ON) durumda olup olmadığını kontrol eder.
     * - Öncelikli kontrol: input[type=checkbox].isSelected()
     * - İkincil kontrol: wrapper class içinde "e-switch-active"
     * - Son kontrol: aria-checked="true"
     */
    private boolean isSwitchActive(WebElement switchWrapper) {
        boolean selected = false;
        boolean classActive = false;
        boolean ariaChecked = false;

        try {
            WebElement checkbox = switchWrapper.findElement(By.cssSelector("input.e-switch"));
            selected = checkbox.isSelected();

            String aria = checkbox.getAttribute("aria-checked");
            ariaChecked = "true".equalsIgnoreCase(aria);
        } catch (NoSuchElementException ignored) {
        }

        String wrapperClass = switchWrapper.getAttribute("class");
        classActive = wrapperClass != null && wrapperClass.contains("e-switch-active");

        boolean isActive = selected || classActive || ariaChecked;

        System.out.printf("[OfficialHolidays] Switch durumu -> selected=%s, classActive=%s, ariaChecked=%s, isActive=%s%n",
                selected, classActive, ariaChecked, isActive);

        return isActive;
    }

    /**
     * Switch'e tıklar (önce handle, olmazsa wrapper, en son JS click).
     */
    private void toggleSwitch(WebElement switchWrapper) {
        try {
            WebElement handle = switchWrapper.findElement(By.cssSelector("span.e-switch-handle"));
            safeClick(handle);
        } catch (NoSuchElementException e) {
            safeClick(switchWrapper);
        }
    }

    // ----------------------------------------------------------------
    // İŞ KURALI: Durumu aktif hale getirme
    // ----------------------------------------------------------------

    /**
     * Resmi Tatiller gridinde, ilk sütunda holidayName olan satırın
     * 3. sütunundaki "Durum" switch'ini "Aktif" konuma getirir.
     * Zaten aktif ise hiçbir işlem yapmaz.
     */
    public void ensureHolidayStatusActive(String holidayName) {
        String logPrefix = "[OfficialHolidays] ";

        System.out.printf("%s'%s' satırının Durum bilgisini aktif konuma getirme işlemi başlatıldı.%n",
                logPrefix, holidayName);

        By rowBy = buildHolidayRowByNameLocator(holidayName);
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowBy));
        WebElement switchWrapper = getStatusSwitchWrapper(row);

        if (isSwitchActive(switchWrapper)) {
            System.out.printf("%s'%s' satırının Durum bilgisi zaten aktif. İşlem yapılmadı.%n",
                    logPrefix, holidayName);
            return;
        }

        System.out.printf("%s'%s' satırının Durum bilgisi pasif. Aktif konuma getiriliyor...%n",
                logPrefix, holidayName);

        toggleSwitch(switchWrapper);

        boolean nowActive = wait.until(d -> {
            try {
                WebElement currentRow = d.findElement(rowBy);
                WebElement currentSwitch = getStatusSwitchWrapper(currentRow);
                return isSwitchActive(currentSwitch);
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        });

        if (!nowActive) {
            throw new AssertionError(String.format(
                    "%s'%s' satırının Durum bilgisi aktif konuma getirilemedi.",
                    logPrefix, holidayName
            ));
        }

        System.out.printf("%s'%s' satırının Durum bilgisi başarıyla aktif konuma getirildi.%n",
                logPrefix, holidayName);
    }

    /**
     * Resmi Tatiller gridinde, ilk sütunda holidayName olan satırın
     * 3. sütunundaki "Durum" switch'inin aktif olduğunu doğrular.
     */
    public void verifyHolidayStatusIsActive(String holidayName) {
        String logPrefix = "[OfficialHolidays] ";

        System.out.printf("%s'%s' satırının Durum bilgisinin aktif olduğunu doğrulama işlemi başlatıldı.%n",
                logPrefix, holidayName);

        By rowBy = buildHolidayRowByNameLocator(holidayName);

        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowBy));
        WebElement switchWrapper = getStatusSwitchWrapper(row);

        if (!isSwitchActive(switchWrapper)) {
            throw new AssertionError(String.format(
                    "%sGrid'de '%s' satırının Durum bilgisi aktif değil.",
                    logPrefix, holidayName
            ));
        }

        System.out.printf("%s'%s' satırının Durum bilgisi aktif olarak doğrulandı.%n",
                logPrefix, holidayName);
    }
}

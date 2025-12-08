package com.sinannuhoglu.pages.appointment.definitions.resources;

import com.sinannuhoglu.core.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Randevu > Tanımlar > Kaynaklar ekranındaki "Randevu Tipi" sekmesi için
 * Page Object (kaynak grid + Randevu Tipi dialog işlemleri).
 */
public class AppointmentResourcesAppointmentTypePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AppointmentResourcesAppointmentTypePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // ==================== LOCATORS ======================

    private final By headerTitle = By.cssSelector("span[data-testid='header-title']");

    private final By toolbarSearchInput = By.id("search-input");

    private final By gridContent = By.cssSelector("div.e-gridcontent");
    private final By gridRows = By.cssSelector("#Grid_content_table tbody tr.e-row");
    private final By emptyRow = By.cssSelector("#Grid_content_table tbody tr.e-emptyrow");

    private final By appointmentTypeTableBody = By.xpath(
            "//div[contains(@class,'e-item') and contains(@class,'e-active')]" +
                    "//table[contains(@class,'table-fixed') or contains(@class,'e-table')]//tbody"
    );

    private final By appointmentTypeSaveButton = By.xpath(
            "//button[normalize-space()='Kaydet' and contains(@class, 'e-btn')]"
    );

    // ================== STATE (SEÇİLEN SATIRLAR) =================

    private WebElement lastSelectedResourceRow;
    private WebElement lastSelectedAppointmentTypeRow;

    // ==================== HELPERS ===========================

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el
        );
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(el);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void safeClick(WebElement el) {
        scrollIntoView(el);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void typeInto(WebElement el, String text) {
        scrollIntoView(el);
        el.click();
        el.clear();
        el.sendKeys(text);
    }

    /**
     * Grid içeriğinin yüklenmesini bekler.
     * - Grid container görünür
     * - Satır veya "kayıt yok" satırı gelmiş olur
     */
    private void waitForGridLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gridContent));
        wait.until(d -> {
            boolean hasRows = !d.findElements(gridRows).isEmpty();
            boolean hasEmpty = !d.findElements(emptyRow).isEmpty();
            return hasRows || hasEmpty;
        });
    }

    private String getCellText(WebElement row, int cellIndexOneBased) {
        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() >= cellIndexOneBased) {
            return tds.get(cellIndexOneBased - 1).getText().trim();
        }
        return "";
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    // ==================== NAVIGATION ========================

    /**
     * Randevu Kaynakları modülüne gider, sayfa yüklenmesini ve gridin hazır olmasını bekler.
     */
    public void goToAppointmentResources(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "[AppointmentResourcesAppointmentTypePage] URL null/boş. " +
                            "testdata.properties içinde 'appointmentResourcesUrl' tanımlı mı?"
            );
        }

        driver.get(url);

        try {
            wait.until(d ->
                    ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete")
            );
        } catch (TimeoutException ignored) {
        }

        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(headerTitle));
        String text = title.getText().trim();
        if (!text.equals("Randevu Kaynakları")) {
            throw new IllegalStateException(
                    "Beklenen modül başlığı 'Randevu Kaynakları' olmalı, ancak görünen: '" + text + "'. " +
                            "Login yönlendirmesi tamamlanmamış olabilir."
            );
        }

        waitForGridLoaded();
    }

    // ===================== TOOLBAR ARAMA ====================

    /**
     * Toolbar üzerindeki arama alanına yazıp ENTER ile arama yapar ve grid sonuçlarını bekler.
     */
    public void searchOnToolbar(String text) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(toolbarSearchInput)
        );
        typeInto(input, text);
        input.sendKeys(Keys.ENTER);
        waitForGridLoaded();
    }

    // ===================== GRID KAYNAK İŞLEMLERİ ============

    /**
     * Gridde ilk sütunda verilen isimle eşleşen kaydı bulur
     * ve lastSelectedResourceRow alanına atar. Bulunamazsa Assertion ile testi kırar.
     */
    public void ensureResourceRowExists(String resourceName) {
        waitForGridLoaded();
        List<WebElement> rows = driver.findElements(gridRows);

        String target = normalize(resourceName);
        List<String> seen = new ArrayList<>();

        for (WebElement row : rows) {
            String cellText = getCellText(row, 1);
            seen.add(cellText);
            if (normalize(cellText).equals(target)) {
                lastSelectedResourceRow = row;
                scrollIntoView(row);
                return;
            }
        }

        Assert.fail("Randevu Kaynakları gridinde ilk sütunda '" + resourceName +
                "' değerine sahip kayıt bulunamadı. Görünen kayıtlar: " + seen);
    }

    /**
     * Seçili kaydın 4. sütunundaki Durum bilgisini kontrol eder,
     * aktif değilse switch'i aktif eder.
     */
    public void ensureResourceStatusActive() {
        if (lastSelectedResourceRow == null) {
            throw new IllegalStateException("Önce ensureResourceRowExists() ile satır seçilmelidir.");
        }

        List<WebElement> tds = lastSelectedResourceRow.findElements(By.tagName("td"));
        if (tds.size() < 4) {
            throw new IllegalStateException("Beklenen Durum sütunu (4. sütun) bulunamadı.");
        }

        WebElement statusCell = tds.get(3); // 0-based index
        WebElement checkbox = statusCell.findElement(By.cssSelector("input[type='checkbox']"));

        if (!checkbox.isSelected()) {
            WebElement label = statusCell.findElement(By.tagName("label"));
            safeClick(label);

            try {
                wait.until(d -> {
                    try {
                        WebElement cb = statusCell.findElement(By.cssSelector("input[type='checkbox']"));
                        return cb.isSelected();
                    } catch (StaleElementReferenceException e) {
                        // Satır refresh olmuş olabilir, bu durumda tekrar locate etmeye gerek yok
                        return true;
                    }
                });
            } catch (TimeoutException ignored) {
            }
        }
    }

    /**
     * Seçili kaydın 5. sütunundaki Düzenle butonuna tıklar ve
     * Randevu Tipi dialogunun açıldığını doğrular.
     */
    public void clickEditButtonForSelectedResource() {
        if (lastSelectedResourceRow == null) {
            throw new IllegalStateException("Önce ensureResourceRowExists() ile satır seçilmelidir.");
        }

        List<WebElement> tds = lastSelectedResourceRow.findElements(By.tagName("td"));
        if (tds.size() < 5) {
            throw new IllegalStateException("Beklenen Düzenle sütunu (5. sütun) bulunamadı.");
        }

        WebElement editCell = tds.get(4);
        WebElement editButton;

        try {
            editButton = editCell.findElement(By.cssSelector("button[title='Düzenle']"));
        } catch (NoSuchElementException e) {
            editButton = editCell.findElement(By.xpath(
                    ".//button[contains(@class,'e-edit') or contains(@class,'e-icon-btn')]"
            ));
        }

        safeClick(editButton);

        wait.until(ExpectedConditions.visibilityOfElementLocated(appointmentTypeSaveButton));
    }

    // ===================== RANDEVU TİPİ SEKME İŞLEMLERİ =====

    /**
     * Dialog içinde "Randevu Tipi" sekmesine tıklar
     * ve aktif tab içindeki tablo gövdesinin yüklenmesini bekler.
     */
    public void ensureAppointmentTypeTabSelected() {
        By tabFromToolbar = By.xpath(
                "//div[contains(@class,'e-text-wrap')]" +
                        "//div[contains(@class,'e-tab-text') and normalize-space()='Randevu Tipi']" +
                        "/ancestor::div[@role='tab']"
        );

        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(tabFromToolbar));
        safeClick(tab);

        wait.until(ExpectedConditions.visibilityOfElementLocated(appointmentTypeTableBody));
    }

    /**
     * Verilen branş adını (örn. Ek dahiliye) içeren expandable satırı bulur,
     * eğer zaten açık ise tıklamaz, kapalı ise genişletir.
     */
    public void expandBranchRow(String branchName) {
        WebElement tbody = wait.until(
                ExpectedConditions.visibilityOfElementLocated(appointmentTypeTableBody)
        );

        String target = normalize(branchName);
        List<WebElement> rows = tbody.findElements(By.tagName("tr"));

        WebElement headerRow = null;
        int headerIndex = -1;

        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            String rowClass = row.getAttribute("class");
            if (rowClass != null && rowClass.contains("cursor-pointer")) {
                String text = normalize(row.getText());
                if (text.contains(target)) {
                    headerRow = row;
                    headerIndex = i;
                    break;
                }
            }
        }

        if (headerRow == null) {
            throw new NoSuchElementException("Randevu Tipi tablosunda branş satırı bulunamadı: " + branchName);
        }

        scrollIntoView(headerRow);

        boolean alreadyExpanded = false;
        if (headerIndex + 1 < rows.size()) {
            WebElement nextRow = rows.get(headerIndex + 1);
            String nextClass = nextRow.getAttribute("class");
            if (nextClass == null || !nextClass.contains("cursor-pointer")) {
                alreadyExpanded = true;
            }
        }

        if (alreadyExpanded) {
            return;
        }

        safeClick(headerRow);

        int finalHeaderIndex = headerIndex;
        wait.until(d -> {
            WebElement freshTbody = d.findElement(appointmentTypeTableBody);
            List<WebElement> freshRows = freshTbody.findElements(By.tagName("tr"));
            if (finalHeaderIndex + 1 >= freshRows.size()) {
                return false;
            }
            WebElement nextRow = freshRows.get(finalHeaderIndex + 1);
            String nextClass = nextRow.getAttribute("class");
            return nextClass == null || !nextClass.contains("cursor-pointer");
        });
    }

    /**
     * Verilen branş altında Ad sütunu verilen değere (örn. Muayene) eşit olan
     * randevu tipi satırını bulur ve lastSelectedAppointmentTypeRow alanına set eder.
     */
    public void findAppointmentTypeRow(String branchName, String appointmentTypeName) {
        WebElement tbody = wait.until(
                ExpectedConditions.visibilityOfElementLocated(appointmentTypeTableBody)
        );

        String branchNorm = normalize(branchName);
        String typeNorm = normalize(appointmentTypeName);

        List<WebElement> rows = tbody.findElements(By.tagName("tr"));
        boolean inBranchSection = false;

        for (WebElement row : rows) {
            String rowClass = row.getAttribute("class");

            if (rowClass != null && rowClass.contains("cursor-pointer")) {
                String text = normalize(row.getText());
                if (text.contains(branchNorm)) {
                    inBranchSection = true;
                    continue;
                } else if (inBranchSection) {
                    break;
                }
            }

            if (!inBranchSection) {
                continue;
            }

            List<WebElement> tds = row.findElements(By.tagName("td"));
            if (tds.size() < 2) {
                continue;
            }

            WebElement adCell = tds.get(1);
            String adText = adCell.getText();
            if (normalize(adText).contains(typeNorm)) {
                lastSelectedAppointmentTypeRow = row;
                scrollIntoView(row);
                return;
            }
        }

        throw new NoSuchElementException("Branş '" + branchName +
                "' altında Ad alanı '" + appointmentTypeName + "' olan satır bulunamadı.");
    }

    private WebElement requireAppointmentTypeRow() {
        if (lastSelectedAppointmentTypeRow == null) {
            throw new IllegalStateException("Önce findAppointmentTypeRow() ile randevu tipi satırı seçilmelidir.");
        }
        return lastSelectedAppointmentTypeRow;
    }

    /**
     * Seçili randevu tipi satırındaki Durum (1. sütun) checkbox'ını aktif eder.
     */
    public void ensureAppointmentTypeStatusActive(String appointmentTypeName) {
        WebElement row = requireAppointmentTypeRow();
        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() < 1) {
            throw new IllegalStateException("Randevu tipi satırında Durum sütunu bulunamadı: " + appointmentTypeName);
        }

        WebElement statusCell = tds.get(0);
        WebElement checkbox = statusCell.findElement(By.cssSelector("input[type='checkbox']"));

        if (!checkbox.isSelected()) {
            WebElement label = statusCell.findElement(By.tagName("label"));
            safeClick(label);

            try {
                wait.until(d -> {
                    try {
                        WebElement cb = statusCell.findElement(By.cssSelector("input[type='checkbox']"));
                        return cb.isSelected();
                    } catch (StaleElementReferenceException e) {
                        return true;
                    }
                });
            } catch (TimeoutException ignored) {
            }
        }
    }

    /**
     * Seçili randevu tipi satırındaki Süre alanını numeric spinner üzerinden
     * istenen değere getirir. (Varsayılan adım: 10 dk)
     */
    public void setAppointmentTypeDuration(String appointmentTypeName, int duration) {
        WebElement row = requireAppointmentTypeRow();
        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() < 3) {
            throw new IllegalStateException(
                    "Randevu tipi satırında Süre sütunu bulunamadı: " + appointmentTypeName
            );
        }

        WebElement durationCell = tds.get(2);

        scrollIntoView(durationCell);
        safeClick(durationCell);

        WebElement numericWrapper = null;
        try {
            numericWrapper = durationCell.findElement(
                    By.cssSelector("span.e-numeric, span[class*='e-numeric']")
            );
        } catch (NoSuchElementException ignored) {
        }

        if (numericWrapper != null) {
            WebElement input;
            try {
                input = numericWrapper.findElement(By.cssSelector(
                        "input.e-numerictextbox, input[type='text'], input[type='number']"
                ));
            } catch (NoSuchElementException e) {
                input = driver.switchTo().activeElement();
            }

            int current = 0;
            String ariaNow = input.getAttribute("aria-valuenow");
            try {
                if (ariaNow != null && !ariaNow.isBlank()) {
                    current = Integer.parseInt(ariaNow);
                }
            } catch (NumberFormatException ignored) {
            }

            int target = duration;
            if (current != target) {
                int step = 10;
                int diff = target - current;
                int clickCount = Math.abs(diff) / step;
                if (clickCount == 0) {
                    clickCount = 1;
                }

                WebElement spinUp = numericWrapper.findElement(
                        By.cssSelector("span.e-input-group-icon.e-spin-up")
                );
                WebElement spinDown = numericWrapper.findElement(
                        By.cssSelector("span.e-input-group-icon.e-spin-down")
                );

                WebElement buttonToClick = diff > 0 ? spinUp : spinDown;

                for (int i = 0; i < clickCount; i++) {
                    safeClick(buttonToClick);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException ignored) {
                    }
                }

                try {
                    WebElement finalInput = input;
                    wait.until(d -> {
                        String now = finalInput.getAttribute("aria-valuenow");
                        if (now == null || now.isBlank()) return false;
                        try {
                            return Integer.parseInt(now) == target;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    });
                } catch (TimeoutException ignored) {
                }
            }

        } else {
            WebElement input;
            try {
                input = durationCell.findElement(
                        By.cssSelector("input[type='text'], input[type='number']")
                );
            } catch (NoSuchElementException ignore) {
                input = driver.switchTo().activeElement();
            }

            typeInto(input, String.valueOf(duration));
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
        }
    }

    // ===================== KAYNAK DROPDOWN AÇMA =====================

    /**
     * Seçili randevu tipi satırındaki Kaynak multi-select alanını açar
     * ve açılan listenin görünür olmasını bekler.
     */
    public void openSourcesDropdownForSelectedAppointmentTypeRow() {
        WebElement row = requireAppointmentTypeRow();

        List<WebElement> tds = row.findElements(By.tagName("td"));
        WebElement kaynakCell = null;
        WebElement multiElement = null;

        for (WebElement td : tds) {
            List<WebElement> candidates = td.findElements(
                    By.cssSelector("div.e-multiselect, span.e-multiselect, div[class*='e-multiselect']")
            );
            if (!candidates.isEmpty()) {
                kaynakCell = td;
                multiElement = candidates.get(0);
                break;
            }
        }

        if (kaynakCell == null || multiElement == null) {
            throw new IllegalStateException(
                    "Seçili randevu tipi satırında Kaynak alanı için multi-select bulunamadı."
            );
        }

        scrollIntoView(multiElement);
        safeClick(multiElement);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("ul.e-list-parent")
        ));
    }

    // ===================== KAYNAK SEÇİM KONTROLÜ =====================

    /**
     * Kaynak sütununda:
     * - optionText "Hepsini seç" ise tüm kaynakların seçili olmasını sağlar,
     * - aksi halde ilgili option'ın seçili olduğundan emin olur.
     * Eğer ilgili kaynak zaten seçiliyse tekrar seçim yapmaz.
     */
    public void ensureAppointmentTypeSelectAllChecked(String appointmentTypeName, String optionText) {
        WebElement row = requireAppointmentTypeRow();
        List<WebElement> tds = row.findElements(By.tagName("td"));
        if (tds.size() < 4) {
            throw new IllegalStateException("Randevu tipi satırında Kaynak sütunu bulunamadı: " + appointmentTypeName);
        }

        WebElement kaynakCell = tds.get(3);
        String normalizedOption = normalize(optionText);

        if (!normalizedOption.isEmpty() && !normalizedOption.equals("hepsini seç")) {
            WebElement wrapper;
            try {
                wrapper = kaynakCell.findElement(By.cssSelector("div.e-multi-select-wrapper"));
            } catch (NoSuchElementException e1) {
                try {
                    wrapper = kaynakCell.findElement(By.cssSelector("span.e-multi-select-wrapper"));
                } catch (NoSuchElementException e2) {
                    wrapper = kaynakCell;
                }
            }

            List<WebElement> selectedChips = new ArrayList<>();
            selectedChips.addAll(wrapper.findElements(By.cssSelector("span.e-chips")));
            selectedChips.addAll(wrapper.findElements(By.cssSelector("li.e-chip")));
            selectedChips.addAll(wrapper.findElements(By.cssSelector("span[class*='e-chips']")));

            for (WebElement chip : selectedChips) {
                String chipText = chip.getText().trim();
                if (!chipText.isEmpty() && normalize(chipText).equals(normalizedOption)) {
                    return;
                }
            }
        }

        safeClick(kaynakCell);

        WebElement listElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("ul.e-list-parent")
                )
        );
        List<WebElement> items = listElement.findElements(By.cssSelector("li.e-list-item"));

        if (items.isEmpty()) {
            throw new NoSuchElementException("Multi-select popup içinde hiç seçenek bulunamadı.");
        }

        if (normalizedOption.isEmpty() || normalizedOption.equals("hepsini seç")) {

            WebElement selectAllItem = null;
            for (WebElement item : items) {
                String textNorm = normalize(item.getText()).replace('\u00A0', ' ');
                if (textNorm.contains("hepsini seç")) {
                    selectAllItem = item;
                    break;
                }
            }

            if (selectAllItem != null) {
                safeClick(selectAllItem);
            } else {
                for (WebElement item : items) {
                    String txt = item.getText().trim();
                    if (txt.isEmpty()) continue;
                    safeClick(item);
                }
            }

            safeClick(kaynakCell);
            return;
        }

        WebElement targetItem = null;
        for (WebElement item : items) {
            String txt = item.getText().trim();
            if (normalize(txt).equals(normalizedOption)) {
                targetItem = item;
                break;
            }
        }

        if (targetItem == null) {
            safeClick(kaynakCell);
            return;
        }

        safeClick(targetItem);

        try {
            wait.until(driver -> {
                WebElement wrapper;
                try {
                    wrapper = kaynakCell.findElement(By.cssSelector("div.e-multi-select-wrapper"));
                } catch (NoSuchElementException e1) {
                    try {
                        wrapper = kaynakCell.findElement(By.cssSelector("span.e-multi-select-wrapper"));
                    } catch (NoSuchElementException e2) {
                        wrapper = kaynakCell;
                    }
                }

                List<WebElement> chips = new ArrayList<>();
                chips.addAll(wrapper.findElements(By.cssSelector("span.e-chips")));
                chips.addAll(wrapper.findElements(By.cssSelector("li.e-chip")));
                chips.addAll(wrapper.findElements(By.cssSelector("span[class*='e-chips']")));

                for (WebElement chip : chips) {
                    String chipText = chip.getText().trim();
                    if (!chipText.isEmpty() && normalize(chipText).equals(normalizedOption)) {
                        return true;
                    }
                }
                return false;
            });
        } catch (TimeoutException ignored) {
        }

        safeClick(kaynakCell);
    }

    /**
     * Randevu Tipi dialogundaki Kaydet butonuna tıklar ve kapanmasını bekler.
     */
    public void clickSaveOnAppointmentTypeDialog() {
        safeClick(appointmentTypeSaveButton);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(appointmentTypeSaveButton));
        } catch (TimeoutException ignored) {
        }
    }

    // =========== (CHECKBOX HELPER'LARI – İleride kullanılmak üzere) ===========

    /**
     * Syncfusion list item içinde checkbox benzeri elementi bulur.
     * Şu an aktif kullanılmıyor; ileride item bazlı kontrol gerektiğinde kullanılabilir.
     */
    @SuppressWarnings("unused")
    private WebElement resolveCheckboxLikeElement(WebElement item) {
        try {
            return item.findElement(By.cssSelector("input[type='checkbox']"));
        } catch (NoSuchElementException e) {
            try {
                return item.findElement(By.cssSelector("span.e-frame, span.e-checkbox"));
            } catch (NoSuchElementException ex) {
                return item;
            }
        }
    }

    /**
     * Bir seçenek satırının işaretli olup olmadığını anlamak için kullanılabilecek yardımcı metot.
     * (Şu an aktif kullanılmıyor.)
     */
    @SuppressWarnings("unused")
    private boolean isItemChecked(WebElement item, WebElement checkboxLike) {
        String ariaChecked = checkboxLike.getAttribute("aria-checked");
        String ariaSelected = checkboxLike.getAttribute("aria-selected");
        String liAriaSelected = item.getAttribute("aria-selected");
        String classes = checkboxLike.getAttribute("class");

        if (ariaChecked != null && ariaChecked.equalsIgnoreCase("true")) return true;
        if (ariaSelected != null && ariaSelected.equalsIgnoreCase("true")) return true;
        if (liAriaSelected != null && liAriaSelected.equalsIgnoreCase("true")) return true;
        return classes != null && classes.contains("e-check");
    }
}

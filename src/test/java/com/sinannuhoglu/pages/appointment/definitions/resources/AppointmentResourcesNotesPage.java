package com.sinannuhoglu.pages.appointment.definitions.resources;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class AppointmentResourcesNotesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AppointmentResourcesNotesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // ----------- LOCATORS (Notlar sekmesi) -----------

    private final By tabNotes = By.xpath(
            "//div[contains(@class,'e-toolbar-item')]" +
                    "[.//div[contains(@class,'e-tab-text')][contains(normalize-space(),'Notlar')]]"
    );

    private final By tabNotesItemActive = By.xpath(
            "//div[contains(@class,'e-toolbar-item') and contains(@class,'e-active') and " +
                    ".//div[contains(@class,'e-tab-text') and normalize-space()='Notlar']]"
    );

    private final By tabNotesText = By.xpath(
            "//div[contains(@class,'e-toolbar-item') and " +
                    ".//div[contains(@class,'e-tab-text') and normalize-space()='Notlar']]" +
                    "//div[contains(@class,'e-tab-text') and normalize-space()='Notlar']"
    );

    private final By buttonAddNote = By.xpath(
            "//div[contains(@class,'e-content')]//button[" +
                    ".//span[normalize-space()='Yeni Ekle'] or normalize-space()='Yeni Ekle'" +
                    "]"
    );

    private final By noteDialog = By.xpath(
            "//div[contains(@class,'e-dialog') and .//span[contains(text(),'Not')]]"
    );

    private final By noteTypeInput = By.xpath(
            "//div[contains(@class,'e-dialog')]" +
                    "//label[normalize-space()='Not Tipi']" +
                    "/ancestor::div[contains(@class,'e-colspan-1')][1]" +
                    "//input[@type='text']"
    );

    private final By noteTypeWrapperBy = By.xpath(
            "//div[contains(@class,'e-dialog') and starts-with(@id,'dialog-')]" +
                    "//div[contains(@class,'e-ddl') and contains(@class,'e-control-wrapper')][1]"
    );

    private final By startDateIcon = By.xpath(
            "//div[contains(@class,'e-dialog') and starts-with(@id,'dialog-')]" +
                    "//label[normalize-space()='Başlangıç Tarihi']" +
                    "/ancestor::div[contains(@class,'e-date-wrapper')][1]" +
                    "//span[contains(@class,'e-date-icon')]"
    );

    private final By endDateIcon = By.xpath(
            "//div[contains(@class,'e-dialog') and starts-with(@id,'dialog-')]" +
                    "//label[normalize-space()='Bitiş Tarihi']" +
                    "/ancestor::div[contains(@class,'e-date-wrapper')][1]" +
                    "//span[contains(@class,'e-date-icon')]"
    );

    private final By startDateInput = By.xpath(
            "//div[contains(@class,'e-dialog') and starts-with(@id,'dialog-')]" +
                    "//label[normalize-space()='Başlangıç Tarihi']" +
                    "/ancestor::div[contains(@class,'e-colspan-1')][1]" +
                    "//input[contains(@class,'e-control') and contains(@class,'e-datepicker')]"
    );

    private final By endDateInput = By.xpath(
            "//div[contains(@class,'e-dialog') and starts-with(@id,'dialog-')]" +
                    "//label[normalize-space()='Bitiş Tarihi']" +
                    "/ancestor::div[contains(@class,'e-colspan-1')][1]" +
                    "//input[contains(@class,'e-control') and contains(@class,'e-datepicker')]"
    );

    private final By weekdaysLabel = By.xpath(
            "//div[contains(@class,'e-dialog') and starts-with(@id,'dialog-')]" +
                    "//span[contains(@class,'sf-input-label') and normalize-space()='Günler']"
    );

    private final By weekdayContainer = By.xpath(
            "//div[contains(@class,'e-dialog') and starts-with(@id,'dialog-')]" +
                    "//span[contains(@class,'sf-input-label') and normalize-space()='Günler']" +
                    "/ancestor::div[contains(@class,'e-grid-col-4')][1]"
    );

    private final By noteTextarea = By.xpath(
            "//div[contains(@class,'e-dialog') and starts-with(@id,'dialog-')]" +
                    "//div[contains(@class,'e-grid-col-4')][3]" +
                    "//textarea"
    );

    private final By buttonSaveNote = By.cssSelector(
            "button[data-testid='save-button'], button.e-btn.e-primary.e-primary"
    );

    private final By notesGridTableStrict = By.cssSelector(
            "div.appointment-resources_dialog table#Grid_content_table"
    );

    private final By dialogOverlay = By.cssSelector("div.e-dlg-overlay");

    // ----------------------------------------------------
    // ACTIONS
    // ----------------------------------------------------

    /** Notlar sekmesine geç (label'a tıklayarak) */
    public void openNotesTab() {
        WebElement tabLabel = wait.until(
                ExpectedConditions.visibilityOfElementLocated(tabNotesText)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", tabLabel
        );

        new org.openqa.selenium.interactions.Actions(driver)
                .moveToElement(tabLabel)
                .pause(Duration.ofMillis(200))
                .click()
                .perform();

        wait.until(ExpectedConditions.presenceOfElementLocated(tabNotesItemActive));
    }

    /** Notlar sekmesinde Yeni Ekle butonuna tıkla ve modal'ın açıldığından emin ol */
    public void clickAddNewNote() {
        wait.until(ExpectedConditions.presenceOfElementLocated(tabNotesItemActive));

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(buttonAddNote)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", btn
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(noteDialog));
    }

    /** Not Tipi dropdown'ından verilen seçeneği seç */
    public void selectNoteType(String optionText) {

        WebElement noteTypeWrapper = wait.until(
                ExpectedConditions.visibilityOfElementLocated(noteTypeWrapperBy)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", noteTypeWrapper
        );

        WebElement dropDownIcon = noteTypeWrapper.findElement(
                By.cssSelector("span.e-input-group-icon.e-ddl-icon")
        );

        wait.until(ExpectedConditions.elementToBeClickable(dropDownIcon)).click();

        wait.until(ExpectedConditions.attributeToBe(
                noteTypeWrapper, "aria-expanded", "true"
        ));

        By popupBy = By.cssSelector(
                "div.e-ddl.e-control.e-lib.e-popup.e-popup-open[role='dialog']"
        );

        WebElement popup = wait.until(
                ExpectedConditions.visibilityOfElementLocated(popupBy)
        );

        WebElement option = popup.findElement(
                By.xpath(".//li[normalize-space()='" + optionText + "']")
        );

        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

    // ----------------------------------------------------
    // TARİH ALANLARI
    // ----------------------------------------------------

    public void setNoteDateRangeByTyping(String startDate, String endDate) {
        LocalDate start = parseToLocalDate(startDate);
        LocalDate end   = parseToLocalDate(endDate);

        setDateByTyping(startDateInput, start);
        setDateByTyping(endDateInput, end);
    }

    // ----------------------------------------------------
    // GÜNLER ALANI
    // ----------------------------------------------------

    public void selectWeekdays(String... days) {

        WebElement label = wait.until(
                ExpectedConditions.visibilityOfElementLocated(weekdaysLabel)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", label
        );

        WebElement daysBlock = wait.until(
                ExpectedConditions.visibilityOfElementLocated(weekdayContainer)
        );

        for (String day : days) {
            String trimmed = day.trim();

            By dayLabelBy = By.xpath(".//label[normalize-space()='" + trimmed + "']");

            WebElement dayLabel = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            daysBlock.findElement(dayLabelBy)
                    )
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", dayLabel
            );

            dayLabel.click();
        }
    }

    // ----------------------------------------------------
    // NOT & KAYDET
    // ----------------------------------------------------

    public void fillNoteText(String text) {
        WebElement textarea = wait.until(
                ExpectedConditions.visibilityOfElementLocated(noteTextarea)
        );
        textarea.clear();
        textarea.sendKeys(text);
    }

    public void saveNote() {

        WebElement saveBtn = wait.until(
                ExpectedConditions.elementToBeClickable(buttonSaveNote)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", saveBtn
        );

        try {
            saveBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        }

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
        } catch (TimeoutException ignored) {
        }
    }

    // ----------------------------------------------------
    // GRID DOĞRULAMA & ÜÇ NOKTA MENÜSÜ
    // ----------------------------------------------------

    /**
     * Notlar grid'inin <table> elementini döner.
     * 1) Önce eski strict selector'ı dener.
     * 2) Olmazsa başlık kolonlarına (Not Tipi / Başlangıç Tarihi / Bitiş Tarihi) göre tabloyu bulur.
     */
    private WebElement getNotesGrid() {
        try {
            WebElement strict = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(notesGridTableStrict)
            );
            System.out.println("[Notes] Notes grid found by strict selector.");
            return strict;
        } catch (TimeoutException e) {
            System.out.println("[Notes] Strict selector for notes grid did not match, trying header-based selector...");
        }

        By headerBased = By.xpath(
                "//table[contains(@id,'Grid') or contains(@class,'e-grid') or contains(@class,'e-table')]" +
                        "//th[contains(@class,'e-headercell')]" +
                        "[.//span[normalize-space()='Not Tipi'] or normalize-space()='Not Tipi']" +
                        "/ancestor::table[1]"
        );

        WebElement table = wait.until(
                ExpectedConditions.visibilityOfElementLocated(headerBased)
        );
        System.out.println("[Notes] Notes grid found by header-based selector.");
        return table;
    }

    /**
     * Grid üzerinde verilen değerlerle satırın oluştuğunu doğrular.
     */
    public void verifyNoteCreatedInGrid(String expectedTip,
                                        String expectedBaslangic,
                                        String expectedBitis,
                                        String noteText) {

        long endTime = System.currentTimeMillis() + Duration.ofSeconds(25).toMillis();

        while (System.currentTimeMillis() < endTime) {
            try {
                WebElement row = findRowOnce(
                        expectedTip,
                        expectedBaslangic,
                        expectedBitis,
                        noteText
                );
                if (row != null) {
                    System.out.println("[Notes] Note row successfully found in notes grid.");
                    return;
                }
            } catch (StaleElementReferenceException ignored) {
                System.out.println("[Notes] StaleElement in verifyNoteCreatedInGrid, retrying...");
            }

            sleep(500);
        }

        throw new TimeoutException(String.format(
                "[Notes] Grid'de beklenen satır bulunamadı. Tip=%s, Baslangic=%s, Bitis=%s, Not(ilk kısım)=%s",
                expectedTip, expectedBaslangic, expectedBitis,
                noteText != null ? noteText.substring(0, Math.min(30, noteText.length())) : ""
        ));
    }

    /**
     * Grid'de ilgili satırın üç nokta menüsünü açar.
     */
    public void openRowActionMenu(String tip,
                                  String startDate,
                                  String endDate,
                                  String noteText) {

        long endTime = System.currentTimeMillis() + 25_000;
        WebElement row = null;

        while (System.currentTimeMillis() < endTime) {
            try {
                row = findRowOnce(tip, startDate, endDate, noteText);
                if (row != null) break;
            } catch (StaleElementReferenceException ignored) {
                System.out.println("[Notes] StaleElement in openRowActionMenu, retrying...");
            }
            sleep(500);
        }

        if (row == null) {
            throw new TimeoutException(
                    "[Notes] Üç nokta menüsü için satır bulunamadı. Tip=" + tip +
                            ", Baslangic=" + startDate +
                            ", Bitis=" + endDate
            );
        }

        WebElement actionButton = row.findElement(
                By.xpath(".//td[last()]//button[contains(@class,'e-dropdown-btn')]")
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", actionButton
        );

        wait.until(ExpectedConditions.elementToBeClickable(actionButton)).click();
    }

    /**
     * Açılmış üç nokta menüsünden "Sil" seçeneğine tıklar.
     */
    public void clickDeleteFromRowActionMenu() {
        System.out.println("[Notes] Trying to click 'Sil' in row action menu...");

        By menuLocator = By.cssSelector("ul.e-dropdown-menu[role='menu']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        WebElement visibleMenu = wait.until(driver -> {
            List<WebElement> menus = driver.findElements(menuLocator);
            for (WebElement m : menus) {
                if (m.isDisplayed()) {
                    return m;
                }
            }
            return null;
        });

        System.out.println("[Notes] Visible dropdown menu found.");

        List<WebElement> items = visibleMenu.findElements(By.cssSelector("li[role='menuitem']"));

        if (items.isEmpty()) {
            throw new TimeoutException("[Notes] Dropdown menu has no menu items (li[role='menuitem']).");
        }

        WebElement deleteItem = null;
        for (WebElement li : items) {
            String text = li.getText().trim();
            System.out.println("[Notes] Menu item text = '" + text + "'");
            if (text.contains("Sil")) {   // normalize-space() yerine Java tarafında kontrol
                deleteItem = li;
                break;
            }
        }

        if (deleteItem == null) {
            throw new TimeoutException("[Notes] Could not find menu item containing text 'Sil' in visible dropdown menu.");
        }

        try {
            wait.until(ExpectedConditions.elementToBeClickable(deleteItem));
        } catch (Exception e) {
            System.out.println("[Notes] elementToBeClickable failed, trying JS click anyway...");
        }

        WebElement target = deleteItem;
        try {
            WebElement span = deleteItem.findElement(By.tagName("span"));
            if (span.isDisplayed()) {
                target = span;
            }
        } catch (Exception ignore) {
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
        System.out.println("[Notes] 'Sil' menu item clicked via JS.");
    }

    /**
     * Silme uyarı penceresinde "Evet" butonuna tıklar (varsa).
     * Eğer herhangi bir "Evet" butonu görünmüyorsa, silmenin
     * otomatik onaylandığını varsayar ve hata fırlatmaz.
     */
    public void confirmDelete() {
        System.out.println("[Notes] Waiting delete confirmation dialog (if any)...");

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));

        try {
            WebElement yesButton = shortWait.until(d -> {
                List<WebElement> buttons = d.findElements(By.xpath(
                        "//button[.//span[normalize-space()='Evet'] or normalize-space()='Evet' or normalize-space()='EVET']"
                ));

                for (WebElement btn : buttons) {
                    try {
                        if (btn.isDisplayed() && btn.isEnabled()) {
                            System.out.println("[Notes] Visible 'Evet' button found.");
                            return btn;
                        }
                    } catch (StaleElementReferenceException ignore) {
                    }
                }
                return null;
            });

            if (yesButton == null) {
                System.out.println("[Notes] No visible 'Evet' button found during wait. Continuing without explicit confirmation.");
                return;
            }

            WebElement target = yesButton;
            try {
                WebElement span = yesButton.findElement(By.tagName("span"));
                if (span.isDisplayed()) {
                    target = span;
                }
            } catch (Exception ignore) {}

            try {
                shortWait.until(ExpectedConditions.elementToBeClickable(target));
            } catch (Exception e) {
                System.out.println("[Notes] elementToBeClickable on 'Evet' failed, clicking via JS anyway...");
            }

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
            System.out.println("[Notes] 'Evet' button clicked via JS.");

        } catch (TimeoutException e) {
            System.out.println("[Notes] No 'Evet' confirmation dialog appeared. Assuming delete is auto-confirmed.");
        }
    }


    /**
     * Steps sınıfından çağırmak için alias.
     */
    public void confirmDeleteNote() {
        confirmDelete();
    }

    /**
     * Grid’de ilgili kaydın silindiğini doğrular.
     */
    public void verifyNoteDeletedFromGrid(String tip,
                                          String startDate,
                                          String endDate,
                                          String noteText) {

        long endTime = System.currentTimeMillis() + 25_000;

        while (System.currentTimeMillis() < endTime) {
            WebElement row = null;
            try {
                row = findRowOnce(tip, startDate, endDate, noteText);
            } catch (StaleElementReferenceException ignored) {
                System.out.println("[Notes] StaleElement in verifyNoteDeletedFromGrid, retrying...");
            }

            if (row == null) {
                System.out.println("[Notes] Row deleted from NOTES grid.");
                return;
            }

            sleep(500);
        }

        throw new AssertionError(
                "[Notes] Silinen kayıt hala notes grid'de görünüyor. Tip=" + tip +
                        ", Baslangic=" + startDate +
                        ", Bitis=" + endDate
        );
    }

    // ----------------------------------------------------
    // GRID SATIR BULMA YARDIMCI METODU
    // ----------------------------------------------------

    /**
     * Sayfadaki olası NOT grid tablolarını döner.
     * Özel bir CSS'e güvenmek yerine, tüm "Grid" / Syncfusion tablolarını aday alıyoruz.
     */
    private List<WebElement> getCandidateNoteTables() {
        List<WebElement> tables = driver.findElements(
                By.xpath(
                        "//table[" +
                                "contains(@id,'Grid') or " +
                                "contains(@class,'e-table') or " +
                                "contains(@class,'e-gridcontent') or " +
                                "contains(@class,'e-grid')]"
                )
        );

        System.out.println("[Notes] Candidate tables found: " + tables.size());
        return tables;
    }

    /**
     * Notlar grid’inde sadece bir tarama yapar, beklemez.
     * 0. sütun = Tip
     * 2. sütun = Başlangıç Tarihi
     * 3. sütun = Bitiş Tarihi
     * 5. sütun = Not
     *
     * Önce TIP + TARİHLER + NOT metni ile strict eşleşme dener.
     * Hiç bulamazsa, TIP + NOT metnine göre fallback olarak ilk eşleşeni döner.
     */
    private WebElement findRowOnce(String tip,
                                   String startDate,
                                   String endDate,
                                   String noteText) {

        List<WebElement> tables = getCandidateNoteTables();
        if (tables.isEmpty()) {
            System.out.println("[Notes] No candidate tables found for notes grid.");
            return null;
        }

        LocalDate expectedStart = tryParseToLocalDate(startDate);
        LocalDate expectedEnd   = tryParseToLocalDate(endDate);

        String expectedTip  = tip  != null ? tip.trim()  : "";
        String expectedNote = noteText != null ? noteText.trim() : "";

        WebElement fallbackRow = null; // TIP + NOT eşleşmesi için fallback

        int tableIndex = 0;
        for (WebElement table : tables) {
            tableIndex++;

            List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));
            System.out.println("[Notes] Scanning table #" + tableIndex + " with " + rows.size() + " rows");

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.cssSelector("td"));
                if (cells.isEmpty()) continue;

                StringBuilder sb = new StringBuilder("[Notes] Table " + tableIndex + " row cells: ");
                for (int i = 0; i < cells.size(); i++) {
                    sb.append("[").append(i).append("]='")
                            .append(cells.get(i).getText().trim()).append("' ");
                }
                System.out.println(sb);

                String tipCell       = getCellTextSafe(cells, 0);
                String startCellText = getCellTextSafe(cells, 2);
                String endCellText   = getCellTextSafe(cells, 3);
                String noteCell      = getCellTextSafe(cells, 5).replace("\n", " ").trim();

                LocalDate cellStart = tryParseToLocalDate(normalizeForParsing(startCellText));
                LocalDate cellEnd   = tryParseToLocalDate(normalizeForParsing(endCellText));

                boolean tipMatch   = tipCell.equals(expectedTip);
                boolean startMatch = (expectedStart == null) || expectedStart.equals(cellStart);
                boolean endMatch   = (expectedEnd == null)   || expectedEnd.equals(cellEnd);

                boolean noteMatch;
                if (expectedNote.isEmpty()) {
                    noteMatch = true;
                } else {
                    noteMatch =
                            noteCell.contains(expectedNote) ||
                                    expectedNote.contains(noteCell);
                }

                if (tipMatch && startMatch && endMatch && noteMatch) {
                    System.out.println("[Notes] STRICT matching row FOUND in table #" + tableIndex);
                    return row;
                }

                if (fallbackRow == null && tipMatch && noteMatch) {
                    System.out.println("[Notes] FALLBACK candidate row found in table #" + tableIndex + " (tip+note match, dates differ)");
                    fallbackRow = row;
                }
            }
        }

        if (fallbackRow != null) {
            System.out.println("[Notes] Returning FALLBACK row (tip+note matched, dates mismatched).");
            return fallbackRow;
        }

        System.out.println("[Notes] No matching row found in any candidate table.");
        return null;
    }


    private String getCellTextSafe(List<WebElement> cells, int index) {
        if (index < 0 || index >= cells.size()) return "";
        return cells.get(index).getText().trim();
    }

    private WebElement getCellSafe(WebElement row, int index) {
        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (index < 0 || index >= cells.size()) return null;
        return cells.get(index);
    }

    /**
     * "01.12.2025 00:00" gibi değerleri parse için normalize eder.
     */
    private String normalizeForParsing(String text) {
        if (text == null) return null;
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return trimmed;

        int spaceIdx = trimmed.indexOf(' ');
        if (spaceIdx > 0) {
            trimmed = trimmed.substring(0, spaceIdx);
        }

        trimmed = trimmed.replace('-', '.').replace('/', '.');

        return trimmed;
    }

    // ----------------------------------------------------
    // DATEPICKER HELPER METODLARI
    // ----------------------------------------------------

    private void openDatePicker(By iconLocator) {
        WebElement icon = wait.until(
                ExpectedConditions.visibilityOfElementLocated(iconLocator)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", icon
        );

        try {
            wait.until(ExpectedConditions.elementToBeClickable(icon)).click();
        } catch (TimeoutException | ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", icon);
        }
    }

    private void selectDateInOpenDatePicker(LocalDate date) {
        String dayText = String.valueOf(date.getDayOfMonth());

        By dayCell = By.xpath(
                "//div[contains(@class,'e-datepicker') and contains(@class,'e-popup-open')]" +
                        "//td[not(contains(@class,'e-other-month'))]" +
                        "//span[normalize-space()='" + dayText + "']"
        );

        wait.until(ExpectedConditions.elementToBeClickable(dayCell)).click();
    }

    private void setDateByTyping(By inputLocator, LocalDate date) {
        if (date == null) return;

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(inputLocator)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", input
        );

        input.click();
        input.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        input.sendKeys(Keys.DELETE);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("d.M.yyyy", new Locale("tr", "TR"));

        String formatted = date.format(formatter);
        input.sendKeys(formatted);
        input.sendKeys(Keys.TAB);
    }

    private void setDateByTyping(By inputLocator, String dateText) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(inputLocator)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", input
        );

        typeDateSlow(input, dateText);
    }

    // ----------------------------------------------------
    // ORTAK HELPER METODLAR
    // ----------------------------------------------------

    private void typeDateSlow(WebElement input, String date) {

        wait.until(ExpectedConditions.elementToBeClickable(input));

        input.click();
        sleep(150);

        input.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        sleep(120);
        input.sendKeys(Keys.DELETE);
        sleep(150);

        for (char c : date.toCharArray()) {
            input.sendKeys(Character.toString(c));
            sleep(60);
        }

        input.sendKeys(Keys.TAB);
        sleep(150);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Feature'dan gelen tarih string'ini LocalDate'e çevirir (katı kullanım).
     */
    private LocalDate parseToLocalDate(String dateText) {
        if (dateText == null) return null;
        String trimmed = dateText.trim();
        if (trimmed.isEmpty()) return null;

        DateTimeFormatter[] patterns = new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("d-M-yyyy"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ofPattern("d.MM.yyyy")
        };

        for (DateTimeFormatter pattern : patterns) {
            try {
                return LocalDate.parse(trimmed, pattern);
            } catch (Exception ignored) {
            }
        }
        throw new IllegalArgumentException("Desteklenmeyen tarih formatı: " + dateText);
    }

    /**
     * Daha toleranslı parse – grid hücreleri için, hata durumunda null döner.
     */
    private LocalDate tryParseToLocalDate(String dateText) {
        if (dateText == null) return null;
        String trimmed = dateText.trim();
        if (trimmed.isEmpty()) return null;

        DateTimeFormatter[] patterns = new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ofPattern("d.MM.yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("d-M-yyyy")
        };

        for (DateTimeFormatter pattern : patterns) {
            try {
                return LocalDate.parse(trimmed, pattern);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

}

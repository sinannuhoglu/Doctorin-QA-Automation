package com.sinannuhoglu.pages.appointment.definitions.resources;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Randevu Kaynakları > İzinler sekmesi için Page Object.
 */
public class AppointmentResourcesLeavesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AppointmentResourcesLeavesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // ---------------------------- GENEL LOCATOR'LAR ----------------------------

    private final By toolbarItems =
            By.cssSelector("div.e-toolbar div.e-toolbar-items div.e-toolbar-item");

    private static final String LEAVES_TAB_TEXT = "İzinler";

    private final By leavesGridTable = By.xpath(
            "//div[contains(@class,'e-grid')]//table[contains(@class,'e-table')]"
    );

    private final By addNewButton = By.xpath(
            "//button[.//span[normalize-space()='Yeni Ekle'] or normalize-space()='Yeni Ekle']"
    );

    // Aktif izin dialog'u (formun bağlı olduğu e-dialog)
    private final By leavesDialogRoot = By.xpath(
            "//div[contains(@class,'e-dialog') and contains(@class,'e-popup-open')]" +
                    "//form[contains(@class,'e-data-form')]/ancestor::div[contains(@class,'e-dialog')]"
    );

    private final By leavesForm = By.xpath(
            "//div[contains(@class,'e-dialog') and contains(@class,'e-popup-open')]" +
                    "//form[contains(@class,'e-data-form')]"
    );

    // Aktif datepicker popup (takvim)
    private final By datepickerPopup = By.cssSelector(
            "div.e-control.e-datepicker.e-lib.e-popup-wrapper.e-popup-container.e-popup-open[role='dialog']"
    );

    // ---------------------------- HELPER METOTLAR ----------------------------

    private JavascriptExecutor js() {
        return (JavascriptExecutor) driver;
    }

    private void scrollIntoView(WebElement element) {
        js().executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    private void safeClick(WebElement element) {
        scrollIntoView(element);
        try {
            element.click();
        } catch (Exception e) {
            js().executeScript("arguments[0].click();", element);
        }
    }

    // ---------------------------- SEKME İŞLEMLERİ ----------------------------

    /**
     * Toolbar üzerinden "İzinler" sekmesine tıklar,
     * sekmenin aktif olduğunu ve izinler gridinin yüklendiğini bekler.
     */
    public void openLeavesTab() {
        List<WebElement> tabs = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(toolbarItems)
        );

        WebElement leavesTab = null;

        for (WebElement item : tabs) {
            try {
                WebElement textEl = item.findElement(By.cssSelector(".e-tab-text"));
                String text = textEl.getText().trim();
                if (LEAVES_TAB_TEXT.equals(text)) {
                    leavesTab = item;
                    break;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }

        if (leavesTab == null) {
            throw new NoSuchElementException(
                    "[Leaves] Toolbar'da '" + LEAVES_TAB_TEXT + "' sekmesi bulunamadı."
            );
        }

        safeClick(leavesTab);

        wait.until(d -> {
            try {
                List<WebElement> items = d.findElements(toolbarItems);
                for (WebElement item : items) {
                    WebElement textEl = item.findElement(By.cssSelector(".e-tab-text"));
                    String text = textEl.getText().trim();
                    if (LEAVES_TAB_TEXT.equals(text)
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
            wait.until(ExpectedConditions.visibilityOfElementLocated(leavesGridTable));
        } catch (TimeoutException ignored) {
        }

        System.out.println("[Leaves] İzinler sekmesi açıldı.");
    }

    // ---------------------------- YENİ EKLE BUTONU ----------------------------

    /**
     * İzinler sekmesinde Yeni Ekle butonuna tıklar
     * ve izin formu dialog'unun açıldığını doğrular.
     */
    public void clickAddNewLeave() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(addNewButton)
        );

        safeClick(button);
        System.out.println("[Leaves] 'Yeni Ekle' butonuna tıklandı.");

        waitForLeavesForm();
    }

    // ---------------------------- FORM HELPER'LARI ----------------------------

    private void waitForLeavesForm() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(leavesDialogRoot));
        wait.until(ExpectedConditions.visibilityOfElementLocated(leavesForm));
        System.out.println("[Leaves] İzinler formu açıldı.");
    }

    private WebElement getFormGroupByLabel(String labelText) {
        By by = By.xpath(
                "//div[contains(@class,'e-dialog') and contains(@class,'e-popup-open')]" +
                        "//form[contains(@class,'e-data-form')]" +
                        "//label[normalize-space()='" + labelText + "']" +
                        "/parent::div[contains(@class,'e-form-group')]"
        );
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    // ---------------------------- TARİH SEÇİMİ ----------------------------

    private WebElement openDatepickerForLabel(String labelText) {
        WebElement group = getFormGroupByLabel(labelText);
        WebElement icon = group.findElement(
                By.cssSelector("span.e-input-group-icon.e-date-icon")
        );

        safeClick(icon);

        return wait.until(ExpectedConditions.visibilityOfElementLocated(datepickerPopup));
    }

    private void selectDateInOpenDatepicker(WebElement popup, LocalDate targetDate) {
        String dayText = String.valueOf(targetDate.getDayOfMonth());

        By dayBy = By.xpath(
                ".//td[not(contains(@class,'e-other-month'))]//span[normalize-space()='" + dayText + "']"
        );

        WebElement daySpan = popup.findElement(dayBy);
        safeClick(daySpan);

        System.out.println("[Leaves] Datepicker’dan tarih seçildi: " + targetDate);
    }

    private void selectDateForLabel(String labelText, String dateText) {
        WebElement group = getFormGroupByLabel(labelText);
        WebElement input = group.findElement(By.xpath(".//input[contains(@class,'e-datepicker')]"));

        scrollIntoView(input);

        String beforeVal = input.getAttribute("value");
        LocalDate baseDate = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");

        try {
            if (beforeVal != null && !beforeVal.isBlank()) {
                baseDate = LocalDate.parse(beforeVal.trim(), formatter);
            }
        } catch (DateTimeParseException ignored) {
        }

        LocalDate targetDate = null;

        if (dateText != null && !dateText.isBlank()) {
            try {
                targetDate = LocalDate.parse(dateText.trim(), formatter);
            } catch (DateTimeParseException ignored) {
            }
        }

        if (targetDate == null) {
            if (baseDate == null) {
                baseDate = LocalDate.now();
            }
            targetDate = baseDate.plusDays(1);
            System.out.println("[Leaves] Parametre tarih parse edilemedi, yarın seçilecek: " + targetDate);
        }

        WebElement popup = openDatepickerForLabel(labelText);
        selectDateInOpenDatepicker(popup, targetDate);

        try {
            String finalBeforeVal = beforeVal;
            wait.until(d -> {
                try {
                    String val = input.getAttribute("value");
                    return val != null && !val.equals(finalBeforeVal);
                } catch (StaleElementReferenceException e) {
                    WebElement freshGroup = getFormGroupByLabel(labelText);
                    WebElement freshInput = freshGroup.findElement(
                            By.xpath(".//input[contains(@class,'e-datepicker')]")
                    );
                    String val = freshInput.getAttribute("value");
                    return val != null && !val.equals(finalBeforeVal);
                }
            });
        } catch (TimeoutException ignored) {
        }

        try {
            String actual = group.findElement(
                            By.xpath(".//input[contains(@class,'e-datepicker')]"))
                    .getAttribute("value");
            System.out.println("[Leaves] '" + labelText + "' alanı son değer: " + actual);
        } catch (Exception ignored) {
        }
    }

    // ---------------------------- SAAT SEÇİMİ ----------------------------

    private void typeIntoTimeInput(String labelText, String timeValue) {
        WebElement group = getFormGroupByLabel(labelText);

        WebElement input;
        try {
            input = group.findElement(By.xpath(".//input[contains(@class,'e-timepicker')]"));
        } catch (NoSuchElementException e) {
            input = group.findElement(By.xpath(".//input[@type='text']"));
        }

        scrollIntoView(input);
        input.click();

        try {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        } catch (Exception ex) {
            try {
                input.sendKeys(Keys.chord(Keys.COMMAND, "a"));
            } catch (Exception ignored) {
            }
        }
        input.sendKeys(Keys.DELETE);
        input.sendKeys(timeValue);

        js().executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                input, timeValue
        );

        try {
            WebElement finalInput = input;
            wait.until(d -> {
                try {
                    String val = finalInput.getAttribute("value");
                    return timeValue.equals(val);
                } catch (StaleElementReferenceException se) {
                    WebElement freshGroup = getFormGroupByLabel(labelText);
                    WebElement freshInput;
                    try {
                        freshInput = freshGroup.findElement(By.xpath(".//input[contains(@class,'e-timepicker')]"));
                    } catch (NoSuchElementException e) {
                        freshInput = freshGroup.findElement(By.xpath(".//input[@type='text']"));
                    }
                    String val = freshInput.getAttribute("value");
                    return timeValue.equals(val);
                }
            });
        } catch (TimeoutException ignored) {
        }

        try {
            String actual = input.getAttribute("value");
            System.out.println("[Leaves] '" + labelText + "' alanı son değer: " + actual);
        } catch (Exception ignored) {
        }
    }

    // ---------------------------- TEKRAR DROPDOWN ----------------------------

    private void openRepeatDropdown() {
        WebElement group = getFormGroupByLabel("Tekrar");
        WebElement icon = group.findElement(
                By.cssSelector("span.e-input-group-icon.e-ddl-icon")
        );
        safeClick(icon);
    }

    private void selectFromRepeatDropdown(String optionText) {
        String normalized = normalizeDisplayText(optionText);

        By optionBy = By.xpath(
                "//div[contains(@class,'e-ddl') and contains(@class,'e-popup-open') and @role='dialog']" +
                        "//li[normalize-space()='" + normalized + "']"
        );

        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement option = wait.until(
                        ExpectedConditions.elementToBeClickable(optionBy)
                );
                safeClick(option);

                System.out.println("[Leaves] Tekrar dropdown’ından seçenek seçildi: " + normalized);

                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(
                            By.xpath("//div[contains(@class,'e-ddl') and contains(@class,'e-popup-open') and @role='dialog']")
                    ));
                } catch (TimeoutException ignored) {
                }
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                System.out.println("[Leaves] Tekrar dropdown option stale oldu, retry: " + attempts);
            } catch (Exception e) {
                try {
                    WebElement option = driver.findElement(optionBy);
                    safeClick(option);
                    System.out.println("[Leaves] Tekrar dropdown’ında JS click ile seçim yapıldı: " + normalized);
                    return;
                } catch (Exception inner) {
                    attempts++;
                    System.out.println("[Leaves] Tekrar dropdown seçiminde hata, retry: " + attempts
                            + " -> " + inner.getClass().getSimpleName());
                }
            }
        }

        throw new RuntimeException("[Leaves] Tekrar dropdown’ında seçenek seçilemedi: " + normalized);
    }

    private String normalizeDisplayText(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }
        String[] parts = text.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i];
            if (!p.isEmpty()) {
                sb.append(Character.toUpperCase(p.charAt(0)));
                if (p.length() > 1) {
                    sb.append(p.substring(1));
                }
            }
            if (i < parts.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    // ---------------------------- PUBLIC FORM AKIŞI ----------------------------

    public void setStartDate(String dateText) {
        waitForLeavesForm();
        selectDateForLabel("Başlangıç Tarihi", dateText);
    }

    public void selectStartTime(String timeText) {
        typeIntoTimeInput("Başlangıç Saati", timeText);
    }

    public void selectEndTime(String timeText) {
        typeIntoTimeInput("Bitiş Saati", timeText);
    }

    public void selectRepeatType(String optionText) {
        openRepeatDropdown();
        selectFromRepeatDropdown(optionText);
    }

    public void setDescription(String description) {
        By descriptionTextareaBy = By.xpath(
                "//div[contains(@class,'e-dialog') and contains(@class,'e-popup-open')]" +
                        "//form[contains(@class,'e-data-form')]" +
                        "//label[normalize-space()='Açıklama']" +
                        "/parent::div[contains(@class,'e-form-group')]" +
                        "//textarea"
        );

        By descriptionHiddenInputBy = By.xpath(
                "//div[contains(@class,'e-dialog') and contains(@class,'e-popup-open')]" +
                        "//form[contains(@class,'e-data-form')]" +
                        "//input[@type='hidden' and " +
                        "(contains(@name,'Description') or contains(@id,'Description') " +
                        " or contains(@name,'description') or contains(@id,'description'))]"
        );

        int attempts = 0;

        while (attempts < 3) {
            try {
                WebElement textarea = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(descriptionTextareaBy)
                );

                scrollIntoView(textarea);
                textarea.click();
                textarea.clear();
                textarea.sendKeys(description);

                js().executeScript(
                        "arguments[0].value = arguments[1];" +
                                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                                "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                        textarea, description
                );

                try {
                    List<WebElement> hiddenList = driver.findElements(descriptionHiddenInputBy);
                    if (!hiddenList.isEmpty()) {
                        WebElement hidden = hiddenList.get(0);
                        js().executeScript(
                                "arguments[0].value = arguments[1];" +
                                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                                        "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                                hidden, description
                        );
                    }
                } catch (StaleElementReferenceException ignored) {
                }

                try {
                    wait.until(d -> description.equals(textarea.getAttribute("value")));
                } catch (TimeoutException ignored) {
                }

                try {
                    String val = textarea.getAttribute("value");
                    System.out.println("[Leaves] Açıklama alanına metin yazıldı. Son değer: " + val);
                } catch (Exception e) {
                    System.out.println("[Leaves] Açıklama alanına metin yazıldı.");
                }

                return;

            } catch (StaleElementReferenceException e) {
                attempts++;
                System.out.println("[Leaves] Açıklama alanı stale oldu, yeniden denenecek. Attempt: " + attempts);
            }
        }

        throw new RuntimeException("[Leaves] Açıklama alanına metin yazılamadı, 3 deneme de başarısız oldu.");
    }

    // ---------------------------- KAYDET BUTONU ----------------------------

    /**
     * İzin formundaki Kaydet butonuna tıklar ve form dialog'unun kapanmasını bekler.
     */
    public void clickSaveButton() {
        By saveButtonBy = By.xpath(
                "//div[contains(@class,'e-dialog') and contains(@class,'e-popup-open')]" +
                        "//form[contains(@class,'e-data-form')]" +
                        "//button[@type='submit' and (normalize-space()='Kaydet' " +
                        "or .//span[normalize-space()='Kaydet'])]"
        );

        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement saveButton = wait.until(
                        ExpectedConditions.elementToBeClickable(saveButtonBy)
                );
                WebElement dialog = saveButton.findElement(By.xpath(
                        "ancestor::div[contains(@class,'e-dialog') and contains(@class,'e-popup-open')]"
                ));

                safeClick(saveButton);
                System.out.println("[Leaves] Kaydet butonuna tıklandı.");

                // Dialog’un kapanmasını bekle
                wait.until(ExpectedConditions.stalenessOf(dialog));
                System.out.println("[Leaves] İzinler pop-up penceresi kapandı.");

                return;
            } catch (StaleElementReferenceException | TimeoutException | NoSuchElementException e) {
                attempts++;
                System.out.println("[Leaves] Kaydet sırasında timeout/NoSuchElement/Stale, yeniden denenecek. Attempt: " + attempts);
            }
        }

        throw new RuntimeException("[Leaves] Kaydet butonuna tıklanamadı, 3 deneme de başarısız oldu.");
    }

    // ---------------------------- DOĞRULAMALAR – EKLEME ----------------------------

    public void verifyLeaveFormClosedAndGridVisible() {
        boolean dialogVisible;
        try {
            dialogVisible = driver.findElements(leavesDialogRoot)
                    .stream().anyMatch(WebElement::isDisplayed);
        } catch (Exception e) {
            dialogVisible = false;
        }

        if (dialogVisible) {
            throw new AssertionError("[Leaves] Kayıt sonrası izin formu hala açık görünüyor.");
        }

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(leavesGridTable));
        } catch (Exception e) {
            throw new AssertionError("[Leaves] Kayıt sonrası izinler grid’i görüntülenemedi.");
        }

        System.out.println("[Leaves] Kayıt sonrası form kapandı ve izinler grid’i görüntülendi.");
    }

    private By buildRowLocator(String date, String startTime, String endTime, String description) {
        String expectedStart = date + " " + startTime;
        String expectedEnd   = date + " " + endTime;

        StringBuilder xpath = new StringBuilder(
                "//div[contains(@class,'e-grid')]//table[contains(@class,'e-table')]" +
                        "//tr[contains(@class,'e-row')]" +
                        "[.//td[1]//span[normalize-space()='" + expectedStart + "'] " +
                        " and .//td[2]//span[normalize-space()='" + expectedEnd + "']"
        );

        if (description != null && !description.isBlank()) {
            xpath.append(" and .//td[3][normalize-space()='" + description + "']");
        }

        xpath.append("]");

        return By.xpath(xpath.toString());
    }

    private WebElement findLeaveRow(String date, String startTime, String endTime, String description) {
        By rowBy = buildRowLocator(date, startTime, endTime, description);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(rowBy));
    }

    /**
     * Griddeki satırı tarih + saat aralığı + açıklamaya göre doğrular.
     */
    public void verifyLeaveRow(String date, String startTime, String endTime, String description) {
        WebElement row = findLeaveRow(date, startTime, endTime, description);

        WebElement descCell = row.findElement(By.xpath("./td[3]"));
        String actualDesc = descCell.getText().trim();

        System.out.println("[Leaves] Grid satırı bulundu. Açıklama: '" + actualDesc + "'");

        if (!description.equals(actualDesc)) {
            throw new AssertionError("[Leaves] Açıklama metni beklenen ile uyuşmuyor. Beklenen: '"
                    + description + "', Gerçek: '" + actualDesc + "'");
        }
    }

    // ---------------------------- SİLME AKIŞI ----------------------------

    /**
     * Verilen tarih + saat aralığı + açıklamaya sahip satırın
     * 4. sütunundaki üç nokta menüsünden "Sil" seçeneğine tıklar.
     */
    public void clickDeleteFromRow(String date, String startTime, String endTime, String description) {
        String expectedStart = date + " " + startTime;
        String expectedEnd   = date + " " + endTime;

        By rowBy = By.xpath(
                "//div[contains(@class,'e-grid')]//table[contains(@class,'e-table')]" +
                        "//tr[contains(@class,'e-row')]" +
                        "[.//td[1]//span[normalize-space()='" + expectedStart + "'] " +
                        " and .//td[2]//span[normalize-space()='" + expectedEnd + "'] " +
                        " and .//td[3][normalize-space()='" + description + "']]"
        );

        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowBy));
        scrollIntoView(row);

        WebElement actionsCell = row.findElement(By.xpath("./td[4]"));
        WebElement caretIcon = actionsCell.findElement(
                By.cssSelector("button span.e-btn-icon.e-icons.e-caret")
        );

        int attempts = 0;
        while (attempts < 3) {
            attempts++;
            try {
                new Actions(driver)
                        .moveToElement(actionsCell)
                        .perform();

                js().executeScript("arguments[0].click();", caretIcon);
                System.out.println("[Leaves] Üç nokta menüsü caret ikonuna tıklandı. Deneme: " + attempts);

                By menuUlBy = By.xpath(
                        "//ul[contains(@class,'e-dropdown-menu') and @role='menu']"
                );

                WebElement menu = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(menuUlBy)
                );

                By deleteLiBy = By.xpath(
                        ".//li[.//span[normalize-space()='Sil'] or normalize-space()='Sil']"
                );

                WebElement deleteLi = menu.findElement(deleteLiBy);
                safeClick(deleteLi);

                System.out.println("[Leaves] Üç nokta menüsünden 'Sil' seçeneğine tıklandı.");
                return;

            } catch (TimeoutException | StaleElementReferenceException e) {
                System.out.println("[Leaves] Üç nokta menüsü açılamadı / 'Sil' bulunamadı. Retry: " + attempts
                        + " -> " + e.getClass().getSimpleName());
            }
        }

        throw new RuntimeException("[Leaves] Üç nokta menüsü veya 'Sil' seçeneği 3 denemede de açılamadı.");
    }

    /**
     * Silme onay penceresinde 'Evet' butonuna tıklar ve
     * onay dialog'unun kapandığını doğrular.
     */
    public void confirmDeleteYes() {
        By confirmDialogBy = By.xpath(
                "//div[contains(@class,'e-dialog') and contains(@class,'e-popup-open')]" +
                        "[.//button[.//span[normalize-space()='Evet'] or normalize-space()='Evet']]"
        );

        WebElement dialog = wait.until(
                ExpectedConditions.visibilityOfElementLocated(confirmDialogBy)
        );

        By yesButtonBy = By.xpath(
                ".//button[@type='button' and " +
                        "(.//span[normalize-space()='Evet'] or normalize-space()='Evet')]"
        );

        WebElement yesButton = dialog.findElement(yesButtonBy);
        safeClick(yesButton);

        System.out.println("[Leaves] Silme onay popup’ında 'Evet' butonuna tıklandı.");

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(confirmDialogBy));
            System.out.println("[Leaves] Silme onay popup’ı kapandı.");
        } catch (TimeoutException e) {
            throw new AssertionError("[Leaves] Silme onay popup’ı kapanmadı.", e);
        }
    }

    /**
     * Verilen tarih + saat aralığı + açıklamaya sahip izin kaydının
     * grid üzerinde artık bulunmadığını (silindiğini) doğrular.
     */
    public void verifyLeaveRowDeleted(String date, String startTime, String endTime, String description) {
        String expectedStart = date + " " + startTime;
        String expectedEnd   = date + " " + endTime;

        By rowBy = By.xpath(
                "//div[contains(@class,'e-grid')]//table[contains(@class,'e-table')]" +
                        "//tr[contains(@class,'e-row')]" +
                        "[.//td[1]//span[normalize-space()='" + expectedStart + "'] " +
                        " and .//td[2]//span[normalize-space()='" + expectedEnd + "'] " +
                        " and .//td[3][normalize-space()='" + description + "']]"
        );

        boolean deleted = wait.until(driver -> driver.findElements(rowBy).isEmpty());

        if (!deleted) {
            throw new AssertionError(
                    "[Leaves] Silinmesi beklenen izin kaydı grid üzerinde hala görüntüleniyor. Tarih: "
                            + date + " " + startTime + "-" + endTime + ", Açıklama: " + description
            );
        }

        System.out.println("[Leaves] İzin kaydı grid üzerinde bulunamadı, silindiği doğrulandı.");
    }
}

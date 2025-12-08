package com.sinannuhoglu.pages.login;

import com.sinannuhoglu.core.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Doctorin Login sayfası Page Object
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ================== LOCATORS ==================

    private final By usernameInput = By.id("LoginInput_UserNameOrEmailAddress");

    private final By passwordInput = By.id("password-input");

    private final By tenantChangeButton = By.xpath(
            "//*[self::button or self::a]" +
                    "[contains(normalize-space(.),'değiştir') or contains(normalize-space(.),'Değiştir')]"
    );

    private final By loginButton = By.xpath(
            "//button[@type='submit' and @name='Action' and @value='Login']"
    );

    private final By doctorinHeader = By.xpath("//span[normalize-space(.)='Doctorin']");

    // ================== CTOR ==================

    public LoginPage() {
        this.driver = DriverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ================== NAVIGATION ==================

    /**
     * Login sayfasını açar ve DOM yüklenmesini + kullanıcı adı alanının görünmesini bekler.
     */
    public void open(String url) {
        driver.get(url);

        try {
            // document.readyState = complete
            WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            pageWait.until(d -> {
                try {
                    return "complete".equals(
                            ((JavascriptExecutor) d).executeScript("return document.readyState")
                    );
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (TimeoutException ignored) {
        }

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        } catch (TimeoutException ignored) {
        }
    }

    // ================== ACTIONS ==================

    /**
     * "Müşteri değiştir" penceresini açar.
     */
    public void clickTenantChange() {
        safeClick(tenantChangeButton);
    }

    /**
     * Kullanıcı adını girer.
     */
    public void enterUsername(String username) {
        typeInto(usernameInput, username);
    }

    /**
     * Şifreyi girer.
     */
    public void enterPassword(String password) {
        typeInto(passwordInput, password);
    }

    /**
     * Giriş butonuna tıklar.
     *
     * Page-load beklemesinde takılmamak için JS click kullanılır,
     * ardından yönlendirmenin gerçekten tamamlandığı explicit wait ile doğrulanır.
     */
    public void clickLogin() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(loginButton)
        );
        scrollIntoView(btn);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

            longWait.until(d -> {
                try {
                    String currentUrl = d.getCurrentUrl();
                    if (currentUrl == null) return false;

                    boolean leftLoginPage = !currentUrl.toLowerCase().contains("/account/login");
                    boolean onAppDomain = currentUrl.contains("testapp.biklinik.com");
                    boolean headerVisible = !d.findElements(doctorinHeader).isEmpty();

                    return leftLoginPage && (onAppDomain || headerVisible);
                } catch (Exception e) {
                    return false;
                }
            });

        } catch (TimeoutException e) {
            throw new TimeoutException(
                    "[LoginPage] Login sonrası beklenen sayfaya yönlendirme gerçekleşmedi. Mevcut URL: "
                            + driver.getCurrentUrl(), e
            );
        }
    }

    /**
     * Kullanıcının login olduğunu URL ve header üzerinden doğrular.
     */
    public boolean isLoggedInSuccessfully() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));

            return longWait.until(d -> {
                try {
                    String currentUrl = d.getCurrentUrl();
                    boolean leftLoginPage =
                            currentUrl != null &&
                                    !currentUrl.toLowerCase().contains("/account/login");

                    boolean headerVisible = !d.findElements(doctorinHeader).isEmpty();

                    return leftLoginPage && headerVisible;
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ================== HELPERS ==================

    /**
     * Bir input alanına güvenli şekilde text yazar.
     * Stale / intercept durumlarında birkaç kez tekrar dener.
     */
    private void typeInto(By locator, String text) {
        int attempts = 0;

        while (attempts < 3) {
            try {
                WebElement el = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(locator)
                );

                scrollIntoView(el);
                el.click();

                try {
                    el.clear();
                } catch (Exception ignore) {
                }

                try {
                    el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                } catch (Exception ignore) {
                }
                try {
                    el.sendKeys(Keys.chord(Keys.COMMAND, "a"));
                } catch (Exception ignore) {
                }
                el.sendKeys(Keys.DELETE);

                el.sendKeys(text);
                return;

            } catch (StaleElementReferenceException e) {
                attempts++;
                System.out.println("[LoginPage] typeInto() -> StaleElementReferenceException, retry " + attempts);
            } catch (ElementClickInterceptedException e) {
                attempts++;
                System.out.println("[LoginPage] typeInto() -> ElementClickInterceptedException, retry " + attempts);
            }
        }

        WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
        scrollIntoView(el);
        el.click();
        try {
            el.clear();
        } catch (Exception ignore) {
        }
        el.sendKeys(text);
    }

    /**
     * Bir elemana güvenli tıklama (stale/intercept durumlarına toleranslı).
     */
    private void safeClick(By locator) {
        int attempts = 0;

        while (attempts < 3) {
            try {
                WebElement el = wait.until(
                        ExpectedConditions.elementToBeClickable(locator)
                );
                scrollIntoView(el);
                el.click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                System.out.println("[LoginPage] safeClick() -> StaleElementReferenceException, retry " + attempts);
            } catch (ElementClickInterceptedException e) {
                attempts++;
                System.out.println("[LoginPage] safeClick() -> ElementClickInterceptedException, trying JS click. Attempt " + attempts);
                try {
                    WebElement el = wait.until(
                            ExpectedConditions.presenceOfElementLocated(locator)
                    );
                    scrollIntoView(el);
                    jsClick(el);
                    return;
                } catch (Exception inner) {
                    // Bir sonraki attempt'e geç
                }
            }
        }

        WebElement el = wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );
        scrollIntoView(el);
        el.click();
    }

    /**
     * Elemana JS ile tıklama (fallback amaçlı).
     */
    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
    }

    /**
     * Elemanı görünür alana getirir.
     */
    private void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        } catch (Exception ignore) {
        }
    }
}

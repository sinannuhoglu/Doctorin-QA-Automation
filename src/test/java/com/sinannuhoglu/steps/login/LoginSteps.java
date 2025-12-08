package com.sinannuhoglu.steps.login;

import com.sinannuhoglu.core.ConfigReader;
import com.sinannuhoglu.core.DriverFactory;
import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.login.LoginPage;
import com.sinannuhoglu.pages.login.TenantModalPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.testng.Assert.assertTrue;

/**
 * Doctorin Login akışı Step Definitions
 */
public class LoginSteps {

    private final LoginPage loginPage         = new LoginPage();
    private final TenantModalPage tenantModal = new TenantModalPage();

    @Given("kullanıcı login sayfasını görmektedir")
    public void kullanici_login_sayfasini_gormektedir() {
        DriverFactory.initDriver();

        String loginUrl = ConfigReader.get("loginUrl");
        if (loginUrl == null || loginUrl.isBlank()) {
            loginUrl = ConfigReader.get("baseUrl");
        }

        if (loginUrl == null || loginUrl.isBlank()) {
            throw new IllegalStateException(
                    "[LoginSteps] loginUrl/baseUrl config.properties içinde tanımlı değil!"
            );
        }

        loginPage.open(loginUrl);
    }

    @And("Müşteri değiştir penceresini açar")
    public void musteri_degistir_penceresini_acar() {
        loginPage.clickTenantChange();
    }

    @And("Müşteri adını test verisinden girer")
    public void musteri_adini_test_verisinden_girer() {
        String tenantName = TestDataReader.get("tenantName");
        tenantModal.setTenantName(tenantName);
    }

    @And("Müşteri bilgisini kaydeder")
    public void musteri_bilgisini_kaydeder() {
        tenantModal.clickSave();
    }

    @And("kullanıcı adı ve şifreyi test verisinden girer")
    public void kullanici_adi_ve_sifreyi_test_verisinden_girer() {
        String username = TestDataReader.get("username");
        String password = TestDataReader.get("password");

        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("Giriş butonuna tıklar")
    public void giris_butonuna_tiklar() {
        loginPage.clickLogin();
    }

    @Then("kullanıcı sisteme başarılı şekilde giriş yapmış olmalıdır")
    public void kullanici_sisteme_basarili_giris_yapmis_olmalidir() {
        assertTrue(
                loginPage.isLoggedInSuccessfully(),
                "Login sonrası Doctorin başlığı görüntülenemedi, login başarısız olabilir."
        );
    }
}

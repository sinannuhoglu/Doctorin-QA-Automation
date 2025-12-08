package com.sinannuhoglu.steps.appointment.definitions.resources;

import com.sinannuhoglu.pages.appointment.definitions.resources.AppointmentResourcesOfficialHolidaysPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static com.sinannuhoglu.core.DriverFactory.getDriver;

public class AppointmentResourcesOfficialHolidaysSteps {

    private final AppointmentResourcesOfficialHolidaysPage officialHolidaysPage;

    public AppointmentResourcesOfficialHolidaysSteps() {
        WebDriver driver = getDriver();
        this.officialHolidaysPage = new AppointmentResourcesOfficialHolidaysPage(driver);
    }

    // ----------------------------------------------------
    // SEKME & GRID AKIŞI
    // ----------------------------------------------------

    @When("Resmi Tatiller sekmesine gider")
    public void resmiTatillerSekmesineGider() {
        officialHolidaysPage.openOfficialHolidaysTab();
    }

    @When("Resmi Tatiller gridinde birinci sütunda {string} olan satırın Durum bilgisini aktif konuma getirir")
    public void resmiTatillerGridindeBirinciSutundaOlanSatirinDurumBilgisiniAktifKonumaGetirir(String holidayName) {
        officialHolidaysPage.ensureHolidayStatusActive(holidayName);
    }

    @Then("Resmi Tatiller gridinde birinci sütunda {string} olan satırın Durum bilgisinin aktif olduğunu doğrular")
    public void resmiTatillerGridindeBirinciSutundaOlanSatirinDurumBilgisininAktifOldugunuDogrular(String holidayName) {
        officialHolidaysPage.verifyHolidayStatusIsActive(holidayName);
    }
}

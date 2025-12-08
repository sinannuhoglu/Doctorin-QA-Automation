package com.sinannuhoglu.steps.appointment.definitions.resources;

import com.sinannuhoglu.pages.appointment.definitions.resources.AppointmentResourcesLeavesPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static com.sinannuhoglu.core.DriverFactory.getDriver;

public class AppointmentResourcesLeavesSteps {

    private final AppointmentResourcesLeavesPage leavesPage;

    public AppointmentResourcesLeavesSteps() {
        WebDriver driver = getDriver();
        this.leavesPage = new AppointmentResourcesLeavesPage(driver);
    }

    // ---------------------------- SEKME & FORM AKIŞI ----------------------------

    @When("İzinler sekmesine gider")
    public void izinlerSekmesineGider() {
        leavesPage.openLeavesTab();
    }

    @When("İzinler ekranında Yeni Ekle butonuna tıklar")
    public void izinlerEkranindaYeniEkleButonunaTiklar() {
        leavesPage.clickAddNewLeave();
    }

    @When("İzinler pop-up penceresinde Başlangıç Tarihi alanına {string} değerini girer")
    public void izinlerPopupPenceresindeBaslangicTarihiAlaninaDegeriniGirer(String dateText) {
        leavesPage.setStartDate(dateText);
    }

    @When("İzinler pop-up penceresinde Başlangıç Saati alanında {string} saatini seçer")
    public void izinlerPopupPenceresindeBaslangicSaatiAlanindaSaatiniSecer(String timeText) {
        leavesPage.selectStartTime(timeText);
    }

    @When("İzinler pop-up penceresinde Bitiş Saati alanında {string} saatini seçer")
    public void izinlerPopupPenceresindeBitisSaatiAlanindaSaatiniSecer(String timeText) {
        leavesPage.selectEndTime(timeText);
    }

    @When("İzinler pop-up penceresinde Tekrar alanında {string} seçeneğini seçer")
    public void izinlerPopupPenceresindeTekrarAlanindaSeceneginiSecer(String repeatOption) {
        leavesPage.selectRepeatType(repeatOption);
    }

    @When("İzinler pop-up penceresinde Açıklama alanına {string} metnini yazar")
    public void izinlerPopupPenceresindeAciklamaAlaninaMetniniYazar(String description) {
        leavesPage.setDescription(description);
    }

    @When("İzinler pop-up penceresinde Kaydet butonuna tıklar")
    public void izinlerPopupPenceresindeKaydetButonunaTiklar() {
        leavesPage.clickSaveButton();
    }

    @Then("İzinler pop-up penceresinin kapandığını ve izinler gridinin görüntülendiğini doğrular")
    public void izinlerPopupPenceresininKapandiginiVeIzinlerGridininGoruntulendiginiDogrular() {
        leavesPage.verifyLeaveFormClosedAndGridVisible();
    }

    @Then("İzinler gridinde {string} tarihli {string} - {string} saat aralığında {string} açıklamasıyla bir satır oluşturulduğunu doğrular")
    public void izinlerGridindeSatiriDogrular(String date, String startTime, String endTime, String description) {
        leavesPage.verifyLeaveRow(date, startTime, endTime, description);
    }

    // ---------------------------- SİLME ADIMLARI ----------------------------

    @When("İzinler gridinde {string} tarihli {string} - {string} saat aralığında {string} açıklamasına sahip izin kaydının üç nokta menüsünden Sil seçeneğine tıklar")
    public void izinlerGridindeKaydinUcNoktaMenusundenSilSecenegineTiklar(
            String date, String startTime, String endTime, String description
    ) {
        leavesPage.clickDeleteFromRow(date, startTime, endTime, description);
    }

    @When("İzin silme onay penceresinde Evet butonuna tıklar")
    public void izinSilmeOnayPenceresindeEvetButonunaTiklar() {
        leavesPage.confirmDeleteYes();
    }

    @Then("İzinler gridinde {string} tarihli {string} - {string} saat aralığında {string} açıklamasına sahip izin kaydının silindiğini doğrular")
    public void izinlerGridindeKaydinSilindiginiDogrular(String date, String startTime, String endTime, String desc) {
        leavesPage.verifyLeaveRowDeleted(date, startTime, endTime, desc);
    }
}

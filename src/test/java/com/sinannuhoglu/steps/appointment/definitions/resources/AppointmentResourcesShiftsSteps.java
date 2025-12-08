package com.sinannuhoglu.steps.appointment.definitions.resources;

import com.sinannuhoglu.pages.appointment.definitions.resources.AppointmentResourcesShiftsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static com.sinannuhoglu.core.DriverFactory.getDriver;

public class AppointmentResourcesShiftsSteps {

    private final AppointmentResourcesShiftsPage shiftsPage;

    public AppointmentResourcesShiftsSteps() {
        WebDriver driver = getDriver();
        this.shiftsPage = new AppointmentResourcesShiftsPage(driver);
    }

    // ----------------------------------------------------
    // SEKME & FORM AKIŞI
    // ----------------------------------------------------

    @When("Randevu Kaynakları ekranında kaynak detayında \"Mesailer\" sekmesine tıklar")
    public void randevuKaynaklariEkranindaKaynakDetayindaMesailerSekmesineTiklar() {
        shiftsPage.openShiftsTab();
    }

    @When("Mesailer sekmesinde Yeni Ekle butonuna tıklar")
    public void mesailerSekmesindeYeniEkleButonunaTiklar() {
        shiftsPage.clickAddButton();
    }

    @When("Mesailer sekmesinde açılan formda Tarih alanına {string} değerini girer")
    public void mesailerSekmesindeAcilanFormdaTarihAlaninaDegeriniGirer(String dateText) {
        shiftsPage.setDate(dateText);
    }

    @When("Mesailer sekmesinde açılan formda Başlangıç Saati alanından {string} saatini seçer")
    public void mesailerSekmesindeAcilanFormdaBaslangicSaatiAlanindanSaatiniSecer(String startTime) {
        shiftsPage.selectStartTime(startTime);
    }

    @When("Mesailer sekmesinde açılan formda Bitiş Saati alanından {string} saatini seçer")
    public void mesailerSekmesindeAcilanFormdaBitisSaatiAlanindanSaatiniSecer(String endTime) {
        shiftsPage.selectEndTime(endTime);
    }

    @When("Mesailer sekmesinde açılan formda Açıklama alanına {string} açıklamasını girer")
    public void mesailerSekmesindeAcilanFormdaAciklamaAlaninaAciklamasiniGirer(String description) {
        shiftsPage.setDescription(description);
    }

    @When("Mesailer sekmesinde açılan formda Kaydet butonuna tıklar")
    public void mesailerSekmesindeAcilanFormdaKaydetButonunaTiklar() {
        shiftsPage.clickSave();
    }

    // ----------------------------------------------------
    // OLUŞTURMA DOĞRULAMA ADIMLARI
    // ----------------------------------------------------

    @Then("Mesailer sekmesinde grid üzerinde {string} tarihli {string} - {string} saat aralığında {string} açıklamasına sahip kaydın oluşturulduğunu görür")
    public void mesailerSekmesindeGridUzerindeKaydinOlusturuldugunuGorur(
            String date,
            String startTime,
            String endTime,
            String description
    ) {
        shiftsPage.verifyShiftRow(date, startTime, endTime, description);
    }

    // ----------------------------------------------------
    // SİLME ADIMLARI
    // ----------------------------------------------------

    @When("Mesailer sekmesinde grid üzerinde {string} tarihli {string} - {string} saat aralığında {string} açıklamasına sahip kaydın üç nokta menüsüne tıklar")
    public void mesailerGridKaydinUcNoktaMenusuneTiklar(
            String date,
            String startTime,
            String endTime,
            String description
    ) {
        shiftsPage.openActionsMenuForShift(date, startTime, endTime, description);
    }

    @When("Mesailer sekmesinde açılan üç nokta menüsünde {string} seçeneğine tıklar")
    public void mesailerSekmesindeAcilanUcNoktaMenusundeSecenegineTiklar(String option) {
        // Şu an sadece "Sil" akışı destekleniyor
        if ("Sil".equalsIgnoreCase(option.trim())) {
            shiftsPage.clickDeleteInActionsMenu();
        } else {
            throw new IllegalArgumentException("Desteklenmeyen üç nokta menü seçeneği: " + option);
        }
    }

    @When("Mesailer sekmesinde açılan silme onay penceresinde {string} butonuna tıklar")
    public void mesailerSekmesindeAcilanSilmeOnayPenceresindeButonunaTiklar(String buttonText) {
        if ("Evet".equalsIgnoreCase(buttonText.trim())) {
            shiftsPage.confirmDeleteYes();
        } else {
            throw new IllegalArgumentException("Desteklenmeyen onay butonu: " + buttonText);
        }
    }

    @Then("Mesailer sekmesinde grid üzerinde {string} tarihli {string} - {string} saat aralığında {string} açıklamasına sahip kaydın silindiğini görür")
    public void mesailerSekmesindeGridUzerindeKaydinSilindiginiGorur(
            String date,
            String startTime,
            String endTime,
            String description
    ) {
        shiftsPage.verifyShiftRowDeleted(date, startTime, endTime, description);
    }
}

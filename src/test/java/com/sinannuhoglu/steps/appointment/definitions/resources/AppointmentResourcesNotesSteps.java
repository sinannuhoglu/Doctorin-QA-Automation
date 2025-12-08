package com.sinannuhoglu.steps.appointment.definitions.resources;

import com.sinannuhoglu.pages.appointment.definitions.resources.AppointmentResourcesNotesPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static com.sinannuhoglu.core.DriverFactory.getDriver;

public class AppointmentResourcesNotesSteps {

    private final AppointmentResourcesNotesPage notesPage;

    public AppointmentResourcesNotesSteps() {
        WebDriver driver = getDriver();
        this.notesPage = new AppointmentResourcesNotesPage(driver);
    }

    // ----------------- NOT EKLEME -----------------

    @When("Randevu Kaynakları ekranında Notlar sekmesine tıklar")
    public void randevuKaynaklariEkranindaNotlarSekmesineTiklar() {
        notesPage.openNotesTab();
    }

    @When("Randevu Kaynakları ekranında Notlar sekmesinde Yeni Ekle butonuna tıklar")
    public void randevuKaynaklariEkranindaNotlarSekmesindeYeniEkleButonunaTiklar() {
        notesPage.clickAddNewNote();
    }

    @When("Randevu Kaynakları ekranında açılan Not penceresinde Not Tipi alanından {string} seçeneğini seçer")
    public void randevuKaynaklariEkranindaAcilanNotPenceresindeNotTipiAlanindanSeceneginiSecer(String noteType) {
        notesPage.selectNoteType(noteType);
    }

    @When("Randevu Kaynakları ekranında Not penceresinde Başlangıç tarihi alanına {string} ve Bitiş tarihi alanına {string} değerlerini girer")
    public void randevuKaynaklariEkranindaNotPenceresindeBaslangicVeBitisTarihleriniYazar(String startDate, String endDate) {
        notesPage.setNoteDateRangeByTyping(startDate, endDate);
    }

    @When("Randevu Kaynakları ekranında Not penceresinde haftanın günlerinden {string} seçeneklerini işaretler")
    public void randevuKaynaklariEkranindaNotPenceresindeHaftaninGunlerindenSecenekleriniIsaretler(String daysCsv) {
        String[] days = daysCsv.split(",");
        notesPage.selectWeekdays(days);
    }

    @When("Randevu Kaynakları ekranında Not penceresinde Not alanına {string} bilgisini girer")
    public void randevuKaynaklariEkranindaNotPenceresindeNotAlaninaBilgisiniGirer(String note) {
        notesPage.fillNoteText(note);
    }

    @When("Randevu Kaynakları ekranında Not penceresinde Kaydet butonuna tıklar")
    public void randevuKaynaklariEkranindaNotPenceresindeKaydetButonunaTiklar() {
        notesPage.saveNote();
    }

    @Then("Randevu Kaynakları ekranında Notlar gridinde Tip {string}, Başlangıç Tarihi {string}, Bitiş Tarihi {string} ve Not {string} bilgisi ile kaydın oluştuğunu doğrular")
    public void randevuKaynaklariEkranindaNotlarGridindeKaydinOlustugunuDogrular(
            String tip,
            String startDate,
            String endDate,
            String noteText
    ) {
        notesPage.verifyNoteCreatedInGrid(tip, startDate, endDate, noteText);
    }

    // ----------------- NOT SİLME -----------------

    @When("Randevu Kaynakları ekranında Notlar gridinde Tip {string}, Başlangıç Tarihi {string}, Bitiş Tarihi {string} ve Not {string} bilgisine sahip kaydın üç nokta menüsüne tıklar")
    public void randevuKaynaklariEkranindaNotlarGridindeUcNoktaMenusuneTiklar(
            String tip,
            String startDate,
            String endDate,
            String noteText
    ) {
        notesPage.openRowActionMenu(tip, startDate, endDate, noteText);
    }

    @When("Randevu Kaynakları ekranında Notlar gridinde açılan menüden Sil seçeneğine tıklar")
    public void randevuKaynaklariEkranindaNotlarGridindeAcilanMenudenSilSecenegineTiklar() {
        notesPage.clickDeleteFromRowActionMenu();
    }

    @When("Randevu Kaynakları ekranında not silme uyarı penceresinde Evet butonuna tıklar")
    public void randevuKaynaklariEkranindaNotSilmeUyariPenceresindeEvetButonunaTiklar() {
        notesPage.confirmDeleteNote();
    }

    @Then("Randevu Kaynakları ekranında Notlar gridinde Tip {string}, Başlangıç Tarihi {string}, Bitiş Tarihi {string} ve Not {string} bilgisine sahip kaydın silindiğini doğrular")
    public void randevuKaynaklariEkranindaNotlarGridindeKaydinSilindiginiDogrular(
            String tip,
            String startDate,
            String endDate,
            String noteText
    ) {
        notesPage.verifyNoteDeletedFromGrid(tip, startDate, endDate, noteText);
    }
}

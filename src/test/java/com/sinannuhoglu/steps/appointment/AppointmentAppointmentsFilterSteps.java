package com.sinannuhoglu.steps.appointment;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.appointment.AppointmentAppointmentsFilterPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static com.sinannuhoglu.core.DriverFactory.getDriver;

/**
 * Randevu modülü > Randevular ekranı Step Definitions
 */
public class AppointmentAppointmentsFilterSteps {

    private final AppointmentAppointmentsFilterPage appointmentsPage;
    private static final String APPOINTMENTS_URL = TestDataReader.get("appointmentAppointmentsUrl");

    public AppointmentAppointmentsFilterSteps() {
        WebDriver driver = getDriver();
        this.appointmentsPage = new AppointmentAppointmentsFilterPage(driver);
    }

    // ----------------------------------------------------
    // NAVİGASYON
    // ----------------------------------------------------

    @When("Randevular modülüne gider")
    public void randevularModuluneGider() {
        WebDriver driver = getDriver();
        driver.get(APPOINTMENTS_URL);
    }

    // ----------------------------------------------------
    // FİLTRELE PANELİ
    // ----------------------------------------------------

    @When("Randevular ekranında Filitrele butonuna tıklar")
    public void randevularEkranindaFilitreleButonunaTiklar() {
        appointmentsPage.clickFilterButton();
    }

    @And("Randevular ekranında Filitrele penceresinde Şube alanında {string} seçeneğini seçer")
    public void randevularEkranindaFilitrelePenceresindeSubeAlanindaSeceneginiSecer(String subeAdi) {
        appointmentsPage.selectLocation(subeAdi);
    }

    @When("Randevular ekranında Filitrele penceresinde Departmanlar alanında {string} seçeneğini seçer")
    public void randevularEkranindaFilitrelePenceresindeDepartmanlarAlanindaSeceneginiSecer(String departmentName) {
        appointmentsPage.selectDepartment(departmentName);
    }

    @When("Randevular ekranında Filitrele penceresinde Kaynaklar alanında {string} seçeneğini seçer")
    public void randevularEkranindaFilitrelePenceresindeKaynaklarAlanindaSeceneginiSecer(String doctorFullName) {
        appointmentsPage.selectDoctor(doctorFullName);
    }

    @When("Randevular ekranında Filitrele penceresinde Kabul Et butonuna tıklar")
    public void randevularEkranindaFilitrelePenceresindeKabulEtButonunaTiklar() {
        appointmentsPage.clickFilterAccept();
    }

    @When("Randevular ekranında Bugün butonuna tıklar")
    public void randevularEkranindaBugunButonunaTiklar() {
        appointmentsPage.clickTodayButton();
    }

    @When("Randevular ekranında boş bir randevu slotuna tıklar")
    public void randevularEkranindaBosBirRandevuSlotunaTiklar() {
        appointmentsPage.clickFirstEmptySlot();
    }

    // ----------------------------------------------------
    // HASTA ARAMA & RANDEVU DETAYI
    // ----------------------------------------------------

    @And("Randevular ekranında Hasta Arama penceresinde {string} hastasını arar")
    public void randevularEkranindaHastaAramaPenceresindeHastasiniArar(String patientName) {
        appointmentsPage.searchPatientInSidebar(patientName);
    }

    @And("Randevular ekranında Hasta Arama sonuçlarından {string} hastasını seçer")
    public void randevularEkranindaHastaAramaSonuclarindanHastasiniSecer(String patientName) {
        appointmentsPage.selectPatientFromResults(patientName);
    }

    @And("Randevular ekranında Randevu Detayı penceresinde Kaydet butonuna tıklar")
    public void randevularEkranindaRandevuDetayiPenceresindeKaydetButonunaTiklar() {
        appointmentsPage.clickAppointmentSave();
    }

    @And("Randevular ekranında {string} hastasına ait randevu kartını açar")
    public void randevularEkranindaHastasinaAitRandevuKartiniAcar(String patientName) {
        appointmentsPage.openAppointmentCardForPatient(patientName);
    }

    @And("Randevular ekranında Randevu Detayı penceresinde Check-in butonuna tıklar")
    public void randevularEkranindaRandevuDetayiPenceresindeCheckInButonunaTiklar() {
        appointmentsPage.clickCheckInOnQuickPopup();
    }

    @And("Randevular ekranında Randevular Detayı penceresinde Admission butonuna tıklar")
    public void randevularEkranindaRandevularDetayiPenceresindeAdmissionButonunaTiklar() {
        appointmentsPage.clickAdmissionOnQuickPopup();
    }

    @And("Randevular ekranında Hasta Kabul penceresinde Kaydet butonuna tıklar")
    public void randevularEkranindaHastaKabulPenceresindeKaydetButonunaTiklar() {
        appointmentsPage.saveAdmissionForm();
    }

    // ----------------------------------------------------
    // KAPANIŞ ETKİNLİĞİ (ETKİNLİK SEKME) ADIMLARI
    // ----------------------------------------------------

    @And("Randevular ekranında Hasta Arama penceresinde Etkinlik sekmesine tıklar")
    public void randevularEkranindaHastaAramaPenceresindeEtkinlikSekmesineTiklar() {
        appointmentsPage.clickEventTabOnSidebar();
    }

    @And("Randevular ekranında Kapanış Etkinliği penceresinde Kaydet butonuna tıklar")
    public void randevularEkranindaKapanisEtkinligiPenceresindeKaydetButonunaTiklar() {
        appointmentsPage.clickAppointmentSave();
    }

    @And("Randevular ekranında oluşturulan kapanış etkinliği slotunu açar")
    public void randevularEkranindaOlusturulanKapanisEtkinligiSlotunuAcar() {
        appointmentsPage.openClosingEventCardByTime();
    }

    @And("Randevular ekranında Etkinlik Detayı penceresinde Sil butonuna tıklar ve Sil onay penceresinde Tamam butonuna tıklar")
    public void randevularEkranindaEtkinlikDetayiPenceresindeSilButonunaTiklar() {
        appointmentsPage.deleteClosingEventFromQuickPopup();
    }
}

package com.sinannuhoglu.steps.appointment.definitions.resources;

import com.sinannuhoglu.pages.appointment.definitions.resources.AppointmentResourcesServiceItemsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static com.sinannuhoglu.core.DriverFactory.getDriver;

public class AppointmentResourcesServiceItemsSteps {

    private final AppointmentResourcesServiceItemsPage serviceItemsPage;

    public AppointmentResourcesServiceItemsSteps() {
        WebDriver driver = getDriver();
        this.serviceItemsPage = new AppointmentResourcesServiceItemsPage(driver);
    }

    // ----------------------------------------------------
    // SEKME & FORM AKIŞI
    // ----------------------------------------------------

    @When("Servis Öğeleri sekmesine gider")
    public void servisOgeleriSekmesineGider() {
        serviceItemsPage.openServiceItemsTab();
    }

    @When("Servis Öğeleri ekranında Yeni Ekle butonuna tıklar")
    public void servisOgeleriEkranindaYeniEkleButonunaTiklar() {
        serviceItemsPage.clickAddNewServiceItem();
    }

    @When("Servis Öğeleri pop-up penceresinde Servis Öğesi alanında {string} seçeneğini seçer")
    public void servisOgeleriPopupPenceresindeServisOgesiAlanindaSeceneginiSecer(String serviceItem) {
        serviceItemsPage.selectServiceItem(serviceItem);
    }

    @When("Servis Öğeleri pop-up penceresinde Randevu Tipi alanında {string} seçeneğini seçer")
    public void servisOgeleriPopupPenceresindeRandevuTipiAlanindaSeceneginiSecer(String appointmentType) {
        serviceItemsPage.selectAppointmentType(appointmentType);
    }

    @When("Servis Öğeleri pop-up penceresinde Kaydet butonuna tıklar")
    public void servisOgeleriPopupPenceresindeKaydetButonunaTiklar() {
        serviceItemsPage.clickSaveButton();
    }

    @Then("Servis Öğeleri gridinde birinci sütunda {string} ve üçüncü sütunda {string} değerine sahip kaydın oluşturulduğunu doğrular")
    public void servisOgeleriGridindeKaydinOlustugunuDogrular(String serviceItem,
                                                              String appointmentType) {
        serviceItemsPage.verifyServiceItemRow(serviceItem, appointmentType);
    }

    // ----------------------------------------------------
    // SİLME AKIŞI – Üç nokta menüsü + Sil + Evet
    // ----------------------------------------------------

    @When("Servis Öğeleri gridinde birinci sütunda {string} ve üçüncü sütunda {string} değerine sahip satırdaki üç nokta menüsünü açar")
    public void servisOgeleriGridindeSatirdakiUcNoktaMenusunuAcar(String serviceItemName,
                                                                  String appointmentType) {
        serviceItemsPage.openActionMenuForServiceItem(serviceItemName, appointmentType);
    }

    @When("Servis Öğeleri gridindeki üç nokta menüsünden {string} seçeneğine tıklar")
    public void servisOgeleriGridindekiUcNoktaMenusundenSecenegineTiklar(String optionText) {
        // Şimdilik sadece "Sil" destekleniyor; ileride istenirse "Düzenle" vb. eklenebilir.
        if (!"Sil".equals(optionText)) {
            throw new IllegalArgumentException(
                    "Şu anda yalnızca 'Sil' seçeneği destekleniyor. Gelen değer: " + optionText
            );
        }
        serviceItemsPage.clickDeleteOnActionMenu();
    }

    @When("Silme onay penceresinde {string} butonuna tıklar")
    public void silmeOnayPenceresindeButonunaTiklar(String buttonText) {
        if (!"Evet".equals(buttonText)) {
            throw new IllegalArgumentException(
                    "Şu anda yalnızca 'Evet' butonu destekleniyor. Gelen değer: " + buttonText
            );
        }
        serviceItemsPage.confirmDeletionYes();
    }

    @Then("Servis Öğeleri gridinde birinci sütunda {string} ve üçüncü sütunda {string} değerine sahip kaydın silindiğini doğrular")
    public void servisOgeleriGridindeKaydinSilindiginiDogrular(String serviceItem,
                                                               String appointmentType) {
        serviceItemsPage.verifyServiceItemRowDeleted(serviceItem, appointmentType);
    }
}

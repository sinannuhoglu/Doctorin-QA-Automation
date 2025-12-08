package com.sinannuhoglu.steps.resources;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.resources.ResourcesStaffManagementPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class ResourcesStaffManagementSteps {

    private final ResourcesStaffManagementPage staffPage = new ResourcesStaffManagementPage();

    @And("Kaynaklar Personel Yönetimi modülüne gider")
    public void kaynaklar_personel_yonetimi_modulune_gider() {
        String url = TestDataReader.get("resourcesStaffManagementUrl");
        staffPage.goToStaffManagement(url);
    }

    @And("Personel detaylı arama penceresini açar")
    public void personel_detayli_arama_penceresini_acar() {
        staffPage.openDetailsPopup();
    }

    @And("Personel detaylı arama penceresinde Temizle butonuna tıklar")
    public void personel_detayli_arama_penceresinde_temizle_butonuna_tiklar() {
        staffPage.clickDetailsClear();
    }

    @And("Personel arama alanına {string} yazar ve arama yapar")
    public void personel_arama_alanina_yazar_ve_arama_yapar(String fullName) {
        staffPage.searchForStaff(fullName);
    }

    @And("{string} personel kaydının durumu {string} olacak şekilde hazırlanır")
    public void personel_kaydinin_durumu_olacak_sekilde_hazirlanir(String fullName, String expectedStatus) {
        staffPage.ensureStaffStatus(fullName, expectedStatus);
    }

    @Then("Personel listesindeki satırlarda {string} için durum bilgisinin {string} olduğunu görür")
    public void personel_listesindeki_satirlarda_icin_durum_bilgisinin_oldugunu_gorur(
            String fullName,
            String expectedStatus
    ) {
        staffPage.verifyStaffStatusIs(fullName, expectedStatus);
    }
}

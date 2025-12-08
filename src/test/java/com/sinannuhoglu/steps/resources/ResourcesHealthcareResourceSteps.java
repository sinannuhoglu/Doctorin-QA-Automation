package com.sinannuhoglu.steps.resources;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.resources.ResourcesHealthcareResourceManagementPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ResourcesHealthcareResourceSteps {

    private final ResourcesHealthcareResourceManagementPage healthcareResourcePage =
            new ResourcesHealthcareResourceManagementPage();

    @Given("Kullanıcı Oda ve Cihaz yönetimi sayfasına gider")
    public void kullanici_oda_ve_cihaz_yonetimi_sayfasina_gider() {
        String url = TestDataReader.get("resourcesHealthcareResourceManagementUrl");
        healthcareResourcePage.goToHealthcareResourceManagement(url);
    }

    @When("Detaylı arama penceresini temizler ve uygular")
    public void detayli_arama_penceresini_temizler_ve_uygular() {
        healthcareResourcePage.openDetailsPopup();
        healthcareResourcePage.clickDetailsClear();
        healthcareResourcePage.clickDetailsApply();
    }

    @When("{string} kaynağını arar")
    public void kaynagini_arar(String resourceName) {
        healthcareResourcePage.searchForResource(resourceName);
    }

    @When("{string} kaynağının durumu {string} olacak şekilde hazırlanır")
    public void kaynagin_durumu_olacak_sekilde_hazirlanir(String resourceName, String expectedStatus) {
        healthcareResourcePage.ensureResourceStatus(resourceName, expectedStatus);
    }

    @And("{string} kaynağı için adı {string}, tipi {string}, alt tipi {string}, departmanı {string} ve şubesi {string} olacak şekilde yeni oda ve cihaz kaydı oluşturur")
    public void kaynak_icin_yeni_oda_ve_cihaz_kaydi_olusturur(
            String baseResourceName,
            String name,
            String tip,
            String altTip,
            String department,
            String sube
    ) {
        // baseResourceName ve name genellikle aynı kullanılacak (örn: "Göz Ölçümü").
        healthcareResourcePage.createResourceWithAutoIndex(baseResourceName, tip, altTip, department, sube);
    }

    @Then("Kaynak listesindeki satırlarda {string} için durum bilgisinin {string} olduğunu görür")
    public void kaynak_listesindeki_satirlarda_icin_durum_bilgisinin_oldugunu_gorur(
            String resourceName,
            String expectedStatus
    ) {
        String nameToVerify = healthcareResourcePage.getLastCreatedResourceName();

        if (nameToVerify == null || nameToVerify.isBlank()) {
            nameToVerify = resourceName;
        }

        healthcareResourcePage.verifyResourceStatusIs(nameToVerify, expectedStatus);
    }
}

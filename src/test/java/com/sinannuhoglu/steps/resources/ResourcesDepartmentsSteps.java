package com.sinannuhoglu.steps.resources;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.resources.ResourcesDepartmentsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class ResourcesDepartmentsSteps {

    private final ResourcesDepartmentsPage resourcesDepartmentsPage = new ResourcesDepartmentsPage();

    @And("Kaynaklar modülüne gider")
    public void kaynaklar_modulune_gider() {
        String url = TestDataReader.get("resourcesDepartmentsUrl");
        resourcesDepartmentsPage.goToResourcesDepartments(url);
    }

    @And("Detaylı arama penceresini açar")
    public void detayli_arama_penceresini_acar() {
        resourcesDepartmentsPage.openDetailsPopup();
    }

    @And("Detaylı arama penceresinde Temizle butonuna tıklar")
    public void detayli_arama_penceresinde_temizle_butonuna_tiklar() {
        resourcesDepartmentsPage.clickClear();
    }

    @And("Detaylı arama penceresinde Uygula butonuna tıklar")
    public void detayli_arama_penceresinde_uygula_butonuna_tiklar() {
        resourcesDepartmentsPage.clickApply();
    }

    @And("Kaynaklar arama alanına {string} yazar ve arama yapar")
    public void kaynaklar_arama_alanina_yazar_ve_arama_yapar(String text) {
        resourcesDepartmentsPage.searchForDepartment(text);
    }

    @And("Kaynaklar arama alanına {string} yazar ve arama yapar (kayıt olmayabilir)")
    public void kaynaklar_arama_alanina_yazar_ve_arama_yapar_kayit_olmayabilir(String text) {
        resourcesDepartmentsPage.searchForDepartmentAllowEmpty(text);
    }

    @And("{string} departmanının durumu {string} olacak şekilde hazırlanır")
    public void departmaninin_durumu_olacak_sekilde_hazirlanir(String departmentName, String expectedStatus) {
        resourcesDepartmentsPage.ensureDepartmentStatus(departmentName, expectedStatus);
    }

    @Then("Departman listesindeki satırlarda {string} için durum bilgilerinin {string} olduğunu görür")
    public void departman_listesindeki_satirlarda_icin_durum_bilgilerinin_oldugunu_gorur_yeni(
            String departmentName,
            String expectedStatus
    ) {
        String nameToVerify = resourcesDepartmentsPage.getLastCreatedDepartmentName();

        if (nameToVerify == null || nameToVerify.isBlank()) {
            nameToVerify = departmentName;
        }

        resourcesDepartmentsPage.verifyDepartmentStatusIs(nameToVerify, expectedStatus);
    }

    @Then("Departman listesindeki satırlarda {string} için durum bilgisinin {string} olduğunu görür")
    public void departman_listesindeki_satirlarda_icin_durum_bilgisinin_oldugunu_gorur(
            String departmentName,
            String expectedStatus
    ) {
        departman_listesindeki_satirlarda_icin_durum_bilgilerinin_oldugunu_gorur_yeni(
                departmentName,
                expectedStatus
        );
    }

    @And("Eğer {string} departmanı listede yoksa kodu {string}, adı {string}, departman türü {string} ve branşı {string} olacak şekilde yeni departman kaydı oluşturur")
    public void eger_departmani_listede_yoksa_yeni_departman_kaydi_olusturur(
            String departmentName,
            String code,
            String name,
            String departmentType,
            String branch
    ) {
        resourcesDepartmentsPage.createDepartmentIfNotExists(departmentName, code, name, departmentType, branch);
    }

    @And("{string} departmanı için kodu {string}, adı {string}, departman türü {string} ve branşı {string} olacak şekilde yeni departman kaydı oluşturur")
    public void departmani_icin_kodu_adi_departman_turu_ve_bransi_olacak_sekilde_yeni_departman_kaydi_olusturur(
            String departmentName,
            String code,
            String name,
            String departmentType,
            String branch) {

        resourcesDepartmentsPage.createDepartmentWithAutoIndex(departmentName, code, name, departmentType, branch);
    }
}

package com.sinannuhoglu.steps.admission.definitions;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.admission.definitions.AdmissionAutoServiceItemRulesPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class AdmissionAutoServiceItemRulesSteps {

    private final AdmissionAutoServiceItemRulesPage rulesPage =
            new AdmissionAutoServiceItemRulesPage();

    // ============== NAVIGATION ==================

    @And("Otomatik Servis Öğesi Kuralları modülüne gider")
    public void otomatik_servis_ogesi_kurallari_modulune_gider() {
        String url = TestDataReader.get("autoServiceItemRulesUrl");
        rulesPage.goToPage(url);
    }

    // ============== YENİ KURAL FORMU ============

    @And("Otomatik Servis Öğesi Kuralları ekranında Yeni Ekle butonuna tıklar")
    public void otomatik_servis_ogesi_kurallari_yeni_ekle_butonuna_tiklar() {
        rulesPage.clickNewRuleButton();
    }

    @And("Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_hizmet_ogesi_secimi(String optionText) {
        rulesPage.selectServiceItem(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları ekranında Departman alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_departman_secimi(String optionText) {
        rulesPage.selectDepartment(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları ekranında Doktor alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_doktor_secimi(String optionText) {
        rulesPage.selectDoctor(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları ekranında Şube alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_sube_secimi(String optionText) {
        rulesPage.selectBranch(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları ekranında Vizit Tipi alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_vizit_tipi_secimi(String optionText) {
        rulesPage.selectVisitType(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları ekranında Kaydet butonuna tıklar")
    public void otomatik_servis_ogesi_kurallari_kaydet_butonuna_tiklar() {
        rulesPage.clickSave();
    }

    // ============== GRID & ARAMA ================

    @And("Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi arama alanına {string} yazarak kayıt arar")
    public void otomatik_servis_ogesi_kurallari_arama_yapar(String serviceItem) {
        rulesPage.searchByServiceItem(serviceItem);
    }

    @Then("Otomatik Servis Öğesi Kuralları ekranında grid üzerinde Hizmet Öğesi {string}, Doktor {string}, Departman {string}, Şube {string} ve Vizit Tipi {string} değerleri ile listelenen kaydı doğrular")
    public void otomatik_servis_ogesi_kurallari_grid_kaydi_dogrular(
            String serviceItem,
            String doctor,
            String department,
            String branch,
            String visitType
    ) {
        rulesPage.verifyGridRowValues(serviceItem, doctor, department, branch, visitType);
    }

    @Then("Otomatik Servis Öğesi Kuralları ekranında grid üzerinde Hizmet Öğesi {string}, Doktor {string}, Departman {string}, Şube {string} ve Vizit Tipi {string} değerleri ile listelenen kaydın güncellendiğini doğrular")
    public void otomatik_servis_ogesi_kurallari_grid_guncel_kaydi_dogrular(
            String serviceItem,
            String doctor,
            String department,
            String branch,
            String visitType
    ) {
        rulesPage.verifyGridRowValues(serviceItem, doctor, department, branch, visitType);
    }

    // ============== KAYIT MENÜSÜ & DÜZENLE/SİL ======

    @And("Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi {string} olan kayıt için işlem menüsüne tıklar")
    public void otomatik_servis_ogesi_kurallari_islem_menusu_acar(String serviceItem) {
        rulesPage.openRowActionMenu(serviceItem);
    }

    @And("Otomatik Servis Öğesi Kuralları ekranında işlem menüsünden {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_islem_menusunden_secenek_secer(String menuText) {
        rulesPage.clickRowMenuItem(menuText);
    }

    @And("Otomatik Servis Öğesi Kuralları ekranında açılan silme onay penceresinde \"Evet\" butonuna tıklar")
    public void otomatik_servis_ogesi_kurallari_silme_onay_penceresinde_evet_butonuna_tiklar() {
        rulesPage.confirmDelete();
    }

    @Then("Otomatik Servis Öğesi Kuralları ekranında grid üzerinde Hizmet Öğesi {string}, Doktor {string}, Departman {string}, Şube {string} ve Vizit Tipi {string} değerleri ile listelenen herhangi bir kaydın bulunmadığını doğrular")
    public void otomatik_servis_ogesi_kurallari_silinmis_kaydi_dogrular(
            String serviceItem,
            String doctor,
            String department,
            String branch,
            String visitType
    ) {
        rulesPage.verifyRuleNotExists(serviceItem, doctor, department, branch, visitType);
    }

    // ============== DÜZENLE PENCERESİ ===========

    @And("Otomatik Servis Öğesi Kuralları düzenleme penceresinde Hizmet Öğesi alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_duzenle_hizmet_ogesi(String optionText) {
        rulesPage.selectServiceItem(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları düzenleme penceresinde Departman alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_duzenle_departman(String optionText) {
        rulesPage.selectDepartment(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları düzenleme penceresinde Doktor alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_duzenle_doktor(String optionText) {
        rulesPage.selectDoctor(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları düzenleme penceresinde Şube alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_duzenle_sube(String optionText) {
        rulesPage.selectBranch(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları düzenleme penceresinde Vizit Tipi alanından {string} seçeneğini seçer")
    public void otomatik_servis_ogesi_kurallari_duzenle_vizit_tipi(String optionText) {
        rulesPage.selectVisitType(optionText);
    }

    @And("Otomatik Servis Öğesi Kuralları düzenleme penceresinde Kaydet butonuna tıklar")
    public void otomatik_servis_ogesi_kurallari_duzenle_kaydet() {
        rulesPage.clickSave();
    }
}

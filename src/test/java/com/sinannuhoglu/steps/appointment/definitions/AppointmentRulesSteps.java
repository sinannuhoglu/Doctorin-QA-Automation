package com.sinannuhoglu.steps.appointment.definitions;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.appointment.definitions.AppointmentRulesPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

/**
 * Randevu > Tanımlar > Kurallar ekranı için Step Definitions.
 *
 * Tüm step metinleri feature dosyasıyla birebir uyumlu tutulmuştur.
 */
public class AppointmentRulesSteps {

    private final AppointmentRulesPage appointmentRulesPage = new AppointmentRulesPage();

    // ================== NAVIGATION ==================

    @And("Randevu Kuralları modülüne gider")
    public void randevu_kurallari_modulune_gider() {
        String url = TestDataReader.get("appointmentRulesUrl");
        appointmentRulesPage.goToAppointmentRules(url);
    }

    // ================== YENİ KURAL OLUŞTURMA ==========

    @And("Randevu Kuralları ekranında Yeni Ekle butonuna tıklar")
    public void randevu_kurallari_yeni_ekle_butonuna_tiklar() {
        appointmentRulesPage.openNewRuleForm();
    }

    @And("Randevu Kuralları ekranında Randevu Kural Türü dropdownundan {string} seçeneği seçer")
    public void randevu_kurallari_kural_turu_dropdownundan_secenegi_secer(String ruleType) {
        appointmentRulesPage.selectRuleType(ruleType);
    }

    @And("Randevu Kuralları ekranında Kaynaklar alanında {string} kaynağını seçer")
    public void randevu_kurallari_kaynaklar_alaninda_kaynagini_secer(String resourceName) {
        appointmentRulesPage.selectResource(resourceName);
    }

    @And("Randevu Kuralları ekranında Açıklama alanına {string} kural açıklaması yazılır")
    public void randevu_kurallari_aciklama_alanina_kural_aciklamasi_yazilir(String description) {
        appointmentRulesPage.setDescription(description);
    }

    @And("Randevu Kuralları ekranında Kaydet butonuna tıklar")
    public void randevu_kurallari_kaydet_butonuna_tiklar() {
        appointmentRulesPage.clickSaveRule();
    }

    // ================== DETAYLI ARAMA =================

    @And("Randevu Kuralları ekranında Detaylı Arama penceresi açılır")
    public void randevu_kurallari_detayli_arama_penceresi_acilir() {
        appointmentRulesPage.openDetailedSearch();
    }

    @And("Randevu Kuralları ekranında Detaylı Arama penceresinde Temizle butonuna tıklar")
    public void randevu_kurallari_detayli_arama_temizle_butonuna_tiklar() {
        appointmentRulesPage.clickDetailedSearchClear();
    }

    @And("Randevu Kuralları ekranında Detaylı Arama penceresinde Uygula butonuna tıklar")
    public void randevu_kurallari_detayli_arama_uygula_butonuna_tiklar() {
        appointmentRulesPage.clickDetailedSearchApply();
    }

    // ================== TOOLBAR ARAMA ====================

    @And("Randevu Kuralları ekranında ara alanına {string} değeri yazılarak arama yapılır")
    public void randevu_kurallari_ara_alanina_degeri_yazilarak_arama_yapilir(String text) {
        appointmentRulesPage.searchOnToolbar(text);
    }

    // ================== GRID DOĞRULAMA ===================

    @Then("Randevu Kuralları ekranında gridde {string} kural tipine ve {string} açıklamasına sahip kaydın listelendiğini doğrular")
    public void randevu_kurallari_gridde_kural_tipi_ve_aciklamasina_sahip_kaydin_listelendigi_dogrulanir(
            String expectedRuleType,
            String expectedDescription
    ) {
        appointmentRulesPage.verifyRuleListed(expectedRuleType, expectedDescription);
    }

    // ================== DÜZENLE SENARYOSU ==============

    @And("Randevu Kuralları ekranında gridde açıklaması {string} olan kaydın üç nokta menüsünden Düzenle seçeneğine tıklar")
    public void randevu_kurallari_gridde_aciklamasi_olan_kaydin_uc_nokta_menusunden_duzenle_secenegine_tiklar(
            String description
    ) {
        appointmentRulesPage.openEditForRowByDescription(description);
    }

    @And("Randevu Kuralları ekranında Düzenle penceresinde Randevu Kural Türü dropdownundan {string} seçeneği seçer")
    public void randevu_kurallari_duzenle_penceresinde_kural_turu_dropdownundan_secenegi_secer(String ruleType) {
        appointmentRulesPage.selectRuleType(ruleType);
    }

    @And("Randevu Kuralları ekranında Düzenle penceresinde Kaynaklar alanında {string} kaynağını seçer")
    public void randevu_kurallari_duzenle_penceresinde_kaynaklar_alaninda_kaynagini_secer(String resourceName) {
        appointmentRulesPage.selectResource(resourceName);
    }

    @And("Randevu Kuralları ekranında Düzenle penceresinde Açıklama alanına {string} kural açıklaması yazılır")
    public void randevu_kurallari_duzenle_penceresinde_aciklama_alanina_kural_aciklamasi_yazilir(String description) {
        appointmentRulesPage.setDescription(description);
    }

    @And("Randevu Kuralları ekranında Düzenle penceresinde Kaydet butonuna tıklar")
    public void randevu_kurallari_duzenle_penceresinde_kaydet_butonuna_tiklar() {
        appointmentRulesPage.clickSaveRule();
    }

    // ================== SİLME SENARYOSU =================

    @And("Randevu Kuralları ekranında gridde açıklaması {string} olan kaydın üç nokta menüsünden Sil seçeneğine tıklar")
    public void randevu_kurallari_gridde_aciklamasi_olan_kaydin_uc_nokta_menusunden_sil_secenegine_tiklar(
            String description
    ) {
        appointmentRulesPage.deleteRowByDescription(description);
    }

    @And("Randevu Kuralları ekranında Sil onay penceresinde Evet butonuna tıklar")
    public void randevu_kurallari_sil_onay_penceresinde_evet_butonuna_tiklar() {
        appointmentRulesPage.confirmDeleteYes();
    }

    @Then("Randevu Kuralları ekranında açıklaması {string} olan kaydın gridde listelenmediğini ve {string} mesajının görüntülendiğini doğrular")
    public void randevu_kurallari_silinen_kaydin_gridde_listelenmedigini_dogrular(
            String description,
            String expectedMessage
    ) {
        appointmentRulesPage.verifyRuleDeletedWithEmptyMessage(description, expectedMessage);
    }
}

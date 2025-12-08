package com.sinannuhoglu.steps.treatment.definitions;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.treatment.definitions.TreatmentCustomDrugsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.tr.Ozaman;

public class TreatmentCustomDrugsSteps {

    private final TreatmentCustomDrugsPage customDrugsPage = new TreatmentCustomDrugsPage();

    // CREATE senaryosunda hesaplanan auto-index'li isim
    private String autoDrugName;

    // EDIT senaryosunda kullanılacak yeni auto-index isim
    private String editAutoDrugName;

    // ============== NAVIGATION ==================

    @And("Özel İlaçlar modülüne gider")
    public void ozel_ilaclar_modulune_gider() {
        String url = TestDataReader.get("treatmentCustomDrugsUrl");
        customDrugsPage.goToCustomDrugs(url);
    }

    // ============== DETAYLI ARAMA ===============

    @And("Özel İlaçlar ekranında Detaylı Arama penceresi açılır")
    public void ozel_ilaclar_detayli_arama_acilir() {
        customDrugsPage.openDetailedSearch();
    }

    @And("Özel İlaçlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar")
    public void ozel_ilaclar_detayli_arama_temizle() {
        customDrugsPage.clickDetailedSearchClear();
    }

    @And("Özel İlaçlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar")
    public void ozel_ilaclar_detayli_arama_uygula() {
        customDrugsPage.clickDetailedSearchApply();
    }

    // ============== ARAMA ALANI =================

    @And("Özel İlaçlar ekranında ara alanına {string} değeri yazılarak arama yapılır")
    public void ozel_ilaclar_ara_alani_ile_arama_yapilir(String text) {
        customDrugsPage.searchOnToolbar(text);
    }

    // ============== AUTO INDEX (CREATE) =========

    @And("Özel İlaçlar ekranında grid üzerindeki mevcut özel ilaç kayıtları baz alınarak {string} için auto index hesaplanır")
    public void ozel_ilaclar_auto_index_hesapla(String base) {
        autoDrugName = customDrugsPage.generateAutoIndexName(base);
    }

    // ============== YENİ ÖZEL İLAÇ KAYDI =========

    @And("Özel İlaçlar ekranında Yeni Ekle butonuna tıklar")
    public void ozel_ilaclar_yeni_ekle_butonuna_tiklar() {
        customDrugsPage.openNewCustomDrugForm();
    }

    @And("Özel İlaçlar ekranında Ad alanına auto index ile hesaplanan ilaç adı yazılır")
    public void ozel_ilaclar_ad_alanina_auto_index_ile_hesaplanan_ilac_adi_yazilir() {
        customDrugsPage.setDrugName(autoDrugName);
    }

    @And("Özel İlaçlar ekranında Barkod alanına {string} değeri yazılır")
    public void ozel_ilaclar_barkod_alanina_degeri_yazilir(String barcode) {
        customDrugsPage.setBarcode(barcode);
    }

    @And("Özel İlaçlar ekranında Reçete Türü dropdownundan {string} seçeneği seçer")
    public void ozel_ilaclar_recete_turu_dropdownundan_secenegi_secer(String receteTuru) {
        customDrugsPage.selectPrescriptionType(receteTuru);
    }

    @And("Özel İlaçlar ekranında Kaydet butonuna tıklar")
    public void ozel_ilaclar_kaydet_butonuna_tiklar() {
        customDrugsPage.clickSaveCustomDrug();
    }

    // ============== SON ARAMA + DOĞRULAMA =========

    @And("Özel İlaçlar ekranında auto index ile oluşturulan ilaç adı ile arama yapılır")
    public void ozel_ilaclar_auto_index_ile_olusturulan_ilac_adi_ile_arama_yapilir() {
        customDrugsPage.searchOnToolbar(autoDrugName);
    }

    @Then("Özel İlaçlar ekranında gridde auto index ile oluşturulan özel ilaç kaydının {string} barkodu ile listelendiğini doğrular")
    public void gridde_auto_index_ile_olusturulan_ozel_ilac_kaydinin_barkodu_ile_listelendigi_dogrular(
            String expectedBarcode
    ) {
        customDrugsPage.verifyDrugListed(autoDrugName, expectedBarcode);
    }

    // ============== AUTO INDEX (EDIT) ===========

    @And("Özel İlaçlar ekranında grid üzerindeki mevcut özel ilaç kayıtları baz alınarak {string} adı ve {string} barkodu için düzenleme auto index bilgisi hazırlanır")
    public void ozel_ilaclar_edit_auto_index_hazirla(String baseName, String barcode) {
        editAutoDrugName = customDrugsPage.prepareEditAutoIndex(baseName, barcode);
    }

    @And("Özel İlaçlar ekranında grid üzerinde hazırlanan özel ilaç kaydı için Düzenle penceresi açılır")
    public void ozel_ilaclar_edit_penceresi_acilir() {
        customDrugsPage.openEditPopupForPreparedRow();
    }

    // ============== DÜZENLE PENCERESİ ===========

    @And("Özel İlaçlar ekranında Düzenle penceresinde Ad alanı auto index değeri ile güncellenir")
    public void ozel_ilaclar_edit_ad_auto_index_ile_guncellenir() {
        customDrugsPage.setDrugName(editAutoDrugName);
    }

    @And("Özel İlaçlar ekranında Düzenle penceresinde Barkod alanı {string} değeri ile güncellenir")
    public void ozel_ilaclar_edit_barkod_guncellenir(String newBarcode) {
        customDrugsPage.setBarcode(newBarcode);
    }

    @And("Özel İlaçlar ekranında Düzenle penceresinde Reçete Türü dropdownundan {string} seçeneği seçer")
    public void ozel_ilaclar_edit_recete_turu_secer(String receteTuru) {
        customDrugsPage.selectPrescriptionType(receteTuru);
    }

    @And("Özel İlaçlar ekranında Düzenle penceresinde Kaydet butonuna tıklar")
    public void ozel_ilaclar_edit_kaydet_butonuna_tiklar() {
        customDrugsPage.clickSaveCustomDrug();
    }

    // ============== EDIT SONRASI DOĞRULAMA ======

    @And("Özel İlaçlar ekranında düzenleme sonrası auto index ile oluşturulan ilaç adı ile arama yapılır")
    public void ozel_ilaclar_edit_sonrasi_auto_index_adi_ile_arama() {
        customDrugsPage.searchOnToolbar(editAutoDrugName);
    }

    @Then("Özel İlaçlar ekranında gridde düzenlenen özel ilaç kaydının {string} barkodu ve {string} reçete türü ile listelendiğini doğrular")
    public void gridde_duzenlenen_ozel_ilac_kaydinin_barkodu_ve_recete_turu_ile_listelendigi_dogrular(
            String expectedBarcode,
            String expectedPrescriptionType
    ) {
        customDrugsPage.verifyDrugListedWithAllFields(editAutoDrugName, expectedBarcode, expectedPrescriptionType);
    }

    // ============== DURUM (AKTİF/PASİF) STEPLERİ ===========

    @And("Özel İlaçlar ekranında adı {string} ile başlayan ve barkodu {string} olan kayıt durum değiştirme işlemi için hazırlanır")
    public void ozel_ilaclar_durum_degisim_kaydi_hazirlanir(String baseName, String barcode) {
        customDrugsPage.prepareStatusRow(baseName, barcode);
    }

    @And("Özel İlaçlar ekranında hazırlanan kayıt için durum menüsünden {string} seçeneği seçilir")
    public void ozel_ilaclar_hazirlanan_kayit_icin_durum_menusu_secim(String menuText) {
        customDrugsPage.changeStatusFromMenu(menuText);
    }

    @And("Özel İlaçlar ekranında durum değişikliği onay penceresinde Evet butonuna tıklar")
    public void ozel_ilaclar_durum_degisikligi_onay_penceresinde_evet_butonuna_tiklar() {
        customDrugsPage.confirmStatusChange();
    }

    @And("Özel İlaçlar ekranında hazırlanan kayıt için durum bilgisinin {string} olduğu doğrulanır")
    @Ozaman("Özel İlaçlar ekranında hazırlanan kaydın durum bilgisinin {string} olduğu doğrulanır")
    public void ozel_ilaclar_ekraninda_hazirlanan_kaydin_durum_bilgisinin_oldugu_dogrulanir(String expectedStatus) {
        customDrugsPage.verifyPreparedRowStatus(expectedStatus);
    }
}

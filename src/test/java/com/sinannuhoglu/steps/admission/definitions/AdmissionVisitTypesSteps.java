package com.sinannuhoglu.steps.admission.definitions;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.admission.definitions.AdmissionVisitTypesPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class AdmissionVisitTypesSteps {

    private final AdmissionVisitTypesPage visitTypesPage = new AdmissionVisitTypesPage();

    // CREATE + EDIT senaryolarında kullanılan auto-index isim
    private String autoName;

    // PASİF/AKTİF senaryosunda griddeki gerçek kayıt adı
    private String statusTargetName;

    // ============== NAVIGATION ==================

    @And("Vizit Tipleri modülüne gider")
    public void vizit_tipleri_modulune_gider() {
        String url = TestDataReader.get("admissionVisitTypesUrl");
        visitTypesPage.goToVisitTypes(url);
    }

    // ============== DETAYLI ARAMA ===============

    @And("Vizit Tipleri ekranında Detaylı Arama penceresi açılır")
    public void vt_detayli_arama_acilir() {
        visitTypesPage.openDetailedSearch();
    }

    @And("Vizit Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar")
    public void vt_temizle_butonuna_tiklar() {
        visitTypesPage.clickDetailedSearchClear();
    }

    @And("Vizit Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar")
    public void vt_uygula_butonuna_tiklar() {
        visitTypesPage.clickDetailedSearchApply();
    }

    // ============== ARAMA ALANI =================

    @And("Vizit Tipleri ekranında ara alanına {string} değeri yazılarak arama yapılır")
    public void vt_ara_alani_ile_arama_yapilir(String text) {
        visitTypesPage.searchOnToolbar(text);
    }

    // ============== AUTO INDEX (CREATE) =========

    @And("Vizit Tipleri ekranında grid üzerindeki mevcut kayıtlar baz alınarak {string} için auto index hesaplanır")
    public void vt_auto_index_hesapla(String base) {
        autoName = visitTypesPage.generateAutoIndex(base);
    }

    @And("Vizit Tipleri ekranında Yeni Ekle butonuna tıklar")
    public void vizit_tipleri_ekraninda_yeni_ekle_butonuna_tiklar() {
        visitTypesPage.openNewVisitTypeForm();
    }

    @And("Vizit Tipleri ekranındaki Vizit Tipi ad alanına auto index ile hesaplanan isim yazılır")
    public void vt_auto_index_adi_yazar() {
        visitTypesPage.setVisitTypeName(autoName);
    }

    @And("Vizit Tipleri ekranındaki Geliş Tipi dropdownundan {string} seçeneğini seçer")
    public void vt_gelis_tipi_secer(String tip) {
        visitTypesPage.selectArrivalType(tip);
    }

    @And("Vizit Tipleri ekranındaki Sistem Türü dropdownundan {string} seçeneğini seçer")
    public void vt_sistem_turu_secer(String sistem) {
        visitTypesPage.selectSystemType(sistem);
    }

    @And("Vizit Tipleri ekranındaki Kaydet butonuna tıklar")
    public void vt_kaydet_butonuna_tiklar() {
        visitTypesPage.clickSaveVisitType();
    }

    @Then("Vizit Tipleri ekranında gridde auto index ile oluşturulan kaydın listelendiğini doğrular")
    public void vt_gridde_auto_kayit_var() {
        visitTypesPage.verifyVisitTypeListed(autoName);
    }

    // ============== AUTO INDEX (EDIT) ===========

    @And("Vizit Tipleri ekranında grid üzerindeki mevcut kayıtlar baz alınarak {string} için düzenleme auto index bilgisi hazırlanır")
    public void vt_edit_auto_index_hazirla(String base) {
        autoName = visitTypesPage.prepareEditAutoIndex(base);
    }

    @And("Vizit Tipleri ekranında grid üzerinde hazırlanan auto index kaydı için Düzenle penceresi açılır")
    public void vt_edit_penceresi_acilir() {
        visitTypesPage.openEditPopupForPreparedRow();
    }

    @And("Vizit Tipleri ekranındaki Düzenle penceresinde vizit tipi adı auto index değer ile güncellenir")
    public void vt_edit_adi_auto_index_ile_gunceller() {
        visitTypesPage.setVisitTypeName(autoName);
    }

    @And("Vizit Tipleri ekranındaki Düzenle penceresinde Geliş Tipi dropdownundan {string} seçeneğini seçer")
    public void vt_edit_gelis_tipi_secer(String tip) {
        visitTypesPage.selectArrivalType(tip);
    }

    @And("Vizit Tipleri ekranındaki Düzenle penceresinde Sistem Türü dropdownundan {string} seçeneğini seçer")
    public void vt_edit_sistem_turu_secer(String sistem) {
        visitTypesPage.selectSystemType(sistem);
    }

    @And("Vizit Tipleri ekranındaki Düzenle penceresinde Kaydet butonuna tıklar")
    public void vt_edit_kaydet_butonuna_tiklar() {
        visitTypesPage.clickSaveVisitType();
    }

    @And("Vizit Tipleri ekranında auto index ile güncellenen ad ile arama yapılır")
    public void vt_edit_sonra_auto_isim_ile_arama() {
        visitTypesPage.searchOnToolbar(autoName);
    }

    @Then("Vizit Tipleri ekranında gridde auto index ile güncellenen vizit tipi kaydının listelendiğini doğrular")
    public void vt_edit_sonra_auto_kayit_dogrula() {
        visitTypesPage.verifyVisitTypeListed(autoName);
    }

    // ============== DURUM DEĞİŞTİRME (PASİF/AKTİF) =========

    @And("Vizit Tipleri ekranında grid üzerindeki mevcut kayıtlar baz alınarak {string} için durum değişimi hedef satırı belirlenir")
    public void vt_status_hedef_satir_belirle(String base) {
        visitTypesPage.prepareEditAutoIndex(base);
        statusTargetName = visitTypesPage.getEditSourceName();
    }

    @And("Vizit Tipleri ekranında grid üzerinde hazırlanan durum hedef kaydı için üç nokta menüsünden {string} seçeneğini seçer")
    public void vt_status_menu_aksiyonu_secer(String actionText) {
        visitTypesPage.clickStatusChangeForPreparedRow(actionText);
    }

    @And("Vizit Tipleri ekranında açılan onay penceresinde Evet butonuna tıklar")
    public void vt_onay_penceresinde_evet_butonuna_tiklar() {
        visitTypesPage.confirmYesOnDialog();
    }

    @And("Vizit Tipleri ekranında durum hedef kaydın adı ile arama yapılır")
    public void vt_status_hedef_adi_ile_arama() {
        visitTypesPage.searchOnToolbar(statusTargetName);
    }

    @Then("Vizit Tipleri ekranında gridde durum hedef kaydının durum bilgisinin {string} olduğunu doğrular")
    public void vt_status_durum_dogrula(String expectedStatus) {
        visitTypesPage.verifyStatusForRow(statusTargetName, expectedStatus);
    }
}

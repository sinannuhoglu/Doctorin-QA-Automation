package com.sinannuhoglu.steps.appointment.definitions;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.appointment.definitions.AppointmentTypesPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class AppointmentTypesSteps {

    private final AppointmentTypesPage appointmentTypesPage = new AppointmentTypesPage();

    // CREATE senaryosunda hesaplanan auto-index'li isim
    private String autoAppointmentTypeName;

    // EDIT senaryosunda kullanılacak yeni auto-index isim
    private String editAutoAppointmentTypeName;

    // Aktif/pasif senaryosu için hedef kayıt adı
    private String statusToggleTargetName;

    // ============== NAVIGATION ==================

    @And("Randevu Tipleri modülüne gider")
    public void randevu_tipleri_modulune_gider() {
        String url = TestDataReader.get("appointmentTypesUrl");
        appointmentTypesPage.goToAppointmentTypes(url);
    }

    // ============== DETAYLI ARAMA ===============

    @And("Randevu Tipleri ekranında Detaylı Arama penceresi açılır")
    public void appointment_types_detayli_arama_acilir() {
        appointmentTypesPage.openDetailedSearch();
    }

    @And("Randevu Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar")
    public void appointment_types_detayli_arama_temizle() {
        appointmentTypesPage.clickDetailedSearchClear();
    }

    @And("Randevu Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar")
    public void appointment_types_detayli_arama_uygula() {
        appointmentTypesPage.clickDetailedSearchApply();
    }

    // ============== ARAMA ALANI =================

    @And("Randevu Tipleri ekranında ara alanına {string} değeri yazılarak arama yapılır")
    public void appointment_types_ara_alani_ile_arama_yapilir(String text) {
        appointmentTypesPage.searchOnToolbar(text);
    }

    // ============== AUTO INDEX (CREATE) =========

    @And("Randevu Tipleri ekranında grid üzerindeki mevcut randevu tipi kayıtları baz alınarak {string} için auto index hesaplanır")
    public void appointment_types_auto_index_hesapla(String base) {
        autoAppointmentTypeName = appointmentTypesPage.generateAutoIndexName(base);
    }

    // ============== YENİ RANDEVU TİPİ KAYDI =====

    @And("Randevu Tipleri ekranında Yeni Ekle butonuna tıklar")
    public void appointment_types_yeni_ekle_butonuna_tiklar() {
        appointmentTypesPage.openNewAppointmentTypeForm();
    }

    @And("Randevu Tipleri ekranında Ad alanına auto index ile hesaplanan randevu tipi adı yazılır")
    public void appointment_types_ad_alanina_auto_index_ile_hesaplanan_adi_yazar() {
        appointmentTypesPage.setAppointmentTypeName(autoAppointmentTypeName);
    }

    @And("Randevu Tipleri ekranında Tip dropdownundan {string} seçeneği seçer")
    public void appointment_types_tip_dropdownundan_secenegi_secer(String tip) {
        appointmentTypesPage.selectType(tip);
    }

    @And("Randevu Tipleri ekranında Renk dropdownundan {string} seçeneği seçer")
    public void appointment_types_renk_dropdownundan_secenegi_secer(String renk) {
        appointmentTypesPage.selectColor(renk);
    }

    @And("Randevu Tipleri ekranında Kaydet butonuna tıklar")
    public void appointment_types_kaydet_butonuna_tiklar() {
        appointmentTypesPage.clickSaveAppointmentType();
    }

    // ============== SON ARAMA + DOĞRULAMA (CREATE) =========

    @And("Randevu Tipleri ekranında auto index ile oluşturulan randevu tipi adı ile arama yapılır")
    public void appointment_types_auto_index_adi_ile_arama_yapilir() {
        appointmentTypesPage.searchOnToolbar(autoAppointmentTypeName);
    }

    @Then("Randevu Tipleri ekranında gridde auto index ile oluşturulan randevu tipi kaydının listelendiğini doğrular")
    public void appointment_types_gridde_kaydinin_listelendigi_dogrulanir() {
        appointmentTypesPage.verifyAppointmentTypeListed(autoAppointmentTypeName);
    }

    // ============== AUTO INDEX (EDIT) ===========

    @And("Randevu Tipleri ekranında grid üzerindeki mevcut randevu tipi kayıtları baz alınarak {string} adı için düzenleme auto index bilgisi hazırlanır")
    public void appointment_types_edit_auto_index_hazirla(String baseName) {
        editAutoAppointmentTypeName = appointmentTypesPage.prepareEditAutoIndex(baseName);
    }

    @And("Randevu Tipleri ekranında grid üzerinde hazırlanan randevu tipi kaydı için Düzenle penceresi açılır")
    public void appointment_types_edit_penceresi_acilir() {
        appointmentTypesPage.openEditPopupForPreparedRow();
    }

    // ============== DÜZENLE PENCERESİ ===========

    @And("Randevu Tipleri ekranında Düzenle penceresinde Ad alanı auto index değeri ile güncellenir")
    public void appointment_types_edit_ad_auto_index_ile_guncellenir() {
        appointmentTypesPage.setAppointmentTypeName(editAutoAppointmentTypeName);
    }

    @And("Randevu Tipleri ekranında Düzenle penceresinde Tip dropdownundan {string} seçeneği seçer")
    public void appointment_types_edit_tip_dropdownundan_secenegi_secer(String tip) {
        appointmentTypesPage.selectType(tip);
    }

    @And("Randevu Tipleri ekranında Düzenle penceresinde Renk dropdownundan {string} seçeneği seçer")
    public void appointment_types_edit_renk_dropdownundan_secenegi_secer(String renk) {
        appointmentTypesPage.selectColor(renk);
    }

    @And("Randevu Tipleri ekranında Düzenle penceresinde Kaydet butonuna tıklar")
    public void appointment_types_edit_kaydet_butonuna_tiklar() {
        appointmentTypesPage.clickSaveAppointmentType();
    }

    // ============== SON ARAMA + DOĞRULAMA (EDIT) ===========

    @And("Randevu Tipleri ekranında düzenleme sonrası auto index ile oluşturulan randevu tipi adı ile arama yapılır")
    public void appointment_types_edit_sonrasi_auto_index_adi_ile_arama() {
        appointmentTypesPage.searchOnToolbar(editAutoAppointmentTypeName);
    }

    @Then("Randevu Tipleri ekranında gridde düzenlenen randevu tipi kaydının {string} tipi ve {string} rengi ile listelendiğini doğrular")
    public void appointment_types_gridde_duzenlenen_kaydinin_tipi_ve_rengi_ile_listelendigi_dogrular(
            String expectedType,
            String expectedColor
    ) {
        appointmentTypesPage.verifyAppointmentTypeListedWithAllFields(
                editAutoAppointmentTypeName,
                expectedType,
                expectedColor
        );
    }

    // ============== GRID DURUM / AKTİF - PASİF (GENEL KULLANIM) ==============

    @And("Randevu Tipleri ekranında gridde {string} randevu tipi kaydının bulunduğunu doğrular")
    public void appointment_types_gridde_kaydinin_bulundugunu_dogrular(String name) {
        appointmentTypesPage.verifyAppointmentTypeExists(name);
    }

    @And("Randevu Tipleri ekranında gridde {string} randevu tipi kaydının durumu Aktif ise üç nokta menüsünden Pasif Et aksiyonu uygulanır")
    public void appointment_types_aktif_ise_pasif_et_aksiyonu_uygulanir(String name) {
        appointmentTypesPage.changeStatusFromActiveToPassive(name);
    }

    @And("Randevu Tipleri ekranında gridde {string} randevu tipi kaydının durumu Pasif ise üç nokta menüsünden Aktif Et aksiyonu uygulanır")
    public void appointment_types_pasif_ise_aktif_et_aksiyonu_uygulanir(String name) {
        appointmentTypesPage.changeStatusFromPassiveToActive(name);
    }

    @And("Randevu Tipleri ekranında pasif\\/aktif işlemine ait onay penceresinde Evet butonuna tıklar")
    public void appointment_types_pasif_aktif_onay_penceresinde_evet_butonuna_tiklar() {
        appointmentTypesPage.clickStatusChangeConfirmYes();
    }

    @Then("Randevu Tipleri ekranında gridde {string} randevu tipi kaydının durumunun Pasif olarak listelendiğini doğrular")
    public void appointment_types_gridde_kaydinin_pasif_oldugunu_dogrular(String name) {
        appointmentTypesPage.verifyStatusForAppointmentType(name, "Pasif");
    }

    @Then("Randevu Tipleri ekranında gridde {string} randevu tipi kaydının durumunun Aktif olarak listelendiğini doğrular")
    public void appointment_types_gridde_kaydinin_aktif_oldugunu_dogrular(String name) {
        appointmentTypesPage.verifyStatusForAppointmentType(name, "Aktif");
    }

    // ============== GRID DURUM / AKTİF - PASİF (DİNAMİK HEDEF) ==============

    @And("Randevu Tipleri ekranında {string} baz alınarak aktif\\/pasif işlemi için hedef randevu tipi kaydı seçilir")
    public void appointment_types_hedef_kayit_secilir(String baseName) {
        // Grid içinde baseName ile başlayan ilk kaydın gerçek adını al
        statusToggleTargetName = appointmentTypesPage.prepareStatusToggleTarget(baseName);
    }

    @And("Randevu Tipleri ekranında gridde hedef randevu tipi kaydının bulunduğunu doğrular")
    public void appointment_types_gridde_hedef_kaydinin_bulundugunu_dogrular() {
        appointmentTypesPage.verifyAppointmentTypeExists(statusToggleTargetName);
    }

    @And("Randevu Tipleri ekranında hedef randevu tipi kaydının durumu Aktif ise üç nokta menüsünden Pasif Et aksiyonu uygulanır")
    public void appointment_types_hedef_aktif_ise_pasif_et_aksiyonu_uygulanir() {
        appointmentTypesPage.changeStatusFromActiveToPassive(statusToggleTargetName);
    }

    @And("Randevu Tipleri ekranında hedef randevu tipi kaydının durumu Pasif ise üç nokta menüsünden Aktif Et aksiyonu uygulanır")
    public void appointment_types_hedef_pasif_ise_aktif_et_aksiyonu_uygulanir() {
        appointmentTypesPage.changeStatusFromPassiveToActive(statusToggleTargetName);
    }

    @Then("Randevu Tipleri ekranında gridde hedef randevu tipi kaydının durumunun Pasif olarak listelendiğini doğrular")
    public void appointment_types_gridde_hedef_kaydinin_pasif_oldugunu_dogrular() {
        appointmentTypesPage.verifyStatusForAppointmentType(statusToggleTargetName, "Pasif");
    }

    @Then("Randevu Tipleri ekranında gridde hedef randevu tipi kaydının durumunun Aktif olarak listelendiğini doğrular")
    public void appointment_types_gridde_hedef_kaydinin_aktif_oldugunu_dogrular() {
        appointmentTypesPage.verifyStatusForAppointmentType(statusToggleTargetName, "Aktif");
    }
}

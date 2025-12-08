package com.sinannuhoglu.steps.admission;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.admission.AdmissionPatientSearchPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Hasta Arama modülü + Hasta Kabul akışı için step definitions.
 */
public class AdmissionPatientSearchSteps {

    private final AdmissionPatientSearchPage admissionPage =
            new AdmissionPatientSearchPage();

    // ============== HASTA ARAMA MODÜLÜNE GİDİŞ ==================

    @And("Hasta Arama modülüne gider")
    public void hasta_arama_modulune_gider() {
        String url = TestDataReader.get("admissionPatientSearchUrl");
        admissionPage.goToPatientSearch(url);
    }

    // ============== HASTA KABUL FORMU (SIDEBAR) ==================

    @And("Hasta Arama sayfasında Yeni Ekle butonuna tıklar")
    public void hasta_arama_sayfasinda_yeni_ekle_butonuna_tiklar() {
        admissionPage.openNewPatientForm();
    }

    @And("Hasta Kabul formunda Uyruk alanından {string} seçeneğini seçer")
    public void hasta_kabul_formunda_uyruk_alanindan_secim_yapar(String uyruk) {
        admissionPage.selectNationality(uyruk);
    }

    @And("Hasta Kabul formunda Dil alanından {string} seçeneğini seçer")
    public void hasta_kabul_formunda_dil_alanindan_secim_yapar(String dil) {
        admissionPage.selectLanguage(dil);
    }

    @And("Hasta Kabul formunda Kimlik Numarası alanına {string} değerini girer")
    public void hasta_kabul_formunda_kimlik_numarasi_alanina_deger_girer(String kimlikNo) {
        admissionPage.setIdentityNumber(kimlikNo);
    }

    @And("Hasta Kabul formunda Ad alanına {string} değerini girer")
    public void hasta_kabul_formunda_ad_alanina_deger_girer(String ad) {
        admissionPage.setFirstName(ad);
    }

    @And("Hasta Kabul formunda Soyad alanına {string} değerini girer")
    public void hasta_kabul_formunda_soyad_alanina_deger_girer(String soyad) {
        admissionPage.setLastName(soyad);
    }

    @When("Hasta Kabul formunda Doğum Tarihi alanına {string} tarihini girer")
    public void hasta_kabul_formunda_dogum_tarihi_alanina_tarihini_girer(String tarih) {
        admissionPage.setBirthDate(tarih);
    }

    @And("Hasta Kabul formunda Cinsiyet alanından {string} seçeneğini seçer")
    public void hasta_kabul_formunda_cinsiyet_alanindan_secim_yapar(String cinsiyet) {
        admissionPage.selectGender(cinsiyet);
    }

    @When("Hasta Kabul formunda Cep Telefonu alanına {string} değerini girer")
    public void hasta_kabul_formunda_cep_telefonu_alanina_deger_girer(String phone) {
        admissionPage.setMobilePhone(phone);
    }

    @And("Hasta Kabul formunda E-posta alanına {string} değerini girer")
    public void hasta_kabul_formunda_e_posta_alanina_deger_girer(String eposta) {
        admissionPage.setEmail(eposta);
    }

    @And("Hasta Kabul formunda Kaydet butonuna tıklar")
    public void hasta_kabul_formunda_kaydet_butonuna_tiklar() {
        admissionPage.clickSidebarSave();
    }

    // ============== HASTA KABUL EKRANI (VİZİT OLUŞTURMA) ==============

    @And("Hasta Kabul ekranında Vizit Tipi alanından {string} seçeneğini seçer")
    public void hasta_kabul_ekraninda_vizit_tipi_alanindan_secim_yapar(String visitType) throws InterruptedException {
        admissionPage.selectVisitType(visitType);
    }

    @And("Hasta Kabul ekranında Departman alanından {string} seçeneğini seçer")
    public void hasta_kabul_ekraninda_departman_alanindan_secim_yapar(String department) {
        admissionPage.selectDepartment(department);
    }

    @And("Hasta Kabul ekranında Doktor alanından {string} seçeneğini seçer")
    public void hasta_kabul_ekraninda_doktor_alanindan_secim_yapar(String doctor) {
        admissionPage.selectDoctor(doctor);
    }

    @And("Hasta Kabul ekranındaki Kaydet butonuna tıklar")
    public void hasta_kabul_ekranindaki_kaydet_butonuna_tiklar() {
        admissionPage.clickMainSave();
    }

    // ============== HASTA ARAMA EKRANINDA DOĞRULAMA ==================

    @And("Hasta kayıt işlemini doğrulamak için tekrar Hasta Arama ekranına gider")
    public void hasta_kayit_islemini_dogrulamak_icin_tekrar_hasta_arama_ekranina_gider() {
        String url = TestDataReader.get("admissionPatientSearchUrl");
        admissionPage.goToPatientSearch(url);
    }

    @And("Hasta Arama ekranında Detaylı Arama penceresini açar")
    public void hasta_arama_ekraninda_detayli_arama_penceresini_acar() {
        admissionPage.openDetailedFilterDialog();
    }

    @And("Hasta Arama ekranındaki Detaylı Arama penceresinde Temizle butonuna tıklar")
    public void hasta_arama_ekranindaki_detayli_arama_penceresinde_temizle_butonuna_tiklar() {
        admissionPage.clickDetailedFilterClear();
    }

    @And("Hasta Arama ekranındaki Detaylı Arama penceresinde Uygula butonuna tıklar")
    public void hasta_arama_ekranindaki_detayli_arama_penceresinde_uygula_butonuna_tiklar() {
        admissionPage.clickDetailedFilterApply();
    }

    @And("Hasta Arama ekranındaki arama alanına {string} yazar ve arama yapar")
    public void hasta_arama_ekranindaki_arama_alanina_yazar_ve_arama_yapar(String fullName) {
        admissionPage.searchPatientByName(fullName);
    }

    @Then("Hasta Arama listesindeki sonuçlarda {string} kaydını görmelidir")
    public void hasta_arama_listesindeki_sonuclarda_kaydini_gormelidir(String fullName) {
        admissionPage.assertPatientListedInGrid(fullName);
    }
}

package com.sinannuhoglu.steps.policy;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.policy.PolicyTreatmentPlanPackagesPage;
import io.cucumber.java.en.And;
import io.cucumber.java.tr.Ozaman;

/**
 * Poliçe > Tedavi Planı Paketler adımlarının Step Definition sınıfı.
 * Tüm adımlar PolicyTreatmentPlanPackagesPage üzerinden yönetilir.
 */
public class PolicyTreatmentPlanPackagesSteps {

    private final PolicyTreatmentPlanPackagesPage policyPage =
            new PolicyTreatmentPlanPackagesPage();

    private String uniquePackageName;

    // ================== NAVİGASYON ==================

    @And("Poliçe Tedavi Planı Paketler modülüne gider")
    public void police_tedavi_plani_paketler_modulune_gider() {
        String url = TestDataReader.get("policyTreatmentPlanPackagesUrl");
        policyPage.goToTreatmentPlanPackages(url);
    }

    // ================== DETAYLI ARAMA ==================

    @And("Poliçe Detaylı arama penceresini açar")
    public void police_detayli_arama_penceresini_acar() {
        policyPage.openDetailsPopup();
    }

    @And("Poliçe Detaylı arama penceresinde Temizle butonuna tıklar")
    public void police_detayli_aramada_temizle_butonuna_tiklar() {
        policyPage.clickClearInDetailsDialog();
    }

    @And("Poliçe Detaylı arama penceresinde Uygula butonuna tıklar")
    public void police_detayli_aramada_uygula_butonuna_tiklar() {
        policyPage.clickApplyInDetailsDialog();
    }

    // ================== ARAMA & DOĞRULAMA ==================

    @And("Tedavi planı paket arama alanına {string} yazar ve arama yapar")
    public void tedavi_plani_paket_arama_alanina_yazar_ve_arama_yapar(String text) {
        policyPage.searchTreatmentPlanPackageAllowEmpty(text);
    }

    @Ozaman("Tedavi planı paket listesi {string} için filtrelenmiş olmalıdır")
    public void tedavi_plani_paket_listesi_icin_filtrelenmis_olmalidir(String packageName) {
        policyPage.verifyListFilteredBy(packageName);
    }

    // ================== ÜÇ NOKTA MENÜ & DÜZENLE/SİL ==================

    @And("Paket satırı üç nokta menüsüne tıklar")
    public void paket_satiri_uc_nokta_menusune_tiklar() {
        policyPage.openActionMenuForFirstRow();
    }

    @And("Açılan menüden {string} seçeneğini seçer")
    public void acilan_menuden_seceneğini_secer(String optionText) {
        policyPage.clickActionMenuOption(optionText);
    }

    @And("Açılan düzenleme penceresinde Ad alanına {string} değerini girer")
    public void duzenleme_penceresinde_ad_alanina_deger_girer(String value) {
        policyPage.fillEditDialogAd(value);
    }

    @And("Düzenleme penceresinde Fatura Yönetimi alanına tıklar")
    public void duzenleme_penceresinde_fatura_yonetimi_alanina_tiklar() {
        policyPage.openFaturaYonetimiDropdown();
    }

    @And("Açılan Fatura Yönetimi seçeneklerinden {string} seçeneğini seçer")
    public void fatura_yonetimi_listesinden_secim_yapar(String option) {
        policyPage.selectFaturaYonetimi(option);
    }

    @And("Düzenleme penceresinde Para Birimi alanına tıklar")
    public void duzenleme_penceresinde_para_birimi_alanina_tiklar() {
        policyPage.openParaBirimiDropdown();
    }

    @And("Açılan Para Birimi listesinden {string} seçeneğini seçer")
    public void para_birimi_listesinden_secim_yapar(String option) {
        policyPage.selectParaBirimi(option);
    }

    @And("Düzenleme penceresindeki Kaydet butonuna tıklar")
    public void duzenleme_penceresindeki_kaydet_butonuna_tiklar() {
        policyPage.clickEditSave();
    }

    @And("Açılan silme onay penceresinde Evet butonuna tıklar")
    public void silme_onay_penceresinde_evet_butonuna_tiklar() {
        policyPage.confirmDelete();
    }

    @Ozaman("Tedavi planı paket listesi boş olmalıdır")
    public void tedavi_plani_paket_listesi_bos_olmalidir() {
        policyPage.verifyListEmpty();
    }

    // ================== YENİ KAYIT SENARYOSU ADIMLARI ==================

    @And("{string} baz adından benzersiz paket adı oluşturur")
    public void baz_adindan_benzersiz_paket_adi_olusturur(String baseName) {
        uniquePackageName = policyPage.generateUniquePackageName(baseName);
    }

    @And("Toolbar üzerindeki Yeni Ekle butonuna tıklar")
    public void toolbar_uzerindeki_yeni_ekle_butonuna_tiklar() {
        policyPage.clickToolbarYeniEkle();
    }

    @And("Açılan yeni kayıt penceresinde Ad alanına benzersiz paket adını girer")
    public void acilan_yeni_kayit_penceresinde_ad_alanina_benzersiz_paket_adini_girer() {
        policyPage.fillEditDialogAd(uniquePackageName);
    }

    @And("Yeni kayıt penceresinde Fatura Yönetimi alanına tıklar")
    public void yeni_kayit_penceresinde_fatura_yonetimi_alanina_tiklar() {
        policyPage.openFaturaYonetimiDropdown();
    }

    @And("Yeni kayıt penceresinde Para Birimi alanına tıklar")
    public void yeni_kayit_penceresinde_para_birimi_alanina_tiklar() {
        policyPage.openParaBirimiDropdown();
    }

    @And("Yeni kayıt penceresinde Servis Öğesi alanına tıklar")
    public void yeni_kayit_penceresinde_servis_ogesi_alanina_tiklar() {
        policyPage.openServiceItemDropdown();
    }

    @And("Açılan Servis Öğesi listesinden {string} seçeneğini seçer")
    public void servis_ogesi_listesinden_secim_yapar(String serviceName) {
        policyPage.selectServiceItem(serviceName);
    }

    @And("Yeni kayıt penceresindeki Kaydet butonuna tıklar")
    public void yeni_kayit_penceresindeki_kaydet_butonuna_tiklar() {
        policyPage.clickEditSave();
    }

    @And("Tedavi planı paket arama alanına benzersiz paket adını yazar ve arama yapar")
    public void tedavi_plani_paket_arama_alanina_benzersiz_paket_adini_yazar_ve_arama_yapar() {
        policyPage.searchTreatmentPlanPackageAllowEmpty(uniquePackageName);
    }

    @Ozaman("Tedavi planı paket listesi benzersiz paket adı için filtrelenmiş olmalıdır")
    public void tedavi_plani_paket_listesi_benzersiz_paket_adi_icin_filtrelenmis_olmalidir() {
        policyPage.verifyListFilteredBy(uniquePackageName);
    }
}

package com.sinannuhoglu.steps.catalogue.definitions;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.catalogue.definitions.CatalogueCategoryTypesPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

/**
 * Katalog > Tanımlar > Kategori Tipleri ekranı Step Definitions
 */
public class CatalogueCategoryTypesSteps {

    private final CatalogueCategoryTypesPage categoryTypesPage = new CatalogueCategoryTypesPage();

    // ================== NAVIGATION ==================

    @And("Kategori Tipleri modülüne gider")
    public void kategori_tipleri_modulune_gider() {
        String url = TestDataReader.get("catalogueCategoryTypesUrl");
        categoryTypesPage.goToCategoryTypes(url);
    }

    // ================== YENİ KATEGORİ OLUŞTURMA ==================

    @And("Kategori Tipleri ekranında Yeni Ekle butonuna tıklar")
    public void kategori_tipleri_ekraninda_yeni_ekle_butonuna_tiklar() {
        categoryTypesPage.openNewCategoryForm();
    }

    @And("Kategori Tipleri ekranında Yeni Ekle penceresinde Adı alanına {string} verisini girer")
    public void kategori_tipleri_ekraninda_yeni_ekle_penceresinde_adi_alanina_verisini_girer(String name) {
        categoryTypesPage.setName(name);
    }

    @And("Kategori Tipleri ekranında Yeni Ekle penceresinde Servis Türü alanında {string} seçeneğini seçer")
    public void kategori_tipleri_ekraninda_yeni_ekle_penceresinde_servis_turu_alaninda_secenegini_secer(String serviceType) {
        categoryTypesPage.selectServiceType(serviceType);
    }

    @And("Kategori Tipleri ekranında Yeni Ekle penceresinde Branşlar alanında {string} seçeneğini seçer")
    public void kategori_tipleri_ekraninda_yeni_ekle_penceresinde_branslar_alaninda_secenegini_secer(String branch) {
        categoryTypesPage.selectBranch(branch);
    }

    @And("Kategori Tipleri ekranında Yeni Ekle penceresinde Açıklama alanına {string} verisini girer")
    public void kategori_tipleri_ekraninda_yeni_ekle_penceresinde_aciklama_alanina_verisini_girer(String description) {
        categoryTypesPage.setDescription(description);
    }

    @And("Kategori Tipleri ekranında Yeni Ekle penceresinde Kaydet butonuna tıklar")
    public void kategori_tipleri_ekraninda_yeni_ekle_penceresinde_kaydet_butonuna_tiklar() {
        categoryTypesPage.clickSaveCategory();
    }

    // ================== TOOLBAR ARAMA ====================

    @And("Kategori Tipleri ekranında toolbar üzerindeki arama alanına {string} yazarak arama yapar")
    public void kategori_tipleri_ekraninda_toolbar_uzerindeki_arama_alanina_yazarak_arama_yapar(String text) {
        categoryTypesPage.searchOnToolbar(text);
    }

    // ================== GRID DOĞRULAMA ===================

    @Then("Kategori Tipleri ekranında gridde Adı {string}, Servis Türü {string}, Branş {string} ve Açıklama {string} olan kaydın listelendiğini doğrular")
    public void kategori_tipleri_ekraninda_gridde_kaydın_listelendigini_dogrular(
            String expectedName,
            String expectedServiceType,
            String expectedBranch,
            String expectedDescription
    ) {
        categoryTypesPage.verifyCategoryTypeListed(
                expectedName,
                expectedServiceType,
                expectedBranch,
                expectedDescription
        );
    }

    // ================== DÜZENLE SENARYOSU ===================

    @And("Kategori Tipleri ekranında gridde Adı {string} olan kaydın üç nokta menüsünden Düzenle seçeneğine tıklar")
    public void kategori_tipleri_ekraninda_gridde_adi_olan_kaydin_uc_nokta_menusunden_duzenle_secenegine_tiklar(
            String name
    ) {
        categoryTypesPage.openEditForRowByName(name);
    }

    @And("Kategori Tipleri ekranında Düzenle penceresinde Adı alanına {string} verisini girer")
    public void kategori_tipleri_ekraninda_duzenle_penceresinde_adi_alanina_verisini_girer(String name) {
        categoryTypesPage.setName(name);
    }

    @And("Kategori Tipleri ekranında Düzenle penceresinde Servis Türü alanında {string} seçeneğini seçer")
    public void kategori_tipleri_ekraninda_duzenle_penceresinde_servis_turu_alaninda_secenegini_secer(String serviceType) {
        categoryTypesPage.selectServiceType(serviceType);
    }

    @And("Kategori Tipleri ekranında Düzenle penceresinde Branşlar alanında {string} seçeneğini seçer")
    public void kategori_tipleri_ekraninda_duzenle_penceresinde_branslar_alaninda_secenegini_secer(String branch) {
        categoryTypesPage.selectBranch(branch);
    }

    @And("Kategori Tipleri ekranında Düzenle penceresinde Açıklama alanına {string} verisini girer")
    public void kategori_tipleri_ekraninda_duzenle_penceresinde_aciklama_alanina_verisini_girer(String description) {
        categoryTypesPage.setDescription(description);
    }

    @And("Kategori Tipleri ekranında Düzenle penceresinde Kaydet butonuna tıklar")
    public void kategori_tipleri_ekraninda_duzenle_penceresinde_kaydet_butonuna_tiklar() {
        categoryTypesPage.clickSaveCategory();
    }

    // ================== SİLME SENARYOSU ===================

    @And("Kategori Tipleri ekranında gridde Adı {string} olan kaydın üç nokta menüsünden Sil seçeneğine tıklar")
    public void kategori_tipleri_ekraninda_gridde_adi_olan_kaydin_uc_nokta_menusunden_sil_secenegine_tiklar(
            String name
    ) {
        categoryTypesPage.clickDeleteForRowByName(name);
    }

    @And("Kategori Tipleri ekranında Sil onay penceresinde Evet butonuna tıklar")
    public void kategori_tipleri_ekraninda_sil_onay_penceresinde_evet_butonuna_tiklar() {
        categoryTypesPage.confirmDeleteOnDialog();
    }

    @Then("Kategori Tipleri ekranında gridde Adı {string} olan kaydın listelenmediğini doğrular")
    public void kategori_tipleri_ekraninda_gridde_adi_olan_kaydin_listelenmedigini_dogrular(String name) {
        categoryTypesPage.verifyCategoryNotListedByName(name);
    }
}

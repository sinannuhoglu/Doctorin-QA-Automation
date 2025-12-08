package com.sinannuhoglu.steps.admission.definitions;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.admission.definitions.AdmissionTagsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class AdmissionTagsSteps {

    private final AdmissionTagsPage tagsPage = new AdmissionTagsPage();

    // 1. senaryo (yeni kayıt) için auto index ile oluşturulan etiket adı
    private String autoTagName;

    // 2. senaryo (düzenleme) için index artırılmış yeni başlık
    private String editedTagName;

    // 3. senaryo (silme) için kullanılacak etiket adı
    private String deleteTagName;

    // ============== NAVIGATION ==================

    @And("Etiketler modülüne gider")
    public void etiketler_modulune_gider() {
        String url = TestDataReader.get("admissionTagsUrl");
        tagsPage.goToTags(url);
    }

    // ============== DETAYLI ARAMA ===============

    @And("Etiketler ekranında Detaylı Arama penceresi açılır")
    public void etiketler_detayli_arama_acilir() {
        tagsPage.openDetailedSearch();
    }

    @And("Etiketler ekranında Detaylı Arama penceresinde Temizle butonuna tıklar")
    public void etiketler_detayli_arama_temizle() {
        tagsPage.clickDetailedSearchClear();
    }

    @And("Etiketler ekranında Detaylı Arama penceresinde Uygula butonuna tıklar")
    public void etiketler_detayli_arama_uygula() {
        tagsPage.clickDetailedSearchApply();
    }

    // ============== ARAMA ALANI =================

    @And("Etiketler ekranında ara alanına {string} değeri yazılarak arama yapılır")
    public void etiketler_ara_alani_ile_arama_yapilir(String text) {
        tagsPage.searchOnToolbar(text);
    }

    // ============== AUTO INDEX ==================

    @And("Grid üzerindeki mevcut etiket kayıtları baz alınarak {string} için auto index hesaplanır")
    public void etiketler_auto_index_hesapla(String base) {
        autoTagName = tagsPage.generateTagAutoIndex(base);
    }

    // ============== YENİ KAYIT OLUŞTURMA =========

    @And("Etiketler ekranında Yeni Ekle butonuna tıklar")
    public void etiketler_yeni_ekle_butonuna_tiklar() {
        tagsPage.openNewTagForm();
    }

    @And("Etiketler ekranında Başlık alanına auto index ile hesaplanan etiket adı yazılır")
    public void etiketler_baslik_auto_index_yazilir() {
        tagsPage.setTagTitle(autoTagName);
    }

    @And("Etiketler ekranında Açıklama alanına {string} değeri yazılır")
    public void etiketler_aciklama_yazilir(String description) {
        tagsPage.setTagDescription(description);
    }

    @And("Etiketler ekranında Tür dropdownundan {string} seçeneğini seçer")
    public void etiketler_tur_dropdown_secer(String type) {
        tagsPage.selectTagType(type);
    }

    @And("Etiketler ekranında Renk dropdownundan {string} seçeneğini seçer")
    public void etiketler_renk_dropdown_secer(String color) {
        tagsPage.selectTagColor(color);
    }

    @And("Etiketler ekranında Kaydet butonuna tıklar")
    public void etiketler_kaydet_butonuna_tiklar() {
        tagsPage.clickSaveTag();
    }

    // ============== GRID ÜZERİNDEN DÜZENLE / SİL =======

    @And("Grid üzerindeki ilk satırın 5. sütunundaki üç nokta menüsü açılır")
    public void grid_ilk_satir_uc_nokta_menusu_acilir() {
        tagsPage.openActionMenuOnRow();
    }

    @And("Açılan menüden {string} seçeneğine tıklar")
    public void acilan_menuden_secenege_tiklar(String optionText) {
        String opt = optionText.trim().toLowerCase();

        if ("düzenle".equals(opt)) {
            tagsPage.clickEditFromActionMenu();
        } else if ("sil".equals(opt)) {
            tagsPage.clickDeleteFromActionMenu();
        } else {
            throw new IllegalArgumentException("Desteklenmeyen menü seçeneği: " + optionText);
        }
    }

    // ============== DÜZENLE PENCERESİ ============

    @And("Düzenle penceresinde Başlık alanındaki index bir arttırılarak güncellenir")
    public void duzenle_penceresinde_baslik_index_bir_arttirilir() {
        editedTagName = tagsPage.incrementTagTitleIndexInDialog();
    }

    @And("Düzenle penceresinde Açıklama alanına {string} değeri yazılır")
    public void duzenle_penceresinde_aciklama_yazilir(String description) {
        tagsPage.setTagDescription(description);
    }

    @And("Düzenle penceresinde Tür dropdownundan {string} seçeneği seçilir")
    public void duzenle_penceresinde_tur_dropdown_secilir(String type) {
        tagsPage.selectTagType(type);
    }

    @And("Düzenle penceresinde Renk dropdownundan {string} seçeneği seçilir")
    public void duzenle_penceresinde_renk_dropdown_secilir(String color) {
        tagsPage.selectTagColor(color);
    }

    @And("Etiketler ekranındaki Düzenle penceresinde Kaydet butonuna tıklar")
    public void etiketler_duzenle_penceresinde_kaydet_butonuna_tiklar() {
        tagsPage.clickSaveTag();
    }

    // ============== SİLME PENCERESİ ==============

    @And("Açılan silme onay penceresinde {string} butonuna tıklar")
    public void acilan_silme_onay_penceresinde_butonuna_tiklar(String buttonText) {
        String btn = buttonText.trim().toLowerCase();
        if ("evet".equals(btn)) {
            tagsPage.confirmDeleteYes();
        } else {
            throw new IllegalArgumentException(
                    "Desteklenmeyen silme onay butonu: " + buttonText
            );
        }
    }

    // ============== SON DOĞRULAMA ===============

    @And("Etiketler ekranında auto index ile oluşturulan etiket adı ile arama yapılır")
    public void etiketler_auto_index_adi_ile_arama_yapilir() {
        if (autoTagName == null) {
            throw new IllegalStateException(
                    "Auto index ile oluşturulan etiket adı henüz hesaplanmamış. " +
                            "Lütfen senaryoda önce auto index hesaplayan stepi çalıştır."
            );
        }
        tagsPage.searchOnToolbar(autoTagName);
    }

    @Then("gridde auto index ile oluşturulan etiket kaydının listelendiğini doğrular")
    public void gridde_auto_index_etiket_kaydi_dogrulanir() {
        tagsPage.verifyTagListed(autoTagName);
    }

    @And("Etiketler ekranında düzenlenen etiket adı ile arama yapılır")
    public void etiketler_duzenlenen_etiket_adi_ile_arama_yapilir() {
        if (editedTagName == null) {
            throw new IllegalStateException(
                    "Düzenlenen etiket adı henüz set edilmemiş. " +
                            "Lütfen önce Düzenle penceresinde başlık index'ini arttıran stepi çalıştır."
            );
        }
        tagsPage.searchOnToolbar(editedTagName);
    }

    @Then("gridde düzenlenen etiket kaydının listelendiğini doğrular")
    public void gridde_duzenlenen_etiket_kaydi_dogrulanir() {
        if (editedTagName == null) {
            throw new IllegalStateException(
                    "Düzenlenen etiket adı henüz set edilmemiş. " +
                            "Lütfen önce düzenleme adımlarını çalıştır."
            );
        }
        tagsPage.verifyTagListed(editedTagName);
    }

    // ============== SİLME SENARYOSU ==============

    @And("Grid üzerindeki ilk satırdaki etiket adı silinecek etiket adı olarak kaydedilir")
    public void grid_ilk_satirdaki_etiket_adi_silinecek_etiket_adi_olarak_kaydedilir() {
        this.deleteTagName = tagsPage.readFirstRowTitle();
    }

    @And("Etiketler ekranında silinecek etiket adı ile arama yapılır")
    public void etiketler_ekraninda_silinecek_etiket_adi_ile_arama_yapilir() {
        if (deleteTagName == null || deleteTagName.isEmpty()) {
            throw new IllegalStateException(
                    "Silinecek etiket adı set edilmemiş. Lütfen önce ilgili steple değeri belirleyin."
            );
        }
        tagsPage.searchOnToolbar(deleteTagName);
    }

    @Then("gridde silinen etiket kaydının listelenmediğini doğrular")
    public void gridde_silinen_etiket_kaydinin_listelenmedigini_dogrular() {
        if (deleteTagName == null || deleteTagName.isEmpty()) {
            throw new IllegalStateException(
                    "Silinen etiket adı set edilmemiş. Lütfen önce ilgili steple değeri belirleyin."
            );
        }
        tagsPage.verifyTagNotListed(deleteTagName);
    }
}

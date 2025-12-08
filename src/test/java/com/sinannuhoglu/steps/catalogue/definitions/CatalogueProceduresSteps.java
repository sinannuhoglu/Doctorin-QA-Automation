package com.sinannuhoglu.steps.catalogue.definitions;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.catalogue.definitions.CatalogueProceduresPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

/**
 * Katalog > Tanımlar > Hizmet Tanımları ekranı Step Definitions
 */
public class CatalogueProceduresSteps {

    private final CatalogueProceduresPage proceduresPage = new CatalogueProceduresPage();

    // ================== NAVIGATION ==================

    @And("Katalog Hizmet Tanımları modülüne gider")
    public void katalog_hizmet_tanimlari_modulune_gider() {
        String url = TestDataReader.get("catalogueProceduresUrl");
        proceduresPage.goToProcedures(url);
    }

    // ================== YENİ HİZMET OLUŞTURMA / DÜZENLEME =========

    @And("Katalog Hizmet Tanımları ekranında Yeni Hizmet butonuna tıklar")
    public void katalog_hizmet_tanimlari_ekraninda_yeni_hizmet_butonuna_tiklar() {
        proceduresPage.openNewProcedureForm();
    }

    @And("Katalog Hizmet Tanımları ekranında Hizmet adı alanına {string} bilgisi girilir")
    public void katalog_hizmet_tanimlari_ekraninda_hizmet_adi_alanina_bilgisi_girilir(String name) {
        proceduresPage.setName(name);
    }

    @And("Katalog Hizmet Tanımları ekranında Alt tür alanından {string} değeri seçilir")
    public void katalog_hizmet_tanimlari_ekraninda_alt_tur_alanindan_degeri_secilir(String subType) {
        proceduresPage.selectSubType(subType);
    }

    @And("Katalog Hizmet Tanımları ekranında KDV oranı alanından {string} değeri seçilir")
    public void katalog_hizmet_tanimlari_ekraninda_kdv_orani_alanindan_degeri_secilir(String vat) {
        proceduresPage.selectVatRate(vat);
    }

    @And("Katalog Hizmet Tanımları ekranında ilk satır için Ücret alanına {string} değeri girilir")
    public void katalog_hizmet_tanimlari_ekraninda_ilk_satir_icin_ucret_alanina_degeri_girilir(String price) {
        proceduresPage.setFirstRowPrice(price);
    }

    @And("Katalog Hizmet Tanımları ekranında ilk satır için Para birimi alanından {string} değeri seçilir")
    public void katalog_hizmet_tanimlari_ekraninda_ilk_satir_icin_para_birimi_alanindan_degeri_secilir(String currency) {
        proceduresPage.selectFirstRowCurrency(currency);
    }

    @And("Katalog Hizmet Tanımları ekranında Kaydet butonuna tıklar")
    public void katalog_hizmet_tanimlari_ekraninda_kaydet_butonuna_tiklar() {
        proceduresPage.clickSaveProcedure();
    }

    @And("Katalog Hizmet Tanımları ekranında yeni hizmet penceresinin kapandığı doğrulanır")
    public void katalog_hizmet_tanimlari_ekraninda_yeni_hizmet_penceresinin_kapandigi_dogrulanir() {
        Assert.assertTrue(
                proceduresPage.isProcedureDialogClosed(),
                "Yeni hizmet penceresi kapatılmamış görünüyor."
        );
    }

    // ================== FİLTRE & ARAMA ===============

    @And("Katalog Hizmet Tanımları ekranında filtre butonuna tıklar")
    public void katalog_hizmet_tanimlari_ekraninda_filtre_butonuna_tiklar() {
        proceduresPage.clickToolbarFilterButton();
    }

    @And("Katalog Hizmet Tanımları ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar")
    public void katalog_hizmet_tanimlari_ekraninda_filtre_detay_penceresinde_temizle_ve_ardindan_uygula_butonlarina_tiklar() {
        proceduresPage.clearAndApplyDetailedFilter();
    }

    @And("Katalog Hizmet Tanımları ekranında arama alanına {string} Kodu değeri girilir ve aranır")
    public void katalog_hizmet_tanimlari_ekraninda_arama_alanina_kodu_degeri_girilir_ve_aranir(String code) {
        proceduresPage.searchByCode(code);
    }

    @Then("Katalog Hizmet Tanımları ekranında grid üzerinde {string} kodlu kaydın oluştuğu doğrulanır")
    public void katalog_hizmet_tanimlari_ekraninda_grid_uzerinde_kodlu_kaydin_olustugu_dogrulanir(String code) {
        Assert.assertTrue(
                proceduresPage.isRecordWithCodeVisible(code),
                "Grid üzerinde beklenen '" + code + "' kodlu kayıt bulunamadı."
        );
    }

    // ================== DURUM (AKTİF/PASİF) & 3 NOKTA MENÜSÜ ===============

    @And("Katalog Hizmet Tanımları ekranında grid üzerindeki ilk satırın durum alanının {string} olduğu doğrulanır")
    public void katalog_hizmet_tanimlari_ekraninda_grid_ilk_satir_durum_dogrulanir(String expectedStatus) {
        Assert.assertTrue(
                proceduresPage.isFirstRowStatusEquals(expectedStatus),
                "İlk satırdaki durum alanı beklendiği gibi değil. Beklenen: "
                        + expectedStatus + ", Gerçek: " + proceduresPage.getFirstRowStatus()
        );
    }

    @And("Katalog Hizmet Tanımları ekranında ilk satır için üç nokta menüsüne tıklanır")
    public void katalog_hizmet_tanimlari_ekraninda_ilk_satir_icin_uc_nokta_menusu_tiklanir() {
        proceduresPage.openFirstRowActionsMenu();
    }

    @And("Katalog Hizmet Tanımları ekranında açılan menüden {string} seçeneğine tıklanır")
    public void katalog_hizmet_tanimlari_ekraninda_acilan_menuden_secenege_tiklanir(String itemText) {
        proceduresPage.clickFirstRowActionsMenuItem(itemText);
    }

    @And("Katalog Hizmet Tanımları ekranında açılan onay penceresinde Evet butonuna tıklar")
    public void katalog_hizmet_tanimlari_ekraninda_acilan_onay_penceresinde_evet_butonuna_tiklar() {
        proceduresPage.confirmYesOnDialog();
    }
}

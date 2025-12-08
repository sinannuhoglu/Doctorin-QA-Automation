package com.sinannuhoglu.steps.treatment;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.treatment.TreatmentWorkListPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

/**
 * Tıbbi İşlemler > İş Listesi ekranı step definition'ları.
 */
public class TreatmentWorkListSteps {

    private final TreatmentWorkListPage workListPage = new TreatmentWorkListPage();

    // ================== NAVIGATION ==================

    @And("Tıbbi İşlemler İş Listesi modülüne gider")
    public void tibbi_islemler_is_listesi_modulune_gider() {
        String url = TestDataReader.get("treatmentWorkListUrl");
        workListPage.goToWorkList(url);
    }

    // ================== TARİH ARALIĞI ==================

    @And("Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanına tıklar")
    public void tibbi_islemler_is_listesi_ekraninda_tarih_araligi_alanina_tiklar() {
        workListPage.openDateRangePopup();
    }

    @And("Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanından Bu Yıl seçeneğini seçer")
    public void tibbi_islemler_is_listesi_ekraninda_tarih_araligi_alanindan_bu_yil_secenegini_secer() {
        workListPage.selectThisYearOnDateRange();
    }

    // ================== FİLTRE & ARAMA ==================

    @And("Tıbbi İşlemler İş Listesi ekranında filtre butonuna tıklar")
    public void tibbi_islemler_is_listesi_ekraninda_filtre_butonuna_tiklar() {
        workListPage.clickToolbarFilterButton();
    }

    @And("Tıbbi İşlemler İş Listesi ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar")
    public void tibbi_islemler_is_listesi_ekraninda_filtre_detay_penceresinde_temizle_ve_ardindan_uygula_butonlarina_tiklar() {
        workListPage.clearAndApplyDetailedFilter();
    }

    @And("Tıbbi İşlemler İş Listesi ekranında arama alanına {string} hasta adı girilir ve aranır")
    public void tibbi_islemler_is_listesi_ekraninda_arama_alanina_hasta_adi_girilir_ve_aranir(String patientName) {
        workListPage.searchByPatientName(patientName);
    }

    // ================== GRID & DETAY ==================

    @Then("Tıbbi İşlemler İş Listesi ekranında grid üzerinde {string} adlı hastanın listelendiği doğrulanır")
    public void tibbi_islemler_is_listesi_ekraninda_grid_uzerinde_adli_hastanin_listelendigi_dogrulanir(String expectedName) {
        Assert.assertTrue(
                workListPage.isFirstRowPatientNameEquals(expectedName),
                "İlk satırdaki hasta adı beklendiği gibi değil. Beklenen: "
                        + expectedName + ", Gerçek: " + workListPage.getFirstRowPatientName()
        );
    }

    @And("Tıbbi İşlemler İş Listesi ekranında grid üzerindeki ilk satır için Detay butonuna tıklar")
    public void tibbi_islemler_is_listesi_ekraninda_grid_uzerindeki_ilk_satir_icin_detay_butonuna_tiklar() {
        workListPage.clickFirstRowDetailButton();
    }

    // ================== SEKME GEÇİŞLERİ ==================

    /**
     * Mevcut Muayene senaryoları için hala kullanılabilir durumda bırakıyoruz.
     */
    @And("Tıbbi İşlemler İş Listesi ekranında açılan detay sayfasında Muayene sekmesine tıklar")
    public void tibbi_islemler_is_listesi_ekraninda_acilan_detay_sayfasinda_muayene_sekmesine_tiklar() {
        workListPage.clickExaminationTab();
    }

    @And("Tıbbi İşlemler İş Listesi ekranında açılan detay sayfasında Finans sekmesine tıklar")
    public void tibbi_islemler_is_listesi_ekraninda_acilan_detay_sayfasinda_finans_sekmesine_tiklar() {
        workListPage.clickFinanceTab();
    }
}

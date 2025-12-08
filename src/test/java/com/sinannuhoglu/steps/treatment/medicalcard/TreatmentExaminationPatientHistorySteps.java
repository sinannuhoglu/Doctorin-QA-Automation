package com.sinannuhoglu.steps.treatment.medicalcard;

import com.sinannuhoglu.pages.treatment.medicalcard.TreatmentExaminationPatientHistoryPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

/**
 * Muayene sayfası - Hasta Özgeçmişi (Alerjiler) adımları
 */
public class TreatmentExaminationPatientHistorySteps {

    private final TreatmentExaminationPatientHistoryPage patientHistoryPage =
            new TreatmentExaminationPatientHistoryPage();

    private String lastAllergyType;
    private String lastAllergen;
    private String lastDescription;

    // =============== PANEL AÇILIŞI ===============

    @And("Muayene sayfasında Hasta Özgeçmişi penceresini açar")
    public void muayene_sayfasinda_hasta_ozgecmisi_penceresini_acar() {
        patientHistoryPage.openPatientHistoryPanel();
    }

    // =============== ALERJİLER – FORM AÇILIŞI ===============

    @And("Muayene sayfasında Hasta Özgeçmişi penceresinde Alerjiler alanı için yeni kayıt ekleme penceresini açar")
    public void muayene_sayfasinda_hasta_ozgecmisi_penceresinde_alerjiler_alani_icin_yeni_kayit_ekleme_penceresini_acar() {
        patientHistoryPage.openAllergiesNewRecordForm();
    }

    // =============== ALERJİLER – FORM DOLDURMA ===============

    @And("Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında {string} alerji tipi, {string} alerjen ve {string} açıklama girer")
    public void muayene_sayfasinda_hasta_ozgecmisi_alerjiler_alaninda_alerji_tipi_alerjen_ve_aciklama_girer(
            String allergyType,
            String allergen,
            String description
    ) {
        this.lastAllergyType = allergyType;
        this.lastAllergen = allergen;
        this.lastDescription = description;

        patientHistoryPage.fillAllergiesForm(allergyType, allergen, description);
    }

    @And("Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında Ekle butonuna tıklar")
    public void muayene_sayfasinda_hasta_ozgecmisi_alerjiler_alaninda_ekle_butonuna_tiklar() {
        patientHistoryPage.clickAllergiesAddButton();
    }

    // =============== ALERJİLER – DOĞRULAMA ===============

    @Then("Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında {string} alerjeni için {string} açıklamasıyla kaydın oluşturulduğu doğrulanır")
    public void muayene_sayfasinda_hasta_ozgecmisi_alerjiler_alaninda_alerjeni_icin_aciklamasiyla_kaydin_olusturuldugu_dogrulanir(
            String allergen,
            String description
    ) {
        Assert.assertTrue(
                patientHistoryPage.isAllergyRecordVisible(allergen, description),
                "Hasta Özgeçmişi > Alerjiler alanında beklenen kayıt bulunamadı. Allergen=" +
                        allergen + ", Açıklama=" + description
        );
    }

    @And("Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında {string} alerjeni için sil butonuna tıklar")
    public void muayene_sayfasinda_hasta_ozgecmisi_alerjiler_alaninda_alerjeni_icin_sil_butonuna_tiklar(String allergen) {
        patientHistoryPage.deleteAllergyRecord(allergen, lastDescription);
    }

    @Then("Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında {string} alerjenine ait kaydın silindiği doğrulanır")
    public void muayene_sayfasinda_hasta_ozgecmisi_alerjiler_alaninda_alerjenine_ait_kaydin_silindigi_dogrulanir(String allergen) {
        Assert.assertTrue(
                patientHistoryPage.isAllergyRecordNotVisible(allergen),
                "Hasta Özgeçmişi > Alerjiler alanında silinmiş olması beklenen kayıt hâlâ görünüyor. Allergen=" + allergen
        );
    }
}

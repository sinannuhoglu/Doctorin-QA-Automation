package com.sinannuhoglu.steps.treatment.medicalcard;

import com.sinannuhoglu.pages.treatment.medicalcard.TreatmentExaminationBodyMeasurementsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

/**
 * Tıbbi İşlemler > Muayene > Vücut Ölçümleri adımları
 */
public class TreatmentExaminationBodyMeasurementsSteps {

    private final TreatmentExaminationBodyMeasurementsPage bodyMeasurementsPage =
            new TreatmentExaminationBodyMeasurementsPage();

    // ============ VÜCUT ÖLÇÜMLERİ POPUP ============

    @And("Muayene sayfasında Vücut Ölçümleri penceresini açar")
    public void muayene_sayfasinda_vucut_olcumleri_penceresini_acar() {
        bodyMeasurementsPage.openBodyMeasurementsDialog();
    }

    @And("Muayene Vücut Ölçümleri penceresinde bugüne ait bir kayıt varsa geçmişten silinir")
    public void muayene_vucut_olcumleri_penceresinde_bugune_ait_bir_kayit_varsa_gecmisten_silinir() {
        bodyMeasurementsPage.openHistorySection();
        bodyMeasurementsPage.deleteTodayRecordIfExists();
    }

    @And("Muayene Vücut Ölçümleri penceresinde Boy değeri {string} olarak girilir")
    public void muayene_vucut_olcumleri_penceresinde_boy_degeri_olarak_girilir(String height) {
        bodyMeasurementsPage.setHeight(height);
    }

    @And("Muayene Vücut Ölçümleri penceresinde Kilo değeri {string} olarak girilir")
    public void muayene_vucut_olcumleri_penceresinde_kilo_degeri_olarak_girilir(String weight) {
        bodyMeasurementsPage.setWeight(weight);
    }

    @And("Muayene Vücut Ölçümleri penceresinde Kaydet butonuna tıklar")
    public void muayene_vucut_olcumleri_penceresinde_kaydet_butonuna_tiklar() {
        bodyMeasurementsPage.clickSaveMeasurements();
    }

    @And("Muayene sayfasında Vücut Ölçümleri penceresini tekrar açar")
    public void muayene_sayfasinda_vucut_olcumleri_penceresini_tekrar_acar() {
        bodyMeasurementsPage.openBodyMeasurementsDialog();
        bodyMeasurementsPage.openHistorySection();
    }

    @Then("Muayene Vücut Ölçümleri geçmiş alanında bugüne ait kaydın oluştuğu doğrulanır")
    public void muayene_vucut_olcumleri_gecmis_alaninda_bugune_ait_kaydin_olustugu_dogrulanir() {
        Assert.assertTrue(
                bodyMeasurementsPage.isTodayRecordPresentInHistory(),
                "Vücut Ölçümleri geçmişinde bugüne ait kayıt bulunamadı."
        );
    }
}

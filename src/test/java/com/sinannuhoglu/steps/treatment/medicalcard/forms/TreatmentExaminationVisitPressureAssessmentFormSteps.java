package com.sinannuhoglu.steps.treatment.medicalcard.forms;

import com.sinannuhoglu.pages.treatment.medicalcard.forms.TreatmentExaminationVisitPressureAssessmentFormPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * Muayene sekmesi - Vizit içi "Basınç Değerlendirme" adımları
 */
public class TreatmentExaminationVisitPressureAssessmentFormSteps {

    private final TreatmentExaminationVisitPressureAssessmentFormPage pressurePage =
            new TreatmentExaminationVisitPressureAssessmentFormPage();

    // ================= FORM AÇILIŞI =================

    @When("Muayene sayfasında ilk vizitin Basınç Değerlendirme formu penceresini açar")
    public void muayene_sayfasinda_ilk_vizitin_basinc_degerlendirme_formu_penceresini_acar() {
        pressurePage.openFirstVisitPressureAssessmentForm();
    }

    // ================= FORM DOLDURMA (DataTable + Kaydet) =================

    @And("Muayene sayfasında Basınç Değerlendirme formu aşağıdaki risk seçenekleriyle doldurulur ve kaydedilir:")
    public void muayene_sayfasinda_basinc_degerlendirme_formu_asagidaki_risk_secenekleriyle_doldurulur_ve_kaydedilir(
            DataTable dataTable
    ) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        pressurePage.fillPressureAssessmentFromTable(rows);
    }

    // ================= ESKİ DOLDURMA / KAYDET (Geriye dönük uyumluluk için) =================

    @And("Muayene sayfasında Basınç Değerlendirme formunda tüm risk kriterleri için ilk seçenekleri işaretler")
    public void muayene_sayfasinda_basinc_degerlendirme_formunda_tum_risk_kriterleri_icin_ilk_secenekleri_isaretler() {
        pressurePage.selectAllFirstRiskOptions();
    }

    @And("Muayene sayfasında Basınç Değerlendirme formu için Kaydet butonuna tıklar")
    public void muayene_sayfasinda_basinc_degerlendirme_formu_icin_kaydet_butonuna_tiklar() {
        pressurePage.clickPressureSaveButton();
    }

    // ================= KAYIT DOĞRULAMA =================

    @Then("Muayene sayfasında Basınç Değerlendirme formu kaydının başarıyla oluşturulduğu doğrulanır")
    public void muayene_sayfasinda_basinc_degerlendirme_formu_kaydinin_basarili_oldugu_dogrulanir() {
        Assert.assertTrue(
                pressurePage.isPressureAssessmentSavedSuccessfully(),
                "Muayene sayfasında Basınç Değerlendirme formu kaydının başarıyla oluşturulduğu doğrulanamadı."
        );
    }

    // ================= SİLME & DOĞRULAMA =================

    @And("Muayene sayfasında vizitler arasında Basınç Değerlendirme formu bulunan vizitteki formu siler")
    public void muayene_sayfasinda_vizitler_arasinda_basinc_degerlendirme_formu_bulunan_vizitteki_formu_siler() {
        boolean deleted = pressurePage.deletePressureAssessmentFromTargetOrPreviousVisit();
        Assert.assertTrue(
                deleted,
                "Muayene sayfasında vizitler arasında silinebilen bir Basınç Değerlendirme formu bulunamadı."
        );
    }

    @And("Muayene sayfasında Basınç Değerlendirme formu silme işlemi için varsa Onay penceresindeki Evet butonuna tıklar")
    public void muayene_sayfasinda_basinc_degerlendirme_formu_silme_islemi_icin_varsa_onay_penceresindeki_evet_butonuna_tiklar() {
        pressurePage.confirmDeleteIfDialogVisible();
    }

    @Then("Muayene sayfasında Basınç Değerlendirme formu açılan vizitte hiç Basınç Değerlendirme formu kalmadığı doğrulanır")
    public void muayene_sayfasinda_basinc_degerlendirme_formu_acilan_vizitte_hic_basinc_degerlendirme_formu_kalmadigi_dogrulanir() {
        Assert.assertTrue(
                pressurePage.isPressureAssessmentAbsentInCreatedVisit(),
                "Muayene sayfasında Basınç Değerlendirme formu açılan vizitte hâlâ Basınç Değerlendirme formu görünmektedir."
        );
    }
}

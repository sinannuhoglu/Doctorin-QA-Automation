package com.sinannuhoglu.steps.treatment.medicalcard.forms;

import com.sinannuhoglu.pages.treatment.medicalcard.forms.TreatmentExaminationVisitVitalSignsFormPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * Muayene sekmesi - Vizit içi "Vital Bulgular" Formu adımları
 */
public class TreatmentExaminationVisitVitalSignsFormSteps {

    private final TreatmentExaminationVisitVitalSignsFormPage vitalFormPage =
            new TreatmentExaminationVisitVitalSignsFormPage();

    // ================= FORM AÇILIŞI =================

    @And("Muayene sayfasında ilk vizitin Vital Bulgular formu penceresini açar")
    public void muayene_sayfasinda_ilk_vizitin_vital_bulgular_formu_penceresini_acar() {
        vitalFormPage.openFirstVisitVitalForm();
    }

    // ================= ALANLARIN DOLDURULMASI =================

    @And("Muayene sayfasında Vital Bulgular formu aşağıdaki değerlerle doldurulur:")
    public void muayene_sayfasinda_vital_bulgular_formu_asagidaki_degerlerle_doldurulur(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        vitalFormPage.fillVitalFormFieldsFromTable(rows);
    }

    @And("Muayene sayfasında Vital Bulgular formu alanları doldurulur")
    public void muayene_sayfasinda_vital_bulgular_formu_alanlari_doldurulur() {
        vitalFormPage.fillVitalFormFields();
    }

    // ================= KAYDET =================

    @And("Muayene sayfasında Vital Bulgular formu için Kaydet butonuna tıklar")
    public void muayene_sayfasinda_vital_bulgular_formu_icin_kaydet_butonuna_tiklar() {
        vitalFormPage.clickSaveButton();
    }

    // ================= DOĞRULAMA =================

    @Then("Muayene sayfasında Vital Bulgular formu kaydının başarıyla oluşturulduğu doğrulanır")
    public void muayene_sayfasinda_vital_bulgular_formu_kaydinin_basarili_oldugu_dogrulanir() {
        Assert.assertTrue(
                vitalFormPage.isVitalFormSavedSuccessfully(),
                "Vital Bulgular formu kaydının başarıyla oluşturulduğu doğrulanamadı."
        );
    }

    // ================= SİLME & DOĞRULAMA =================

    @And("Muayene sayfasında vizitler arasında Vital Bulgular formu bulunan ilk vizitteki formu siler")
    public void muayene_sayfasinda_vizitler_arasinda_vital_bulgular_formu_bulunan_ilk_vizitteki_formu_siler() {
        boolean deleted = vitalFormPage.deleteAllVitalFormsFromAllVisits();
        Assert.assertTrue(
                deleted,
                "Muayene sayfasında vizitler arasında silinebilen bir Vital Bulgular formu bulunamadı."
        );
    }

    @And("Muayene sayfasında Vital Bulgular formu silme işlemi için varsa Onay penceresindeki Evet butonuna tıklar")
    public void muayene_sayfasinda_vital_bulgular_formu_silme_islemi_icin_varsa_onay_penceresindeki_evet_butonuna_tiklar() {
        vitalFormPage.confirmDeleteIfDialogVisible();
    }

    @Then("Muayene sayfasında Vital Bulgular formu açılan vizitte hiç Vital Bulgular formu kalmadığı doğrulanır")
    public void muayene_sayfasinda_vital_bulgular_formu_acilan_vizitte_hic_vital_bulgular_formu_kalmadigi_dogrulanir() {
        Assert.assertTrue(
                vitalFormPage.isVitalFormAbsentInCreatedVisit(),
                "Muayene sayfasında Vital Bulgular formu açılan vizitte hâlâ Vital Bulgular formu görünmektedir."
        );
    }
}

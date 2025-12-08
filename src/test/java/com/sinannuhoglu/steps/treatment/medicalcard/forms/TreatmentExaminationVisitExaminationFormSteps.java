package com.sinannuhoglu.steps.treatment.medicalcard.forms;

import com.sinannuhoglu.pages.treatment.medicalcard.forms.TreatmentExaminationVisitExaminationFormPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * Muayene sekmesi - Vizit içi "Muayene Formu" adımları
 */
public class TreatmentExaminationVisitExaminationFormSteps {

    private final TreatmentExaminationVisitExaminationFormPage examinationFormPage =
            new TreatmentExaminationVisitExaminationFormPage();

    // ================= FORM AÇILIŞI =================

    @When("Muayene sayfasında ilk vizitin Muayene Formu penceresini açar")
    public void muayene_sayfasinda_ilk_vizitin_muayene_formu_penceresini_acar() {
        examinationFormPage.openFirstVisitExaminationForm();
    }

    // ================= FORM DOLDURMA (DataTable) + KAYDET =================

    @And("Muayene sayfasında Muayene Formu aşağıdaki verilerle doldurulur ve kaydedilir:")
    public void muayene_sayfasinda_muayene_formu_asagidaki_verilerle_doldurulur_ve_kaydedilir(
            DataTable dataTable
    ) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        examinationFormPage.fillExaminationFormFromTable(rows);
    }

    // ================= KAYDIN DOĞRULANMASI =================

    @Then("Muayene sayfasında Muayene Formu kaydının başarıyla oluşturulduğu doğrulanır")
    public void muayene_sayfasinda_muayene_formu_kaydinin_basarili_oldugu_dogrulanir() {
        Assert.assertTrue(
                examinationFormPage.isExaminationFormSavedSuccessfully(),
                "Muayene Formu kaydının başarıyla oluşturulduğu doğrulanamadı."
        );
    }

    // ================= KAYDET BUTONU (Diğer senaryolar için kullanılabilir) =================

    @And("Muayene sayfasında Muayene Formu için Kaydet butonuna tıklar")
    public void muayene_sayfasinda_muayene_formu_icin_kaydet_butonuna_tiklar() {
        examinationFormPage.clickSaveButton();
    }

    // ================= VİZİTLERDE FORM ARAMA & SİLME =================

    @And("Muayene sayfasında vizitler arasında Muayene Formu bulunan ilk vizitteki formu siler")
    public void muayene_sayfasinda_vizitler_arasinda_muayene_formu_bulunan_ilk_vizitteki_formu_siler() {
        boolean deleted = examinationFormPage.deleteAllExaminationFormsFromAllVisits();
        Assert.assertTrue(
                deleted,
                "Muayene sayfasında vizitler arasında silinebilen bir Muayene Formu bulunamadı."
        );
    }

    @And("Muayene sayfasında Muayene Formu silme işlemi için varsa Onay penceresindeki Evet butonuna tıklar")
    public void muayene_sayfasinda_muayene_formu_silme_islemi_icin_varsa_onay_penceresindeki_evet_butonuna_tiklar() {
        examinationFormPage.confirmDeleteIfDialogVisible();
    }

    @Then("Muayene sayfasında Muayene Formu açılan vizitte hâlâ Muayene Formu kalmadığı doğrulanır")
    public void muayene_sayfasinda_vizitler_arasinda_hic_muayene_formu_kalmadigi_dogrulanir() {
        Assert.assertTrue(
                examinationFormPage.isExaminationFormAbsentInCreatedVisit(),
                "Muayene sayfasında Muayene Formu açılan vizitte hâlâ Muayene Formu görünmektedir."
        );
    }
}

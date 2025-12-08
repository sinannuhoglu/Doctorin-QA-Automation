package com.sinannuhoglu.steps.treatment.medicalcard.forms;

import com.sinannuhoglu.pages.treatment.medicalcard.forms.TreatmentExaminationVisitSurgeryReportFormPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * Muayene sekmesi - Vizit içi "Ameliyat Raporu" adımları
 */
public class TreatmentExaminationVisitSurgeryReportFormSteps {

    private final TreatmentExaminationVisitSurgeryReportFormPage surgeryReportPage =
            new TreatmentExaminationVisitSurgeryReportFormPage();

    // ================= FORM AÇILIŞI =================

    @When("Muayene sayfasında ilk vizitin Ameliyat Raporu penceresini açar")
    public void muayene_sayfasinda_ilk_vizitin_ameliyat_raporu_penceresini_acar() {
        surgeryReportPage.openFirstVisitSurgeryReportForm();
    }

    // ================= ALANLARIN DOLDURULMASI (DataTable + Kaydet) =================

    @And("Muayene sayfasında Ameliyat Raporu aşağıdaki verilerle doldurulur ve kaydedilir:")
    public void muayene_sayfasinda_ameliyat_raporu_asagidaki_verilerle_doldurulur_ve_kaydedilir(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        surgeryReportPage.fillSurgeryReportFromTable(rows);
    }

    // ================= KAYIT DOĞRULAMA =================

    @Then("Muayene sayfasında Ameliyat Raporu kaydının başarıyla oluşturulduğu doğrulanır")
    public void muayene_sayfasinda_ameliyat_raporu_kaydinin_basarili_oldugu_dogrulanir() {
        Assert.assertTrue(
                surgeryReportPage.isSurgeryReportSavedSuccessfully(),
                "Muayene sayfasında Ameliyat Raporu kaydının başarıyla oluşturulduğu doğrulanamadı."
        );
    }

    // ================= SİLME & DOĞRULAMA =================

    @And("Muayene sayfasında vizitler arasında Ameliyat Raporu bulunan vizitteki formu siler")
    public void muayene_sayfasinda_vizitler_arasinda_ameliyat_raporu_bulunan_vizitteki_formu_siler() {
        boolean deleted = surgeryReportPage.deleteSurgeryReportFromTargetOrPreviousVisit();
        Assert.assertTrue(
                deleted,
                "Muayene sayfasında vizitler arasında silinebilen bir Ameliyat Raporu bulunamadı."
        );
    }

    @And("Muayene sayfasında Ameliyat Raporu silme işlemi için varsa Onay penceresindeki Evet butonuna tıklar")
    public void muayene_sayfasinda_ameliyat_raporu_silme_islemi_icin_varsa_onay_penceresindeki_evet_butonuna_tiklar() {
        surgeryReportPage.confirmDeleteIfDialogVisible();
    }

    @Then("Muayene sayfasında Ameliyat Raporu açılan vizitte hiç Ameliyat Raporu kalmadığı doğrulanır")
    public void muayene_sayfasinda_ameliyat_raporu_acilan_vizitte_hic_ameliyat_raporu_kalmadigi_dogrulanir() {
        Assert.assertTrue(
                surgeryReportPage.isSurgeryReportAbsentInCreatedVisit(),
                "Muayene sayfasında Ameliyat Raporu açılan vizitte hâlâ Ameliyat Raporu görünmektedir."
        );
    }
}

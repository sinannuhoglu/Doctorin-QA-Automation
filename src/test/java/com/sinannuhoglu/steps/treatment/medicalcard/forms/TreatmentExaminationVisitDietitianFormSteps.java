package com.sinannuhoglu.steps.treatment.medicalcard.forms;

import com.sinannuhoglu.pages.treatment.medicalcard.forms.TreatmentExaminationVisitDietitianFormPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class TreatmentExaminationVisitDietitianFormSteps {

    private static final Logger LOGGER =
            LogManager.getLogger(TreatmentExaminationVisitDietitianFormSteps.class);

    private final TreatmentExaminationVisitDietitianFormPage dietitianFormPage =
            new TreatmentExaminationVisitDietitianFormPage();

    // ================= FORM AÇILIŞI =================

    @And("Muayene sayfasında vizit içinde Diyetisyen formu açılır")
    public void muayene_sayfasinda_vizit_icerisinde_diyetisyen_formu_acilir() {
        LOGGER.info("STEP: Muayene sayfasında vizit içinde Diyetisyen formu açılır");
        dietitianFormPage.openDietitianFormFromPopup();
    }

    // ================= FORM DOLDURMA (DataTable) + KAYDET =================

    @And("Muayene sayfasında vizit içinde Diyetisyen formu aşağıdaki verilerle doldurulur ve kaydedilir:")
    public void muayene_sayfasinda_vizit_icerisinde_diyetisyen_formu_asagidaki_verilerle_doldurulur_ve_kaydedilir(
            DataTable dataTable
    ) {
        LOGGER.info("STEP: Muayene sayfasında vizit içinde Diyetisyen formu DataTable ile doldurulur ve kaydedilir");

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        dietitianFormPage.fillDietitianFormFromTable(rows);
    }

    // ================= DOĞRULAMA: EKLENDİ Mİ? =================

    @Then("Muayene sayfasında Diyetisyen formu açılan vizitte Diyetisyen formu eklendiği doğrulanır")
    public void muayene_sayfasinda_diyetisyen_formu_acilan_vizitte_diyetisyen_formu_eklendigini_dogrular() {
        LOGGER.info("STEP: Muayene sayfasında Diyetisyen formu açılan vizitte Diyetisyen formu eklendiği doğrulanır");

        Assert.assertTrue(
                dietitianFormPage.isDietitianFormPresentInOpenedVisit(),
                "Muayene sayfasında Diyetisyen formu açılan vizitte herhangi bir Diyetisyen formu bulunamadı."
        );
    }

    // ================= SİLME + DOĞRULAMA =================

    @And("Muayene sayfasında Diyetisyen formu açılan vizitteki tüm Diyetisyen formları silinir")
    public void muayene_sayfasinda_diyetisyen_formu_acilan_vizitteki_tum_diyetisyen_formlari_silinir() {
        LOGGER.info("STEP: Muayene sayfasında Diyetisyen formu açılan vizitteki tüm Diyetisyen formları silinir");

        boolean deleted = dietitianFormPage.deleteAllDietitianFormsInOpenedVisit();
        Assert.assertTrue(
                deleted,
                "Muayene sayfasında Diyetisyen formu açılan vizitte silinecek bir Diyetisyen formu bulunamadı."
        );
    }

    @Then("Muayene sayfasında Diyetisyen formu açılan vizitte hiç Diyetisyen formu kalmadığı doğrulanır")
    public void muayene_sayfasinda_diyetisyen_formu_acilan_vizitte_hic_diyetisyen_formu_kalmadigi_dogrulanir() {
        LOGGER.info("STEP: Muayene sayfasında Diyetisyen formu açılan vizitte hiç Diyetisyen formu kalmadığı doğrulanır");

        Assert.assertTrue(
                dietitianFormPage.isNoDietitianFormLeftInOpenedVisit(),
                "Muayene sayfasında Diyetisyen formu açılan vizitte hâlâ Diyetisyen formu bulunmaktadır."
        );
    }
}

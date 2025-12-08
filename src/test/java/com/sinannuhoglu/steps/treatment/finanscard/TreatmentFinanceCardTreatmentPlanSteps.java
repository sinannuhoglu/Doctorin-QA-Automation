package com.sinannuhoglu.steps.treatment.finanscard;

import com.sinannuhoglu.pages.treatment.finanscard.TreatmentFinanceCardTreatmentPlanPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TreatmentFinanceCardTreatmentPlanSteps {

    private final TreatmentFinanceCardTreatmentPlanPage treatmentPlanPage =
            new TreatmentFinanceCardTreatmentPlanPage();

    @When("Finans kartında Tedavi Planları panelini açar")
    public void finans_kartinda_tedavi_planlari_panelini_acar() {
        treatmentPlanPage.openTreatmentPlansPanel();
    }

    @When("Finans kartında Tedavi Planları panelinde {string} servisi için {string} adlı {int} oranında indirimli tedavi planı ekler")
    public void finans_kartinda_tedavi_planlari_panelinde_servis_icin_indirimli_tedavi_plani_ekler(
            String serviceName,
            String planName,
            Integer discountRate
    ) {
        treatmentPlanPage.openNewTreatmentPlanDialog();
        treatmentPlanPage.createDiscountedTreatmentPlan(serviceName, planName, discountRate);
    }

    @When("Finans kartında açılan Fatura penceresinde artı ikonuna tıklar ve {int} saniye bekler")
    public void finans_kartinda_acilan_fatura_penceresinde_arti_ikonuna_tiklar_ve_bekler(Integer seconds) {
        treatmentPlanPage.clickInvoicePlusAndWait(seconds);
    }

    @When("Finans kartında açılan Fatura penceresinde Kaydet butonuna tıklar")
    public void finans_kartinda_acilan_fatura_penceresinde_kaydet_butonuna_tiklar() {
        treatmentPlanPage.clickInvoiceSaveButton();
    }

    @Then("Finans kartında Tedavi Planı penceresinde {string} plan satırı için {string} statüsünün göründüğü doğrulanır")
    public void finans_kartinda_tedavi_plani_penceresinde_plan_satiri_icin_status_dogrulanir(
            String planName,
            String statusText
    ) {
        treatmentPlanPage.assertTreatmentPlanInDialogWithStatus(planName, statusText);
    }

}

package com.sinannuhoglu.steps.treatment.finanscard;

import com.sinannuhoglu.pages.treatment.finanscard.TreatmentFinanceCardAdvancePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TreatmentFinanceCardAdvanceSteps {

    private final TreatmentFinanceCardAdvancePage advancePage =
            new TreatmentFinanceCardAdvancePage();

    @When("Finans kartında Avans penceresini açar")
    public void finans_kartinda_avans_penceresini_acar() {
        advancePage.openAdvanceDialog();
    }

    @When("Finans kartında Avans penceresinde {string} tutarı ve {string} açıklaması ile avans kaydı oluşturur")
    public void finans_kartinda_avans_penceresinde_tutar_ve_aciklamasi_ile_avans_kaydi_olusturur(
            String amount,
            String description
    ) {
        advancePage.createAdvance(amount, description);
    }

    @When("Finans kartında Avans listesinde {string} tutarlı ve {string} açıklamalı kaydın bulunduğu satır için üç nokta menüsünden İptal Et seçeneğini seçer")
    public void finans_kartinda_avans_listesinde_kaydin_satiri_icin_uc_nokta_menusunden_iptal_et_secer(
            String amount,
            String description
    ) {
        advancePage.cancelAdvance(amount, description);
    }

    @Then("Finans kartında Avans listesinde {string} tutarlı ve {string} açıklamalı kaydın silindiği doğrulanır")
    public void finans_kartinda_avans_listesinde_kaydin_silindigi_dogrulanir(
            String amount,
            String description
    ) {
        advancePage.verifyAdvanceDeleted(amount, description);
    }
}

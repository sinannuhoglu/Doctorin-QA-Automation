package com.sinannuhoglu.steps.treatment.finanscard;

import com.sinannuhoglu.pages.treatment.finanscard.TreatmentFinanceCardDiscountPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TreatmentFinanceCardDiscountSteps {

    private final TreatmentFinanceCardDiscountPage discountPage =
            new TreatmentFinanceCardDiscountPage();

    // --- İndirim oluşturma adımları ---

    @When("Finans kartinda Indirim penceresini acar")
    public void finans_kartinda_indirim_penceresini_acar() {
        discountPage.openDiscountCreatePanel();
    }

    @When("Finans kartinda Indirim penceresinde Indirim Yontemi olarak Tutar secenegini secer")
    public void finans_kartinda_indirim_penceresinde_indirim_yontemi_olarak_tutar_secenegini_secer() {
        discountPage.selectAmountMethodInDiscountPanel();
    }

    @When("Finans kartinda Indirim penceresinde {string} tutarini girer")
    public void finans_kartinda_indirim_penceresinde_tutarini_girer(String amount) {
        discountPage.enterDiscountAmount(amount);
    }

    @When("Finans kartinda Indirim penceresinde degeri guncellemek icin Tutar secenegini tekrar secer")
    public void finans_kartinda_indirim_penceresinde_degeri_guncellemek_icin_tutar_secenegini_tekrar_secer() {
        discountPage.reselectAmountMethodToRefresh();
    }

    @When("Finans kartinda Indirim penceresinde Kaydet butonuna tiklar")
    public void finans_kartinda_indirim_penceresinde_kaydet_butonuna_tiklar() {
        discountPage.clickDiscountSaveButton();
    }

    // --- İndirim listesi ve iptal adımları ---

    @When("Finans kartinda Indirim listesi penceresini acar")
    public void finans_kartinda_indirim_listesi_penceresini_acar() {
        discountPage.openDiscountListPanel();
    }

    @When("Finans kartinda Indirim listesinde {string} tutarli bugunun tarihli kaydin bulundugu satir icin uc nokta menusunden Iptal Et secenegini secer")
    public void finans_kartinda_indirim_listesinde_tutarli_bugunun_tarihli_kaydin_bulundugu_satir_icin_uc_nokta_menusunden_iptal_et_secer(
            String amount) {
        discountPage.cancelDiscountForToday(amount);
    }

    @Then("Finans kartinda Indirim listesinde {string} tutarli bugunun tarihli kaydin silindigi dogrulanir")
    public void finans_kartinda_indirim_listesinde_tutarli_bugunun_tarihli_kaydin_silindigi_dogrulanir(
            String amount) {
        discountPage.verifyDiscountDeletedForToday(amount);
    }
}

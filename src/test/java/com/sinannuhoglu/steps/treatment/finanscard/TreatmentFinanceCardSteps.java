package com.sinannuhoglu.steps.treatment.finanscard;

import com.sinannuhoglu.pages.treatment.finanscard.TreatmentFinanceCardPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

/**
 * Tıbbi İşlemler > Finans Kartı
 */
public class TreatmentFinanceCardSteps {

    private final TreatmentFinanceCardPage financeCardPage = new TreatmentFinanceCardPage();

    // ==================== HİZMET ÖĞELERİ PENCERESİ ====================

    @And("Finans kartında Hizmet Öğeleri penceresini açar")
    public void finans_kartinda_hizmet_ogeleri_penceresini_acar() {
        financeCardPage.openServiceItemsDialog();
    }

    @And("Hizmet Öğeleri penceresinde Hizmetler sekmesine tıklar")
    public void hizmet_ogeleri_penceresinde_hizmetler_sekmesine_tiklar() {
        financeCardPage.selectServicesTab();
    }

    @And("Hizmet Öğeleri penceresinde {string} hizmetini arayarak seçer")
    public void hizmet_ogeleri_penceresinde_hizmeti_arayarak_secer(String serviceName) {
        financeCardPage.searchAndSelectServiceByName(serviceName);
    }

    @And("Hizmet Öğeleri penceresinde eklenen hizmet satırı silinir")
    public void hizmet_ogeleri_penceresinde_eklenen_hizmet_satiri_silinir() {
        financeCardPage.deleteFirstServiceRowIfExists();
    }

    @And("Hizmet Öğeleri penceresinde {string} hizmeti tekrar eklenir ve tarih-saat bilgisi alınır")
    public void hizmet_ogeleri_penceresinde_hizmet_tekrar_eklenir_ve_tarih_saat_bilgisi_alinir(String serviceName) {
        financeCardPage.addServiceAgainAndCaptureDateTime(serviceName);
    }

    @And("Hizmet Öğeleri penceresinde Kaydet butonuna tıklar")
    public void hizmet_ogeleri_penceresinde_kaydet_butonuna_tiklar() {
        financeCardPage.clickServiceDialogSave();
    }

    @Then("Finans kartında vizit içerisindeki hizmet listesinde {string} hizmeti ve ilgili tarih-saat bilgisi görünmelidir")
    public void finans_kartinda_vizit_icerisindeki_hizmet_listesinde_hizmet_ve_ilgili_tarih_saat_bilgisi_gorunmelidir(
            String serviceName) {

        Assert.assertTrue(
                financeCardPage.isServiceShownInVisitWithLastDateTime(serviceName),
                "Finans kartında vizit içi hizmet listesinde beklenen hizmet ve/veya tarih-saat bilgisi bulunamadı."
        );
    }

    // ==================== VİZİT İÇİ DÜZENLE & İPTAL ====================

    @And("Finans kartında vizit içerisindeki hizmet satırında {string} için Servis Öğesini Düzenle seçeneği seçilir")
    public void finans_kartinda_vizit_icindeki_hizmet_satirinda_servis_ogesini_duzenle_seceneği_secilir(
            String serviceName) {

        financeCardPage.openEditServiceFromVisitActions(serviceName);
    }

    @And("Finans kartında Servis Öğesini Düzenle penceresinde Kaydet butonuna tıklar")
    public void finans_kartinda_servis_ogesini_duzenle_penceresinde_kaydet_butonuna_tiklar() {
        financeCardPage.clickEditServiceDialogSave();
    }

    @And("Finans kartında vizit içerisindeki hizmet satırında {string} için Servis Öğesini İptal Et seçeneği seçilir")
    public void finans_kartinda_vizit_icindeki_hizmet_satirinda_servis_ogesini_iptal_et_seceneği_secilir(
            String serviceName) {

        financeCardPage.openCancelServiceFromVisitActions(serviceName);
    }

    @And("Finans kartında Servis Öğesini İptal Et onay penceresinde Evet butonuna tıklar")
    public void finans_kartinda_servis_ogesini_iptal_et_onay_penceresinde_evet_butonuna_tiklar() {
        financeCardPage.clickCancelConfirmYes();
    }

    @Then("Finans kartında vizit içerisindeki hizmet listesinde {string} hizmet kaydının silindiği doğrulanır")
    public void finans_kartinda_vizit_icerisindeki_hizmet_listesinde_hizmet_kaydinin_silindigi_dogrulanir(
            String serviceName) {

        Assert.assertTrue(
                financeCardPage.isServiceRemovedFromVisit(serviceName),
                "Finans kartında vizit içi hizmet listesinde beklenen hizmet kaydı hala görünüyor."
        );
    }
}

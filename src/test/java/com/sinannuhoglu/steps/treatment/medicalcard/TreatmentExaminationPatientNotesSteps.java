package com.sinannuhoglu.steps.treatment.medicalcard;

import com.sinannuhoglu.pages.treatment.medicalcard.TreatmentExaminationPatientNotesPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

/**
 * Muayene sayfası - Hasta Notu adımları
 */
public class TreatmentExaminationPatientNotesSteps {

    private final TreatmentExaminationPatientNotesPage patientNotesPage =
            new TreatmentExaminationPatientNotesPage();

    private String lastEnteredNoteText;

    // ================== PANEL AÇILIŞI ==================

    @And("Muayene sayfasında Hasta Notu penceresini açar")
    public void muayene_sayfasinda_hasta_notu_penceresini_acar() {
        patientNotesPage.openPatientNotePanel();
    }

    // ================== NOT OLUŞTURMA & GÜNCELLEME ==================

    @And("Muayene sayfasında Hasta Notu alanına {string} hasta notu girer")
    public void muayene_sayfasinda_hasta_notu_alanina_hasta_notu_girer(String noteText) {
        lastEnteredNoteText = noteText;
        patientNotesPage.typePatientNote(noteText);
    }

    @And("Muayene sayfasında Hasta Notu penceresinde Kaydet butonuna tıklar")
    public void muayene_sayfasinda_hasta_notu_penceresinde_kaydet_butonuna_tiklar() {
        patientNotesPage.clickSavePatientNote();
    }

    @And("Muayene sayfasında Hasta Notu penceresinde Güncelle butonuna tıklar")
    public void muayene_sayfasinda_hasta_notu_penceresinde_guncelle_butonuna_tiklar() {
        patientNotesPage.clickUpdatePatientNote();
    }


    // ================== DOĞRULAMA ==================

    @Then("Muayene sayfasında hasta notları listesinde girilen notun oluşturulduğu doğrulanır")
    public void muayene_sayfasinda_hasta_notlari_listesinde_girilen_notun_olusturuldugu_dogrulanir() {
        Assert.assertTrue(
                patientNotesPage.isPatientNoteVisible(lastEnteredNoteText),
                "Hasta notları listesinde beklenen not bulunamadı: " + lastEnteredNoteText
        );
    }

    @Then("Muayene sayfasında hasta notuna ait tarih bilgisinin bugünün tarihi olduğu doğrulanır")
    public void muayene_sayfasinda_hasta_notuna_ait_tarih_bilgisinin_bugunun_tarihi_oldugu_dogrulanir() {
        Assert.assertTrue(
                patientNotesPage.isTodayDateOrAtLeastNoteVisible(lastEnteredNoteText),
                "Hasta notu kartında bugüne ait tarih veya en azından not kaydı bulunamadı."
        );
    }

    @Then("Muayene sayfasında hasta notları listesinde {string} metnine ait kaydın güncellendiği doğrulanır")
    public void muayene_sayfasinda_hasta_notlari_listesinde_metnine_ait_kaydın_guncellendigi_dogrulanir(String updatedText) {
        Assert.assertTrue(
                patientNotesPage.isPatientNoteVisible(updatedText),
                "Güncellenen hasta notu listede bulunamadı: " + updatedText
        );
    }

    @Then("Muayene sayfasında hasta notları listesinde {string} metnine ait kaydın silindiği doğrulanır")
    public void muayene_sayfasinda_hasta_notlari_listesinde_metnine_ait_kaydın_silindigi_dogrulanir(String deletedText) {
        Assert.assertTrue(
                patientNotesPage.isPatientNoteNotVisible(deletedText),
                "Silinmiş olması beklenen hasta notu hâlâ listede görünüyor: " + deletedText
        );
    }

    // ================== DÜZENLEME & SİLME BUTONLARI ==================

    @And("Muayene sayfasında hasta notları listesinde {string} metnine ait düzenleme butonuna tıklar")
    public void muayene_sayfasinda_hasta_notlari_listesinde_metnine_ait_duzenleme_butonuna_tiklar(String noteText) {
        patientNotesPage.openEditPanelForNote(noteText);
    }

    @And("Muayene sayfasında hasta notları listesinde {string} metnine ait silme butonuna tıklar")
    public void muayene_sayfasinda_hasta_notlari_listesinde_metnine_ait_silme_butonuna_tiklar(String noteText) {
        patientNotesPage.deleteNoteByText(noteText);
    }
}

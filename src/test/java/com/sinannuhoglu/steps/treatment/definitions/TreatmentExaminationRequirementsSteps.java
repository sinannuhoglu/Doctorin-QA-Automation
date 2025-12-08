package com.sinannuhoglu.steps.treatment.definitions;

import com.sinannuhoglu.core.DriverFactory;
import com.sinannuhoglu.pages.treatment.definitions.TreatmentExaminationRequirementsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

/**
 * Muayene Zorunlu Alanlar ekranı step definition sınıfı
 */
public class TreatmentExaminationRequirementsSteps {

    private final TreatmentExaminationRequirementsPage examReqPage;

    public TreatmentExaminationRequirementsSteps() {
        this.examReqPage = new TreatmentExaminationRequirementsPage(DriverFactory.getDriver());
    }

    @Given("Muayene Zorunlu Alanlar modülüne gider")
    public void muayene_zorunlu_alanlar_modulune_gider() {
        examReqPage.goToExaminationRequirements();
    }

    @Given("Muayene Zorunlu Alanlar ekranında Yeni Ekle butonuna tıklar")
    public void muayene_zorunlu_alanlar_ekraninda_yeni_ekle_butonuna_tiklar() {
        examReqPage.openNewRequirementForm();
    }

    @Given("Muayene Zorunlu Alanlar ekranında Şube dropdownundan {string} seçeneği seçer")
    public void muayene_zorunlu_alanlar_ekraninda_sube_dropdownundan_secenegi_secer(String branchName) {
        examReqPage.selectBranch(branchName);
    }

    @Given("Muayene Zorunlu Alanlar ekranında Alan dropdownundan {string} seçeneği seçer")
    public void muayene_zorunlu_alanlar_ekraninda_alan_dropdownundan_secenegi_secer(String fieldName) {
        examReqPage.selectField(fieldName);
    }

    @Given("Muayene Zorunlu Alanlar ekranında Departmanlar alanından {string} departmanı seçer")
    public void muayene_zorunlu_alanlar_ekraninda_departmanlar_alanindan_departmani_secer(String departmentName) {
        examReqPage.selectDepartment(departmentName);
    }

    @Given("Muayene Zorunlu Alanlar ekranında Kaydet butonuna tıklar")
    public void muayene_zorunlu_alanlar_ekraninda_kaydet_butonuna_tiklar() {
        examReqPage.clickSave();
    }

    @Given("Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresi açılır")
    public void muayene_zorunlu_alanlar_ekraninda_detayli_arama_penceresi_acilir() {
        examReqPage.openDetailedSearch();
    }

    @Given("Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar")
    public void muayene_zorunlu_alanlar_ekraninda_detayli_arama_penceresinde_temizle_butonuna_tiklar() {
        examReqPage.clickDetailedSearchClear();
    }

    @Given("Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar")
    public void muayene_zorunlu_alanlar_ekraninda_detayli_arama_penceresinde_uygula_butonuna_tiklar() {
        examReqPage.clickDetailedSearchApply();
    }

    @Given("Muayene Zorunlu Alanlar ekranında ara alanına {string} değeri yazılarak arama yapılır")
    public void muayene_zorunlu_alanlar_ekraninda_ara_alanina_degeri_yazilarak_arama_yapilir(String fieldName) {
        examReqPage.searchOnToolbar(fieldName);
    }

    @Then("Muayene Zorunlu Alanlar ekranında gridde Alan bilgisi {string}, Şube bilgisi {string} ve Departman bilgisi {string} olan kaydın listelendiğini doğrular")
    public void muayene_zorunlu_alanlar_ekraninda_gridde_kaydin_listelendigini_dogrular(
            String fieldName,
            String branchName,
            String departmentName
    ) {
        boolean exists = examReqPage.isRecordDisplayedInGrid(fieldName, branchName, departmentName);
        Assert.assertTrue(
                exists,
                "Kayıt gridde bulunamadı. Alan=" + fieldName +
                        ", Şube=" + branchName +
                        ", Departman=" + departmentName
        );
    }

    @Given("Muayene Zorunlu Alanlar ekranında Alan bilgisi {string}, Şube bilgisi {string} ve Departman bilgisi {string} olan kaydın Düzenle menüsünü açar")
    public void muayene_zorunlu_alanlar_ekraninda_kaydin_duzenle_menusunu_acar(
            String fieldName,
            String branchName,
            String departmentName
    ) {
        examReqPage.openEditMenuForRecord(fieldName, branchName, departmentName);
    }

    @Given("Muayene Zorunlu Alanlar ekranında Düzenle penceresinde Şube dropdownundan {string} seçeneği seçer")
    public void muayene_zorunlu_alanlar_ekraninda_duzenle_penceresinde_sube_dropdownundan_secenegi_secer(
            String newBranch
    ) {
        examReqPage.selectBranch(newBranch);
    }

    @Given("Muayene Zorunlu Alanlar ekranında Düzenle penceresinde Alan dropdownundan {string} seçeneği seçer")
    public void muayene_zorunlu_alanlar_ekraninda_duzenle_penceresinde_alan_dropdownundan_secenegi_secer(
            String newField
    ) {
        examReqPage.selectField(newField);
    }

    @Given("Muayene Zorunlu Alanlar ekranında Düzenle penceresinde Departmanlar alanında {string} departmanının seçimini kaldırıp {string} departmanını seçer")
    public void muayene_zorunlu_alanlar_ekraninda_duzenle_penceresinde_departmanlar_gunceller(
            String deselectDepartment,
            String selectDepartment
    ) {
        examReqPage.updateDepartments(deselectDepartment, selectDepartment);
    }

    @Given("Muayene Zorunlu Alanlar ekranında Alan bilgisi {string}, Şube bilgisi {string} ve Departman bilgisi {string} olan kaydın Sil menüsünü açar")
    public void muayene_zorunlu_alanlar_ekraninda_kaydin_sil_menusunu_acar(
            String fieldName,
            String branchName,
            String departmentName
    ) {
        examReqPage.openDeleteMenuForRecord(fieldName, branchName, departmentName);
    }

    @Given("Muayene Zorunlu Alanlar ekranında Sil onay penceresinde Evet butonuna tıklar")
    public void muayene_zorunlu_alanlar_ekraninda_sil_onay_penceresinde_evet_butonuna_tiklar() {
        examReqPage.confirmDelete();
    }

    @Then("Muayene Zorunlu Alanlar ekranında gridde Alan bilgisi {string}, Şube bilgisi {string} ve Departman bilgisi {string} olan kaydın listelenmediğini doğrular")
    public void muayene_zorunlu_alanlar_ekraninda_gridde_kaydin_listelenmedigini_dogrular(
            String fieldName,
            String branchName,
            String departmentName
    ) {
        boolean exists = examReqPage.isRecordDisplayedInGrid(fieldName, branchName, departmentName);
        Assert.assertFalse(
                exists,
                "Kayıt hala gridde görünüyor. Alan=" + fieldName +
                        ", Şube=" + branchName +
                        ", Departman=" + departmentName
        );
    }
}

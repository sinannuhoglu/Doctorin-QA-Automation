package com.sinannuhoglu.steps.appointment.definitions.resources;

import com.sinannuhoglu.core.TestDataReader;
import com.sinannuhoglu.pages.appointment.definitions.resources.AppointmentResourcesAppointmentTypePage;
import io.cucumber.java.en.And;

public class AppointmentResourcesAppointmentTypeSteps {

    private final AppointmentResourcesAppointmentTypePage page =
            new AppointmentResourcesAppointmentTypePage();

    // ================== NAVIGATION ==================

    @And("Randevu Kaynakları modülüne gider")
    public void randevu_kaynaklari_modulune_gider() {
        String url = TestDataReader.get("appointmentResourcesUrl");
        page.goToAppointmentResources(url);
    }

    // ================== GRID ARAMA ==================

    @And("Randevu Kaynakları ekranında ara alanına {string} değeri yazılarak arama yapılır")
    public void randevu_kaynaklari_ara_alanina_degeri_yazilarak_arama_yapilir(String text) {
        page.searchOnToolbar(text);
    }

    @And("Randevu Kaynakları ekranında gridde ilk sütunda {string} değerine sahip kaydı bulur")
    public void randevu_kaynaklari_gridde_ilk_sutunda_degerine_sahip_kaydi_bulur(String resourceName) {
        page.ensureResourceRowExists(resourceName);
    }

    @And("Randevu Kaynakları ekranında bu kaydın Durum bilgisinin aktif olduğundan emin olur")
    public void randevu_kaynaklari_ekraninda_bu_kaydin_durum_bilgisinin_aktif_oldugundan_emin_olur() {
        page.ensureResourceStatusActive();
    }

    @And("Randevu Kaynakları ekranında bu kaydın Düzenle butonuna tıklar")
    public void randevu_kaynaklari_ekraninda_bu_kaydin_duzenle_butonuna_tiklar() {
        page.clickEditButtonForSelectedResource();
    }

    // ================== RANDEVU TİPİ SEKME İŞLEMLERİ ==================

    @And("Randevu Kaynakları ekranında açılan pencerede Randevu Tipi sekmesinin seçili olduğundan emin olur")
    public void randevu_kaynaklari_ekraninda_randevu_tipi_sekmesinin_secililigini_kontrol_eder() {
        page.ensureAppointmentTypeTabSelected();
    }

    @And("Randevu Kaynakları ekranında Randevu Tipi sekmesinde {string} branş satırını genişletir")
    public void randevu_kaynaklari_ekraninda_randevu_tipi_sekmesinde_brans_satirini_genisletir(String branchName) {
        page.expandBranchRow(branchName);
    }

    @And("Randevu Kaynakları ekranında {string} branşı altında Ad alanı {string} olan satırı bulur")
    public void randevu_kaynaklari_ekraninda_bransi_altinda_ad_alani_olan_satiri_bulur(
            String branchName,
            String appointmentTypeName
    ) {
        page.findAppointmentTypeRow(branchName, appointmentTypeName);
    }

    @And("Randevu Kaynakları ekranında bu {string} satırındaki Durum alanını aktif hale getirir")
    public void randevu_kaynaklari_ekraninda_bu_satirdaki_durum_alanini_aktif_hale_getirir(String appointmentTypeName) {
        page.ensureAppointmentTypeStatusActive(appointmentTypeName);
    }

    @And("Randevu Kaynakları ekranında bu {string} satırındaki Süre alanına {int} değeri girer")
    public void randevu_kaynaklari_ekraninda_bu_satirdaki_sure_alanina_degeri_girer(
            String appointmentTypeName,
            int duration
    ) {
        page.setAppointmentTypeDuration(appointmentTypeName, duration);
    }

    @And("Randevu Kaynakları ekranında bu {string} satırındaki Kaynak alanında {string} seçeneğinin işaretli olduğundan emin olur")
    public void randevu_kaynaklari_ekraninda_bu_satirdaki_kaynak_alaninda_seceneginin_isaretli_oldugundan_emin_olur(
            String appointmentTypeName,
            String optionText
    ) {
        page.openSourcesDropdownForSelectedAppointmentTypeRow();

        page.ensureAppointmentTypeSelectAllChecked(appointmentTypeName, optionText);
    }

    @And("Randevu Kaynakları ekranında Randevu Tipi penceresinde Kaydet butonuna tıklar")
    public void randevu_kaynaklari_ekraninda_randevu_tipi_penceresinde_kaydet_butonuna_tiklar() {
        page.clickSaveOnAppointmentTypeDialog();
    }
}

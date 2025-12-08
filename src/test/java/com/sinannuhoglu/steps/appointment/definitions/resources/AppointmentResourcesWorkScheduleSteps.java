package com.sinannuhoglu.steps.appointment.definitions.resources;

import com.sinannuhoglu.pages.appointment.definitions.resources.AppointmentResourcesWorkSchedulePage;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static com.sinannuhoglu.core.DriverFactory.getDriver;

/**
 * Randevu Kaynakları > Çalışma Takvimi sekmesi için Step Definitions
 */
public class AppointmentResourcesWorkScheduleSteps {

    private final AppointmentResourcesWorkSchedulePage workSchedulePage;

    public AppointmentResourcesWorkScheduleSteps() {
        WebDriver driver = getDriver();
        this.workSchedulePage = new AppointmentResourcesWorkSchedulePage(driver);
    }

    // ----------------------------------------------------
    // SEKME & GÜN HÜCRESİ
    // ----------------------------------------------------

    @When("Randevu Kaynakları ekranında kaynak detayında {string} sekmesine tıklar")
    public void randevuKaynaklariEkranindaKaynakDetayindaSekmesineTiklar(String tabName) {
        if ("Çalışma Takvimi".equalsIgnoreCase(tabName.trim())) {
            workSchedulePage.openWorkScheduleTab();
        } else {
            throw new IllegalArgumentException("Desteklenmeyen sekme adı: " + tabName);
        }
    }

    @When("Çalışma Takvimi sekmesinde {string} gününe ait hücreyi seçer")
    public void calismaTakvimiSekmesindeGununeAitHucresiniSecer(String dayName) {
        workSchedulePage.selectDayCell(dayName);
    }

    // ----------------------------------------------------
    // TAKVİM PLANI DİALOGU – ALAN İŞLEMLERİ
    // ----------------------------------------------------

    @When("Çalışma Takvimi penceresinde Başlangıç Saati alanından {string} saatini seçer")
    public void calismaTakvimiPenceresindeBaslangicSaatiAlanindanSaatiniSecer(String time) {
        workSchedulePage.selectStartTime(time);
    }

    @When("Çalışma Takvimi penceresinde Bitiş Saati alanından {string} saatini seçer")
    public void calismaTakvimiPenceresindeBitisSaatiAlanindanSaatiniSecer(String time) {
        workSchedulePage.selectEndTime(time);
    }

    @When("Çalışma Takvimi penceresinde Şube alanında {string} seçeneğini seçer")
    public void calismaTakvimiPenceresindeSubeAlanindaSeceneginiSecer(String branchName) {
        workSchedulePage.selectBranch(branchName);
    }

    @When("Çalışma Takvimi penceresinde Randevu Tipi alanında {string} seçeneklerini seçer")
    public void calismaTakvimiPenceresindeRandevuTipiAlanindaSecenekleriniSecer(String optionsCsv) {
        workSchedulePage.selectAppointmentTypesByCsv(optionsCsv);
    }

    @When("Çalışma Takvimi penceresinde Platform alanında {string} seçeneklerini seçer")
    public void calismaTakvimiPenceresindePlatformAlanindaSecenekleriniSecer(String optionsCsv) {
        workSchedulePage.selectPlatformsByCsv(optionsCsv);
    }

    @When("Çalışma Takvimi penceresinde Departmanlar alanında {string} seçeneklerini seçer")
    public void calismaTakvimiPenceresindeDepartmanlarAlanindaSecenekleriniSecer(String optionsCsv) {
        workSchedulePage.selectDepartmentsByCsv(optionsCsv);
    }

    /**
     * Bu step:
     *  - Kaydet butonuna tıklar.
     *  - Eğer aynı gün + başlangıç saatinde çakışan bir plan yoksa,
     *      dialog normal şekilde kapanır ve işlem biter.
     *  - Eğer sistem "farklı takvim planı bulunmaktadır" benzeri bir uyarı/çakışma toast'ı gösterip
     *      dialogu kapatmıyorsa, Page Object içindeki mantık devreye girer:
     *        1) Açık dialogda İptal'e basar,
     *        2) Grid'de ilgili hücreyi açıp mevcut planı SİLER,
     *        3) Aynı gün + saat için dialogu tekrar açar,
     *        4) Feature'da seçilen son değerleri (saat, şube, randevu tipi, platform, departman)
     *           tekrar set eder,
     *        5) Son kez Kaydet'e basar ve dialogun kapanmasını zorunlu tutmaya çalışır.
     */
    @When("Çalışma Takvimi penceresinde Kaydet butonuna tıklar")
    public void calismaTakvimiPenceresindeKaydetButonunaTiklar() {
        workSchedulePage.clickSaveInWorkScheduleDialog();
    }
}

# language: tr
@treatment @treatmentFinanceCard
Özellik: Tıbbi İşlemler > Finans Kartı (Hizmet Öğeleri, Tedavi Planı, Avans ve İndirim işlemleri)

  Geçmiş:
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi modülüne gider
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanına tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanından Bu Yıl seçeneğini seçer
    Ve Tıbbi İşlemler İş Listesi ekranında filtre butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında arama alanına "HASTANUR İYİLEŞMEZ" hasta adı girilir ve aranır
    O zaman Tıbbi İşlemler İş Listesi ekranında grid üzerinde "HASTANUR İYİLEŞMEZ" adlı hastanın listelendiği doğrulanır
    Ve Tıbbi İşlemler İş Listesi ekranında grid üzerindeki ilk satır için Detay butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında açılan detay sayfasında Finans sekmesine tıklar

  Senaryo: Finans kartında hizmet oluşturma, silme, tekrar ekleme, vizitte doğrulama ve kaydın iptali
    Ve Finans kartında Hizmet Öğeleri penceresini açar
    Ve Hizmet Öğeleri penceresinde Hizmetler sekmesine tıklar
    Ve Hizmet Öğeleri penceresinde "Açlık Kan Şekeri" hizmetini arayarak seçer
    Ve Hizmet Öğeleri penceresinde eklenen hizmet satırı silinir
    Ve Hizmet Öğeleri penceresinde "Açlık Kan Şekeri" hizmeti tekrar eklenir ve tarih-saat bilgisi alınır
    Ve Hizmet Öğeleri penceresinde Kaydet butonuna tıklar
    O zaman Finans kartında vizit içerisindeki hizmet listesinde "Açlık Kan Şekeri" hizmeti ve ilgili tarih-saat bilgisi görünmelidir
    Ve Finans kartında vizit içerisindeki hizmet satırında "Açlık Kan Şekeri" için Servis Öğesini Düzenle seçeneği seçilir
    Ve Finans kartında Servis Öğesini Düzenle penceresinde Kaydet butonuna tıklar
    Ve Finans kartında vizit içerisindeki hizmet satırında "Açlık Kan Şekeri" için Servis Öğesini İptal Et seçeneği seçilir
    Ve Finans kartında Servis Öğesini İptal Et onay penceresinde Evet butonuna tıklar
    O zaman Finans kartında vizit içerisindeki hizmet listesinde "Açlık Kan Şekeri" hizmet kaydının silindiği doğrulanır

  @TreatmentPlan @FinanceCard
  Senaryo: Finans kartında tedavi planı oluşturma, indirim ekleme ve fatura oluşturma
    Ve Finans kartında Hizmet Öğeleri penceresini açar
    Ve Hizmet Öğeleri penceresinde Hizmetler sekmesine tıklar
    Ve Hizmet Öğeleri penceresinde "Açlık Kan Şekeri" hizmetini arayarak seçer
    Ve Hizmet Öğeleri penceresinde Kaydet butonuna tıklar
    Ve Finans kartında Tedavi Planları panelini açar
    Ve Finans kartında Tedavi Planları panelinde "Açlık Kan Şekeri" servisi için "Müşteri Özel" adlı 10 oranında indirimli tedavi planı ekler
    Ve Finans kartında açılan Fatura penceresinde artı ikonuna tıklar ve 3 saniye bekler
    Ve Finans kartında açılan Fatura penceresinde Kaydet butonuna tıklar
    O zaman Finans kartında Tedavi Planı penceresinde "Müşteri Özel" plan satırı için "Faturalandı" statüsünün göründüğü doğrulanır

  @Advance @FinanceCard
  Senaryo: Finans kartında avans ekleme, görüntüleme ve iptal etme
    Ve Finans kartında Avans penceresini açar
    Ve Finans kartında Avans penceresinde "500" tutarı ve "Test avans açıklaması" açıklaması ile avans kaydı oluşturur
    Ve Finans kartında Avans listesinde "500" tutarlı ve "Test avans açıklaması" açıklamalı kaydın bulunduğu satır için üç nokta menüsünden İptal Et seçeneğini seçer
    O zaman Finans kartında Avans listesinde "500" tutarlı ve "Test avans açıklaması" açıklamalı kaydın silindiği doğrulanır

  @treatment @treatment_finance_card @treatment_finance_card_discount
  Senaryo: Finans kartında indirim ekleme, görüntüleme ve iptal etme
    Ve Finans kartinda Indirim penceresini acar
    Ve Finans kartinda Indirim penceresinde Indirim Yontemi olarak Tutar secenegini secer
    Ve Finans kartinda Indirim penceresinde "200" tutarini girer
    Ve Finans kartinda Indirim penceresinde degeri guncellemek icin Tutar secenegini tekrar secer
    Ve Finans kartinda Indirim penceresinde Kaydet butonuna tiklar
    Ve Finans kartinda Indirim listesi penceresini acar
    Ve Finans kartinda Indirim listesinde "200" tutarli bugunun tarihli kaydin bulundugu satir icin uc nokta menusunden Iptal Et secenegini secer
    O zaman Finans kartinda Indirim listesinde "200" tutarli bugunun tarihli kaydin silindigi dogrulanir

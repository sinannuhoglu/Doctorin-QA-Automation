# language: tr
@treatment @treatmentExaminationPatientHistory @treatmentExaminationPatientHistoryAllergies
Özellik: Tıbbi İşlemler > İş Listesi > Muayene - Hasta Özgeçmişi (Alerjiler)

  Geçmiş:
    # LOGIN → TIBBİ İŞLEMLER İŞ LİSTESİ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi modülüne gider

    # TARİH ARALIĞI SEÇİMİ
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanına tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanından Bu Yıl seçeneğini seçer

    # FİLTRE VE ARAMA
    Ve Tıbbi İşlemler İş Listesi ekranında filtre butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında arama alanına "HASTANUR İYİLEŞMEZ" hasta adı girilir ve aranır
    O zaman Tıbbi İşlemler İş Listesi ekranında grid üzerinde "HASTANUR İYİLEŞMEZ" adlı hastanın listelendiği doğrulanır

    # HASTA DETAYINA GİDİŞ (Muayene sayfası)
    Ve Tıbbi İşlemler İş Listesi ekranında grid üzerindeki ilk satır için Detay butonuna tıklar

    # HASTA ÖZGEÇMİŞİ PENCERESİ
    Ve Muayene sayfasında Hasta Özgeçmişi penceresini açar

  Senaryo: Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında kayıt oluşturulması ve silinmesi

    # Alerji kaydının oluşturulması
    Ve Muayene sayfasında Hasta Özgeçmişi penceresinde Alerjiler alanı için yeni kayıt ekleme penceresini açar
    Ve Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında "Mevsimsel" alerji tipi, "Polen" alerjen ve "Polen alerjisi mevcut." açıklama girer
    Ve Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında Ekle butonuna tıklar

    # Alerji kaydının doğrulanması ve silinmesi
    O zaman Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında "Polen" alerjeni için "Polen alerjisi mevcut." açıklamasıyla kaydın oluşturulduğu doğrulanır
    Ve Muayene sayfasında Hasta Özgeçmişi Alerjiler alanında "Polen" alerjeni için sil butonuna tıklar

# language: tr
@treatment @treatmentExaminationBodyMeasurements
Özellik: Tıbbi İşlemler > Muayene > Vücut Ölçümleri

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

  Senaryo: Muayene sayfasında Vücut Ölçümleri kaydının oluşturulması ve geçmişte doğrulanması

    # VÜCUT ÖLÇÜMLERİ PENCERESİ AÇILIŞI
    Ve Muayene sayfasında Vücut Ölçümleri penceresini açar

    # BUGÜNE AİT KAYIT VARSA SİLİNMESİ
    Ve Muayene Vücut Ölçümleri penceresinde bugüne ait bir kayıt varsa geçmişten silinir

    # YENİ BOY / KİLO KAYDI OLUŞTURMA
    Ve Muayene Vücut Ölçümleri penceresinde Boy değeri "180" olarak girilir
    Ve Muayene Vücut Ölçümleri penceresinde Kilo değeri "70" olarak girilir
    Ve Muayene Vücut Ölçümleri penceresinde Kaydet butonuna tıklar

    # GEÇMİŞTE DOĞRULAMA
    Ve Muayene sayfasında Vücut Ölçümleri penceresini tekrar açar
    O zaman Muayene Vücut Ölçümleri geçmiş alanında bugüne ait kaydın oluştuğu doğrulanır

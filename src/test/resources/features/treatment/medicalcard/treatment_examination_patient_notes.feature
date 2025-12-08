# language: tr
@treatment @treatmentExaminationPatientNotes
Özellik: Tıbbi İşlemler > İş Listesi > Muayene - Hasta Notu

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

  Senaryo: Muayene sayfasında hasta notunun oluşturulması, güncellenmesi ve silinmesi

    # 1) HASTA NOTU OLUŞTURMA
    Ve Muayene sayfasında Hasta Notu penceresini açar
    Ve Muayene sayfasında Hasta Notu alanına "HASTANUR İYİLEŞMEZ için kontrol amaçlı hasta notu eklenmiştir." hasta notu girer
    Ve Muayene sayfasında Hasta Notu penceresinde Kaydet butonuna tıklar

    # 2) OLUŞAN NOTUN VE TARİHİN DOĞRULANMASI
    O zaman Muayene sayfasında hasta notları listesinde girilen notun oluşturulduğu doğrulanır
    Ve Muayene sayfasında hasta notuna ait tarih bilgisinin bugünün tarihi olduğu doğrulanır
    # Not: Tarih alanı bulunamazsa step, sadece not metninin oluşturulduğunu doğrulayarak
    # yine başarıyla tamamlanacaktır.

    # 3) NOTUN SİLİNMESİ
    Ve Muayene sayfasında hasta notları listesinde "HASTANUR İYİLEŞMEZ için kontrol amaçlı hasta notu eklenmiştir." metnine ait silme butonuna tıklar
    O zaman Muayene sayfasında hasta notları listesinde "HASTANUR İYİLEŞMEZ için kontrol amaçlı hasta notu eklenmiştir." metnine ait kaydın silindiği doğrulanır

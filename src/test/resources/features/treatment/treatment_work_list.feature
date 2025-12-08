# language: tr
@treatment @treatmentWorkList
Özellik: Tıbbi İşlemler > İş Listesi

  Geçmiş:
    # LOGIN → TIBBİ İŞLEMLER İŞ LİSTESİ MODÜLÜ
  Diyelim ki kullanıcı login sayfasını görmektedir
  Ve Müşteri değiştir penceresini açar
  Ve Müşteri adını test verisinden girer
  Ve Müşteri bilgisini kaydeder
  Ve kullanıcı adı ve şifreyi test verisinden girer
  Eğer ki Giriş butonuna tıklar
  Ve Tıbbi İşlemler İş Listesi modülüne gider

  Senaryo: İş Listesinde tarih aralığının seçilmesi, filtreleme yapılması ve hastanın detayına erişilmesi

    # 1) TARİH ARALIĞI SEÇİMİ
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanına tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanından Bu Yıl seçeneğini seçer

    # 2) FİLTRELEME
    Ve Tıbbi İşlemler İş Listesi ekranında filtre butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar

    # 3) ARAMA VE GRID DOĞRULAMASI
    Ve Tıbbi İşlemler İş Listesi ekranında arama alanına "HASTANUR İYİLEŞMEZ" hasta adı girilir ve aranır
    O zaman Tıbbi İşlemler İş Listesi ekranında grid üzerinde "HASTANUR İYİLEŞMEZ" adlı hastanın listelendiği doğrulanır

    # 4) DETAY VE MUAYENE SEKME
    Ve Tıbbi İşlemler İş Listesi ekranında grid üzerindeki ilk satır için Detay butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında açılan detay sayfasında Muayene sekmesine tıklar

  Senaryo: İş Listesinde tarih aralığının seçilmesi, filtreleme yapılması ve hastanın Finans kartına erişilmesi

    # 1) TARİH ARALIĞI SEÇİMİ
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanına tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanından Bu Yıl seçeneğini seçer

    # 2) FİLTRELEME
    Ve Tıbbi İşlemler İş Listesi ekranında filtre butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar

    # 3) ARAMA VE GRID DOĞRULAMASI
    Ve Tıbbi İşlemler İş Listesi ekranında arama alanına "HASTANUR İYİLEŞMEZ" hasta adı girilir ve aranır
    O zaman Tıbbi İşlemler İş Listesi ekranında grid üzerinde "HASTANUR İYİLEŞMEZ" adlı hastanın listelendiği doğrulanır

    # 4) DETAY VE FİNANS SEKME
    Ve Tıbbi İşlemler İş Listesi ekranında grid üzerindeki ilk satır için Detay butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında açılan detay sayfasında Finans sekmesine tıklar
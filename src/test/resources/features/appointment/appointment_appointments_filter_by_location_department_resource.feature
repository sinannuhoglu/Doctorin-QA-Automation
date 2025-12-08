# language: tr
Özellik: Randevular ekranında filtreleme
  Randevular için şube, departman ve kaynak filtreleri ile bugünün randevularının listelenmesi

  Geçmiş:
    # 1) LOGIN → MÜŞTERİ SEÇİMİ VE GİRİŞ → RANDEVULAR MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Randevular modülüne gider

  Senaryo: Randevular ekranında Çankaya / KBB / Uzm. Dr. Aykan BÜYÜKAYDIN filtresi ile bugünün randevularının listelenmesi

    # 2) RANDEVU FİLTRELERİ (ŞUBE / DEPARTMAN / KAYNAK)
    Ve Randevular ekranında Filitrele butonuna tıklar
    Ve Randevular ekranında Filitrele penceresinde Şube alanında "Çankaya" seçeneğini seçer
    Ve Randevular ekranında Filitrele penceresinde Departmanlar alanında "KBB" seçeneğini seçer
    Ve Randevular ekranında Filitrele penceresinde Kaynaklar alanında "Uzm. Dr. Aykan BÜYÜKAYDIN" seçeneğini seçer
    Ve Randevular ekranında Filitrele penceresinde Kabul Et butonuna tıklar
    Eğer ki Randevular ekranında Bugün butonuna tıklar
    Ve Randevular ekranında boş bir randevu slotuna tıklar

    # 3) RANDEVU OLUŞTURMA, CHECK-IN VE HASTA KABULÜ
    Ve Randevular ekranında Hasta Arama penceresinde "HASTANUR İYİLEŞMEZ" hastasını arar
    Ve Randevular ekranında Hasta Arama sonuçlarından "HASTANUR İYİLEŞMEZ" hastasını seçer
    Ve Randevular ekranında Randevu Detayı penceresinde Kaydet butonuna tıklar
    Ve Randevular ekranında "HASTANUR İYİLEŞMEZ" hastasına ait randevu kartını açar
    Ve Randevular ekranında Randevu Detayı penceresinde Check-in butonuna tıklar
    Ve Randevular ekranında Randevular Detayı penceresinde Admission butonuna tıklar
    Ve Randevular ekranında Hasta Kabul penceresinin yüklenmesini bekler
    Ve Randevular ekranında Hasta Kabul penceresinde Kaydet butonuna tıklar

  Senaryo: Randevular ekranında Çankaya / KBB / Uzm. Dr. Aykan BÜYÜKAYDIN filtresi ile kapanış etkinliği oluşturulması ve silinmesi

    # 2) RANDEVU FİLTRELERİ (ŞUBE / DEPARTMAN / KAYNAK)
    Ve Randevular ekranında Filitrele butonuna tıklar
    Ve Randevular ekranında Filitrele penceresinde Şube alanında "Çankaya" seçeneğini seçer
    Ve Randevular ekranında Filitrele penceresinde Departmanlar alanında "KBB" seçeneğini seçer
    Ve Randevular ekranında Filitrele penceresinde Kaynaklar alanında "Uzm. Dr. Aykan BÜYÜKAYDIN" seçeneğini seçer
    Ve Randevular ekranında Filitrele penceresinde Kabul Et butonuna tıklar
    Eğer ki Randevular ekranında Bugün butonuna tıklar
    Ve Randevular ekranında boş bir randevu slotuna tıklar

    # 3) KAPANIŞ ETKİNLİĞİ OLUŞTURMA VE SİLME
    Ve Randevular ekranında Hasta Arama penceresinde Etkinlik sekmesine tıklar
    Ve Randevular ekranında Kapanış Etkinliği penceresinde Kaydet butonuna tıklar
    Ve Randevular ekranında oluşturulan kapanış etkinliği slotunu açar
    Ve Randevular ekranında Etkinlik Detayı penceresinde Sil butonuna tıklar ve Sil onay penceresinde Tamam butonuna tıklar

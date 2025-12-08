# language: tr
@appointment @appointmentResources @appointmentType
Özellik: Randevu > Tanımlar > Kaynaklar > Randevu Tipi sekmesi
  Randevu Kaynakları ekranında Randevu Tipi sekmesindeki ayarların
  ilgili doktor kaynağı için doğru yapılandırıldığını kontrol eder.

  Geçmiş:
    # 1) LOGIN → RANDEVU KAYNAKLARI MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Randevu Kaynakları modülüne gider

  Senaryo: Uzm. Dr. Aykan BÜYÜKAYDIN kaynağı için Randevu Tipi ayarlarının güncellenmesi

    # 2) KAYNAĞIN GRID ÜZERİNDEN BULUNMASI VE DURUMUN AKTİF EDİLMESİ
    Ve Randevu Kaynakları ekranında ara alanına "Uzm. Dr. Aykan BÜYÜKAYDIN" değeri yazılarak arama yapılır
    Ve Randevu Kaynakları ekranında gridde ilk sütunda "Uzm. Dr. Aykan BÜYÜKAYDIN" değerine sahip kaydı bulur
    Ve Randevu Kaynakları ekranında bu kaydın Durum bilgisinin aktif olduğundan emin olur
    Ve Randevu Kaynakları ekranında bu kaydın Düzenle butonuna tıklar

    # 3) RANDEVU TİPİ SEKME İŞLEMLERİ
    Ve Randevu Kaynakları ekranında açılan pencerede Randevu Tipi sekmesinin seçili olduğundan emin olur
    Ve Randevu Kaynakları ekranında Randevu Tipi sekmesinde "ek dahiliye" branş satırını genişletir
    Ve Randevu Kaynakları ekranında "ek dahiliye" branşı altında Ad alanı "Muayene" olan satırı bulur
    Ve Randevu Kaynakları ekranında bu "Muayene" satırındaki Durum alanını aktif hale getirir
    Ve Randevu Kaynakları ekranında bu "Muayene" satırındaki Süre alanına 40 değeri girer
    Ve Randevu Kaynakları ekranında bu "Muayene" satırındaki Kaynak alanında "WEB" seçeneğinin işaretli olduğundan emin olur
    Ve Randevu Kaynakları ekranında Randevu Tipi penceresinde Kaydet butonuna tıklar

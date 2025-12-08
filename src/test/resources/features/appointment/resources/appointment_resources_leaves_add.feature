# language: tr
@appointment_resources @leaves @ui
Özellik: Randevu kaynağı için izin tanımlama
  Randevu Kaynakları ekranında belirli bir kaynak için
  İzinler sekmesi üzerinden günlük izin eklenmesi ve silinmesi senaryoları.

  Geçmiş:
    # 1) LOGIN → RANDEVU KAYNAKLARI MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Randevu Kaynakları modülüne gider

    # 2) KAYNAĞIN GRID ÜZERİNDEN BULUNMASI VE DURUMUN AKTİF EDİLMESİ
    Ve Randevu Kaynakları ekranında ara alanına "Uzm. Dr. Aykan BÜYÜKAYDIN" değeri yazılarak arama yapılır
    Ve Randevu Kaynakları ekranında gridde ilk sütunda "Uzm. Dr. Aykan BÜYÜKAYDIN" değerine sahip kaydı bulur
    Ve Randevu Kaynakları ekranında bu kaydın Durum bilgisinin aktif olduğundan emin olur
    Ve Randevu Kaynakları ekranında bu kaydın Düzenle butonuna tıklar

  Senaryo: Randevu kaynağı için günlük izin eklenmesi
    # 3) İZİNLER SEKME VE FORM İŞLEMLERİ
    Ve İzinler sekmesine gider
    Ve İzinler ekranında Yeni Ekle butonuna tıklar
    Ve İzinler pop-up penceresinde Başlangıç Tarihi alanına "11.12.2025" değerini girer
    Ve İzinler pop-up penceresinde Başlangıç Saati alanında "08:00" saatini seçer
    Ve İzinler pop-up penceresinde Bitiş Saati alanında "18:00" saatini seçer
    Ve İzinler pop-up penceresinde Tekrar alanında "Tekrar yok" seçeneğini seçer
    Ve İzinler pop-up penceresinde Açıklama alanına "Tüm gün planlı izin" metnini yazar
    Eğer ki İzinler pop-up penceresinde Kaydet butonuna tıklar
    O zaman İzinler pop-up penceresinin kapandığını ve izinler gridinin görüntülendiğini doğrular
    Ve İzinler gridinde "11.12.2025" tarihli "08:00" - "18:00" saat aralığında "Tüm gün planlı izin" açıklamasıyla bir satır oluşturulduğunu doğrular

  Senaryo: Randevu kaynağı için günlük izin kaydının silinmesi
    # 3) İZİNLER SEKME VE OLUŞTURULAN KAYDIN SİLİNMESİ
    Ve İzinler sekmesine gider
    Ve İzinler gridinde "11.12.2025" tarihli "08:00" - "18:00" saat aralığında "Tüm gün planlı izin" açıklamasına sahip izin kaydının üç nokta menüsünden Sil seçeneğine tıklar
    Ve İzin silme onay penceresinde Evet butonuna tıklar
    O zaman İzinler gridinde "11.12.2025" tarihli "08:00" - "18:00" saat aralığında "Tüm gün planlı izin" açıklamasına sahip izin kaydının silindiğini doğrular

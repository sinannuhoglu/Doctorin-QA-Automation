# language: tr
Özellik: Randevu Kaynakları Çalışma Takvimi yönetimi
  Randevu kaynağı için çalışma takvimi planının oluşturulması

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

  Senaryo: Randevu kaynağı için Çarşamba günü çalışma takvimi planının oluşturulması
    # 3) ÇALIŞMA TAKVİMİ PLANLAMA
    Ve Randevu Kaynakları ekranında kaynak detayında "Çalışma Takvimi" sekmesine tıklar
    Ve Çalışma Takvimi sekmesinde "Çarşamba" gününe ait hücreyi seçer
    Ve Çalışma Takvimi penceresinde Başlangıç Saati alanından "08:00" saatini seçer
    Ve Çalışma Takvimi penceresinde Bitiş Saati alanından "18:00" saatini seçer
    Ve Çalışma Takvimi penceresinde Şube alanında "Çankaya" seçeneğini seçer
    Ve Çalışma Takvimi penceresinde Randevu Tipi alanında "Kontrol Muayenesi, Video Muayene, Video Kontrol, Muayene" seçeneklerini seçer
    Ve Çalışma Takvimi penceresinde Platform alanında "Doctorin, KIB, WEB, Diğer" seçeneklerini seçer
    Ve Çalışma Takvimi penceresinde Departmanlar alanında "KBB" seçeneklerini seçer
    Eğer ki Çalışma Takvimi penceresinde Kaydet butonuna tıklar

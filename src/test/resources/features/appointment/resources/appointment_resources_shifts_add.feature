# language: tr
Özellik: Randevu Kaynakları Mesailer yönetimi
  Randevu kaynağı için mesai kayıtlarının oluşturulması ve grid üzerinden doğrulanması

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

  Senaryo: Randevu kaynağı için günlük mesai kaydının eklenmesi
    # 3) MESAİ KAYDININ OLUŞTURULMASI
    Ve Randevu Kaynakları ekranında kaynak detayında "Mesailer" sekmesine tıklar
    Ve Mesailer sekmesinde Yeni Ekle butonuna tıklar
    Ve Mesailer sekmesinde açılan formda Tarih alanına "10.12.2025" değerini girer
    Ve Mesailer sekmesinde açılan formda Başlangıç Saati alanından "08:30" saatini seçer
    Ve Mesailer sekmesinde açılan formda Bitiş Saati alanından "18:30" saatini seçer
    Ve Mesailer sekmesinde açılan formda Açıklama alanına "Gün içi planlı mesai" açıklamasını girer
    Eğer ki Mesailer sekmesinde açılan formda Kaydet butonuna tıklar

    # 4) GRID ÜZERİNDEN DOĞRULAMA
    O zaman Mesailer sekmesinde grid üzerinde "10.12.2025" tarihli "08:30" - "18:30" saat aralığında "Gün içi planlı mesai" açıklamasına sahip kaydın oluşturulduğunu görür

  Senaryo: Randevu kaynağı için günlük mesai kaydının eklenmesi ve silinmesi
    # 3) OLUŞTURULAN KAYDIN SİLİNMESİ
    Ve Randevu Kaynakları ekranında kaynak detayında "Mesailer" sekmesine tıklar
    Ve Mesailer sekmesinde grid üzerinde "10.12.2025" tarihli "08:30" - "18:30" saat aralığında "Gün içi planlı mesai" açıklamasına sahip kaydın üç nokta menüsüne tıklar
    Ve Mesailer sekmesinde açılan üç nokta menüsünde "Sil" seçeneğine tıklar
    Ve Mesailer sekmesinde açılan silme onay penceresinde "Evet" butonuna tıklar
    O zaman Mesailer sekmesinde grid üzerinde "10.12.2025" tarihli "08:30" - "18:30" saat aralığında "Gün içi planlı mesai" açıklamasına sahip kaydın silindiğini görür

# language: tr
@appointment_resources @service_items @ui
Özellik: Randevu kaynağı için Servis Öğeleri tanımlama

  # Bu feature Servis Öğeleri sekmesi için servis ekleme ve silme akışlarını içerir.

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

  Senaryo: Randevu kaynağı için servis öğesi eklenmesi
    # 3) SERVİS ÖĞELERİ SEKME VE FORM İŞLEMLERİ
    Ve Servis Öğeleri sekmesine gider
    Ve Servis Öğeleri ekranında Yeni Ekle butonuna tıklar
    Ve Servis Öğeleri pop-up penceresinde Servis Öğesi alanında "EKG" seçeneğini seçer
    Ve Servis Öğeleri pop-up penceresinde Randevu Tipi alanında "Kontrol Muayenesi" seçeneğini seçer
    Eğer ki Servis Öğeleri pop-up penceresinde Kaydet butonuna tıklar
    O zaman Servis Öğeleri gridinde birinci sütunda "EKG" ve üçüncü sütunda "Kontrol Muayenesi" değerine sahip kaydın oluşturulduğunu doğrular

  Senaryo: Randevu kaynağı için servis öğesinin silinmesi
    # 3) KAYDIN SİLİNMESİ
    Ve Servis Öğeleri sekmesine gider
    Eğer ki Servis Öğeleri gridinde birinci sütunda "EKG" ve üçüncü sütunda "Kontrol Muayenesi" değerine sahip satırdaki üç nokta menüsünü açar
    Ve Servis Öğeleri gridindeki üç nokta menüsünden "Sil" seçeneğine tıklar
    Ve Silme onay penceresinde "Evet" butonuna tıklar
    O zaman Servis Öğeleri gridinde birinci sütunda "EKG" ve üçüncü sütunda "Kontrol Muayenesi" değerine sahip kaydın silindiğini doğrular

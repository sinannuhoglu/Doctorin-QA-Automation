# language: tr
@appointment_resources @official_holidays @ui
Özellik: Randevu kaynağı için Resmi Tatiller durum yönetimi

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

  Senaryo: Randevu kaynağı için 23 Nisan resmi tatilinin Durum bilgisinin aktif hale getirilmesi
    # 3) RESMİ TATİLLER SEKME VE DURUM YÖNETİMİ
    Ve Resmi Tatiller sekmesine gider
    Ve Resmi Tatiller gridinde birinci sütunda "23 Nisan" olan satırın Durum bilgisini aktif konuma getirir
    O zaman Resmi Tatiller gridinde birinci sütunda "23 Nisan" olan satırın Durum bilgisinin aktif olduğunu doğrular

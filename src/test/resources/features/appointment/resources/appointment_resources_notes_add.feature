# language: tr
@Appointment @Resources @Notes
Özellik: Randevu Kaynakları - Not eklenmesi ve silinmesi

  Randevu kaynakları için Notlar sekmesinden not eklenmesi ve silinmesi senaryoları.

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

  Senaryo: Randevu kaynağı için randevu notunun eklenmesi
    # 3) NOTLAR SEKME ALANI - YENİ NOT OLUŞTURMA
    Ve Randevu Kaynakları ekranında Notlar sekmesine tıklar
    Ve Randevu Kaynakları ekranında Notlar sekmesinde Yeni Ekle butonuna tıklar
    Ve Randevu Kaynakları ekranında açılan Not penceresinde Not Tipi alanından "Randevu Öncesi" seçeneğini seçer
    Ve Randevu Kaynakları ekranında Not penceresinde Başlangıç tarihi alanına "01-12-2025" ve Bitiş tarihi alanına "02-12-2025" değerlerini girer
    Ve Randevu Kaynakları ekranında Not penceresinde haftanın günlerinden "Pazartesi,Çarşamba,Perşembe,Cuma,Cumartesi,Pazar" seçeneklerini işaretler
    Ve Randevu Kaynakları ekranında Not penceresinde Not alanına "Hasta için randevu öncesi bilgilendirme yapılacaktır. Gerekli tetkikler ve hazırlıklar hakkında telefon ile bilgilendirme planlandı." bilgisini girer
    Eğer ki Randevu Kaynakları ekranında Not penceresinde Kaydet butonuna tıklar
    O zaman Randevu Kaynakları ekranında Notlar gridinde Tip "Randevu Öncesi", Başlangıç Tarihi "01-12-2025", Bitiş Tarihi "02-12-2025" ve Not "Hasta için randevu öncesi bilgilendirme yapılacaktır. Gerekli tetkikler ve hazırlıklar hakkında telefon ile bilgilendirme planlandı." bilgisi ile kaydın oluştuğunu doğrular

  Senaryo: Randevu kaynağı için randevu notunun silinmesi
    # 3) NOTLAR SEKME ALANI - MEVCUT NOTU SİLME
    Ve Randevu Kaynakları ekranında Notlar sekmesine tıklar
    Ve Randevu Kaynakları ekranında Notlar gridinde Tip "Randevu Öncesi", Başlangıç Tarihi "01-12-2025", Bitiş Tarihi "02-12-2025" ve Not "Hasta için randevu öncesi bilgilendirme yapılacaktır. Gerekli tetkikler ve hazırlıklar hakkında telefon ile bilgilendirme planlandı." bilgisine sahip kaydın üç nokta menüsüne tıklar
    Ve Randevu Kaynakları ekranında Notlar gridinde açılan menüden Sil seçeneğine tıklar
    Ve Randevu Kaynakları ekranında not silme uyarı penceresinde Evet butonuna tıklar
    O zaman Randevu Kaynakları ekranında Notlar gridinde Tip "Randevu Öncesi", Başlangıç Tarihi "01-12-2025", Bitiş Tarihi "02-12-2025" ve Not "Hasta için randevu öncesi bilgilendirme yapılacaktır. Gerekli tetkikler ve hazırlıklar hakkında telefon ile bilgilendirme planlandı." bilgisine sahip kaydın silindiğini doğrular

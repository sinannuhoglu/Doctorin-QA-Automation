# language: tr
@admission
Özellik: Hasta Arama modülü - Yeni hasta kaydı oluşturma
  Hasta Arama modülü üzerinden yeni hasta kaydı açılması, vizit oluşturulması
  ve kayıtların Hasta Arama ekranında doğrulanması senaryosu.

  Geçmiş:
    # 1) LOGIN → HASTA ARAMA MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Hasta Arama modülüne gider


  Senaryo: Hasta Arama ekranından yeni hasta kaydı açılması ve doğrulanması

    # 2) HASTA KABUL FORMUNDA YENİ HASTA OLUŞTURMA
    Ve Hasta Arama sayfasında Yeni Ekle butonuna tıklar
    Ve Hasta Kabul formunda Uyruk alanından "TÜRKİYE CUMHURİYETİ" seçeneğini seçer
    Ve Hasta Kabul formunda Dil alanından "Türkçe" seçeneğini seçer
    Ve Hasta Kabul formunda Kimlik Numarası alanına "12355678919" değerini girer
    Ve Hasta Kabul formunda Ad alanına "Hastanur" değerini girer
    Ve Hasta Kabul formunda Soyad alanına "Yılmaz" değerini girer
    Ve Hasta Kabul formunda Doğum Tarihi alanına "11.01.1990" tarihini girer
    Ve Hasta Kabul formunda Cinsiyet alanından "Erkek" seçeneğini seçer
    Ve Hasta Kabul formunda Cep Telefonu alanına "5551112233" değerini girer
    Ve Hasta Kabul formunda E-posta alanına "hastanur.yilmaz@doctorin.com" değerini girer
    Ve Hasta Kabul formunda Kaydet butonuna tıklar

    # 3) HASTA KABUL EKRANINDA VİZİT OLUŞTURMA
    Ve Hasta Kabul ekranında Vizit Tipi alanından "kontrol muayene" seçeneğini seçer
    Ve Hasta Kabul ekranında Departman alanından "Psikiyatri" seçeneğini seçer
    Ve Hasta Kabul ekranında Doktor alanından "Uzm. Dr. Aykan BÜYÜKAYDIN" seçeneğini seçer
    Ve Hasta Kabul ekranındaki Kaydet butonuna tıklar

    # 4) HASTA ARAMA EKRANINDA KAYDIN DOĞRULANMASI
    Ve Hasta kayıt işlemini doğrulamak için tekrar Hasta Arama ekranına gider
    Ve Hasta Arama ekranında Detaylı Arama penceresini açar
    Ve Hasta Arama ekranındaki Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Hasta Arama ekranındaki Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Hasta Arama ekranındaki arama alanına "HASTANUR YILMAZ" yazar ve arama yapar
    O zaman Hasta Arama listesindeki sonuçlarda "HASTANUR YILMAZ" kaydını görmelidir

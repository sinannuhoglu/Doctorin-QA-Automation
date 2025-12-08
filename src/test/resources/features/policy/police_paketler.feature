# language: tr
@policy
Özellik: Poliçe modülü - Tedavi Planı Paketler yönetimi
  Doctorin uygulamasında Poliçe > Tedavi Planı Paketler ekranında
  paketlerin filtrelenmesi, düzenlenmesi, silinmesi ve yeni paket oluşturulmasının doğrulanması

  Geçmiş:
    # 1) LOGIN → SİSTEME GİRİŞ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar

  Senaryo: "Kasıma Özel" paketinin arama ile filtrelenmesi

    # 2) MODÜLE GİDİŞ VE FİLTRE UYGULAMA
    Ve Poliçe Tedavi Planı Paketler modülüne gider
    Ve Poliçe Detaylı arama penceresini açar
    Ve Poliçe Detaylı arama penceresinde Temizle butonuna tıklar
    Ve Poliçe Detaylı arama penceresinde Uygula butonuna tıklar
    Ve Tedavi planı paket arama alanına "Kasıma Özel" yazar ve arama yapar

    # 3) FİLTRE SONUCUNUN DOĞRULANMASI
    O zaman Tedavi planı paket listesi "Kasıma Özel" için filtrelenmiş olmalıdır

  Senaryo: "Kasıma Özel" baz adından yeni paket oluşturulması

    # 2) MODÜLE GİDİŞ VE REFERANS KAYDIN FİLTRELENMESİ
    Ve Poliçe Tedavi Planı Paketler modülüne gider
    Ve Poliçe Detaylı arama penceresini açar
    Ve Poliçe Detaylı arama penceresinde Temizle butonuna tıklar
    Ve Poliçe Detaylı arama penceresinde Uygula butonuna tıklar
    Ve Tedavi planı paket arama alanına "Kasıma Özel" yazar ve arama yapar

    # 3) BENZERSİZ PAKET ADININ OLUŞTURULMASI
    Ve "Kasıma Özel" baz adından benzersiz paket adı oluşturur

    # 4) YENİ PAKET OLUŞTURMA
    Ve Toolbar üzerindeki Yeni Ekle butonuna tıklar
    Ve Açılan yeni kayıt penceresinde Ad alanına benzersiz paket adını girer
    Ve Yeni kayıt penceresinde Fatura Yönetimi alanına tıklar
    # Ve Açılan Fatura Yönetimi seçeneklerinden "Vizit Bazlı" seçeneğini seçer
    Ve Yeni kayıt penceresinde Para Birimi alanına tıklar
    Ve Açılan Para Birimi listesinden "USD" seçeneğini seçer
    Ve Yeni kayıt penceresinde Servis Öğesi alanına tıklar
    Ve Açılan Servis Öğesi listesinden "Açlık Kan Şekeri" seçeneğini seçer
    Ve Yeni kayıt penceresindeki Kaydet butonuna tıklar

    # 5) OLUŞTURULAN PAKETİN FİLTRELENMESİ VE DOĞRULANMASI
    Ve Tedavi planı paket arama alanına benzersiz paket adını yazar ve arama yapar
    O zaman Tedavi planı paket listesi benzersiz paket adı için filtrelenmiş olmalıdır

  Senaryo: "Kasıma Özel" paketinin düzenlenmesi

    # 2) MODÜLE GİDİŞ VE KAYDIN BULUNMASI
    Ve Poliçe Tedavi Planı Paketler modülüne gider
    Ve Poliçe Detaylı arama penceresini açar
    Ve Poliçe Detaylı arama penceresinde Temizle butonuna tıklar
    Ve Poliçe Detaylı arama penceresinde Uygula butonuna tıklar
    Ve Tedavi planı paket arama alanına "Kasıma Özel" yazar ve arama yapar

    # 3) DÜZENLE PENCERESİNİN AÇILMASI
    Ve Paket satırı üç nokta menüsüne tıklar
    Ve Açılan menüden "Düzenle" seçeneğini seçer

    # 4) PAKET BİLGİLERİNİN GÜNCELLENMESİ
    Ve Açılan düzenleme penceresinde Ad alanına "Kasıma Özel" değerini girer
    Ve Düzenleme penceresinde Fatura Yönetimi alanına tıklar
    # Ve Açılan Fatura Yönetimi seçeneklerinden "Plan Bazlı" seçeneğini seçer
    Ve Düzenleme penceresinde Para Birimi alanına tıklar
    Ve Açılan Para Birimi listesinden "USD" seçeneğini seçer
    Ve Düzenleme penceresindeki Kaydet butonuna tıklar

    # 5) GÜNCELLENEN AD İLE FİLTRE VE DOĞRULAMA
    Ve Tedavi planı paket arama alanına "Kasıma Özel" yazar ve arama yapar
    O zaman Tedavi planı paket listesi "Kasıma Özel" için filtrelenmiş olmalıdır


  Senaryo: "Kasıma Özel" paketinin silinmesi

    # 2) MODÜLE GİDİŞ VE SİLİNECEK KAYDIN BULUNMASI
    Ve Poliçe Tedavi Planı Paketler modülüne gider
    Ve Poliçe Detaylı arama penceresini açar
    Ve Poliçe Detaylı arama penceresinde Temizle butonuna tıklar
    Ve Poliçe Detaylı arama penceresinde Uygula butonuna tıklar
    Ve Tedavi planı paket arama alanına "Kasıma Özel" yazar ve arama yapar

    # 3) SİLME İŞLEMİ
    Ve Paket satırı üç nokta menüsüne tıklar
    Ve Açılan menüden "Sil" seçeneğini seçer
    Ve Açılan silme onay penceresinde Evet butonuna tıklar

    # 4) SİLME SONRASI DOĞRULAMA
    Ve Tedavi planı paket arama alanına "Kasıma Özel" yazar ve arama yapar
    O zaman Tedavi planı paket listesi boş olmalıdır
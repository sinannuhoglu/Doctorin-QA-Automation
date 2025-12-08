# language: tr
@admission @autoservice
Özellik: Hasta Kabul > Tanımlar > Otomatik Servis Öğesi Kuralları

  Senaryo: Otomatik servis öğesi kuralı eklenmesi

    # 1) LOGIN → OTOMATİK SERVİS ÖĞESİ KURALLARI MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Otomatik Servis Öğesi Kuralları modülüne gider

    # 2) YENİ KURAL EKLEME
    Ve Otomatik Servis Öğesi Kuralları ekranında Yeni Ekle butonuna tıklar
    Ve Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi alanından "EKG" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları ekranında Departman alanından "Dahiliye" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları ekranında Doktor alanından "Uzm. Dr. Aykan BÜYÜKAYDIN" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları ekranında Şube alanından "Altındağ" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları ekranında Vizit Tipi alanından "genel muayene" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları ekranında Kaydet butonuna tıklar

    # 3) GRID ÜZERİNDEN DOĞRULAMA
    Ve Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi arama alanına "EKG" yazarak kayıt arar
    O zaman Otomatik Servis Öğesi Kuralları ekranında grid üzerinde Hizmet Öğesi "EKG", Doktor "Uzm. Dr. Aykan BÜYÜKAYDIN", Departman "Dahiliye", Şube "Altındağ" ve Vizit Tipi "genel muayene" değerleri ile listelenen kaydı doğrular


  Senaryo: Otomatik servis öğesi kuralı düzenlenmesi

    # 1) LOGIN → OTOMATİK SERVİS ÖĞESİ KURALLARI MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Otomatik Servis Öğesi Kuralları modülüne gider

    # 2) MEVCUT KAYDIN BULUNMASI
    Ve Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi arama alanına "EKG" yazarak kayıt arar
    Ve Otomatik Servis Öğesi Kuralları ekranında grid üzerinde Hizmet Öğesi "EKG", Doktor "Uzm. Dr. Aykan BÜYÜKAYDIN", Departman "Dahiliye", Şube "Altındağ" ve Vizit Tipi "genel muayene" değerleri ile listelenen kaydı doğrular

    # 3) KAYDIN DÜZENLENMESİ
    Ve Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi "EKG" olan kayıt için işlem menüsüne tıklar
    Ve Otomatik Servis Öğesi Kuralları ekranında işlem menüsünden "Düzenle" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları düzenleme penceresinde Hizmet Öğesi alanından "PCR" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları düzenleme penceresinde Departman alanından "KBB" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları düzenleme penceresinde Doktor alanından "Uzm. Dt. Aişe YILMAZ" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları düzenleme penceresinde Şube alanından "Çankaya" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları düzenleme penceresinde Vizit Tipi alanından "kontrol muayene" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları düzenleme penceresinde Kaydet butonuna tıklar

    # 4) GÜNCELLENEN KAYDIN DOĞRULANMASI
    Ve Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi arama alanına "PCR" yazarak kayıt arar
    O zaman Otomatik Servis Öğesi Kuralları ekranında grid üzerinde Hizmet Öğesi "PCR", Doktor "Uzm. Dt. Aişe YILMAZ", Departman "KBB", Şube "Çankaya" ve Vizit Tipi "kontrol muayene" değerleri ile listelenen kaydın güncellendiğini doğrular


  Senaryo: Otomatik servis öğesi kuralının silinmesi

    # 1) LOGIN → OTOMATİK SERVİS ÖĞESİ KURALLARI MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Otomatik Servis Öğesi Kuralları modülüne gider

    # 2) SİLİNECEK KAYDIN BULUNMASI
    Ve Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi arama alanına "PCR" yazarak kayıt arar
    Ve Otomatik Servis Öğesi Kuralları ekranında grid üzerinde Hizmet Öğesi "PCR", Doktor "Uzm. Dt. Aişe YILMAZ", Departman "KBB", Şube "Çankaya" ve Vizit Tipi "kontrol muayene" değerleri ile listelenen kaydı doğrular

    # 3) KAYDIN SİLİNMESİ
    Ve Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi "PCR" olan kayıt için işlem menüsüne tıklar
    Ve Otomatik Servis Öğesi Kuralları ekranında işlem menüsünden "Sil" seçeneğini seçer
    Ve Otomatik Servis Öğesi Kuralları ekranında açılan silme onay penceresinde "Evet" butonuna tıklar

    # 4) SİLİNEN KAYDIN DOĞRULANMASI
    Ve Otomatik Servis Öğesi Kuralları ekranında Hizmet Öğesi arama alanına "PCR" yazarak kayıt arar
    O zaman Otomatik Servis Öğesi Kuralları ekranında grid üzerinde Hizmet Öğesi "PCR", Doktor "Uzm. Dt. Aişe YILMAZ", Departman "KBB", Şube "Çankaya" ve Vizit Tipi "kontrol muayene" değerleri ile listelenen herhangi bir kaydın bulunmadığını doğrular

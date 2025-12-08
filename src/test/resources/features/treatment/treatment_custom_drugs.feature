# language: tr
@treatment @customDrugs
Özellik: Tıbbi İşlemler > Tanımlar > Özel İlaçlar
  Özel İlaçlar ekranında yeni ilaç kaydı oluşturma, düzenleme ve durum değiştirme akışları.

  Geçmiş:
    # 1) LOGIN → ÖZEL İLAÇLAR MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Özel İlaçlar modülüne gider


  Senaryo: Auto-index ile yeni özel ilaç kaydı oluşturulması ve doğrulanması

    # 2) DETAYLI ARAMA → TEMİZLE / UYGULA + AUTO INDEX HAZIRLIĞI
    Ve Özel İlaçlar ekranında Detaylı Arama penceresi açılır
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Özel İlaçlar ekranında ara alanına "Contramal" değeri yazılarak arama yapılır
    Ve Özel İlaçlar ekranında grid üzerindeki mevcut özel ilaç kayıtları baz alınarak "Contramal" için auto index hesaplanır

    # 3) YENİ ÖZEL İLAÇ KAYDI
    Ve Özel İlaçlar ekranında Yeni Ekle butonuna tıklar
    Ve Özel İlaçlar ekranında Ad alanına auto index ile hesaplanan ilaç adı yazılır
    Ve Özel İlaçlar ekranında Barkod alanına "8680001234567" değeri yazılır
    Ve Özel İlaçlar ekranında Reçete Türü dropdownundan "Kırmızı" seçeneği seçer
    Ve Özel İlaçlar ekranında Kaydet butonuna tıklar

    # 4) SON ARAMA + DOĞRULAMA
    Ve Özel İlaçlar ekranında Detaylı Arama penceresi açılır
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Özel İlaçlar ekranında auto index ile oluşturulan ilaç adı ile arama yapılır
    O zaman Özel İlaçlar ekranında gridde auto index ile oluşturulan özel ilaç kaydının "8680001234567" barkodu ile listelendiğini doğrular


  Senaryo: Mevcut özel ilaç kaydının auto-index ile düzenlenmesi

    # 2) DETAYLI ARAMA → TEMİZLE / UYGULA + HEDEF SATIR
    Ve Özel İlaçlar ekranında Detaylı Arama penceresi açılır
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Özel İlaçlar ekranında ara alanına "Contramal" değeri yazılarak arama yapılır
    Ve Özel İlaçlar ekranında grid üzerindeki mevcut özel ilaç kayıtları baz alınarak "Contramal" adı ve "8680001234567" barkodu için düzenleme auto index bilgisi hazırlanır
    Ve Özel İlaçlar ekranında grid üzerinde hazırlanan özel ilaç kaydı için Düzenle penceresi açılır

    # 3) DÜZENLE PENCERESİ
    Ve Özel İlaçlar ekranında Düzenle penceresinde Ad alanı auto index değeri ile güncellenir
    Ve Özel İlaçlar ekranında Düzenle penceresinde Barkod alanı "8680001234567" değeri ile güncellenir
    Ve Özel İlaçlar ekranında Düzenle penceresinde Reçete Türü dropdownundan "Turuncu" seçeneği seçer
    Ve Özel İlaçlar ekranında Düzenle penceresinde Kaydet butonuna tıklar

    # 4) SON ARAMA + DOĞRULAMA
    Ve Özel İlaçlar ekranında Detaylı Arama penceresi açılır
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Özel İlaçlar ekranında düzenleme sonrası auto index ile oluşturulan ilaç adı ile arama yapılır
    O zaman Özel İlaçlar ekranında gridde düzenlenen özel ilaç kaydının "8680001234567" barkodu ve "Turuncu" reçete türü ile listelendiğini doğrular


  Senaryo: Mevcut özel ilaç kaydının durumunun Aktif/Pasif olarak değiştirilmesi

    # 2) DETAYLI ARAMA + HEDEF KAYDIN HAZIRLANMASI
    Ve Özel İlaçlar ekranında Detaylı Arama penceresi açılır
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Özel İlaçlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Özel İlaçlar ekranında ara alanına "Contramal" değeri yazılarak arama yapılır
    Ve Özel İlaçlar ekranında adı "Contramal" ile başlayan ve barkodu "8680001234567" olan kayıt durum değiştirme işlemi için hazırlanır
    Ve Özel İlaçlar ekranında hazırlanan kayıt için durum bilgisinin "Aktif" olduğu doğrulanır

    # 3) AKTİF KAYDIN PASİF EDİLMESİ
    Ve Özel İlaçlar ekranında hazırlanan kayıt için durum menüsünden "Pasif Et" seçeneği seçilir
    Ve Özel İlaçlar ekranında durum değişikliği onay penceresinde Evet butonuna tıklar
    O zaman Özel İlaçlar ekranında hazırlanan kaydın durum bilgisinin "Pasif" olduğu doğrulanır

    # 4) PASİF KAYDIN TEKRAR AKTİF EDİLMESİ
    Ve Özel İlaçlar ekranında hazırlanan kayıt için durum menüsünden "Aktif Et" seçeneği seçilir
    Ve Özel İlaçlar ekranında durum değişikliği onay penceresinde Evet butonuna tıklar
    O zaman Özel İlaçlar ekranında hazırlanan kaydın durum bilgisinin "Aktif" olduğu doğrulanır

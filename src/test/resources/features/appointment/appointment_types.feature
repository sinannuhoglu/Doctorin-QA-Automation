# language: tr
@appointment @appointmentTypes
Özellik: Randevu > Tanımlar > Randevu Tipleri

  Senaryo: Randevu tipi kaydının auto index ile oluşturulması ve gridde doğrulanması

    # 1) LOGIN → RANDEVU TİPLERİ MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Randevu Tipleri modülüne gider

    # 2) DETAYLI ARAMA → TEMİZLE / UYGULA + AUTO INDEX HAZIRLIĞI
    Ve Randevu Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Tipleri ekranında ara alanına "Sabah Seansı" değeri yazılarak arama yapılır
    Ve Randevu Tipleri ekranında grid üzerindeki mevcut randevu tipi kayıtları baz alınarak "Sabah Seansı" için auto index hesaplanır

    # 3) YENİ RANDEVU TİPİ KAYDI
    Ve Randevu Tipleri ekranında Yeni Ekle butonuna tıklar
    Ve Randevu Tipleri ekranında Ad alanına auto index ile hesaplanan randevu tipi adı yazılır
    Ve Randevu Tipleri ekranında Tip dropdownundan "Genel Muayene" seçeneği seçer
    Ve Randevu Tipleri ekranında Renk dropdownundan "Sarı" seçeneği seçer
    Ve Randevu Tipleri ekranında Kaydet butonuna tıklar

    # 4) SON ARAMA + DOĞRULAMA
    Ve Randevu Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Tipleri ekranında auto index ile oluşturulan randevu tipi adı ile arama yapılır
    O zaman Randevu Tipleri ekranında gridde auto index ile oluşturulan randevu tipi kaydının listelendiğini doğrular


  Senaryo: Mevcut randevu tipinin auto-index ile düzenlenmesi

    # 1) LOGIN → RANDEVU TİPLERİ MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Randevu Tipleri modülüne gider

    # 2) DETAYLI ARAMA → TEMİZLE / UYGULA + HEDEF SATIR
    Ve Randevu Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Tipleri ekranında ara alanına "Sabah Seansı" değeri yazılarak arama yapılır
    Ve Randevu Tipleri ekranında grid üzerindeki mevcut randevu tipi kayıtları baz alınarak "Sabah Seansı" adı için düzenleme auto index bilgisi hazırlanır
    Ve Randevu Tipleri ekranında grid üzerinde hazırlanan randevu tipi kaydı için Düzenle penceresi açılır

    # 3) DÜZENLE PENCERESİ
    Ve Randevu Tipleri ekranında Düzenle penceresinde Ad alanı auto index değeri ile güncellenir
    Ve Randevu Tipleri ekranında Düzenle penceresinde Tip dropdownundan "Kontrol Muayene" seçeneği seçer
    Ve Randevu Tipleri ekranında Düzenle penceresinde Renk dropdownundan "Turuncu" seçeneği seçer
    Ve Randevu Tipleri ekranında Düzenle penceresinde Kaydet butonuna tıklar

    # 4) SON ARAMA + DOĞRULAMA
    Ve Randevu Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Tipleri ekranında düzenleme sonrası auto index ile oluşturulan randevu tipi adı ile arama yapılır
    O zaman Randevu Tipleri ekranında gridde düzenlenen randevu tipi kaydının "Kontrol Muayene" tipi ve "Turuncu" rengi ile listelendiğini doğrular


  Senaryo: Mevcut randevu tipinin aktif/pasif durumunun değiştirilmesi

    # 1) LOGIN → RANDEVU TİPLERİ MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Randevu Tipleri modülüne gider

    # 2) DETAYLI ARAMA → HEDEF KAYDIN BULUNMASI
    Ve Randevu Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Tipleri ekranında ara alanına "Sabah Seansı" değeri yazılarak arama yapılır
    Ve Randevu Tipleri ekranında "Sabah Seansı" baz alınarak aktif/pasif işlemi için hedef randevu tipi kaydı seçilir
    Ve Randevu Tipleri ekranında gridde hedef randevu tipi kaydının bulunduğunu doğrular

    # 3) AKTİF → PASİF İŞLEMİ
    Ve Randevu Tipleri ekranında hedef randevu tipi kaydının durumu Aktif ise üç nokta menüsünden Pasif Et aksiyonu uygulanır
    Ve Randevu Tipleri ekranında pasif/aktif işlemine ait onay penceresinde Evet butonuna tıklar

    # 4) PASİF DURUMUNUN DOĞRULANMASI
    Ve Randevu Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Tipleri ekranında ara alanına "Sabah Seansı" değeri yazılarak arama yapılır
    O zaman Randevu Tipleri ekranında gridde hedef randevu tipi kaydının durumunun Pasif olarak listelendiğini doğrular

    # 5) PASİF → AKTİF İŞLEMİ
    Ve Randevu Tipleri ekranında hedef randevu tipi kaydının durumu Pasif ise üç nokta menüsünden Aktif Et aksiyonu uygulanır
    Ve Randevu Tipleri ekranında pasif/aktif işlemine ait onay penceresinde Evet butonuna tıklar

    # 6) AKTİF DURUMUNUN DOĞRULANMASI
    Ve Randevu Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Tipleri ekranında ara alanına "Sabah Seansı" değeri yazılarak arama yapılır
    O zaman Randevu Tipleri ekranında gridde hedef randevu tipi kaydının durumunun Aktif olarak listelendiğini doğrular

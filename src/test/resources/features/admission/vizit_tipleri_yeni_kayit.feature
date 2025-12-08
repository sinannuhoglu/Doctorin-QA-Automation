# language: tr
@admission @visitTypes
Özellik: Hasta Kabul > Tanımlar > Vizit Tipleri
  Vizit tipi tanımlarında auto-index ile kayıt oluşturma, düzenleme ve
  durum (Aktif/Pasif) değişim akışlarının doğrulanması.

  Geçmiş:
    # 1) LOGIN → VİZİT TİPLERİ MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Vizit Tipleri modülüne gider


  Senaryo: Auto-index ile yeni vizit tipi kaydı oluşturulması ve doğrulanması

    # 2) DETAYLI ARAMA → TEMİZLE / UYGULA
    Ve Vizit Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar

    # 3) ARAMA + AUTO INDEX HAZIRLIĞI
    Ve Vizit Tipleri ekranında ara alanına "Göz Ölçüm Odası" değeri yazılarak arama yapılır
    Ve Vizit Tipleri ekranında grid üzerindeki mevcut kayıtlar baz alınarak "Göz Ölçüm Odası" için auto index hesaplanır

    # 4) YENİ VİZİT TİPİ KAYDI
    Ve Vizit Tipleri ekranında Yeni Ekle butonuna tıklar
    Ve Vizit Tipleri ekranındaki Vizit Tipi ad alanına auto index ile hesaplanan isim yazılır
    Ve Vizit Tipleri ekranındaki Geliş Tipi dropdownundan "Günübirlik" seçeneğini seçer
    Ve Vizit Tipleri ekranındaki Sistem Türü dropdownundan "Kontrol Muayenesi" seçeneğini seçer
    Ve Vizit Tipleri ekranındaki Kaydet butonuna tıklar

    # 5) SON ARAMA + DOĞRULAMA
    Ve Vizit Tipleri ekranında ara alanına "Göz Ölçüm Odası" değeri yazılarak arama yapılır
    O zaman Vizit Tipleri ekranında gridde auto index ile oluşturulan kaydın listelendiğini doğrular


  Senaryo: Mevcut vizit tipinin auto-index ile düzenlenmesi

    # 2) DETAYLI ARAMA → TEMİZLE / UYGULA
    Ve Vizit Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar

    # 3) ARAMA + GRID ÜZERİNDEN DÜZENLE HAZIRLIĞI
    Ve Vizit Tipleri ekranında ara alanına "Göz Ölçüm Odası" değeri yazılarak arama yapılır
    Ve Vizit Tipleri ekranında grid üzerindeki mevcut kayıtlar baz alınarak "Göz Ölçüm Odası" için düzenleme auto index bilgisi hazırlanır
    Ve Vizit Tipleri ekranında grid üzerinde hazırlanan auto index kaydı için Düzenle penceresi açılır

    # 4) DÜZENLE PENCERESİ
    Ve Vizit Tipleri ekranındaki Düzenle penceresinde vizit tipi adı auto index değer ile güncellenir
    Ve Vizit Tipleri ekranındaki Düzenle penceresinde Geliş Tipi dropdownundan "Poliklinik" seçeneğini seçer
    Ve Vizit Tipleri ekranındaki Düzenle penceresinde Sistem Türü dropdownundan "Genel Muayene" seçeneğini seçer
    Ve Vizit Tipleri ekranındaki Düzenle penceresinde Kaydet butonuna tıklar

    # 5) SON ARAMA + DOĞRULAMA
    Ve Vizit Tipleri ekranında auto index ile güncellenen ad ile arama yapılır
    O zaman Vizit Tipleri ekranında gridde auto index ile güncellenen vizit tipi kaydının listelendiğini doğrular


  Senaryo: Vizit tipinin Pasif ve tekrar Aktif duruma alınması

    # 2) HEDEF KAYDI BUL VE PASİF ET
    Ve Vizit Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar

    Ve Vizit Tipleri ekranında ara alanına "Göz Ölçüm Odası" değeri yazılarak arama yapılır
    Ve Vizit Tipleri ekranında grid üzerindeki mevcut kayıtlar baz alınarak "Göz Ölçüm Odası" için durum değişimi hedef satırı belirlenir
    Ve Vizit Tipleri ekranında grid üzerinde hazırlanan durum hedef kaydı için üç nokta menüsünden "Pasif Et" seçeneğini seçer
    Ve Vizit Tipleri ekranında açılan onay penceresinde Evet butonuna tıklar

    # 3) PASİF DOĞRULAMA
    Ve Vizit Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Vizit Tipleri ekranında durum hedef kaydın adı ile arama yapılır
    O zaman Vizit Tipleri ekranında gridde durum hedef kaydının durum bilgisinin "Pasif" olduğunu doğrular

    # 4) AYNI KAYDI TEKRAR AKTİF ET
    Ve Vizit Tipleri ekranında grid üzerinde hazırlanan durum hedef kaydı için üç nokta menüsünden "Aktif Et" seçeneğini seçer
    Ve Vizit Tipleri ekranında açılan onay penceresinde Evet butonuna tıklar

    # 5) AKTİF DOĞRULAMA
    Ve Vizit Tipleri ekranında Detaylı Arama penceresi açılır
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Vizit Tipleri ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Vizit Tipleri ekranında durum hedef kaydın adı ile arama yapılır
    O zaman Vizit Tipleri ekranında gridde durum hedef kaydının durum bilgisinin "Aktif" olduğunu doğrular

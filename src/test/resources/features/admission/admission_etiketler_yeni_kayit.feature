# language: tr
@admission @tags
Özellik: Hasta Kabul > Tanımlar > Etiketler

  Geçmiş:
    # 1) LOGIN → ETİKETLER MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Etiketler modülüne gider


  Senaryo: Auto-index ile yeni etiket kaydı oluşturulması ve doğrulanması

    # 2) DETAYLI ARAMA → TEMİZLE / UYGULA
    Ve Etiketler ekranında Detaylı Arama penceresi açılır
    Ve Etiketler ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Etiketler ekranında Detaylı Arama penceresinde Uygula butonuna tıklar

    # 3) ARAMA + AUTO INDEX HAZIRLIĞI
    Ve Etiketler ekranında ara alanına "Şiddet" değeri yazılarak arama yapılır
    Ve Grid üzerindeki mevcut etiket kayıtları baz alınarak "Şiddet" için auto index hesaplanır

    # 4) YENİ ETİKET KAYDI
    Ve Etiketler ekranında Yeni Ekle butonuna tıklar
    Ve Etiketler ekranında Başlık alanına auto index ile hesaplanan etiket adı yazılır
    Ve Etiketler ekranında Açıklama alanına "Şiddete meyilli" değeri yazılır
    Ve Etiketler ekranında Tür dropdownundan "Kara Liste" seçeneğini seçer
    Ve Etiketler ekranında Renk dropdownundan "Kırmızı" seçeneğini seçer
    Ve Etiketler ekranında Kaydet butonuna tıklar

    # 5) SON ARAMA + DOĞRULAMA
    Ve Etiketler ekranında auto index ile oluşturulan etiket adı ile arama yapılır
    O zaman gridde auto index ile oluşturulan etiket kaydının listelendiğini doğrular


  Senaryo: Mevcut etiket kaydının auto-index ile düzenlenmesi

    # 2) DETAYLI ARAMA → TEMİZLE / UYGULA
    Ve Etiketler ekranında Detaylı Arama penceresi açılır
    Ve Etiketler ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Etiketler ekranında Detaylı Arama penceresinde Uygula butonuna tıklar

    # 3) ARAMA + GRID ÜZERİNDEN DÜZENLE
    Ve Etiketler ekranında ara alanına "Şiddet" değeri yazılarak arama yapılır
    Ve Grid üzerindeki ilk satırın 5. sütunundaki üç nokta menüsü açılır
    Ve Açılan menüden "Düzenle" seçeneğine tıklar

    # 4) DÜZENLE PENCERESİ
    Ve Düzenle penceresinde Başlık alanındaki index bir arttırılarak güncellenir
    Ve Düzenle penceresinde Açıklama alanına "Şiddete meyilli birisi" değeri yazılır
    Ve Düzenle penceresinde Tür dropdownundan "VIP" seçeneği seçilir
    Ve Düzenle penceresinde Renk dropdownundan "Siyah" seçeneği seçilir
    Ve Etiketler ekranındaki Düzenle penceresinde Kaydet butonuna tıklar

    # 5) SON ARAMA + DOĞRULAMA
    Ve Etiketler ekranında düzenlenen etiket adı ile arama yapılır
    O zaman gridde düzenlenen etiket kaydının listelendiğini doğrular


  Senaryo: Etiket kaydının silinmesi

    # 2) DETAYLI ARAMA → TEMİZLE / UYGULA
    Ve Etiketler ekranında Detaylı Arama penceresi açılır
    Ve Etiketler ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Etiketler ekranında Detaylı Arama penceresinde Uygula butonuna tıklar

    # 3) ARAMA + GRID ÜZERİNDEN SİLME
    Ve Etiketler ekranında ara alanına "Şiddet" değeri yazılarak arama yapılır
    Ve Grid üzerindeki ilk satırdaki etiket adı silinecek etiket adı olarak kaydedilir
    Ve Grid üzerindeki ilk satırın 5. sütunundaki üç nokta menüsü açılır
    Ve Açılan menüden "Sil" seçeneğine tıklar
    Ve Açılan silme onay penceresinde "Evet" butonuna tıklar

    # 4) SON ARAMA + DOĞRULAMA
    Ve Etiketler ekranında Detaylı Arama penceresi açılır
    Ve Etiketler ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Etiketler ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Etiketler ekranında silinecek etiket adı ile arama yapılır
    O zaman gridde silinen etiket kaydının listelenmediğini doğrular

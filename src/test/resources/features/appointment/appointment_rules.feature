# language: tr
@appointment @appointmentRules
Özellik: Randevu > Tanımlar > Kurallar
  Randevu kurallarının oluşturulması, güncellenmesi ve silinmesi süreçlerinin yönetimi

  # Geçmiş (Background):
  # - Tüm senaryolarda ortak olan login ve modüle giriş adımları burada toparlanır.
  # - Böylece her senaryoda tekrar yazmak yerine tek bir kez tanımlanır.

  Geçmiş:
    # 1) LOGIN → RANDEVU KURALLARI MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Randevu Kuralları modülüne gider

  Senaryo: Yeni randevu kuralının oluşturulması ve gridde doğrulanması

    # 2) YENİ KURAL OLUŞTURMA
    Ve Randevu Kuralları ekranında Yeni Ekle butonuna tıklar
    Ve Randevu Kuralları ekranında Randevu Kural Türü dropdownundan "Takvim Kontrolü" seçeneği seçer
    Ve Randevu Kuralları ekranında Kaynaklar alanında "Uzm. Dr. Aykan BÜYÜKAYDIN" kaynağını seçer
    Ve Randevu Kuralları ekranında Açıklama alanına "Takvimde doktorun müsaitlik durumuna göre randevu kısıtlaması uygulanır." kural açıklaması yazılır
    Ve Randevu Kuralları ekranında Kaydet butonuna tıklar

    # 3) DETAYLI ARAMA → TEMİZLE / UYGULA
    Ve Randevu Kuralları ekranında Detaylı Arama penceresi açılır
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Uygula butonuna tıklar

    # 4) SON ARAMA + DOĞRULAMA
    Ve Randevu Kuralları ekranında ara alanına "Takvimde doktorun müsaitlik durumuna göre randevu kısıtlaması uygulanır." değeri yazılarak arama yapılır
    O zaman Randevu Kuralları ekranında gridde "Takvim Kontrolü" kural tipine ve "Takvimde doktorun müsaitlik durumuna göre randevu kısıtlaması uygulanır." açıklamasına sahip kaydın listelendiğini doğrular


  Senaryo: Mevcut randevu kuralının düzenlenmesi ve gridde güncel değerlerin doğrulanması

    # 2) MEVCUT KAYDI BULMA (DETAYLI ARAMA + TOOLBAR ARAMA)
    Ve Randevu Kuralları ekranında Detaylı Arama penceresi açılır
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Kuralları ekranında ara alanına "Takvimde doktorun müsaitlik durumuna göre randevu kısıtlaması uygulanır." değeri yazılarak arama yapılır
    Ve Randevu Kuralları ekranında gridde açıklaması "Takvimde doktorun müsaitlik durumuna göre randevu kısıtlaması uygulanır." olan kaydın üç nokta menüsünden Düzenle seçeneğine tıklar

    # 3) DÜZENLE PENCERESİNDEN KAYDIN GÜNCELLENMESİ
    Ve Randevu Kuralları ekranında Düzenle penceresinde Randevu Kural Türü dropdownundan "Ara Randevu" seçeneği seçer
    Ve Randevu Kuralları ekranında Düzenle penceresinde Kaynaklar alanında "Uzm. Dt. Aişe YILMAZ" kaynağını seçer
    Ve Randevu Kuralları ekranında Düzenle penceresinde Açıklama alanına "Çakışan randevu girişlerini engellemek için kontrol mekanizması sağlar." kural açıklaması yazılır
    Ve Randevu Kuralları ekranında Düzenle penceresinde Kaydet butonuna tıklar

    # 4) GÜNCEL DEĞERLERİN ARAMA VE DOĞRULAMA İLE KONTROLÜ
    Ve Randevu Kuralları ekranında Detaylı Arama penceresi açılır
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Kuralları ekranında ara alanına "Çakışan randevu girişlerini engellemek için kontrol mekanizması sağlar." değeri yazılarak arama yapılır
    O zaman Randevu Kuralları ekranında gridde "Ara Randevu" kural tipine ve "Çakışan randevu girişlerini engellemek için kontrol mekanizması sağlar." açıklamasına sahip kaydın listelendiğini doğrular


  Senaryo: Mevcut randevu kuralının silinmesi ve gridde görüntülenmediğinin doğrulanması

    # 2) SİLİNECEK KAYDIN BULUNMASI
    Ve Randevu Kuralları ekranında Detaylı Arama penceresi açılır
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Kuralları ekranında ara alanına "Çakışan randevu girişlerini engellemek için kontrol mekanizması sağlar." değeri yazılarak arama yapılır
    Ve Randevu Kuralları ekranında gridde açıklaması "Çakışan randevu girişlerini engellemek için kontrol mekanizması sağlar." olan kaydın üç nokta menüsünden Sil seçeneğine tıklar
    Ve Randevu Kuralları ekranında Sil onay penceresinde Evet butonuna tıklar

    # 3) SİLME SONRASI ARAMA VE DOĞRULAMA
    Ve Randevu Kuralları ekranında Detaylı Arama penceresi açılır
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Randevu Kuralları ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Randevu Kuralları ekranında ara alanına "Çakışan randevu girişlerini engellemek için kontrol mekanizması sağlar." değeri yazılarak arama yapılır
    O zaman Randevu Kuralları ekranında açıklaması "Çakışan randevu girişlerini engellemek için kontrol mekanizması sağlar." olan kaydın gridde listelenmediğini ve "Gösterilecek kayıt yok" mesajının görüntülendiğini doğrular

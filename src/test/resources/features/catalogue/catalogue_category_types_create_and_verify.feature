# language: tr
@catalogue @categoryTypes
Özellik: Katalog > Tanımlar > Kategori Tipleri
  Kategori Tipleri ekranında yeni kategori oluşturma, düzenleme ve silme işlemlerinin doğrulanması

  Geçmiş:
    # 1) LOGIN → KATEGORİ TİPLERİ MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Kategori Tipleri modülüne gider

  Senaryo: Yeni kategori tipinin oluşturulması ve gridde doğrulanması

    # 2) YENİ KATEGORİ TİPİ OLUŞTURMA
    Ve Kategori Tipleri ekranında Yeni Ekle butonuna tıklar
    Ve Kategori Tipleri ekranında Yeni Ekle penceresinde Adı alanına "Dahiliye Muayene Hizmetleri" verisini girer
    Ve Kategori Tipleri ekranında Yeni Ekle penceresinde Servis Türü alanında "Hizmet" seçeneğini seçer
    Ve Kategori Tipleri ekranında Yeni Ekle penceresinde Branşlar alanında "Dahiliye" seçeneğini seçer
    Ve Kategori Tipleri ekranında Yeni Ekle penceresinde Açıklama alanına "Dahiliye uzmanlarının gerçekleştirdiği genel iç hastalıkları muayene ve değerlendirme hizmetlerinin sınıflandırılması için kullanılır." verisini girer
    Ve Kategori Tipleri ekranında Yeni Ekle penceresinde Kaydet butonuna tıklar

    # 3) ARAMA + DOĞRULAMA
    Ve Kategori Tipleri ekranında toolbar üzerindeki arama alanına "Dahiliye Muayene Hizmetleri" yazarak arama yapar
    O zaman Kategori Tipleri ekranında gridde Adı "Dahiliye Muayene Hizmetleri", Servis Türü "Hizmet", Branş "Dahiliye" ve Açıklama "Dahiliye uzmanlarının gerçekleştirdiği genel iç hastalıkları muayene ve değerlendirme hizmetlerinin sınıflandırılması için kullanılır." olan kaydın listelendiğini doğrular


  Senaryo: Mevcut kategori tipinin düzenlenmesi ve gridde güncel değerlerin doğrulanması

    # 2) MEVCUT KAYDI BULMA
    Ve Kategori Tipleri ekranında toolbar üzerindeki arama alanına "Dahiliye Muayene Hizmetleri" yazarak arama yapar
    Ve Kategori Tipleri ekranında gridde Adı "Dahiliye Muayene Hizmetleri" olan kaydın üç nokta menüsünden Düzenle seçeneğine tıklar

    # 3) DÜZENLE PENCERESİNDEN GÜNCELLEME
    Ve Kategori Tipleri ekranında Düzenle penceresinde Adı alanına "Dahiliye Muayene Hizmetleri - 1" verisini girer
    Ve Kategori Tipleri ekranında Düzenle penceresinde Branşlar alanında "Dahiliye" seçeneğini seçer
    Ve Kategori Tipleri ekranında Düzenle penceresinde Açıklama alanına "Dahiliye uzmanlarının gerçekleştirdiği genel iç hastalıkları muayene ve değerlendirme hizmetlerinin sınıflandırılması için kullanılır." verisini girer
    Ve Kategori Tipleri ekranında Düzenle penceresinde Kaydet butonuna tıklar

    # 4) GÜNCEL DEĞERLERİN ARAMA VE DOĞRULAMA İLE KONTROLÜ
    Ve Kategori Tipleri ekranında toolbar üzerindeki arama alanına "Dahiliye Muayene Hizmetleri - 1" yazarak arama yapar
    O zaman Kategori Tipleri ekranında gridde Adı "Dahiliye Muayene Hizmetleri - 1", Servis Türü "Hizmet", Branş "Dahiliye" ve Açıklama "Dahiliye uzmanlarının gerçekleştirdiği genel iç hastalıkları muayene ve değerlendirme hizmetlerinin sınıflandırılması için kullanılır." olan kaydın listelendiğini doğrular


  Senaryo: Mevcut kategori tipinin silinmesi ve gridde listelenmediğinin doğrulanması

    # 2) SİLİNECEK KAYDI BULMA
    Ve Kategori Tipleri ekranında toolbar üzerindeki arama alanına "Dahiliye Muayene Hizmetleri - 1" yazarak arama yapar
    Ve Kategori Tipleri ekranında gridde Adı "Dahiliye Muayene Hizmetleri - 1" olan kaydın üç nokta menüsünden Sil seçeneğine tıklar
    Ve Kategori Tipleri ekranında Sil onay penceresinde Evet butonuna tıklar

    # 3) SİLME SONRASI ARAMA VE DOĞRULAMA
    Ve Kategori Tipleri ekranında toolbar üzerindeki arama alanına "Dahiliye Muayene Hizmetleri - 1" yazarak arama yapar
    O zaman Kategori Tipleri ekranında gridde Adı "Dahiliye Muayene Hizmetleri - 1" olan kaydın listelenmediğini doğrular

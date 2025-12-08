# language: tr
@resources
Özellik: Kaynaklar modülü - Departman arama

  Senaryo: Detaylı arama penceresi ve arama alanı ile departman arama
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar

    Ve Kaynaklar modülüne gider
    Ve Detaylı arama penceresini açar
    Ve Detaylı arama penceresinde Temizle butonuna tıklar
    Ve Detaylı arama penceresinde Uygula butonuna tıklar
    Ve Kaynaklar arama alanına "Beslenme ve Diyetisyen" yazar ve arama yapar
    Ve "Beslenme ve Diyetisyen" departmanının durumu "Aktif" olacak şekilde hazırlanır
    O zaman Departman listesindeki satırlarda "Beslenme ve Diyetisyen" için durum bilgisinin "Aktif" olduğunu görür

  Senaryo: Yeni departman kaydı oluşturma
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar

    Ve Kaynaklar modülüne gider
    Ve "Aile Terapisti" departmanı için kodu "98", adı "Aile Terapisti", departman türü "Tıbbi Birim" ve branşı "Psikiyatri" olacak şekilde yeni departman kaydı oluşturur
    O zaman Departman listesindeki satırlarda "Aile Terapisti" için durum bilgisinin "Aktif" olduğunu görür

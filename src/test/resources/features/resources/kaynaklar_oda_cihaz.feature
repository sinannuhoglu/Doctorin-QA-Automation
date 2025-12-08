# language: tr
@resources
@oda_cihaz
Özellik: Kaynaklar modülü - Oda & Cihaz Yönetimi

  Senaryo: Oda & Cihaz kaynağının aktif durumda olduğunun kontrol edilmesi
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar

    Ve Kullanıcı Oda ve Cihaz yönetimi sayfasına gider
    Ve Detaylı arama penceresini temizler ve uygular
    Ve "Göz Ölçümü" kaynağını arar
    Ve "Göz Ölçümü" kaynağının durumu "Aktif" olacak şekilde hazırlanır
    # O zaman Kaynak listesindeki satırlarda "Göz Ölçümü" için durum bilgisinin "Aktif" olduğunu görür

  Senaryo: Yeni Oda & Cihaz kaydı oluşturma (auto-index ile)
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar

    Ve Kullanıcı Oda ve Cihaz yönetimi sayfasına gider
    Ve "Göz Ölçümü" kaynağı için adı "Göz Ölçümü", tipi "Yer", alt tipi "Oda", departmanı "Aile Terapisti" ve şubesi "Çankaya" olacak şekilde yeni oda ve cihaz kaydı oluşturur
    # O zaman Kaynak listesindeki satırlarda "Göz Ölçümü" için durum bilgisinin "Aktif" olduğunu görür

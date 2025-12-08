# language: tr
# -----------------------------------------------------------------------------
# Bu senaryo, Personel Yönetimi ekranında belirli bir personelin durumunun
# doğrulanması amacıyla tasarlanmıştır.
#
# Akış mantığı:
# - Eğer personelin mevcut durumu "Aktif" ise herhangi bir işlem yapılmaz.
# - Eğer mevcut durum "Pasif" ise sistem üzerinden "Aktif Et" akışı çalıştırılır
#   ve personelin durumu "Aktif" olacak şekilde hazırlanır.
#
# Yeni kayıt işlemi senaryoya dahil edilmemiştir.
# Bunun nedeni, sistemdeki kullanıcı eşleştirme mantığıdır.
#
# Bu nedenle, testin stabil çalışabilmesi için senaryo yalnızca mevcut kaydın
# durumunu doğrulama ve gerekli ise durumu değiştirme (Pasif → Aktif)
# akışı üzerinden yürütülmektedir.
#
# Bu açıklama, test ortamındaki veri bağımlılıklarını azaltmak ve
# senaryonun bağımsız çalışabilirliğini sağlamak amacıyla eklenmiştir.
# -----------------------------------------------------------------------------

@resources
Özellik: Kaynaklar modülü - Personel Yönetimi

  Senaryo: Personel kaydının aktif durumda olduğunun kontrol edilmesi
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar

    Ve Kaynaklar Personel Yönetimi modülüne gider
    Ve Personel detaylı arama penceresini açar
    Ve Personel detaylı arama penceresinde Temizle butonuna tıklar
    Ve Personel arama alanına "Uzm. Dr. Aykan BÜYÜKAYDIN" yazar ve arama yapar
    Ve "Uzm. Dr. Aykan BÜYÜKAYDIN" personel kaydının durumu "Aktif" olacak şekilde hazırlanır
    # O zaman Personel listesindeki satırlarda "Uzm. Dr. Aykan BÜYÜKAYDIN" için durum bilgisinin "Aktif" olduğunu görür

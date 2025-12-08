# language: tr
@treatment @examinationRequirements
Özellik: Tıbbi İşlemler > Tanımlar > Muayene Zorunlu Alanlar
  Muayene Zorunlu Alanlar ekranında kayıt oluşturma, düzenleme ve silme işlemlerinin doğrulanması.

  Geçmiş:
    # 1) LOGIN → MUAYENE ZORUNLU ALANLAR MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Muayene Zorunlu Alanlar modülüne gider


  Senaryo: Muayene zorunlu alan kaydının oluşturulması ve doğrulanması

    # 2) YENİ KAYIDIN OLUŞTURULMASI
    Ve Muayene Zorunlu Alanlar ekranında Yeni Ekle butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında Şube dropdownundan "Altındağ" seçeneği seçer
    Ve Muayene Zorunlu Alanlar ekranında Alan dropdownundan "Geçmiş" seçeneği seçer
    Ve Muayene Zorunlu Alanlar ekranında Departmanlar alanından "KBB" departmanı seçer
    Ve Muayene Zorunlu Alanlar ekranında Kaydet butonuna tıklar

    # 3) DETAYLI ARAMA + GRID DOĞRULAMA
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresi açılır
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında ara alanına "Geçmiş" değeri yazılarak arama yapılır
    O zaman Muayene Zorunlu Alanlar ekranında gridde Alan bilgisi "Geçmiş", Şube bilgisi "Altındağ" ve Departman bilgisi "KBB" olan kaydın listelendiğini doğrular


  Senaryo: Muayene zorunlu alan kaydının düzenlenmesi ve güncel verilerin doğrulanması

    # 2) ÖNCE KAYDI BUL (DETAYLI ARAMA + GRID FİLTRELEME)
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresi açılır
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında ara alanına "Geçmiş" değeri yazılarak arama yapılır

    # 3) GRID ÜZERİNDEN DÜZENLE’Yİ AÇ
    Ve Muayene Zorunlu Alanlar ekranında Alan bilgisi "Geçmiş", Şube bilgisi "Altındağ" ve Departman bilgisi "KBB" olan kaydın Düzenle menüsünü açar

    # 4) DÜZENLE PENCERESİNDE GÜNCELLE
    Ve Muayene Zorunlu Alanlar ekranında Düzenle penceresinde Şube dropdownundan "Çankaya" seçeneği seçer
    Ve Muayene Zorunlu Alanlar ekranında Düzenle penceresinde Alan dropdownundan "Geçmiş" seçeneği seçer
    Ve Muayene Zorunlu Alanlar ekranında Düzenle penceresinde Departmanlar alanında "KBB" departmanının seçimini kaldırıp "Dahiliye" departmanını seçer
    Ve Muayene Zorunlu Alanlar ekranında Kaydet butonuna tıklar

    # 5) YENİDEN FİLTRELE + GÜNCEL GRID DOĞRULAMA
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresi açılır
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında ara alanına "Geçmiş" değeri yazılarak arama yapılır
    O zaman Muayene Zorunlu Alanlar ekranında gridde Alan bilgisi "Geçmiş", Şube bilgisi "Çankaya" ve Departman bilgisi "Dahiliye" olan kaydın listelendiğini doğrular


  Senaryo: Muayene zorunlu alan kaydının silinmesi ve gridden kaldırıldığının doğrulanması

    # 2) ÖNCE KAYDI BUL (DETAYLI ARAMA + GRID FİLTRELEME)
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresi açılır
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında ara alanına "Geçmiş" değeri yazılarak arama yapılır

    # 3) GRID ÜZERİNDEN SİL İŞLEMİNİ BAŞLAT
    Ve Muayene Zorunlu Alanlar ekranında Alan bilgisi "Geçmiş", Şube bilgisi "Çankaya" ve Departman bilgisi "Dahiliye" olan kaydın Sil menüsünü açar
    Ve Muayene Zorunlu Alanlar ekranında Sil onay penceresinde Evet butonuna tıklar

    # 4) SİLME SONRASI GRID DOĞRULAMA
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresi açılır
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Temizle butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında Detaylı Arama penceresinde Uygula butonuna tıklar
    Ve Muayene Zorunlu Alanlar ekranında ara alanına "Geçmiş" değeri yazılarak arama yapılır
    O zaman Muayene Zorunlu Alanlar ekranında gridde Alan bilgisi "Geçmiş", Şube bilgisi "Çankaya" ve Departman bilgisi "Dahiliye" olan kaydın listelenmediğini doğrular

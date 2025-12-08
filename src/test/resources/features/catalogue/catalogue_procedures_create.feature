# language: tr
@catalogue @catalogueProcedures
Özellik: Katalog > Tanımlar > Hizmet Tanımları
  Katalog Hizmet Tanımları ekranında yeni hizmet oluşturma, filtreleme, durum değiştirme ve düzenleme işlemlerinin doğrulanması

  Geçmiş:
    # 1) LOGIN → KATALOG HİZMET TANIMLARI MODÜLÜ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Katalog Hizmet Tanımları modülüne gider

  Senaryo: Yeni hizmetin oluşturulması, filtrelenmesi ve grid üzerinde kod ile doğrulanması

    # 2) YENİ HİZMET OLUŞTURMA
    Ve Katalog Hizmet Tanımları ekranında Yeni Hizmet butonuna tıklar
    Ve Katalog Hizmet Tanımları ekranında Hizmet adı alanına "Dahiliye İkinci Muayenesi" bilgisi girilir
    Ve Katalog Hizmet Tanımları ekranında Alt tür alanından "Muayene Hizmeti" değeri seçilir
    Ve Katalog Hizmet Tanımları ekranında KDV oranı alanından "%12" değeri seçilir
    Ve Katalog Hizmet Tanımları ekranında ilk satır için Ücret alanına "200" değeri girilir
    Ve Katalog Hizmet Tanımları ekranında ilk satır için Para birimi alanından "TRY" değeri seçilir
    Ve Katalog Hizmet Tanımları ekranında Kaydet butonuna tıklar
    Ve Katalog Hizmet Tanımları ekranında yeni hizmet penceresinin kapandığı doğrulanır

    # 3) FİLTRELEME VE ARAMA
    Ve Katalog Hizmet Tanımları ekranında filtre butonuna tıklar
    Ve Katalog Hizmet Tanımları ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar

    # 4) KOD İLE ARAMA VE GRID DOĞRULAMASI
    Ve Katalog Hizmet Tanımları ekranında arama alanına "Dahiliye İkinci Muayenesi" Kodu değeri girilir ve aranır
    O zaman Katalog Hizmet Tanımları ekranında grid üzerinde "Dahiliye İkinci Muayenesi" kodlu kaydın oluştuğu doğrulanır


  Senaryo: Var olan hizmet kaydının pasif/aktif durumunun değiştirilmesi

    # 2) FİLTRELEME VE ARAMA
    Ve Katalog Hizmet Tanımları ekranında filtre butonuna tıklar
    Ve Katalog Hizmet Tanımları ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar
    Ve Katalog Hizmet Tanımları ekranında arama alanına "Dahiliye İkinci Muayenesi" Kodu değeri girilir ve aranır
    Ve Katalog Hizmet Tanımları ekranında grid üzerinde "Dahiliye İkinci Muayenesi" kodlu kaydın oluştuğu doğrulanır

    # 3) AKTİF → PASİF
    Ve Katalog Hizmet Tanımları ekranında grid üzerindeki ilk satırın durum alanının "Aktif" olduğu doğrulanır
    Ve Katalog Hizmet Tanımları ekranında ilk satır için üç nokta menüsüne tıklanır
    Ve Katalog Hizmet Tanımları ekranında açılan menüden "Pasif Et" seçeneğine tıklanır
    Ve Katalog Hizmet Tanımları ekranında açılan onay penceresinde Evet butonuna tıklar
    Ve Katalog Hizmet Tanımları ekranında grid üzerindeki ilk satırın durum alanının "Pasif" olduğu doğrulanır

    # 4) PASİF → AKTİF
    Ve Katalog Hizmet Tanımları ekranında ilk satır için üç nokta menüsüne tıklanır
    Ve Katalog Hizmet Tanımları ekranında açılan menüden "Aktif Et" seçeneğine tıklanır
    Ve Katalog Hizmet Tanımları ekranında açılan onay penceresinde Evet butonuna tıklar
    O zaman Katalog Hizmet Tanımları ekranında grid üzerindeki ilk satırın durum alanının "Aktif" olduğu doğrulanır


  Senaryo: Var olan hizmet kaydının düzenlenmesi

    # 2) KAYDIN BULUNMASI İÇİN FİLTRELEME VE ARAMA
    Ve Katalog Hizmet Tanımları ekranında filtre butonuna tıklar
    Ve Katalog Hizmet Tanımları ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar
    Ve Katalog Hizmet Tanımları ekranında arama alanına "Dahiliye İkinci Muayenesi" Kodu değeri girilir ve aranır
    Ve Katalog Hizmet Tanımları ekranında grid üzerinde "Dahiliye İkinci Muayenesi" kodlu kaydın oluştuğu doğrulanır

    # 3) DÜZENLE PENCERESİNİN AÇILMASI
    Ve Katalog Hizmet Tanımları ekranında ilk satır için üç nokta menüsüne tıklanır
    Ve Katalog Hizmet Tanımları ekranında açılan menüden "Düzenle" seçeneğine tıklanır

    # 4) HİZMET BİLGİLERİNİN GÜNCELLENMESİ
    Ve Katalog Hizmet Tanımları ekranında Alt tür alanından "Diğer Hizmetler" değeri seçilir
    Ve Katalog Hizmet Tanımları ekranında KDV oranı alanından "%8" değeri seçilir
    Ve Katalog Hizmet Tanımları ekranında ilk satır için Ücret alanına "300" değeri girilir
    Ve Katalog Hizmet Tanımları ekranında Kaydet butonuna tıklar
    Ve Katalog Hizmet Tanımları ekranında yeni hizmet penceresinin kapandığı doğrulanır

    # 5) GÜNCELLENEN KAYDIN TEKRAR FİLTRELENMESİ VE BULUNMASI
    Ve Katalog Hizmet Tanımları ekranında filtre butonuna tıklar
    Ve Katalog Hizmet Tanımları ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar
    O zaman Katalog Hizmet Tanımları ekranında grid üzerinde "Dahiliye İkinci Muayenesi" kodlu kaydın oluştuğu doğrulanır

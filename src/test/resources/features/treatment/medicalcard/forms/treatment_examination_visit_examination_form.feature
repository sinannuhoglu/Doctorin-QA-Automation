# language: tr
@treatment @treatmentExaminationForms
Özellik: Tıbbi İşlemler > Muayene > Muayene Formu & Ameliyat Raporu & Basınç Değerlendirme & Diyetisyen

  Geçmiş:
    # LOGIN → TIBBİ İŞLEMLER İŞ LİSTESİ
    Diyelim ki kullanıcı login sayfasını görmektedir
    Ve Müşteri değiştir penceresini açar
    Ve Müşteri adını test verisinden girer
    Ve Müşteri bilgisini kaydeder
    Ve kullanıcı adı ve şifreyi test verisinden girer
    Eğer ki Giriş butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi modülüne gider

    # TARİH ARALIĞI SEÇİMİ
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanına tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında tarih aralığı alanından Bu Yıl seçeneğini seçer

    # FİLTRE VE ARAMA
    Ve Tıbbi İşlemler İş Listesi ekranında filtre butonuna tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında filtre detay penceresinde Temizle ve ardından Uygula butonlarına tıklar
    Ve Tıbbi İşlemler İş Listesi ekranında arama alanına "HASTANUR İYİLEŞMEZ" hasta adı girilir ve aranır
    O zaman Tıbbi İşlemler İş Listesi ekranında grid üzerinde "HASTANUR İYİLEŞMEZ" adlı hastanın listelendiği doğrulanır

    # HASTA DETAYINA GİDİŞ (Muayene sayfası)
    Ve Tıbbi İşlemler İş Listesi ekranında grid üzerindeki ilk satır için Detay butonuna tıklar

  Senaryo: Muayene sayfasında ilk vizitte Muayene Formu doldurulması, kaydedilmesi ve aynı vizitte silinmesi
    # VİZİT İÇİ MUAYENE FORMU PENCERESİNİN AÇILMASI
    Ve Muayene sayfasında ilk vizitin Muayene Formu penceresini açar

    # FORM ALANLARININ DOLDURULMASI (DataTable ile + Kaydet)
    Ve Muayene sayfasında Muayene Formu aşağıdaki verilerle doldurulur ve kaydedilir:
      | alan                      | tip       | değer                                                                                                                                   |
      | Şikayet                   | textarea  | 3 gündür süren boğaz ağrısı, yutkunma güçlüğü ve ara ara öksürük.                                                                       |
      | Geçmiş                    | textarea  | Benzer şikayetlerle yılda 1–2 kez başvuru hikayesi mevcut. Bilinen kronik hastalık yok. Düzenli ilaç kullanımı bulunmuyor.             |
      | Muayene                   | textarea  | Orofarenks hiperemik, tonsiller bilateral grade 2 büyümüş. Postnazal akıntı gözlendi. Burun pasajı hafif dar. Servikal lenf bezlerinde minimal hassasiyet mevcut. |
      | Tedavi Planı ve Önerileri | textarea  | Semptomatik tedavi için uygun ağrı kesici reçete edildi.                                                                                |

    # KAYDIN DOĞRULANMASI
    O zaman Muayene sayfasında Muayene Formu kaydının başarıyla oluşturulduğu doğrulanır

    # VİZİTLERDE MUAYENE FORMU ARANMASI VE SİLİNMESİ
    Ve Muayene sayfasında vizitler arasında Muayene Formu bulunan ilk vizitteki formu siler
    Ve Muayene sayfasında Muayene Formu silme işlemi için varsa Onay penceresindeki Evet butonuna tıklar
    O zaman Muayene sayfasında Muayene Formu açılan vizitte hâlâ Muayene Formu kalmadığı doğrulanır

  Senaryo: Muayene sayfasında vizit içinde Ameliyat Raporu doldurulması, kaydedilmesi ve aynı vizitte silinmesi
    # VİZİT İÇİ AMELİYAT RAPORU PENCERESİNİN AÇILMASI
    Ve Muayene sayfasında ilk vizitin Ameliyat Raporu penceresini açar

    # FORM ALANLARININ DOLDURULMASI (DataTable ile + Kaydet)
    Ve Muayene sayfasında Ameliyat Raporu aşağıdaki verilerle doldurulur ve kaydedilir:
      | alan                                      | tip       | değer                                                                                          |
      | Ameliyat Tarihi                           | date      | BUGÜN                                                                                          |
      | Ameliyat Saati                            | time      | 12:30                                                                                          |
      | Ameliyat Ekibi                            | textarea  | Uzm. Dr. Aykan BÜYÜKAYDIN                                                                     |
      | Ameliyat Öncesi Tanısı                    | text      | Kronik tonsillit                                                                              |
      | Ameliyat Sonrası Tanısı                   | text      | Başarılı bilateral tonsillektomi                                                              |
      | Alınan Numuneler                          | textarea  | Tonsil dokusu                                                                                 |
      | Periperatif Komplikasyonlar               | textarea  | Operasyon boyunca vital bulgular stabildi.                                                    |
      | Kaybedilen ve Transfüzyon Yapılan Kan Miktarı | textarea  | Tahmini kan kaybı: 20 ml                                                                      |
      | Ameliyatta Konulan Dren Tüp Sonda         | textarea  | Dren konulmadı. Cerrahi alan temiz ve kontrollü şekilde kapatıldı.                            |

    # KAYDIN DOĞRULANMASI
    O zaman Muayene sayfasında Ameliyat Raporu kaydının başarıyla oluşturulduğu doğrulanır

    # AMELİYAT RAPORU SİLME VE DOĞRULAMA
    Ve Muayene sayfasında vizitler arasında Ameliyat Raporu bulunan vizitteki formu siler
    Ve Muayene sayfasında Ameliyat Raporu silme işlemi için varsa Onay penceresindeki Evet butonuna tıklar
    O zaman Muayene sayfasında Ameliyat Raporu açılan vizitte hiç Ameliyat Raporu kalmadığı doğrulanır

  Senaryo: Muayene sayfasında vizit içinde Basınç Değerlendirme formunun doldurulması, kaydedilmesi ve aynı vizitte silinmesi
    # VİZİT İÇİ BASINÇ DEĞERLENDİRME FORMU PENCERESİNİN AÇILMASI
    Ve Muayene sayfasında ilk vizitin Basınç Değerlendirme formu penceresini açar

    # FORM ALANLARININ DOLDURULMASI (DataTable ile + Kaydet)
    Ve Muayene sayfasında Basınç Değerlendirme formu aşağıdaki risk seçenekleriyle doldurulur ve kaydedilir:
      | alan              | tip      | değer                                                                 |
      | Algılama          | checkbox | Tamamen Sınırlı                                                       |
      | Nemlilik          | checkbox | Her Zaman Nemli                                                       |
      | Fiziksel Aktivite | checkbox | Tam Hareketiz                                                         |
      | Mobilite          | checkbox | Yatağa Bağımlı                                                        |
      | Beslenme          | checkbox | Kötü Beslenme                                                         |
      | Sürtünme ve Kayma | checkbox | Sorunlu (Hareket etmek için büyük ölçüde yardım gerek)               |

    # KAYDIN DOĞRULANMASI
    O zaman Muayene sayfasında Basınç Değerlendirme formu kaydının başarıyla oluşturulduğu doğrulanır

    # BASINÇ FORMU SİLME VE DOĞRULAMA
    Ve Muayene sayfasında vizitler arasında Basınç Değerlendirme formu bulunan vizitteki formu siler
    Ve Muayene sayfasında Basınç Değerlendirme formu silme işlemi için varsa Onay penceresindeki Evet butonuna tıklar
    O zaman Muayene sayfasında Basınç Değerlendirme formu açılan vizitte hiç Basınç Değerlendirme formu kalmadığı doğrulanır

  Senaryo: Muayene sayfasında vizit içinde Diyetisyen formunun doldurulması, kaydedilmesi ve aynı vizitte silinmesi
    # VİZİT İÇİ DİYETİSYEN FORMU PENCERESİNİN AÇILMASI
    Ve Muayene sayfasında vizit içinde Diyetisyen formu açılır

    # FORM ALANLARININ DOLDURULMASI (DataTable ile)
    Ve Muayene sayfasında vizit içinde Diyetisyen formu aşağıdaki verilerle doldurulur ve kaydedilir:
      | alan                       | tip      | değer                                                                 |
      | Ziyaret Tarihi             | date     | BUGÜN                                                                |
      | Boy (cm)                   | numeric  | 172                                                                  |
      | Kilo (kg)                  | numeric  | 84                                                                   |
      | Vücut Kitle İndeksi (VKİ)  | numeric  | 28.4                                                                 |
      | Bel çevresi (cm)           | numeric  | 98                                                                   |
      | Vücut Yağ Oranı %          | numeric  | 27                                                                   |
      | Notlar                     | textarea | Son haftalarda düzensiz beslenme ve hafif ödem gözlendi. Su tüketimi düşük. |

    # KAYDIN DOĞRULANMASI
    O zaman Muayene sayfasında Diyetisyen formu açılan vizitte Diyetisyen formu eklendiği doğrulanır

    # DİYETİSYEN FORMU SİLME VE DOĞRULAMA
    Ve Muayene sayfasında Diyetisyen formu açılan vizitteki tüm Diyetisyen formları silinir
    O zaman Muayene sayfasında Diyetisyen formu açılan vizitte hiç Diyetisyen formu kalmadığı doğrulanır

  Senaryo: Muayene sayfasında vizit içinde Vital Bulgular formunun doldurulması, kaydedilmesi ve aynı vizitte silinmesi
    # VİZİT İÇİ VITAL BULGULAR FORMU PENCERESİNİN AÇILMASI
    Ve Muayene sayfasında ilk vizitin Vital Bulgular formu penceresini açar

    # FORM ALANLARININ DOLDURULMASI
    Ve Muayene sayfasında Vital Bulgular formu aşağıdaki değerlerle doldurulur:
      | alan                                   | tip           | değer                                   |
      | LOC Değeri                             | dropdown      | Ağrıya Yanıt Veriyor                    |
      | Davranışsal                            | radio         | Huzursuz ama sakinleştirilebiliyor      |
      | Solunum Sayısı (sayı / dk)             | radio         | Normal                                  |
      | Solunum Yolu                           | radio         | Retraksiyon Yok                         |
      | Oda Havası / Oksijen Desteği           | radio         | Oksijen                                 |
      | SpO2                                   | numeric       | 95                                      |
      | Cilt Rengi ve Kapiller Dolum Süresi    | dropdown      | Cilt rengi gri KDS: 4 sn                |
      | Kalp Hızı (sayı / dk)                  | dropdownFirst |                                         |
      | Nabız (dk)                             | numeric       | 88                                      |
      | Bilinç Düzeyi                          | radio         | Uyanık / Normal                         |
      | Ateş                                   | numeric       | 37                                      |

    # FORMUN KAYDEDİLMESİ
    Ve Muayene sayfasında Vital Bulgular formu için Kaydet butonuna tıklar

    # KAYDIN DOĞRULANMASI
    O zaman Muayene sayfasında Vital Bulgular formu kaydının başarıyla oluşturulduğu doğrulanır

    # VİZİTTEKİ FORMUN SİLİNMESİ VE DOĞRULAMA
    Ve Muayene sayfasında vizitler arasında Vital Bulgular formu bulunan ilk vizitteki formu siler
    Ve Muayene sayfasında Vital Bulgular formu silme işlemi için varsa Onay penceresindeki Evet butonuna tıklar
    O zaman Muayene sayfasında Vital Bulgular formu açılan vizitte hiç Vital Bulgular formu kalmadığı doğrulanır

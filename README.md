# Fırın Gelir Gider Uygulaması

## ÖZET
Marketlere, Bakkallara, Büfelere, ekmek ve benzeri hamurişi yiyecekleri satan bir fırın çin, basit ölçekli gelir gider uygulaması.
Şimdilik 5 kısım bulunmaktadır. **Cari Hesap**, **Ürünler**, **Sipariş**, **Tahsilat** ve **Rapor**.

### Cari Hesap
Market, Bakkal, Büfe ya da gelirini giderini tutacağı her hangi bir esnafın kaydını yapıp, güncelleyebileceği ekran.
> Cari Hesap'ı silmek isterseniz, o Cari Hesapla ilgili Sipariş ya da Tahsilat işlemi yapılmamış olması gerekiyor.

### Ürünler
Fırının ürettiği hamurişi yiyecekleri, ekleyeceği, güncelleyecği ekran.
> - Ürünü yanlış girdiniz, güncelleyebilirsiniz. Silmek isterseniz, o ürünle ilgili Sipariş işleminin yapılmamış olması gerekiyor. 
> - Aynı ürün, farklı kişilere farkı fiyattan verilebilir. Bunun için ayrı bir yapı kurulmadı. Ürünü çoklayıp, ismini ve fiyatını değiştirmek yeterlidir. 

### Sipariş
Satış işleminin yapıldığı ekran. Hangi üründen, kime, ne zaman, kaç adet sipariş alındı? Sipariş onaylandı mı?
> - Sipariş'te, Satış da olabilir İade de.  
> - Ekmek tarzı ürünlerden, gün sonunda satılmayanların, ertesi gün **İade** olarak şiparişi girilir. Böylelikle satılmayan ürünler, siparişten düşülür.  
> - Sipariş onaylı da girilebilir, onaysız da. Müşteri siparişten vazgeçtiğinde onaysız sipariş kolayca silinebilir.  
> - Önceki borçlar __ DEVİR __ ve SATIŞ işlemi olarak girilebilir. Fiyatı 1TL belirlenmiştir, değiştirilebilir. 1000TL borç devir edilmişse, __ DEVİR __ ürününden 1000 adet onaylı sipariş işlemi girmelisiniz.

### Tahsilat
Yapılan satışlar için alınan ödemelerin kaydının tutulduğu ekran.
> - Verilen sipariş kadar, tahsilat yapılması beklense de, daha az bir miktarın tahsilatı da yapılabilir.  
> - Cari kısımdan ANLIK BORÇ bilgisi gösterilerek, borç girişi kolaylaştırılmıştır.  
> - ANLIK BORÇ'ta, Onaylanmış Sipariş'ten doğan borş dikkate alınmaktadır.

### Rapor
Üç adet rapor tipi bulunmaktadır. **Tahsilat Raporu**, **Sipariş Raporu** ve **Borç Raporu**

**Tahsilat Raporu**
Verilen tarih aralığında, tüm hesapların ya da seçilen hesabın tahsilat kayıtlarını listeler. Sayfalama kullanılmıştır. Sonuçlar istenirse excel'e de aktarılabilir.

**Sipariş Raporu**
Verilen tarih aralığında, tüm hesapların ya da seçilen hesabın, verdikleri tüm siparişleri ya da ürün bazlı siparişlerin kayıtlarını listeler. Bu Liste, excel'e de aktarılabilir.

**Borç Raporu**
Verilen tarih aralığında, tüm hesapların ya da seçilen hesabın, verdiği sipariş tutarından, tahsilat tutarı çıkarılarak borç olarak listelenir. Bu Liste, excel'e de aktarılabilir.

## ÇALIŞTIRILMASI
Java 8 ve üzeri kurulu bir bilgisayarda (Win, Linux, Mac)  
- `java -jar {app.jar}` ile uygulama başlatılır.  

Uygulama arka planda çalışsın istenirse.  
- `nohup java -jar {app.jar} &` ile uygulama başlatılır.  

Özellikle, kayıt ekleme, çıkarma ve güncelleme işlemleri log dosyasına kayıt ediliyor.  

Uygulama taşınabilir dosya yapısındaki SQLITE veritabanını kullanıyor. MYSQL gibi ekstra tecrübe ve bilgi gerektiren bir yapı kullanılmadı. Bu sayede yalnızca jar dosyası ile uygulamayı istediğiniz yerde çalıştırabilirsiniz.  

Bootstrap sayesinde, aynı ağdaki bir telefondan da uygulamayı kullanabilirsiniz.

Login kısmı yapılmadı. Şifre ve Güvenlik kısmı kullanıcıya bırakıldı.

## KULLANILAN TEKNOLOJİLER ve KÜTÜPHANELER
- Java 8  
- Spring Boot  
- Restful  
- Thymeleaf  
- Bootstrap  
- Fontawesome  
- Sqlite  
- Google Fonts, Jetbrains Mono  
- Apache POI (Excel için)  
- Lombok 

## EKRAN GÖRÜNTÜLERİ

<img width="527" alt="Ekran Resmi 2022-06-16 20 10 49" src="https://user-images.githubusercontent.com/7379/174398226-dc63424e-3d64-4445-aaa1-b4367e28b06d.png">

<img width="1130" alt="Ekran Resmi 2022-06-16 20 10 39" src="https://user-images.githubusercontent.com/7379/174398222-feef1d35-bc2a-41ed-ae4b-73a097a0ff29.png">

<img width="1138" alt="Ekran Resmi 2022-06-16 20 10 30" src="https://user-images.githubusercontent.com/7379/174398220-e6c51013-dc13-4935-a500-754c727426ed.png">

<img width="1143" alt="Ekran Resmi 2022-06-16 20 10 18" src="https://user-images.githubusercontent.com/7379/174398219-0f537924-77fa-45a4-9efd-0e59f0c6b1a3.png">

<img width="1132" alt="Ekran Resmi 2022-06-16 20 09 53" src="https://user-images.githubusercontent.com/7379/174398217-a059b087-504f-479b-a0d4-cfb778b41634.png">

<img width="1146" alt="Ekran Resmi 2022-06-16 20 09 40" src="https://user-images.githubusercontent.com/7379/174398215-8ba44274-17ca-48eb-b407-e7853326ab54.png">

<img width="1138" alt="Ekran Resmi 2022-06-16 20 09 25" src="https://user-images.githubusercontent.com/7379/174398213-46449c08-1cc8-4900-ace3-d6ec9aac5e4f.png">

<img width="1140" alt="Ekran Resmi 2022-06-16 20 09 16" src="https://user-images.githubusercontent.com/7379/174398200-620fb015-eda2-4e83-a323-ff29b79864b9.png">

<img width="1152" alt="Ekran Resmi 2022-06-16 20 09 07" src="https://user-images.githubusercontent.com/7379/174398187-dd93a686-a6cd-4ecc-bdb7-6d1408f0204f.png">
